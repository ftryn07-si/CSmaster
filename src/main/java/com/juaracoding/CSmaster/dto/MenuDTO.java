package com.juaracoding.CSmaster.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.juaracoding.CSmaster.model.Akses;
import com.juaracoding.CSmaster.utils.ConstantMessage;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class MenuDTO {

    private Long idMenu;
    @NotNull
    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 25)
    private String namaMenu;


    @NotNull
    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 50)
    private String pathMenu;

    @NotNull
    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 50)
    private String endPoint;

    @JsonIgnoreProperties("listMenuAkses")
    private List<Akses> listAksesMenu;

    private MenuHeaderDTO menuHeader;

    public List<Akses> getListAksesMenu() {
        return listAksesMenu;
    }

    public void setListAksesMenu(List<Akses> listAksesMenu) {
        this.listAksesMenu = listAksesMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getPathMenu() {
        return pathMenu;
    }

    public void setPathMenu(String pathMenu) {
        this.pathMenu = pathMenu;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
