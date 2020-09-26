
package com.utkuakgungor.filmtavsiye.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIMovieVideos {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
