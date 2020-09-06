package com.utkuakgungor.filmtavsiye.details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.utils.FirebaseAdapter;
import com.utkuakgungor.filmtavsiye.utils.Movie;
import com.utkuakgungor.filmtavsiye.utils.Oyuncu;
import com.utkuakgungor.filmtavsiye.utils.TMDBApi;
import com.utkuakgungor.filmtavsiye.utils.Yonetmen;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class OyuncuDetailsActivity extends AppCompatActivity {

    private String oyuncuadi,oyuncular,secenek,person_id,day,month,year,dateRespond;
    private DatabaseReference referenceFilmler,referenceOyuncular,referenceYonetmenler;
    private TextView txt_oyuncutarih;
    private TextView txt_oyuncusehir;
    private TextView txt_oyuncusite;
    private StringBuilder date;
    private List<Movie> list;
    private ImageView btnTwitter,btnInstagram,btnFacebook,btnImdb;
    private CircleImageView image_oyuncu;
    private FirebaseAdapter adapter;
    private ExpandableTextView expandableTextView;
    private RequestQueue requestQueue;
    private LinearLayout linearLayout;

    private String urlTwitter = "https://twitter.com/";
    private String urlInstagram = "https://www.instagram.com/";
    private String urlImdb = "https://www.imdb.com/name/";
    private String urlFacebook = "https://www.facebook.com/";

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences=getSharedPreferences("Ayarlar",MODE_PRIVATE);
        if(sharedPreferences.contains("Gece")){
            setTheme(R.style.AppThemeDark);
        }
        else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyuncudetails);
        getIncomingIntent();
        ActionBar actionBar=getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        expandableTextView=findViewById(R.id.detayoyuncuextext);
        TextView txt_oyuncuadi = findViewById(R.id.detayoyuncuad);
        image_oyuncu=findViewById(R.id.detayoyuncuimage);
        txt_oyuncutarih=findViewById(R.id.detayoyuncutarih);
        linearLayout=findViewById(R.id.btnlayout);
        txt_oyuncusehir=findViewById(R.id.detayoyuncusehir);
        txt_oyuncusite=findViewById(R.id.detayoyuncusite);
        RecyclerView recyclerView=findViewById(R.id.detayoyunculist);
        btnInstagram=findViewById(R.id.detayoyuncuinstagram);
        btnTwitter=findViewById(R.id.detayoyuncutwitter);
        btnFacebook=findViewById(R.id.detayoyuncufacebook);
        btnImdb=findViewById(R.id.detayoyuncuimdb);
        requestQueue= Volley.newRequestQueue(this);
        referenceFilmler= FirebaseDatabase.getInstance().getReference("Filmler");
        referenceOyuncular=FirebaseDatabase.getInstance().getReference("Oyuncular");
        referenceYonetmenler=FirebaseDatabase.getInstance().getReference("Yonetmenler");
        list=new ArrayList<>();
        date=new StringBuilder();
        adapter=new FirebaseAdapter(getBaseContext(),list);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);

        txt_oyuncuadi.setText(oyuncuadi);

        if(Objects.equals(secenek,"oyuncu")){
            updateOyuncu();
            updateOyuncuFilmler();
        }
        else{
            updateYonetmen();
            updateYonetmenFilmler();
        }
    }

    private void jsonTMDB(){
        String urlBasPerson = "https://api.themoviedb.org/3/person/";
        String urlSonPerson = "?api_key="+ TMDBApi.getApiKey() +"&language=en-US";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, urlBasPerson + person_id + urlSonPerson, null,
                response -> {
                    try {
                        if(Objects.equals(response.getString("homepage"),"null") || Objects.equals(response.getString("homepage"),"")){
                            txt_oyuncusite.setVisibility(View.GONE);
                        }
                        else{
                            txt_oyuncusite.setText(response.getString("homepage"));
                        }
                        if(Objects.equals(response.getString("biography"),"null") || Objects.equals(response.getString("biography"),"")){
                            expandableTextView.setVisibility(View.GONE);
                        }
                        else{
                            expandableTextView.setText(response.getString("biography"));
                        }
                        if(Objects.equals(response.getString("place_of_birth"),"null") || Objects.equals(response.getString("place_of_birth"),"")){
                            txt_oyuncusehir.setVisibility(View.GONE);
                        }
                        else{
                            txt_oyuncusehir.setText(response.getString("place_of_birth"));
                        }
                        dateRespond=response.getString("birthday");
                        year=dateRespond.substring(0,4);
                        month=dateRespond.substring(5,7);
                        day=dateRespond.substring(8,10);
                        date.append(day);
                        date.append(".");
                        date.append(month);
                        date.append(".");
                        date.append(year);
                        dateRespond=response.getString("deathday");
                        if(!Objects.equals(dateRespond,"null") && !Objects.equals(dateRespond,"")){
                            year=dateRespond.substring(0,4);
                            month=dateRespond.substring(5,7);
                            day=dateRespond.substring(8,10);
                            date.append("-");
                            date.append(day);
                            date.append(".");
                            date.append(month);
                            date.append(".");
                            date.append(year);
                        }
                        txt_oyuncutarih.setText(date);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void jsonSosyal(){
        String urlBasSosyal = "https://api.themoviedb.org/3/person/";
        String urlSonSosyal = "/external_ids?api_key="+ TMDBApi.getApiKey() +"&language=en-US";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, urlBasSosyal + person_id + urlSonSosyal, null,
                response -> {
                    try {
                        if(Objects.equals(response.getString("twitter_id"),"null") || Objects.equals(response.getString("twitter_id"),"")){
                            btnTwitter.setVisibility(View.GONE);
                            linearLayout.setWeightSum(linearLayout.getWeightSum()-1);
                        }
                        else{
                            btnTwitter.setVisibility(View.VISIBLE);
                            Uri uriTwitter = Uri.parse(urlTwitter+response.getString("twitter_id"));
                            Intent intentTwitter = new Intent(Intent.ACTION_VIEW, uriTwitter);
                            btnTwitter.setOnClickListener(v -> startActivity(intentTwitter));
                        }
                        if(Objects.equals(response.getString("instagram_id"),"null") || Objects.equals(response.getString("instagram_id"),"")){
                            btnInstagram.setVisibility(View.GONE);
                            linearLayout.setWeightSum(linearLayout.getWeightSum()-1);
                        }
                        else{
                            btnInstagram.setVisibility(View.VISIBLE);
                            Uri uriInstagram = Uri.parse(urlInstagram+response.getString("instagram_id"));
                            Intent intentInstagram = new Intent(Intent.ACTION_VIEW, uriInstagram);
                            btnInstagram.setOnClickListener(v -> startActivity(intentInstagram));
                        }
                        if(Objects.equals(response.getString("facebook_id"),"null") || Objects.equals(response.getString("facebook_id"),"")){
                            btnFacebook.setVisibility(View.GONE);
                            linearLayout.setWeightSum(linearLayout.getWeightSum()-1);
                        }
                        else{
                            btnFacebook.setVisibility(View.VISIBLE);
                            Uri uriFacebook = Uri.parse(urlFacebook+response.getString("facebook_id"));
                            Intent intentFacebook = new Intent(Intent.ACTION_VIEW, uriFacebook);
                            btnFacebook.setOnClickListener(v -> startActivity(intentFacebook));
                        }
                        if(Objects.equals(response.getString("imdb_id"),"null") || Objects.equals(response.getString("imdb_id"),"")){
                            btnImdb.setVisibility(View.GONE);
                            linearLayout.setWeightSum(linearLayout.getWeightSum()-1);
                        }
                        else{
                            btnImdb.setVisibility(View.VISIBLE);
                            Uri uriImdb = Uri.parse(urlImdb+response.getString("imdb_id"));
                            Intent intentImdb = new Intent(Intent.ACTION_VIEW, uriImdb);
                            btnImdb.setOnClickListener(v -> startActivity(intentImdb));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void updateYonetmenFilmler(){
        referenceFilmler.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(Objects.equals(Objects.requireNonNull(dataSnapshot.getValue(Movie.class)).getFilm_yonetmen(),oyuncuadi)){
                    list.add(dataSnapshot.getValue(Movie.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateYonetmen(){
        referenceYonetmenler.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(Objects.equals(oyuncuadi, Objects.requireNonNull(dataSnapshot.getValue(Yonetmen.class)).getYonetmen_adi())){
                    person_id= Objects.requireNonNull(dataSnapshot.getValue(Yonetmen.class)).getPerson_id();
                    Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Yonetmen.class)).getYonetmen_resim_url()).networkPolicy(NetworkPolicy.OFFLINE).into(image_oyuncu, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Yonetmen.class)).getYonetmen_resim_url()).into(image_oyuncu);
                        }
                    });
                    jsonTMDB();
                    jsonSosyal();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getIncomingIntent(){
        oyuncuadi = getIntent().getStringExtra("oyuncu");
        secenek=getIntent().getStringExtra("secenek");
    }

    private void updateOyuncuFilmler(){
        referenceFilmler.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                oyuncular=Objects.requireNonNull(dataSnapshot.getValue(Movie.class)).getFilm_oyuncular().replace(",","");
                if(oyuncular.contains(oyuncuadi)){
                    list.add(dataSnapshot.getValue(Movie.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateOyuncu(){
        referenceOyuncular.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(Objects.equals(oyuncuadi, Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_adi())){
                    person_id= Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getPerson_id();
                    Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).networkPolicy(NetworkPolicy.OFFLINE).into(image_oyuncu, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).into(image_oyuncu);
                        }
                    });
                    jsonTMDB();
                    jsonSosyal();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
