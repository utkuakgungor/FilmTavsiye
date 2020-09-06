package com.utkuakgungor.filmtavsiye.utils;

public class Yonetmen {

    private String yonetmen_adi,yonetmen_resim_url,person_id;

    public Yonetmen(){

    }

    public Yonetmen(String yonetmen_adi, String yonetmen_resim_url,String person_id){
        this.yonetmen_adi=yonetmen_adi;
        this.yonetmen_resim_url = yonetmen_resim_url;
        this.person_id=person_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getYonetmen_adi() {
        return yonetmen_adi;
    }

    public String getYonetmen_resim_url() {
        return yonetmen_resim_url;
    }

}
