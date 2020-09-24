package com.utkuakgungor.filmtavsiye.details;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Pair;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import android.view.View;
import com.google.android.material.button.MaterialButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.utils.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Arrays;

public class DetailsActivity extends AppCompatActivity {

    private Movie movie;
    private RequestQueue requestQueue;
    private TextView oyuncuisim1,oyuncuisim2,oyuncuisim3,oyuncuisim4,oyuncuisim5,oyuncuisim6;
    private ImageView imageView;
    private CircleImageView image_yonetmen,oyuncuresim1,oyuncuresim2,oyuncuresim3,oyuncuresim4,oyuncuresim5,oyuncuresim6;
    private Favorites favorites;
    private FloatingActionButton fab;
    private List<String> oyuncuList,resimList,sinifList;
    private int dotscount;
    private ImageView[] dots;
    private int sayac=0;
    private Snackbar snackbar;
    private View view;
    private TextView snackbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);
        movie=new Movie();
        oyuncuList= new ArrayList<>();
        resimList= new ArrayList<>();
        sinifList= new ArrayList<>();
        getIncomingIntent();
        final Pair[] pairs = new Pair[2];
        favorites=new Favorites(DetailsActivity.this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference_yonetmen = database.getReference("Yonetmenler");
        reference_yonetmen.keepSynced(true);
        DatabaseReference reference_oyuncu = database.getReference("Oyuncular");
        reference_oyuncu.keepSynced(true);
        image_yonetmen=findViewById(R.id.detayyonetmenresim);
        YouTubePlayerView youTubePlayerView = findViewById(R.id.detay_youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        TextView textFilmYili = findViewById(R.id.detayYilDegisken);
        TextView textPuan = findViewById(R.id.detayPuan);
        TextView textOzet = findViewById(R.id.detayOzet);
        LinearLayout linearLayout = findViewById(R.id.detay_dots);
        Toolbar toolbar = findViewById(R.id.detay_toolbar);
        ViewPager viewPager = findViewById(R.id.detay_viewpager);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ChipGroup chipGroup = findViewById(R.id.detaytur);
        requestQueue= Volley.newRequestQueue(this);
        imageView=findViewById(R.id.detayImage);
        TextView textSure = findViewById(R.id.detaySureDegisken);
        final TextView textYonetmen = findViewById(R.id.detayyonetmen);
        fab=findViewById(R.id.detayFab);
        CollapsingToolbarLayout textFilmAdi = findViewById(R.id.collapsing_toolbar);
        ImageView image_aile = findViewById(R.id.detayaile);
        ImageView image_cinsellik = findViewById(R.id.detaycinsellik);
        ImageView image_olumsuz = findViewById(R.id.detayolumsuz);
        ImageView image_siddet = findViewById(R.id.detaysiddet);
        ImageView image_7 = findViewById(R.id.detay7);
        ImageView image_13 = findViewById(R.id.detay13);
        ImageView image_15 = findViewById(R.id.detay15);
        ImageView image_18 = findViewById(R.id.detay18);
        oyuncuisim1=findViewById(R.id.detayoyuncu1);
        oyuncuisim2=findViewById(R.id.detayoyuncu2);
        oyuncuisim3=findViewById(R.id.detayoyuncu3);
        oyuncuisim4=findViewById(R.id.detayoyuncu4);
        oyuncuisim5=findViewById(R.id.detayoyuncu5);
        oyuncuisim6=findViewById(R.id.detayoyuncu6);
        oyuncuresim1=findViewById(R.id.detayoyuncuresim1);
        oyuncuresim2=findViewById(R.id.detayoyuncuresim2);
        oyuncuresim3=findViewById(R.id.detayoyuncuresim3);
        oyuncuresim4=findViewById(R.id.detayoyuncuresim4);
        oyuncuresim5=findViewById(R.id.detayoyuncuresim5);
        oyuncuresim6=findViewById(R.id.detayoyuncuresim6);
        RelativeLayout oyuncu1_relative = findViewById(R.id.detayoyuncuRelative1);
        RelativeLayout oyuncu2_relative = findViewById(R.id.detayoyuncuRelative2);
        RelativeLayout oyuncu3_relative = findViewById(R.id.detayoyuncuRelative3);
        RelativeLayout oyuncu4_relative = findViewById(R.id.detayoyuncuRelative4);
        RelativeLayout oyuncu5_relative = findViewById(R.id.detayoyuncuRelative5);
        RelativeLayout oyuncu6_relative = findViewById(R.id.detayoyuncuRelative6);
        RelativeLayout yonetmen_relative = findViewById(R.id.detayyonetmenRelative);
        MaterialButton diger = findViewById(R.id.detaydiger);
        oyuncuList=Arrays.asList(movie.getFilm_oyuncular().split(", "));
        resimList = Arrays.asList(movie.getFilm_resimler().split(", "));
        sinifList = Arrays.asList(movie.getFilm_sinif().split(", "));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(DetailsActivity.this, resimList);
        viewPager.setAdapter(viewPagerAdapter);
        dotscount=viewPagerAdapter.getCount();
        dots=new ImageView[dotscount];
        for (int i=0;i<dotscount;i++){
            dots[i]=new ImageView(DetailsActivity.this);
            dots[i].setImageResource(R.drawable.ic_nonactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            linearLayout.addView(dots[i],params);

        }
        dots[0].setImageResource(R.drawable.ic_active);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<dotscount;i++){
                    dots[i].setImageResource(R.drawable.ic_nonactive);
                }
                dots[position].setImageResource(R.drawable.ic_active);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        String[] turler = movie.getFilm_tur().split(", ");
        String[] turler_eng = movie.getFilm_tur_eng().split(", ");

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(movie.getYoutube(),0);
            }
        });

        for(sayac=0; sayac< turler.length; sayac++){
            if(Objects.equals(Locale.getDefault().getDisplayLanguage(),"Türkçe" )){
                final Chip chip = new Chip(chipGroup.getContext());
                chip.setText(turler[sayac]);
                chip.setTextSize(14);
                chip.setOnClickListener(v -> {
                    Intent turactivity = new Intent(DetailsActivity.this, TurlerDetailsActivity.class);
                    ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this);
                    turactivity.putExtra("tur",chip.getText());
                    startActivity(turactivity,activityOptions.toBundle());
                });
                chipGroup.addView(chip);
            }
            else{
                final Chip chip = new Chip(chipGroup.getContext());
                chip.setText(turler_eng[sayac]);
                chip.setTextSize(14);
                chip.setOnClickListener(v -> {
                    Intent turactivity = new Intent(DetailsActivity.this, TurlerDetailsActivity.class);
                    ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this);
                    turactivity.putExtra("tur",chip.getText());
                    startActivity(turactivity,activityOptions.toBundle());
                });
                chipGroup.addView(chip);
            }
        }

        if(!favorites.Kontrol(movie.getFilm_adi())){
            Drawable favFab = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_favorite_ekli,getTheme());
            assert favFab != null;
            Drawable whiteFav = Objects.requireNonNull(favFab.getConstantState()).newDrawable();
            whiteFav.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            fab.setImageDrawable(whiteFav);
        }
        else{
            Drawable favFab = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_favorite_beyaz,getTheme());
            assert favFab != null;
            Drawable whiteFav = Objects.requireNonNull(favFab.getConstantState()).newDrawable();
            whiteFav.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            fab.setImageDrawable(whiteFav);
        }

        diger.setOnClickListener(v -> {
            CustomDialogClass customDialogClass = new CustomDialogClass();
            Bundle bundle= new Bundle();
            bundle.putStringArray("oyuncular",oyuncuList.toArray(new String[oyuncuList.size()]));
            customDialogClass.setArguments(bundle);
            customDialogClass.show(getSupportFragmentManager(),"Custom Dialog");
        });

        if(Objects.equals(Locale.getDefault().getDisplayLanguage(),"Türkçe" )){
            textOzet.setText(movie.getOzet());
            textSure.setText(movie.getSure());
        }
        else {
            textSure.setText(movie.getFilm_sure_eng());
            textOzet.setText(movie.getFilm_ozet_eng());
        }
        if(Objects.equals(movie.getText_color(),"evet")){
            int color = Color.parseColor("#000000");
            textFilmAdi.setExpandedTitleColor(color);
        }
        textYonetmen.setText(movie.getFilm_yonetmen());
        textFilmAdi.setTitle(movie.getFilm_adi());
        textFilmYili.setText(movie.getFilm_yili());
        textPuan.setText(movie.getFilm_puani());
        imageView.setOnClickListener(v -> {
            Intent imageActivity = new Intent(DetailsActivity.this,ImageDetails.class);
            imageActivity.putExtra("image",movie.getImage());
            Pair[] pair = new Pair[1];
            pair[0] = new Pair<View,String>(imageView,"cardPicture");
            ActivityOptions activityOptions=ActivityOptions
                    .makeSceneTransitionAnimation(DetailsActivity.this,pair);
            startActivity(imageActivity,activityOptions.toBundle());
        });
        Picasso.get().load(movie.getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(movie.getImage()).into(imageView);
            }
        });

        fab.setOnClickListener(v -> {
            if (favorites.Kontrol(movie.getFilm_adi())){
                favorites.ekle(movie);
                Snackbar.make(v,getResources().getString(R.string.favoriekle),Snackbar.LENGTH_LONG).show();
                Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_favorite_ekli,getTheme());
                assert myFabSrc != null;
                Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
                willBeWhite.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                fab.setImageDrawable(willBeWhite);
            }else {
                favorites.deleteData(movie.getFilm_adi());
                Snackbar.make(v,getResources().getString(R.string.favoricikti),Snackbar.LENGTH_LONG).show();
                Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_favorite_beyaz,getTheme());
                assert myFabSrc != null;
                Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
                willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                fab.setImageDrawable(willBeWhite);
            }
        });

        reference_yonetmen.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                if(Objects.equals(movie.getFilm_yonetmen(),Objects.requireNonNull(dataSnapshot.getValue(Yonetmen.class)).getYonetmen_adi())){
                    jsonTMDB(dataSnapshot.getValue(Yonetmen.class).getPerson_id(),image_yonetmen);
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

        reference_oyuncu.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                for(sayac=0;sayac<6;sayac++){
                    if(Objects.equals(oyuncuList.get(sayac),Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_adi())){
                        if(Objects.equals(oyuncuisim1.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim1.setText(oyuncuList.get(sayac));
                            jsonTMDB(dataSnapshot.getValue(Oyuncu.class).getPerson_id(),oyuncuresim1);
                        }
                        else if(Objects.equals(oyuncuisim2.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim2.setText(oyuncuList.get(sayac));
                            jsonTMDB(dataSnapshot.getValue(Oyuncu.class).getPerson_id(),oyuncuresim2);
                        }
                        else if(Objects.equals(oyuncuisim3.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim3.setText(oyuncuList.get(sayac));
                            jsonTMDB(dataSnapshot.getValue(Oyuncu.class).getPerson_id(),oyuncuresim3);
                        }
                        else if(Objects.equals(oyuncuisim4.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim4.setText(oyuncuList.get(sayac));
                            jsonTMDB(dataSnapshot.getValue(Oyuncu.class).getPerson_id(),oyuncuresim4);

                        }
                        else if(Objects.equals(oyuncuisim5.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim5.setText(oyuncuList.get(sayac));
                            jsonTMDB(dataSnapshot.getValue(Oyuncu.class).getPerson_id(),oyuncuresim5);

                        }
                        else if(Objects.equals(oyuncuisim6.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim6.setText(oyuncuList.get(sayac));
                            jsonTMDB(dataSnapshot.getValue(Oyuncu.class).getPerson_id(),oyuncuresim6);
                        }
                    }
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
        if(sinifList.contains("Cinsellik")){
            image_cinsellik.setVisibility(View.VISIBLE);
            image_cinsellik.setOnClickListener(v -> {
                Snackbar.make(v,getResources().getString(R.string.cinsellik),Snackbar.LENGTH_LONG).show();
            });
        }
        else{
            image_cinsellik.setVisibility(View.GONE);
        }
        if(sinifList.contains("Şiddet")){
            image_siddet.setVisibility(View.VISIBLE);
            image_siddet.setOnClickListener(v -> {
                Snackbar.make(v,getResources().getString(R.string.siddet),Snackbar.LENGTH_LONG).show();
            });
        }
        else{
            image_siddet.setVisibility(View.GONE);
        }
        if(sinifList.contains("Olumsuz")){
            image_olumsuz.setVisibility(View.VISIBLE);
            image_olumsuz.setOnClickListener(v -> {
                Snackbar.make(v,getResources().getString(R.string.olmsuz),Snackbar.LENGTH_LONG).show();
            });
        }
        else{
            image_olumsuz.setVisibility(View.GONE);
        }
        if(sinifList.contains("Aile")){
            image_aile.setVisibility(View.VISIBLE);
            image_aile.setOnClickListener(v -> {
                Snackbar.make(v,getResources().getString(R.string.aile),Snackbar.LENGTH_LONG).show();
            });
        }
        else{
            image_aile.setVisibility(View.GONE);
        }
        if(sinifList.contains("7")){
            image_7.setVisibility(View.VISIBLE);
            image_7.setOnClickListener(v -> {
                Snackbar.make(v,getResources().getString(R.string.yas7),Snackbar.LENGTH_LONG).show();
            });
        }
        else{
            image_7.setVisibility(View.GONE);
        }
        if(sinifList.contains("13")){
            image_13.setVisibility(View.VISIBLE);
            image_13.setOnClickListener(v -> {
                Snackbar.make(v,getResources().getString(R.string.yas13),Snackbar.LENGTH_LONG).show();
            });
        }
        else{
            image_13.setVisibility(View.GONE);
        }
        if(sinifList.contains("15")){
            image_15.setVisibility(View.VISIBLE);
            image_15.setOnClickListener(v ->{
                Snackbar.make(v,getResources().getString(R.string.yas15),Snackbar.LENGTH_LONG).show();
            });
        }
        else{
            image_15.setVisibility(View.GONE);
        }
        if(sinifList.contains("18")){
            image_18.setVisibility(View.VISIBLE);
            image_18.setOnClickListener(v -> {
                Snackbar.make(v,getResources().getString(R.string.yas18),Snackbar.LENGTH_LONG).show();
            });
        }
        else{
            image_18.setVisibility(View.GONE);
        }
        oyuncu1_relative.setOnClickListener(v -> {
            Intent oyuncu1 = new Intent(DetailsActivity.this,OyuncuDetailsActivity.class);
            pairs[0] = new Pair<View,String>(oyuncuresim1,"picture");
            pairs[1] = new Pair<View,String>(oyuncuisim1,"text");
            ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this,pairs);
            oyuncu1.putExtra("oyuncu",oyuncuisim1.getText());
            oyuncu1.putExtra("secenek","oyuncu");
            startActivity(oyuncu1,activityOptions.toBundle());
        });
        oyuncu2_relative.setOnClickListener(v -> {
            Intent oyuncu2 = new Intent(DetailsActivity.this,OyuncuDetailsActivity.class);
            pairs[0] = new Pair<View,String>(oyuncuresim2,"picture");
            pairs[1] = new Pair<View,String>(oyuncuisim2,"text");
            ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this,pairs);
            oyuncu2.putExtra("oyuncu",oyuncuisim2.getText());
            oyuncu2.putExtra("secenek","oyuncu");
            startActivity(oyuncu2,activityOptions.toBundle());
        });
        oyuncu3_relative.setOnClickListener(v -> {
            Intent oyuncu3 = new Intent(DetailsActivity.this,OyuncuDetailsActivity.class);
            pairs[0] = new Pair<View,String>(oyuncuresim3,"picture");
            pairs[1] = new Pair<View,String>(oyuncuisim3,"text");
            ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this,pairs);
            oyuncu3.putExtra("oyuncu",oyuncuisim3.getText());
            oyuncu3.putExtra("secenek","oyuncu");
            startActivity(oyuncu3,activityOptions.toBundle());
        });
        oyuncu4_relative.setOnClickListener(v -> {
            Intent oyuncu4 = new Intent(DetailsActivity.this,OyuncuDetailsActivity.class);
            pairs[0] = new Pair<View,String>(oyuncuresim4,"picture");
            pairs[1] = new Pair<View,String>(oyuncuisim4,"text");
            ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this,pairs);
            oyuncu4.putExtra("oyuncu",oyuncuisim4.getText());
            oyuncu4.putExtra("secenek","oyuncu");
            startActivity(oyuncu4,activityOptions.toBundle());
        });
        oyuncu5_relative.setOnClickListener(v -> {
            Intent oyuncu5 = new Intent(DetailsActivity.this,OyuncuDetailsActivity.class);
            pairs[0] = new Pair<View,String>(oyuncuresim5,"picture");
            pairs[1] = new Pair<View,String>(oyuncuisim5,"text");
            ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this,pairs);
            oyuncu5.putExtra("oyuncu",oyuncuisim5.getText());
            oyuncu5.putExtra("secenek","oyuncu");
            startActivity(oyuncu5,activityOptions.toBundle());
        });
        oyuncu6_relative.setOnClickListener(v -> {
            Intent oyuncu6 = new Intent(DetailsActivity.this,OyuncuDetailsActivity.class);
            pairs[0] = new Pair<View,String>(oyuncuresim6,"picture");
            pairs[1] = new Pair<View,String>(oyuncuisim6,"text");
            ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this,pairs);
            oyuncu6.putExtra("oyuncu",oyuncuisim6.getText());
            oyuncu6.putExtra("secenek","oyuncu");
            startActivity(oyuncu6,activityOptions.toBundle());
        });
        yonetmen_relative.setOnClickListener(v -> {
            Intent yonetmen = new Intent(DetailsActivity.this,OyuncuDetailsActivity.class);
            pairs[0] = new Pair<View,String>(image_yonetmen,"picture");
            pairs[1] = new Pair<View,String>(textYonetmen,"text");
            ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this,pairs);
            yonetmen.putExtra("oyuncu",textYonetmen.getText());
            yonetmen.putExtra("secenek","yonetmen");
            startActivity(yonetmen,activityOptions.toBundle());
        });
    }

    private void jsonTMDB(String id,CircleImageView imageView){
        String urlBasPerson = "https://api.themoviedb.org/3/person/";
        String urlSonPerson = "?api_key="+ TMDBApi.getApiKey() +"&language=en-US";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, urlBasPerson + id + urlSonPerson, null,
                response -> {
                    try {
                        if(Objects.equals(response.getString("profile_path"),"null") || Objects.equals(response.getString("profile_path"),"")){
                            Picasso.get().load(R.drawable.ic_person).into(imageView);
                        }
                        else{
                            Picasso.get().load("https://image.tmdb.org/t/p/original"+response.getString("profile_path")).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    try {
                                        Picasso.get().load("https://image.tmdb.org/t/p/original"+response.getString("profile_path")).into(imageView);
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void getIncomingIntent(){
        movie.setFilm_adi(getIntent().getStringExtra("ad"));
        movie.setFilm_yili(getIntent().getStringExtra("yil"));
        movie.setFilm_puani(getIntent().getStringExtra("puan"));
        movie.setImage(getIntent().getStringExtra("image"));
        movie.setYoutube(getIntent().getStringExtra("youtube"));
        movie.setOzet(getIntent().getStringExtra("ozet"));
        movie.setSure(getIntent().getStringExtra("sure"));
        movie.setFilm_ozet_eng(getIntent().getStringExtra("ozet_eng"));
        movie.setFilm_sure_eng(getIntent().getStringExtra("sure_eng"));
        movie.setText_color(getIntent().getStringExtra("color"));
        movie.setFilm_oyuncular(getIntent().getStringExtra("oyuncular"));
        movie.setFilm_tur(getIntent().getStringExtra("tur"));
        movie.setFilm_tur_eng(getIntent().getStringExtra("tur_eng"));
        movie.setFilm_yonetmen(getIntent().getStringExtra("yonetmen"));
        movie.setFilm_sinif(getIntent().getStringExtra("sinif"));
        movie.setFilm_resimler(getIntent().getStringExtra("resimler"));
    }
}
