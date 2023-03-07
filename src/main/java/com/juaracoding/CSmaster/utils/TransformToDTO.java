package com.juaracoding.CSmaster.utils;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public class TransformToDTO {


    public Map<String,Object> transformObject(Map<String,Object> mapz, List ls, Page page)
    {
        mapz.put("content",ls);
        mapz.put("currentPage",page.getNumber());
        mapz.put("totalItems",page.getTotalElements());
        mapz.put("totalPages",page.getTotalPages());
        mapz.put("sort",page.getSort());
        mapz.put("numberOfElements",page.getNumberOfElements());

        return mapz;
    }
    public Map<String,Object> transformObject(Map<String,Object> mapz, List ls, Page page,Map<String,String> searchParams)//<PENAMBAHAN 07-03-2023>
    {
        mapz.put("content",ls);
        mapz.put("currentPage",page.getNumber());
        mapz.put("totalItems",page.getTotalElements());
        mapz.put("totalPages",page.getTotalPages());
        mapz.put("sort",page.getSort());
        mapz.put("numberOfElements",page.getNumberOfElements());
        mapz.put("searchParam",searchParams);

        return mapz;
    }
}