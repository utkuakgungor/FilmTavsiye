package com.utkuakgungor.filmtavsiye.utils;

public class Oyuncu {

    private String person_id;
    private String oyuncu_adi;

    public Oyuncu(){

    }

    public String getOyuncu_adi() {
        return oyuncu_adi;
    }

    public void setOyuncu_adi(String oyuncu_adi) {
        this.oyuncu_adi = oyuncu_adi;
    }

    public Oyuncu(String person_id){
        this.person_id=person_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }
}
