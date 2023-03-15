package com.juaracoding.CSmaster.controller;

import com.juaracoding.CSmaster.configuration.OtherConfig;
import com.juaracoding.CSmaster.dto.CategoryDTO;
import com.juaracoding.CSmaster.dto.DemoDTO;
import com.juaracoding.CSmaster.model.Category;
import com.juaracoding.CSmaster.model.Demo;
import com.juaracoding.CSmaster.service.CategoryService;
import com.juaracoding.CSmaster.service.DemoService;
import com.juaracoding.CSmaster.utils.ConstantMessage;
import com.juaracoding.CSmaster.utils.ManipulationMap;
import com.juaracoding.CSmaster.utils.MappingAttribute;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/item")
public class CategoryContoller {

    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();

    private List<Category> lsCPUpload = new ArrayList<Category>();

    private String [] strExceptionArr = new String[2];

    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public CategoryContoller(CategoryService categoryService) {
        strExceptionArr[0] = "CategoryController";
        mapSorting();
        this.categoryService = categoryService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","idCategory");
        mapSorting.put("nama","namaCategory");
    }

    @GetMapping("/v1/category/new")
    public String createCategory(Model model, WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("category", new CategoryDTO());
        return "category/create_category";
    }

    @GetMapping("/v1/category/edit/{id}")
    public String editCategory(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = categoryService.findById(id,request);
        CategoryDTO categoryDTO= (objectMapper.get("data")==null?null:(CategoryDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success"))
        {
            CategoryDTO categoryDTOForSelect = (CategoryDTO) objectMapper.get("data");
            model.addAttribute("category", categoryDTO);
            return "category/edit_category";

        }
        else
        {
            model.addAttribute("category", new CategoryDTO());
            return "redirect:/api/item/category/default";
        }
    }
    @PostMapping("/v1/category/new")
    public String newCategory(@ModelAttribute(value = "category")
                              @Valid CategoryDTO categoryDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
    )
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }

        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("category",categoryDTO);
            model.addAttribute("status","error");

            return "category/create_category";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("category",categoryDTO);
            return "category/create_category";
        }
        /* END OF VALIDATION */

        Category category = modelMapper.map(categoryDTO, new TypeToken<Category>() {}.getType());
        objectMapper = categoryService.saveCategory(category,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/item/v1/category/fbpsb/0/asc/idCategory?columnFirst=idCategory&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("category",new CategoryDTO());
            model.addAttribute("status","error");
            return "category/create_category";
        }
    }

    @PostMapping("/v1/category/edit/{id}")
    public String doRegis(@ModelAttribute("category")
                          @Valid CategoryDTO categoryDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("category",categoryDTO);
            return "category/edit_category";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("category",categoryDTO);

            return "categori/edit_category";
        }
        /* END OF VALIDATION */

        Category category = modelMapper.map(categoryDTO, new TypeToken<Category>() {}.getType());
        objectMapper = categoryService.updateCategory(id,category,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("category",new CategoryDTO());
            return "redirect:/api/item/v1/category/fbpsb/0/asc/idCategory?columnFirst=idCategory&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("category",new CategoryDTO());
            return "category/edit_category";
        }
    }


    @GetMapping("/v1/category/default")
    public String getDefaultData(Model model,WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        Pageable pageable = PageRequest.of(0,5, Sort.by("idCategory"));
        objectMapper = categoryService.findAllCategory(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper,request);

        model.addAttribute("category",new CategoryDTO());
        model.addAttribute("sortBy","idCategory");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/category/category";
    }

    @GetMapping("/v1/category/fbpsb/{page}/{sort}/{sortby}")
    public String findByCategory(
            Model model,
            @PathVariable("page") Integer pagez,
            @PathVariable("sort") String sortz,
            @PathVariable("sortby") String sortzBy,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            @RequestParam String sizeComponent,
            WebRequest request
    ){
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"idCategory":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = categoryService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("category",new CategoryDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/category/category";
    }
    @GetMapping("/v1/category/delete/{id}")
    public String doRegis(Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = categoryService.deleteCategory(id,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("category",new CategoryDTO());
            return "redirect:/api/item/v1/category/fbpsb/0/asc/idCategory?columnFirst=idCategory&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
//            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("category",new CategoryDTO());
            return "/category/category";
        }
    }



}
