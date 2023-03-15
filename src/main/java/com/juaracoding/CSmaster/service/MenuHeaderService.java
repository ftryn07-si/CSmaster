package com.juaracoding.CSmaster.service;

import com.juaracoding.CSmaster.configuration.OtherConfig;
import com.juaracoding.CSmaster.dto.MenuDTO;
import com.juaracoding.CSmaster.dto.MenuHeaderDTO;
import com.juaracoding.CSmaster.handler.ResponseHandler;
import com.juaracoding.CSmaster.model.MenuHeader;
import com.juaracoding.CSmaster.repo.MenuHeaderRepo;
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

import java.util.*;
/*
    KODE MODUL 02
 */

@Service
@Transactional
public class MenuHeaderService {

    private MenuHeaderRepo menuHeaderRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private List<MenuHeader> lsCPUpload = new ArrayList<MenuHeader>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public MenuHeaderService(MenuHeaderRepo menuHeaderRepo) {
        strExceptionArr[0] = "MenuHeaderService";
        this.menuHeaderRepo = menuHeaderRepo;
    }

    public Map<String, Object> saveMenuHeader(MenuHeader menuHeader, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_IDZ",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV02001",request);
            }
            menuHeader.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            menuHeader.setCreatedDate(new Date());
            menuHeaderRepo.save(menuHeader);
        } catch (Exception e) {
            strExceptionArr[1] = "saveMenuHeader(MenuHeader menuHeader, WebRequest request) --- LINE 58";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE02001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED, null, null, request);
    }

    public Map<String, Object> updateMenu(Long idMenuHeader,MenuHeader menuHeader, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_IDZ",1);

        try {
            MenuHeader nextMenuHeader = menuHeaderRepo.findById(idMenuHeader).orElseThrow(
                    ()->null
            );

            if(nextMenuHeader==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_MENU_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV02002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV02003",request);
            }
            nextMenuHeader.setNamaMenuHeader(menuHeader.getNamaMenuHeader());
            nextMenuHeader.setDeskripsiMenuHeader(menuHeader.getDeskripsiMenuHeader());
            nextMenuHeader.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextMenuHeader.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "updateMenu(Long idMenuHeader,MenuHeader menuHeader, WebRequest request)  --- LINE 91";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_UPDATE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE02002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED, null, null, request);
    }

    public Map<String,Object> findPageAllMenuHeader(Pageable pageable, WebRequest request)
    {
        List<MenuDTO> listMenuDTO = null;
        Map<String,Object> mapResult = null;
        Page<MenuHeader> pageMenu = null;
        List<MenuHeader> listMenuHeader = null;

        try
        {
            pageMenu = menuHeaderRepo.findByIsDelete(pageable,(byte)1);
            listMenuHeader = pageMenu.getContent();
            if(listMenuHeader.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                null,
                                null,
                                request);
            }
            listMenuDTO = modelMapper.map(listMenuHeader, new TypeToken<List<MenuDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listMenuDTO,pageMenu,mapColumnSearch);
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllMenu(Pageable pageable, WebRequest request) --- LINE 121";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE03003", request);
        }



        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }
    public Map<String,Object> findAllMenuHeader()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<MenuHeaderDTO> listMenuHeaderDTO = null;
        Map<String,Object> mapResult = null;
        Page<MenuHeader> pageMenu = null;
        List<MenuHeader> listMenu = null;

        try
        {
            listMenu = menuHeaderRepo.findByIsDelete((byte)1);
            if(listMenu.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                null,
                                null,
                                null);
            }
            listMenuHeaderDTO = modelMapper.map(listMenu, new TypeToken<List<MenuHeaderDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllMenu(Pageable pageable, WebRequest request) --- LINE 121";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE03003", null);
        }



        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        listMenuHeaderDTO,
                        null,
                        null);
    }

    public Map<String,Object> findById(Long id)
    {
        MenuHeader menuHeader = menuHeaderRepo.findById(id).orElseThrow (
                ()-> null
        );
        if(menuHeader == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_MENU_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV03005",null);
        }
        MenuDTO menuDTO = modelMapper.map(menuHeader, new TypeToken<MenuDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        menuDTO,
                        null,
                        null);
    }

    public Map<String,String> mapColumn()
    {
        /*
        key = id di web, value = tampilan ke user
         */
        mapColumnSearch.put("id","ID");
        mapColumnSearch.put("nama","NAMA");
        mapColumnSearch.put("deskripsi","DESKRIPSI");

        return mapColumnSearch;
    }

    public Page<MenuHeader> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramColumn.equals("id"))
        {
            menuHeaderRepo.findByIsDeleteAndIdMenuHeader(pageable,(byte) 1,paramValue);
        } else if (paramColumn.equals("nama")) {
            menuHeaderRepo.findByIsDeleteAndNamaMenuHeader(pageable,(byte) 1,paramValue);
        } else if (paramColumn.equals("deskripsi")) {
            menuHeaderRepo.findByIsDeleteAndDeskripsiMenuHeader(pageable,(byte) 1,paramValue);
        }

        return menuHeaderRepo.findByIsDeleteAndIdMenuHeader(pageable,(byte) 1,paramValue);
    }
}
