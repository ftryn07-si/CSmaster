package com.juaracoding.CSmaster.service;


import com.juaracoding.CSmaster.configuration.OtherConfig;
import com.juaracoding.CSmaster.core.BcryptImpl;
import com.juaracoding.CSmaster.dto.CategoryDTO;
import com.juaracoding.CSmaster.dto.ForgetPasswordDTO;
import com.juaracoding.CSmaster.dto.UserDTO;
import com.juaracoding.CSmaster.handler.ResponseHandler;
import com.juaracoding.CSmaster.model.Akses;
import com.juaracoding.CSmaster.model.Category;
import com.juaracoding.CSmaster.model.Userz;
import com.juaracoding.CSmaster.repo.UserRepo;
import com.juaracoding.CSmaster.utils.ConstantMessage;
import com.juaracoding.CSmaster.utils.ExecuteSMTP;
import com.juaracoding.CSmaster.utils.LoggingFile;
import com.juaracoding.CSmaster.utils.TransformToDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@Service
@Transactional
public class UserService {

    private UserRepo userRepo;

    private String [] strExceptionArr = new String[2];


    @Autowired
    private ModelMapper modelMapper;
    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public UserService(UserRepo userService) {
        strExceptionArr[0] = "UserService";
        this.userRepo = userService;
    }

    public Map<String,Object> checkRegis(Userz userz, WebRequest request) {
        int intVerification = new Random().nextInt(100000,999999);
        List<Userz> listUserResult = userRepo.findByEmailOrNoHPOrUsername(userz.getEmail(),userz.getNoHP(),userz.getUsername());//INI VALIDASI USER IS EXISTS
        String emailForSMTP = "";
        try
        {



            if(listUserResult.size()!=0)//kondisi mengecek apakah user terdaftar
            {

                emailForSMTP = userz.getEmail();
                Userz nextUser = listUserResult.get(0);
                if(nextUser.getIsDelete()!=0)//sudah terdaftar dan aktif
                {
                    //PEMBERITAHUAN SAAT REGISTRASI BAGIAN MANA YANG SUDAH TERDAFTAR (USERNAME, EMAIL ATAU NOHP)
                    if(nextUser.getEmail().equals(userz.getEmail()))
                    {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMAIL_ISEXIST,
                                HttpStatus.NOT_ACCEPTABLE,null,"FV01001",request);//EMAIL SUDAH TERDAFTAR DAN AKTIF
                    } else if (nextUser.getNoHP().equals(userz.getNoHP())) {//FV = FAILED VALIDATION
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_NOHP_ISEXIST,
                                HttpStatus.NOT_ACCEPTABLE,null,"FV01002",request);//NO HP SUDAH TERDAFTAR DAN AKTIF
                    } else if (nextUser.getUsername().equals(userz.getUsername())) {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USERNAME_ISEXIST,
                                HttpStatus.NOT_ACCEPTABLE,null,"FV01003",request);//USERNAME SUDAH TERDAFTAR DAN AKTIF
                    } else {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_ISACTIVE,
                                HttpStatus.NOT_ACCEPTABLE,null,"FV01004",request);//KARENA YANG DIAMBIL DATA YANG PERTAMA JADI ANGGAPAN NYA SUDAH TERDAFTAR SAJA
                    }
                }
                else
                {
                    nextUser.setPassword(BcryptImpl.hash(userz.getPassword()+userz.getUsername()));
                    nextUser.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                    nextUser.setTokenCounter(nextUser.getTokenCounter()+1);//setiap kali mencoba ditambah 1
                    nextUser.setModifiedBy(Integer.parseInt(nextUser.getIdUser().toString()));
                    nextUser.setModifiedDate(new Date());
                }
            }
            else//belum terdaftar
            {
                userz.setPassword(BcryptImpl.hash(userz.getPassword()+userz.getUsername()));
                userz.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                userRepo.save(userz);
            }
//            strProfile[0]="TOKEN UNTUK VERIFIKASI EMAIL";
//            strProfile[1]=userz.getNamaLengkap();
//            strProfile[2]=String.valueOf(intVerification);

            /*EMAIL NOTIFICATION*/
            if(OtherConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !emailForSMTP.equals(""))
            {
                new ExecuteSMTP().sendSMTPToken(emailForSMTP,"VERIFIKASI TOKEN REGISTRASI",
                        "TOKEN UNTUK VERIFIKASI EMAIL",String.valueOf(intVerification));
            }
            System.out.println("VERIFIKASI -> "+intVerification);
        }catch (Exception e)
        {
            strExceptionArr[1]="checkRegis(Userz userz) --- LINE 70";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.NOT_FOUND,null,"FE01001",request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CHECK_REGIS,
                HttpStatus.CREATED,null,null,request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> confirmRegis(Userz userz, String emails, WebRequest request) {
        List<Userz> listUserResult = userRepo.findByEmail(emails);
        try
        {
            if(listUserResult.size()!=0)
            {
                Userz nextUser = listUserResult.get(0);
                if(!BcryptImpl.verifyHash(userz.getToken(),nextUser.getToken()))
                {
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_TOKEN_INVALID,
                            HttpStatus.NOT_ACCEPTABLE,null,"FV01005",request);
                }
                nextUser.setIsDelete((byte) 1);//SET REGISTRASI BERHASIL
                Akses akses = new Akses();
                akses.setIdAkses(1L);
                nextUser.setAkses(akses);
            }
            else
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                    HttpStatus.NOT_FOUND,null,"FV01006",request);
            }
        }
        catch (Exception e)
        {
            strExceptionArr[1]="confirmRegis(Userz userz)  --- LINE 103";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FE01002",request);
        }

        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CHECK_REGIS,
                HttpStatus.OK,null,null,request);
    }
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> doLogin(Userz userz, WebRequest request) {
        userz.setUsername(userz.getEmail());
        userz.setNoHP(userz.getNoHP());
        List<Userz> listUserResult = userRepo.findByEmailOrNoHPOrUsername(userz.getEmail(),userz.getNoHP(),userz.getUsername());//DATANYA PASTI HANYA 1
        Userz nextUser = null;
        try
        {
            if(listUserResult.size()!=0)
            {
                nextUser = listUserResult.get(0);
                if(!BcryptImpl.verifyHash(userz.getPassword()+nextUser.getUsername(),nextUser.getPassword()))//dicombo dengan userName
                {
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_LOGIN_FAILED,
                            HttpStatus.NOT_ACCEPTABLE,null,"FV01007",request);
                }
                nextUser.setLastLoginDate(new Date());
                nextUser.setTokenCounter(0);//SETIAP KALI LOGIN BERHASIL , BERAPA KALIPUN UJI COBA REQUEST TOKEN YANG SEBELUMNYA GAGAL AKAN SECARA OTOMATIS DIRESET MENJADI 0
                nextUser.setPasswordCounter(0);//SETIAP KALI LOGIN BERHASIL , BERAPA KALIPUN UJI COBA YANG SEBELUMNYA GAGAL AKAN SECARA OTOMATIS DIRESET MENJADI 0
            }
            else
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV01008",request);
            }
        }

        catch (Exception e)
        {
            strExceptionArr[1]="doLogin(Userz userz,WebRequest request)  --- LINE 132";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_LOGIN_FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FE01003",request);
        }

        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_LOGIN,
                HttpStatus.OK,nextUser,null,request);
    }

    public Map<String,Object> getNewToken(String emailz, WebRequest request) {
        List<Userz> listUserResult = userRepo.findByEmail(emailz);//DATANYA PASTI HANYA 1
        String emailForSMTP = "";
        int intVerification = 0;
        try
        {
            if(listUserResult.size()!=0)
            {
                intVerification = new Random().nextInt(100000,999999);
                Userz userz = listUserResult.get(0);
                userz.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                userz.setModifiedDate(new Date());
                userz.setModifiedBy(Integer.parseInt(userz.getIdUser().toString()));
                System.out.println("New Token -> "+intVerification);
                emailForSMTP = userz.getEmail();
            }
            else
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV01009",request);
            }
        }
        catch (Exception e)
        {
            strExceptionArr[1]="getNewToken(String emailz, WebRequest request)  --- LINE 185";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FE01004",request);
        }

        /*
                call method send SMTP
         */

        if(OtherConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !emailForSMTP.equals(""))
        {
            new ExecuteSMTP().sendSMTPToken(emailForSMTP,"VERIFIKASI TOKEN GANTI PASSWORD",
                    "TOKEN BARU UNTUK VERIFIKASI GANTI PASSWORD",String.valueOf(intVerification));
        }

        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_LOGIN,
                HttpStatus.OK,null,null,request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> sendMailForgetPwd(String email,WebRequest request)
    {
        int intVerification =0;
        List<Userz> listUserResults = userRepo.findByEmail(email);
        try
        {
            if(listUserResults.size()==0)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                        HttpStatus.NOT_FOUND,null,"FV01010",request);
            }
            intVerification = new Random().nextInt(100000,999999);
            Userz userz = listUserResults.get(0);
            userz.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
            userz.setModifiedDate(new Date());
            userz.setModifiedBy(Integer.parseInt(userz.getIdUser().toString()));
            System.out.println("New Forget Password Token -> "+intVerification);
        }
        catch (Exception e)
        {
            strExceptionArr[1]="sendMailForgetPwd(String email)  --- LINE 214";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FE01005",request);
        }
        /*
            INI BUTUH

         */
        if(OtherConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !email.equals(""))
        {
            new ExecuteSMTP().sendSMTPToken(email,"VERIFIKASI TOKEN LUPA PASSWORD",
                    "TOKEN UNTUK VERIFIKASI LUPA PASSWORD",String.valueOf(intVerification));
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_SEND_NEW_TOKEN,
                HttpStatus.OK,null,null,request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> confirmTokenForgotPwd(ForgetPasswordDTO forgetPasswordDTO, WebRequest request)
    {
        String emailz = forgetPasswordDTO.getEmail();
        String token = forgetPasswordDTO.getToken();

        List<Userz> listUserResults = userRepo.findByEmail(emailz);
        try
        {
            if(listUserResults.size()==0)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                        HttpStatus.NOT_FOUND,null,"FV01011",request);
            }

            Userz userz = listUserResults.get(0);

            if(!BcryptImpl.verifyHash(token,userz.getToken()))//VALIDASI TOKEN
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_TOKEN_FORGOTPWD_NOT_SAME,
                        HttpStatus.NOT_FOUND,null,"FV01012",request);
            }
        }
        catch (Exception e)
        {
            strExceptionArr[1]="confirmTokenForgotPwd(ForgetPasswordDTO forgetPasswordDTO, WebRequest request)  --- LINE 250";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FE01006",request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_TOKEN_MATCH,
                HttpStatus.OK,null,null,request);
    }


    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> confirmPasswordChange(ForgetPasswordDTO forgetPasswordDTO, WebRequest request)
    {
        String emailz = forgetPasswordDTO.getEmail();
        String newPassword = forgetPasswordDTO.getNewPassword();
        String oldPassword = forgetPasswordDTO.getOldPassword();
        String confirmPassword = forgetPasswordDTO.getConfirmPassword();

        List<Userz> listUserResults = userRepo.findByEmail(emailz);
        try
        {
            if(listUserResults.size()==0)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_FOUND,null,"FV01012",request);
            }

            Userz userz = listUserResults.get(0);
            if(!BcryptImpl.verifyHash(oldPassword+userz.getUsername(),userz.getPassword()))//kalau password lama tidak sama dengan yang diinput
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_PASSWORD_NOT_SAME,
                        HttpStatus.NOT_FOUND,null,"FV01013",request);
            }
            if(oldPassword.equals(newPassword))//PASSWORD BARU SAMA DENGAN PASSWORD LAMA
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_PASSWORD_IS_SAME,
                        HttpStatus.NOT_FOUND,null,"FV01014",request);
            }
            if(!confirmPassword.equals(newPassword))//PASSWORD BARU DENGAN PASSWORD KONFIRMASI TIDAK SAMA
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_PASSWORD_CONFIRM_FAILED,
                        HttpStatus.NOT_FOUND,null,"FV01014",request);
            }

            userz.setPassword(BcryptImpl.hash(String.valueOf(newPassword+userz.getUsername())));
            userz.setModifiedDate(new Date());
            userz.setModifiedBy(Integer.parseInt(userz.getIdUser().toString()));
            System.out.println("New Forget Password -> "+newPassword);
        }

        catch (Exception e)
        {
            strExceptionArr[1]="confirmPasswordChange(ForgetPasswordDTO forgetPasswordDTO, WebRequest request)  --- LINE 297";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FE01006",request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CHANGE_PWD,
                HttpStatus.OK,null,null,request);
    }
//    public Map<String,Object> findAllUserz(Pageable pageable, WebRequest request)
//    {
//        List<UserDTO> listUserDTO = null;
//        Map<String,Object> mapResult = null;
//        Page<Userz> pageUserz= null;
//        List<Userz> listUserz= null;
//
//        try
//        {
//            pageUserz = userRepo.findByIsDelete(pageable,(byte)1);
//            listUserz = pageUserz.getContent();
//            if(listUserz.size()==0)
//            {
//                return new ResponseHandler().
//                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
//                                HttpStatus.OK,
//                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
//                                "FV03005",
//                                request);
//            }
//            listUserDTO = modelMapper.map(listUserz, new TypeToken<List<UserDTO>>() {}.getType());
//            mapResult = transformToDTO.transformObject(objectMapper,listUserDTO,pageUserz,mapColumnSearch);
//
//        }
//        catch (Exception e)
//        {
//            strExceptionArr[1] = "findAllUser(Pageable pageable, WebRequest request) --- LINE 178";
//            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
//            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
//                    "FE03003", request);
//        }
//
//        return new ResponseHandler().
//                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
//                        HttpStatus.OK,
//                        mapResult,
//                        null,
//                        null);
//    }
//    public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst)
//    {
//        Page<Userz> pageUserz = null;
//        List<Userz> listUserz = null;
//        List<UserDTO> listUserDTO = null;
//        Map<String,Object> mapResult = null;
//
//        try
//        {
//            if(columFirst.equals("id"))
//            {
//                try
//                {
//                    Long.parseLong(valueFirst);
//                }
//                catch (Exception e)
//                {
//                    strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 209";
//                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
//                    return new ResponseHandler().
//                            generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
//                                    HttpStatus.OK,
//                                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
//                                    "FE03004",
//                                    request);
//                }
//            }
//            pageUserz = getDataByValue(pageable,columFirst,valueFirst);
//            listUserz= pageUserz.getContent();
//            if(listUserz.size()==0)
//            {
//                return new ResponseHandler().
//                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
//                                HttpStatus.OK,
//                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
//                                "FV03006",
//                                request);
//            }
//            listUserDTO = modelMapper.map(listUserz, new TypeToken<List<UserDTO>>() {}.getType());
//            mapResult = transformToDTO.transformObject(objectMapper,listUserDTO,pageUserz,mapColumnSearch);
//            System.out.println("LIST DATA => "+listUserDTO.size());
//        }
//
//        catch (Exception e)
//        {
//            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 237";
//            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
//            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
//                    "FE03005", request);
//        }
//        return new ResponseHandler().
//                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
//                        HttpStatus.OK,
//                        mapResult,
//                        null,
//                        request);
//    }
//    private Page<Userz> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
//    {
//        if(paramValue.equals(""))
//        {
//            return userRepo.findByIsDelete(pageable,(byte) 1);
//        }
//        if(paramColumn.equals("id"))
//        {
//            return userRepo.findByIsDeleteAndIdUserContainsIgnoreCase(pageable,(byte) 1,Long.parseLong(paramValue));
//        } else if (paramColumn.equals("nama")) {
//            return userRepo.findByIsDeleteAndNamaLengkapContainsIgnoreCase(pageable,(byte) 1,paramValue);
//        }
//
//        return userRepo.findByIsDelete(pageable,(byte) 1);
//    }
//
//    public Map<String,Object> findById(Long id, WebRequest request)
//    {
//        Userz userz = userRepo.findById(id).orElseThrow (
//                ()-> null
//        );
//        if(userz == null)
//        {
//            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_CATEGORY_NOT_EXISTS,
//                    HttpStatus.NOT_ACCEPTABLE,
//                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
//                    "FV03005",request);
//        }
//        UserDTO userDTO = modelMapper.map(userz, new TypeToken<UserDTO>() {}.getType());
//        return new ResponseHandler().
//                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
//                        HttpStatus.OK,
//                        userDTO,
//                        null,
//                        request);
//    }
//
//
//
//
//
//    private void mapColumn()
//    {
//        mapColumnSearch.put("id","ID CATEGORY");
//        mapColumnSearch.put("nama","NAMA CATEGORY");
//
//    }


}