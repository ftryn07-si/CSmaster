package com.juaracoding.CSmaster.service;

import com.juaracoding.CSmaster.configuration.OtherConfig;
import com.juaracoding.CSmaster.dto.DemoDTO;
import com.juaracoding.CSmaster.handler.ResourceNotFoundException;
import com.juaracoding.CSmaster.handler.ResponseHandler;
import com.juaracoding.CSmaster.model.Demo;
import com.juaracoding.CSmaster.repo.DemoRepo;
import com.juaracoding.CSmaster.utils.ConstantMessage;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    KODE MODUL 11
 */
@Service
@Transactional
public class DemoService {

    private DemoRepo demoRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public DemoService(DemoRepo demoRepo) {
        strExceptionArr[0]="DemoService";
        mapColumn();
        this.demoRepo = demoRepo;
    }

    public Map<String, Object> saveDemo(Demo demo, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV11001",request);
            }
            demo.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            demo.setCreatedDate(new Date());
            demoRepo.save(demo);
        } catch (Exception e) {
            strExceptionArr[1] = "saveDemo(Demo demo, WebRequest request) --- LINE 70";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE11001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, demo.getIdDemo(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updateDemo(Long idDemo,Demo demo, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Demo nextDemo = demoRepo.findById(idDemo).orElseThrow(
                    ()->null
            );

            if(nextDemo==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV11002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV11003",request);
            }
            nextDemo.setIdDemo(demo.getIdDemo());
            nextDemo.setNamaDemo(demo.getNamaDemo());
            nextDemo.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextDemo.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updateDemo(Long idDemo,Demo demo, WebRequest request) --- LINE 112";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE11002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }



    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveUploadFileDemo(List<Demo> listDemo,
                                                  MultipartFile multipartFile,
                                                  WebRequest request) throws Exception {
        List<Demo> listDemoResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            listDemoResult = demoRepo.saveAll(listDemo);
            if (listDemoResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFileDemo(List<Demo> listDemo, MultipartFile multipartFile, WebRequest request) --- LINE 136";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV11004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFileDemo(List<Demo> listDemo, MultipartFile multipartFile, WebRequest request) --- LINE 88";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE11002", request);
        }
        return new ResponseHandler().
                generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        null,
                        request);
    }

    public Map<String,Object> findAllDemo(Pageable pageable, WebRequest request)
    {
        List<DemoDTO> listDemoDTO = null;
        Map<String,Object> mapResult = null;
        Page<Demo> pageDemo = null;
        List<Demo> listDemo = null;

        try
        {
            pageDemo = demoRepo.findByIsDelete(pageable,(byte)1);
            listDemo = pageDemo.getContent();
            if(listDemo.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV11005",
                                request);
            }
            listDemoDTO = modelMapper.map(listDemo, new TypeToken<List<DemoDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listDemoDTO,pageDemo,mapColumnSearch);

        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllDemo(Pageable pageable, WebRequest request) --- LINE 182";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE11003", request);
        }

        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

    public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst)
    {
        Page<Demo> pageDemo = null;
        List<Demo> listDemo = null;
        List<DemoDTO> listDemoDTO = null;
        Map<String,Object> mapResult = null;

        try
        {
            if(columFirst.equals("id"))
            {
                if(!valueFirst.equals("") && valueFirst!=null)
                {
                    try
                    {
                        Long.parseLong(valueFirst);
                    }
                    catch (Exception e)
                    {
                        strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 215";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                        return new ResponseHandler().
                                generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                        HttpStatus.OK,
                                        transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                        "FE11004",
                                        request);
                    }
                }

            }
            pageDemo = getDataByValue(pageable,columFirst,valueFirst);
            listDemo = pageDemo.getContent();
            if(listDemo.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV11006",
                                request);
            }
            listDemoDTO = modelMapper.map(listDemo, new TypeToken<List<DemoDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listDemoDTO,pageDemo,mapColumnSearch);
            System.out.println("LIST DATA => "+listDemoDTO.size());
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 243";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE11005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    public Map<String,Object> findById(Long id, WebRequest request)
    {
        Demo demo = demoRepo.findById(id).orElseThrow (
                ()-> null
        );
        if(demo == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV11005",request);
        }
        DemoDTO demoDTO = modelMapper.map(demo, new TypeToken() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        demoDTO,
                        null,
                        request);
    }



    private void mapColumn()
    {
        mapColumnSearch.put("id","ID DEMO");
        mapColumnSearch.put("nama","NAMA DEMO");
    }

    private Page<Demo> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals("") || paramValue==null)
        {
            return demoRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id"))
        {
            return demoRepo.findByIsDeleteAndIdDemoContainsIgnoreCase(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return demoRepo.findByIsDeleteAndNamaDemoContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }

        return demoRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }
}
