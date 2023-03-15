package com.juaracoding.CSmaster.service;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
Build #IU-222.4345.14, built on October 5, 2022
@Author Moh. Ikhsan a.k.a. Fitriyani
Java Developer
Created on 3/7/2023 6:45 AM
@Last Modified 3/7/2023 6:45 AM
Version 1.0
*/

import com.juaracoding.CSmaster.configuration.OtherConfig;
import com.juaracoding.CSmaster.dto.CategoryDTO;

import com.juaracoding.CSmaster.handler.ResourceNotFoundException;
import com.juaracoding.CSmaster.handler.ResponseHandler;
import com.juaracoding.CSmaster.model.Category;

import com.juaracoding.CSmaster.repo.CategoryRepo;

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
public class CategoryService{

    private CategoryRepo categoryRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];




    @Autowired
    public CategoryService(CategoryRepo categoryRepo) {
        mapColumn();
//        listItemPerPage();
        strExceptionArr[0] = "CategoryService";
        this.categoryRepo = categoryRepo;
    }




    public Map<String, Object> saveCategory(Category category, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV03001",request);
            }
            category.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            category.setCreatedDate(new Date());
            categoryRepo.save(category);
        } catch (Exception e) {
            strExceptionArr[1] = "saveCategory(Category category, WebRequest request) --- LINE 67";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE03001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, category.getIdCategory(),mapColumnSearch),
                null, request);
    }






    public Map<String, Object> updateCategory(Long idCategory,Category category, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Category nextCategory = categoryRepo.findById(idCategory).orElseThrow(
                    ()->null
            );

            if(nextCategory==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_CATEGORY_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV03002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV03003",request);
            }

            nextCategory.setNamaCategory(category.getNamaCategory());
            nextCategory.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextCategory.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "updateCategory(Long idCategory,Category category, WebRequest request) --- LINE 92";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE03002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    public Map<String, Object> deleteCategory(Long idCategory, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Category nextCategory = categoryRepo.findById(idCategory).orElseThrow(
                    ()->null
            );

            if(nextCategory==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_CATEGORY_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV03002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV03003",request);
            }

            nextCategory.setIsDelete((byte) 0);
            nextCategory.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextCategory.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "updateCategory(Long idCategory,Category category, WebRequest request) --- LINE 92";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE03002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }




    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveUploadFileCategory(List<Category> listCategory,
                                                  MultipartFile multipartFile,
                                                  WebRequest request) throws Exception {
        List<Category> listCategoryResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            listCategoryResult = categoryRepo.saveAll(listCategory);
            if (listCategoryResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFile(List<Category> listCategory, MultipartFile multipartFile, WebRequest request) --- LINE 82";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV03004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFile(List<Category> listCategory, MultipartFile multipartFile, WebRequest request) --- LINE 88";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE03002", request);
        }
        return new ResponseHandler().
                generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        null,
                        request);
    }





    public Map<String,Object> findAllCategory(Pageable pageable, WebRequest request)
    {
        List<CategoryDTO> listCategoryDTO = null;
        Map<String,Object> mapResult = null;
        Page<Category> pageCategory = null;
        List<Category> listCategory = null;

        try
        {
            pageCategory = categoryRepo.findByIsDelete(pageable,(byte)1);
            listCategory = pageCategory.getContent();
            if(listCategory.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV03005",
                                request);
            }
            listCategoryDTO = modelMapper.map(listCategory, new TypeToken<List<CategoryDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listCategoryDTO,pageCategory,mapColumnSearch);

        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllCategory(Pageable pageable, WebRequest request) --- LINE 178";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE03003", request);
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
        Page<Category> pageCategory = null;
        List<Category> listCategory = null;
        List<CategoryDTO> listCategoryDTO = null;
        Map<String,Object> mapResult = null;

        try
        {
            if(columFirst.equals("id"))
            {
                try
                {
                    Long.parseLong(valueFirst);
                }
                catch (Exception e)
                {
                    strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 209";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().
                            generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                    HttpStatus.OK,
                                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                    "FE03004",
                                    request);
                }
            }
            pageCategory = getDataByValue(pageable,columFirst,valueFirst);
            listCategory= pageCategory.getContent();
            if(listCategory.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV03006",
                                request);
            }
            listCategoryDTO = modelMapper.map(listCategory, new TypeToken<List<CategoryDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,listCategoryDTO,pageCategory,mapColumnSearch);
            System.out.println("LIST DATA => "+listCategoryDTO.size());
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 237";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE03005", request);
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
        Category category = categoryRepo.findById(id).orElseThrow (
                ()-> null
        );
        if(category == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_CATEGORY_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV03005",request);
        }
        CategoryDTO categoryDTO = modelMapper.map(category, new TypeToken<CategoryDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                       categoryDTO,
                        null,
                        request);
    }





    private void mapColumn()
    {
        mapColumnSearch.put("id","ID CATEGORY");
        mapColumnSearch.put("nama","NAMA CATEGORY");

    }

    private Page<Category> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        if(paramValue.equals(""))
        {
            return categoryRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id"))
        {
            return categoryRepo.findByIsDeleteAndIdCategoryContainsIgnoreCase(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return categoryRepo.findByIsDeleteAndNamaCategoryContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }

        return categoryRepo.findByIsDelete(pageable,(byte) 1);
    }

}
