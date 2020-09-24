package com.utkuakgungor.filmtavsiye.utils;

public class Movie {

    private String film_adi,film_puani,image,youtube,ozet,sure,film_yili,film_ozet_eng,film_sure_eng,text_color,film_oyuncular,film_tur,film_tur_eng,film_yonetmen,film_sinif,film_resimler;


    public Movie(){

    }

    public Movie(String film_adi, String film_puani, String image, String youtube, String ozet, String sure,String film_yili,String film_ozet_eng,String film_sure_eng,String text_color,String film_oyuncular,String film_tur,String film_tur_eng,String film_yonetmen,String film_sinif,String film_resimler) {
        this.film_adi=film_adi;
        this.film_puani = film_puani;
        this.image = image;
        this.youtube=youtube;
        this.ozet=ozet;
        this.sure=sure;
        this.film_yili=film_yili;
        this.film_ozet_eng=film_ozet_eng;
        this.film_sure_eng=film_sure_eng;
        this.text_color=text_color;
        this.film_oyuncular=film_oyuncular;
        this.film_tur=film_tur;
        this.film_tur_eng=film_tur_eng;
        this.film_yonetmen=film_yonetmen;
        this.film_sinif=film_sinif;
        this.film_resimler=film_resimler;
    }

    public String getFilm_adi() {
        return film_adi;
    }

    public void setFilm_adi(String film_adi) {
        this.film_adi = film_adi;
    }

    public String getFilm_puani() {
        return film_puani;
    }

    public void setFilm_puani(String film_puani) {
        this.film_puani = film_puani;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getOzet() {
        return ozet;
    }

    public void setOzet(String ozet) {
        this.ozet = ozet;
    }

    public String getSure() {
        return sure;
    }

    public void setSure(String sure) {
        this.sure = sure;
    }

    public String getFilm_yili() {
        return film_yili;
    }

    public void setFilm_yili(String film_yili) {
        this.film_yili = film_yili;
    }

    public String getFilm_ozet_eng() {
        return film_ozet_eng;
    }

    public void setFilm_ozet_eng(String film_ozet_eng) {
        this.film_ozet_eng = film_ozet_eng;
    }

    public String getFilm_sure_eng() {
        return film_sure_eng;
    }

    public void setFilm_sure_eng(String film_sure_eng) {
        this.film_sure_eng = film_sure_eng;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public String getFilm_oyuncular() {
        return film_oyuncular;
    }

    public void setFilm_oyuncular(String film_oyuncular) {
        this.film_oyuncular = film_oyuncular;
    }

    public String getFilm_tur() {
        return film_tur;
    }

    public void setFilm_tur(String film_tur) {
        this.film_tur = film_tur;
    }

    public String getFilm_tur_eng() {
        return film_tur_eng;
    }

    public void setFilm_tur_eng(String film_tur_eng) {
        this.film_tur_eng = film_tur_eng;
    }

    public String getFilm_yonetmen() {
        return film_yonetmen;
    }

    public void setFilm_yonetmen(String film_yonetmen) {
        this.film_yonetmen = film_yonetmen;
    }

    public String getFilm_sinif() {
        return film_sinif;
    }

    public void setFilm_sinif(String film_sinif) {
        this.film_sinif = film_sinif;
    }

    public String getFilm_resimler() {
        return film_resimler;
    }

    public void setFilm_resimler(String film_resimler) {
        this.film_resimler = film_resimler;
    }
}
