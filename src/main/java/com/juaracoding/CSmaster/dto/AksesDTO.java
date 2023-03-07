package com.juaracoding.CSmaster.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.juaracoding.CSmaster.model.Divisi;
import com.juaracoding.CSmaster.model.Menu;

import java.util.List;

public class AksesDTO {

    private Long idAkses;

    private String namaAkses;

    @JsonIgnoreProperties("listAksesMenu")
    private List<Menu> listMenuAkses;

    private Divisi divisi;

    public Long getIdAkses() {
        return idAkses;
    }

    public void setIdAkses(Long idAkses) {
        this.idAkses = idAkses;
    }

    public String getNamaAkses() {
        return namaAkses;
    }

    public void setNamaAkses(String namaAkses) {
        this.namaAkses = namaAkses;
    }

    public List<Menu> getListMenuAkses() {
        return listMenuAkses;
    }

    public void setListMenuAkses(List<Menu> listMenuAkses) {
        this.listMenuAkses = listMenuAkses;
    }

    public Divisi getDivisi() {
        return divisi;
    }

    public void setDivisi(Divisi divisi) {
        this.divisi = divisi;
    }
}
