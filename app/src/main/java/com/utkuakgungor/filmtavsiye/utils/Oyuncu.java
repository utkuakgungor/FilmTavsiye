package com.utkuakgungor.filmtavsiye.utils;

public class Oyuncu {

    private String oyuncu_adi,oyuncu_resim_url,person_id;

    public Oyuncu(){

    }
    public Oyuncu(String oyuncu_adi,String oyuncu_resim_url,String person_id){
        this.oyuncu_adi=oyuncu_adi;
        this.oyuncu_resim_url=oyuncu_resim_url;
        this.person_id=person_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getOyuncu_adi() {
        return oyuncu_adi;
    }

    public String getOyuncu_resim_url() {
        return oyuncu_resim_url;
    }

}
