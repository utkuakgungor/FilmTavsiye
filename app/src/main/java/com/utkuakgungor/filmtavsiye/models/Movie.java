package com.utkuakgungor.filmtavsiye.models;

public class Movie {

    private String film_id;
    private String film_adi;
    private String film_yil;
    private String film_puan;
    private String film_image;
    private String film_ozet;
    private String film_sure;
    private String film_ozet_eng;
    private String film_sure_eng;
    private String film_tur;
    private String film_tur_eng;
    private String film_sinif;

    public Movie(){

    }

    public Movie(String film_adi,String film_yil,String film_puan,String film_image,String film_ozet,String film_sure,String film_ozet_eng,String film_sure_eng,String film_id,String film_tur,String film_tur_eng,String film_sinif) {
        this.film_tur=film_tur;
        this.film_adi=film_adi;
        this.film_yil=film_yil;
        this.film_image=film_image;
        this.film_puan=film_puan;
        this.film_ozet=film_ozet;
        this.film_ozet_eng=film_ozet_eng;
        this.film_sure=film_sure;
        this.film_sure_eng=film_sure_eng;
        this.film_tur_eng=film_tur_eng;
        this.film_sinif=film_sinif;
    }

    public String getFilm_adi() {
        return film_adi;
    }

    public void setFilm_adi(String film_adi) {
        this.film_adi = film_adi;
    }

    public String getFilm_yil() {
        return film_yil;
    }

    public void setFilm_yil(String film_yil) {
        this.film_yil = film_yil;
    }

    public String getFilm_puan() {
        return film_puan;
    }

    public void setFilm_puan(String film_puan) {
        this.film_puan = film_puan;
    }

    public String getFilm_image() {
        return film_image;
    }

    public void setFilm_image(String film_image) {
        this.film_image = film_image;
    }

    public String getFilm_ozet() {
        return film_ozet;
    }

    public void setFilm_ozet(String film_ozet) {
        this.film_ozet = film_ozet;
    }

    public String getFilm_sure() {
        return film_sure;
    }

    public void setFilm_sure(String film_sure) {
        this.film_sure = film_sure;
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

    public String getFilm_id() {
        return film_id;
    }

    public void setFilm_id(String film_id) {
        this.film_id = film_id;
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

    public String getFilm_sinif() {
        return film_sinif;
    }

    public void setFilm_sinif(String film_sinif) {
        this.film_sinif = film_sinif;
    }
}
