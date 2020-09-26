
package com.utkuakgungor.filmtavsiye.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIMovieCast implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<MovieCast> cast = null;
    @SerializedName("crew")
    @Expose
    private List<MovieCrew> crew = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieCast> getMovieCast() {
        return cast;
    }

    public void setMovieCast(List<MovieCast> cast) {
        this.cast = cast;
    }

    public List<MovieCrew> getMovieCrew() {
        return crew;
    }

    public void setMovieCrew(List<MovieCrew> crew) {
        this.crew = crew;
    }

}
