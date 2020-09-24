package com.utkuakgungor.filmtavsiye.utils;

public class Yonetmen {

    private String person_id;
    private String yonetmen_adi;

    public Yonetmen(){

    }

    public String getYonetmen_adi() {
        return yonetmen_adi;
    }

    public void setYonetmen_adi(String yonetmen_adi) {
        this.yonetmen_adi = yonetmen_adi;
    }

    public Yonetmen(String person_id){
        this.person_id=person_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }
}
