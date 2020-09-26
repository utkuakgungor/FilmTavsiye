package com.utkuakgungor.filmtavsiye.models;

public class MovieFirebase {

    private String film_id;
    private String film_tur;
    private String film_tur_eng;
    private String film_sinif;

    public MovieFirebase(){

    }

    public MovieFirebase(String film_id, String film_tur, String film_tur_eng, String film_sinif) {
        this.film_tur=film_tur;
        this.film_tur_eng=film_tur_eng;
        this.film_sinif=film_sinif;
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
