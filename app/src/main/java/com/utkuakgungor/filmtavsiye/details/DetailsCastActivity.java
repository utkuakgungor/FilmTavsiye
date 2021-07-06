package com.utkuakgungor.filmtavsiye.details;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.models.APIMovieCast;
import com.utkuakgungor.filmtavsiye.models.MovieCast;
import com.utkuakgungor.filmtavsiye.models.Oyuncu;
import com.utkuakgungor.filmtavsiye.utils.FirebaseAdapter;
import com.utkuakgungor.filmtavsiye.utils.OyuncularAdapter;
import com.utkuakgungor.filmtavsiye.utils.TMDBApi;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsCastActivity extends AppCompatActivity {

    private String movieID;
    private List<Oyuncu> result;
    private RequestQueue requestQueue;
    private OyuncularAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        movieID=getIntent().getStringExtra("id");
        requestQueue = Volley.newRequestQueue(this);
        result =new ArrayList<>();
        adapter = new OyuncularAdapter(this, result);
        final RecyclerView recyclerView = findViewById(R.id.castRecyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        getCast();
    }

    private void getCast() {
        String url = "https://api.themoviedb.org/3/movie/" + movieID + "/credits?api_key=" + TMDBApi.getApiKey();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    String jsonString = response.toString();
                    Gson gson = new Gson();
                    APIMovieCast dataCast = gson.fromJson(jsonString, APIMovieCast.class);
                    getOyuncu(dataCast.getMovieCast());
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void getOyuncu(List<MovieCast> dataCast) {
        for(int i=0;i<dataCast.size();i++){
            String url = "https://api.themoviedb.org/3/person/" + dataCast.get(i).getId() + "?api_key=" + TMDBApi.getApiKey();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            Oyuncu oyuncu = new Oyuncu();
                            oyuncu.setOyuncuAdi(response.getString("name"));
                            oyuncu.setOyuncuID(response.getString("id"));
                            oyuncu.setOyuncuResim("https://image.tmdb.org/t/p/original" + response.getString("profile_path"));
                            result.add(oyuncu);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace);
            requestQueue.add(request);
        }
    }
}
