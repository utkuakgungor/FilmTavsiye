package com.utkuakgungor.filmtavsiye.details;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Pair;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.models.APIMovieCast;
import com.utkuakgungor.filmtavsiye.models.APIMoviePictures;
import com.utkuakgungor.filmtavsiye.models.APIMovieVideos;
import com.utkuakgungor.filmtavsiye.models.Movie;
import com.utkuakgungor.filmtavsiye.models.MovieFirebase;
import com.utkuakgungor.filmtavsiye.utils.*;

import org.json.JSONException;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Arrays;

public class DetailsActivity extends YouTubeBaseActivity {

    private FirebaseAuth firebaseAuth;
    private Movie movie;
    private RequestQueue requestQueue;
    private TextView oyuncuisim1, oyuncuisim2, oyuncuisim3, oyuncuisim4, oyuncuisim5, oyuncuisim6;
    private ImageView imageView;
    private CircleImageView image_yonetmen, oyuncuresim1, oyuncuresim2, oyuncuresim3, oyuncuresim4, oyuncuresim5, oyuncuresim6;
    private Favorites favorites;
    private TextView textYonetmen;
    private String yonetmenID, oyuncu1ID, oyuncu2ID, oyuncu3ID, oyuncu4ID, oyuncu5ID, oyuncu6ID;
    private FloatingActionButton fab;
    private List<String> sinifList, resimlerList;
    private List<String> firebaseList, firebaseKeys;
    private DatabaseReference reference_favoriler;
    private MaterialButton diger;
    private int dotscount;
    private ImageView[] dots;
    private int sayac;
    private ViewPager viewPager;
    private RelativeLayout yonetmen_relative, oyuncu1_relative, oyuncu2_relative, oyuncu3_relative, oyuncu4_relative, oyuncu5_relative, oyuncu6_relative;
    private LinearLayout linearLayout;
    private Pair[] pairs;
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            reference_favoriler = FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail().replace(".", "").replace("#", "").replace("$", "").replace("[", "").replace("]", "")));
        }
        movie = new Movie();
        resimlerList = new ArrayList<>();
        sinifList = new ArrayList<>();
        pairs = new Pair[2];
        sayac = 0;
        getIncomingIntent();
        favorites = new Favorites(DetailsActivity.this);
        image_yonetmen = findViewById(R.id.detayyonetmenresim);
        TextView textFilmYili = findViewById(R.id.detayYilDegisken);
        youTubePlayerView = findViewById(R.id.detay_youtube_player_view);
        TextView textPuan = findViewById(R.id.detayPuan);
        TextView textOzet = findViewById(R.id.detayOzet);
        linearLayout = findViewById(R.id.detay_dots);
        Toolbar toolbar = findViewById(R.id.detay_toolbar);
        viewPager = findViewById(R.id.detay_viewpager);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ChipGroup chipGroup = findViewById(R.id.detaytur);
        requestQueue = Volley.newRequestQueue(this);
        imageView = findViewById(R.id.detayImage);
        TextView textSure = findViewById(R.id.detaySureDegisken);
        textYonetmen = findViewById(R.id.detayyonetmen);
        fab = findViewById(R.id.detayFab);
        CollapsingToolbarLayout textFilmAdi = findViewById(R.id.collapsing_toolbar);
        ImageView image_aile = findViewById(R.id.detayaile);
        ImageView image_cinsellik = findViewById(R.id.detaycinsellik);
        ImageView image_olumsuz = findViewById(R.id.detayolumsuz);
        ImageView image_siddet = findViewById(R.id.detaysiddet);
        ImageView image_7 = findViewById(R.id.detay7);
        ImageView image_13 = findViewById(R.id.detay13);
        ImageView image_15 = findViewById(R.id.detay15);
        ImageView image_18 = findViewById(R.id.detay18);
        oyuncuisim1 = findViewById(R.id.detayoyuncu1);
        oyuncuisim2 = findViewById(R.id.detayoyuncu2);
        oyuncuisim3 = findViewById(R.id.detayoyuncu3);
        oyuncuisim4 = findViewById(R.id.detayoyuncu4);
        oyuncuisim5 = findViewById(R.id.detayoyuncu5);
        oyuncuisim6 = findViewById(R.id.detayoyuncu6);
        oyuncuresim1 = findViewById(R.id.detayoyuncuresim1);
        oyuncuresim2 = findViewById(R.id.detayoyuncuresim2);
        firebaseList = new ArrayList<>();
        firebaseKeys = new ArrayList<>();
        oyuncuresim3 = findViewById(R.id.detayoyuncuresim3);
        oyuncuresim4 = findViewById(R.id.detayoyuncuresim4);
        oyuncuresim5 = findViewById(R.id.detayoyuncuresim5);
        oyuncuresim6 = findViewById(R.id.detayoyuncuresim6);
        oyuncu1_relative = findViewById(R.id.detayoyuncuRelative1);
        oyuncu2_relative = findViewById(R.id.detayoyuncuRelative2);
        oyuncu3_relative = findViewById(R.id.detayoyuncuRelative3);
        oyuncu4_relative = findViewById(R.id.detayoyuncuRelative4);
        oyuncu5_relative = findViewById(R.id.detayoyuncuRelative5);
        oyuncu6_relative = findViewById(R.id.detayoyuncuRelative6);
        yonetmen_relative = findViewById(R.id.detayyonetmenRelative);
        diger = findViewById(R.id.detaydiger);
        sinifList = Arrays.asList(movie.getFilm_sinif().split(", "));
        getPictures();

        String[] turler = movie.getFilm_tur().split(", ");
        String[] turler_eng = movie.getFilm_tur_eng().split(", ");
        getYoutube();
        for (sayac = 0; sayac < turler.length; sayac++) {
            if (Objects.equals(Locale.getDefault().getDisplayLanguage(), "Türkçe")) {
                final Chip chip = new Chip(chipGroup.getContext());
                chip.setText(turler[sayac]);
                if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                    chip.setChipStrokeColorResource(R.color.colorWhite);
                }
                chip.setTextSize(14);
                chip.setOnClickListener(v -> {
                    Intent turactivity = new Intent(DetailsActivity.this, TurlerDetailsActivity.class);
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this);
                    turactivity.putExtra("tur", chip.getText());
                    startActivity(turactivity, activityOptions.toBundle());
                });
                chipGroup.addView(chip);
            } else {
                final Chip chip = new Chip(chipGroup.getContext());
                chip.setText(turler_eng[sayac]);
                if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                    chip.setChipStrokeColorResource(R.color.colorWhite);
                }
                chip.setTextSize(14);
                chip.setOnClickListener(v -> {
                    Intent turactivity = new Intent(DetailsActivity.this, TurlerDetailsActivity.class);
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this);
                    turactivity.putExtra("tur", chip.getText());
                    startActivity(turactivity, activityOptions.toBundle());
                });
                chipGroup.addView(chip);
            }
        }

        Drawable favFab = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_beyaz, getTheme());
        assert favFab != null;
        Drawable whiteFav = Objects.requireNonNull(favFab.getConstantState()).newDrawable();
        whiteFav.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        fab.setImageDrawable(whiteFav);

        if (firebaseAuth.getCurrentUser() != null) {
            reference_favoriler.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    firebaseList.add(snapshot.getValue(MovieFirebase.class).getFilm_id());
                    firebaseKeys.add(snapshot.getKey());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference_favoriler.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (firebaseList.contains(movie.getFilm_id())) {
                        Drawable favFabFirebase = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_ekli, getTheme());
                        Drawable whiteFavFirebase = Objects.requireNonNull(favFabFirebase.getConstantState()).newDrawable();
                        whiteFavFirebase.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                        fab.setImageDrawable(whiteFavFirebase);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            if (!favorites.Kontrol(movie.getFilm_id())) {
                favFab = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_ekli, getTheme());
                whiteFav = Objects.requireNonNull(favFab.getConstantState()).newDrawable();
                whiteFav.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                fab.setImageDrawable(whiteFav);
            }
        }

        if (Objects.equals(Locale.getDefault().getDisplayLanguage(), "Türkçe")) {
            textOzet.setText(movie.getFilm_ozet());
            textSure.setText(movie.getFilm_sure());
        } else {
            textSure.setText(movie.getFilm_sure_eng());
            textOzet.setText(movie.getFilm_ozet_eng());
        }
        textFilmAdi.setTitle(movie.getFilm_adi());
        textFilmYili.setText(movie.getFilm_yil());
        textPuan.setText(movie.getFilm_puan());
        imageView.setOnClickListener(v -> {
            Intent imageActivity = new Intent(DetailsActivity.this, ImageDetailsActivity.class);
            imageActivity.putExtra("image", movie.getFilm_image());
            Pair[] pair = new Pair[1];
            pair[0] = new Pair<View, String>(imageView, "cardPicture");
            ActivityOptions activityOptions = ActivityOptions
                    .makeSceneTransitionAnimation(DetailsActivity.this, pair);
            startActivity(imageActivity, activityOptions.toBundle());
        });
        Picasso.get().load(movie.getFilm_image()).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(movie.getFilm_image()).into(imageView);
            }
        });

        fab.setOnClickListener(v -> {
            if (firebaseAuth.getCurrentUser() != null) {
                firebaseList = new ArrayList<>();
                firebaseKeys = new ArrayList<>();
                reference_favoriler.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        firebaseList.add(snapshot.getValue(MovieFirebase.class).getFilm_id());
                        firebaseKeys.add(snapshot.getKey());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                reference_favoriler.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (firebaseList.contains(movie.getFilm_id())) {
                            reference_favoriler.child(firebaseKeys.get(firebaseList.indexOf(movie.getFilm_id()))).removeValue();
                            Snackbar.make(v, getResources().getString(R.string.favoricikti), Snackbar.LENGTH_LONG).show();
                            Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_beyaz, getTheme());
                            assert myFabSrc != null;
                            Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
                            willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                            fab.setImageDrawable(willBeWhite);
                        } else {
                            String id = reference_favoriler.push().getKey();
                            MovieFirebase movieFirebase = new MovieFirebase();
                            movieFirebase.setFilm_id(movie.getFilm_id());
                            movieFirebase.setFilm_sinif(movie.getFilm_sinif());
                            movieFirebase.setFilm_tur(movie.getFilm_tur());
                            movieFirebase.setFilm_tur_eng(movie.getFilm_tur_eng());
                            reference_favoriler.child(Objects.requireNonNull(id)).setValue(movie);
                            Snackbar.make(v, getResources().getString(R.string.favoriekle), Snackbar.LENGTH_LONG).show();
                            Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_ekli, getTheme());
                            assert myFabSrc != null;
                            Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
                            willBeWhite.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                            fab.setImageDrawable(willBeWhite);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                if (favorites.Kontrol(movie.getFilm_id())) {
                    favorites.ekle(movie);
                    Snackbar.make(v, getResources().getString(R.string.favoriekle), Snackbar.LENGTH_LONG).show();
                    Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_ekli, getTheme());
                    assert myFabSrc != null;
                    Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
                    willBeWhite.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                    fab.setImageDrawable(willBeWhite);
                } else {
                    favorites.deleteData(movie.getFilm_id());
                    Snackbar.make(v, getResources().getString(R.string.favoricikti), Snackbar.LENGTH_LONG).show();
                    Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_beyaz, getTheme());
                    assert myFabSrc != null;
                    Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
                    willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    fab.setImageDrawable(willBeWhite);
                }
            }
        });
        getCast();

        if (sinifList.contains("Cinsellik")) {
            image_cinsellik.setVisibility(View.VISIBLE);
            image_cinsellik.setOnClickListener(v -> {
                Snackbar.make(v, getResources().getString(R.string.cinsellik), Snackbar.LENGTH_LONG).show();
            });
        } else {
            image_cinsellik.setVisibility(View.GONE);
        }
        if (sinifList.contains("Şiddet")) {
            image_siddet.setVisibility(View.VISIBLE);
            image_siddet.setOnClickListener(v -> {
                Snackbar.make(v, getResources().getString(R.string.siddet), Snackbar.LENGTH_LONG).show();
            });
        } else {
            image_siddet.setVisibility(View.GONE);
        }
        if (sinifList.contains("Olumsuz")) {
            image_olumsuz.setVisibility(View.VISIBLE);
            image_olumsuz.setOnClickListener(v -> {
                Snackbar.make(v, getResources().getString(R.string.olmsuz), Snackbar.LENGTH_LONG).show();
            });
        } else {
            image_olumsuz.setVisibility(View.GONE);
        }
        if (sinifList.contains("Aile")) {
            image_aile.setVisibility(View.VISIBLE);
            image_aile.setOnClickListener(v -> {
                Snackbar.make(v, getResources().getString(R.string.aile), Snackbar.LENGTH_LONG).show();
            });
        } else {
            image_aile.setVisibility(View.GONE);
        }
        if (sinifList.contains("7")) {
            image_7.setVisibility(View.VISIBLE);
            image_7.setOnClickListener(v -> {
                Snackbar.make(v, getResources().getString(R.string.yas7), Snackbar.LENGTH_LONG).show();
            });
        } else {
            image_7.setVisibility(View.GONE);
        }
        if (sinifList.contains("13")) {
            image_13.setVisibility(View.VISIBLE);
            image_13.setOnClickListener(v -> {
                Snackbar.make(v, getResources().getString(R.string.yas13), Snackbar.LENGTH_LONG).show();
            });
        } else {
            image_13.setVisibility(View.GONE);
        }
        if (sinifList.contains("15")) {
            image_15.setVisibility(View.VISIBLE);
            image_15.setOnClickListener(v -> {
                Snackbar.make(v, getResources().getString(R.string.yas15), Snackbar.LENGTH_LONG).show();
            });
        } else {
            image_15.setVisibility(View.GONE);
        }
        if (sinifList.contains("18")) {
            image_18.setVisibility(View.VISIBLE);
            image_18.setOnClickListener(v -> {
                Snackbar.make(v, getResources().getString(R.string.yas18), Snackbar.LENGTH_LONG).show();
            });
        } else {
            image_18.setVisibility(View.GONE);
        }
    }

    private void getPictures() {
        String url = "https://api.themoviedb.org/3/movie/" + movie.getFilm_id() + "/images?api_key=" + TMDBApi.getApiKey();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    String jsonString = response.toString();
                    Gson gson = new Gson();
                    APIMoviePictures dataPictures = gson.fromJson(jsonString, APIMoviePictures.class);
                    for (int i = 0; i < dataPictures.getBackdrops().size(); i++) {
                        if (Double.compare(Double.parseDouble(dataPictures.getBackdrops().get(i).getAspectRatio()), 1) > 0) {
                            resimlerList.add("https://image.tmdb.org/t/p/original" + dataPictures.getBackdrops().get(i).getFilePath());
                        }
                    }
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(DetailsActivity.this, resimlerList);
                    viewPager.setAdapter(viewPagerAdapter);
                    dotscount = viewPagerAdapter.getCount() < 10 ? viewPagerAdapter.getCount() : 10;
                    dots = new ImageView[dotscount];
                    for (int i = 0; i < dotscount; i++) {
                        dots[i] = new ImageView(DetailsActivity.this);
                        dots[i].setImageResource(R.drawable.ic_nonactive);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8, 0, 8, 0);
                        linearLayout.addView(dots[i], params);

                    }
                    dots[0].setImageResource(R.drawable.ic_active);
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            for (int i = 0; i < dotscount; i++) {
                                dots[i].setImageResource(R.drawable.ic_nonactive);
                            }
                            dots[position].setImageResource(R.drawable.ic_active);

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void getYoutube() {
        String url = "https://api.themoviedb.org/3/movie/" + movie.getFilm_id() + "/videos?api_key=" + TMDBApi.getApiKey();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    String jsonString = response.toString();
                    Gson gson = new Gson();
                    APIMovieVideos dataVideos = gson.fromJson(jsonString, APIMovieVideos.class);
                    for (int i = 0; i < dataVideos.getResults().size(); i++) {
                        if (dataVideos.getResults().get(i).getSite().toLowerCase().equals("youtube")
                                && dataVideos.getResults().get(i).getType().toLowerCase().equals("trailer")) {
                            int finalI = i;
                            youTubePlayerView.initialize(YoutubeApi.getApiKey(), new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                    youTubePlayer.cueVideo(dataVideos.getResults().get(finalI).getKey());
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });
                            break;
                        }
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void getCast() {
        String url = "https://api.themoviedb.org/3/movie/" + movie.getFilm_id() + "/credits?api_key=" + TMDBApi.getApiKey();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    String jsonString = response.toString();
                    Gson gson = new Gson();
                    APIMovieCast dataCast = gson.fromJson(jsonString, APIMovieCast.class);
                    getYonetmen(dataCast);
                    getOyuncu(dataCast);
                    diger.setOnClickListener(v -> {
                        Intent intent = new Intent(this, DetailsCastActivity.class);
                        intent.putExtra("id", movie.getFilm_id());
                        startActivity(intent);
                    });
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void getOyuncu(APIMovieCast dataCast) {
        String url1 = "https://api.themoviedb.org/3/person/" + dataCast.getMovieCast().get(0).getId() + "?api_key=" + TMDBApi.getApiKey();
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null,
                response1 -> {
                    try {
                        oyuncuisim1.setText(response1.getString("name"));
                        oyuncu1ID = response1.getString("id").toString();
                        Picasso.get().load("https://image.tmdb.org/t/p/original" + response1.getString("profile_path")).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim1, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                try {
                                    Picasso.get().load("https://image.tmdb.org/t/p/original" + response1.getString("profile_path")).into(oyuncuresim1);
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    oyuncu1_relative.setOnClickListener(v -> {
                        Intent oyuncu1 = new Intent(DetailsActivity.this, OyuncuDetailsActivity.class);
                        pairs[0] = new Pair<View, String>(oyuncuresim1, "picture");
                        pairs[1] = new Pair<View, String>(oyuncuisim1, "text");
                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this, pairs);
                        oyuncu1.putExtra("person_id", oyuncu1ID);
                        oyuncu1.putExtra("secenek", "oyuncu");
                        startActivityForResult(oyuncu1, 3, activityOptions.toBundle());
                    });
                    String url2 = "https://api.themoviedb.org/3/person/" + dataCast.getMovieCast().get(1).getId() + "?api_key=" + TMDBApi.getApiKey();
                    JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                            response2 -> {
                                try {
                                    oyuncuisim2.setText(response2.getString("name"));
                                    oyuncu2ID = response2.getString("id").toString();
                                    Picasso.get().load("https://image.tmdb.org/t/p/original" + response2.getString("profile_path")).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim2, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            try {
                                                Picasso.get().load("https://image.tmdb.org/t/p/original" + response2.getString("profile_path")).into(oyuncuresim2);
                                            } catch (JSONException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                oyuncu2_relative.setOnClickListener(v -> {
                                    Intent oyuncu2 = new Intent(DetailsActivity.this, OyuncuDetailsActivity.class);
                                    pairs[0] = new Pair<View, String>(oyuncuresim2, "picture");
                                    pairs[1] = new Pair<View, String>(oyuncuisim2, "text");
                                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this, pairs);
                                    oyuncu2.putExtra("person_id", oyuncu2ID);
                                    oyuncu2.putExtra("secenek", "oyuncu");
                                    startActivityForResult(oyuncu2, 3, activityOptions.toBundle());
                                });
                                String url3 = "https://api.themoviedb.org/3/person/" + dataCast.getMovieCast().get(2).getId() + "?api_key=" + TMDBApi.getApiKey();
                                JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET, url3, null,
                                        response3 -> {
                                            try {
                                                oyuncuisim3.setText(response3.getString("name"));
                                                oyuncu3ID = response3.getString("id").toString();
                                                Picasso.get().load("https://image.tmdb.org/t/p/original" + response3.getString("profile_path")).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim3, new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError(Exception e) {
                                                        try {
                                                            Picasso.get().load("https://image.tmdb.org/t/p/original" + response3.getString("profile_path")).into(oyuncuresim3);
                                                        } catch (JSONException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            oyuncu3_relative.setOnClickListener(v -> {
                                                Intent oyuncu3 = new Intent(DetailsActivity.this, OyuncuDetailsActivity.class);
                                                pairs[0] = new Pair<View, String>(oyuncuresim3, "picture");
                                                pairs[1] = new Pair<View, String>(oyuncuisim3, "text");
                                                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this, pairs);
                                                oyuncu3.putExtra("person_id", oyuncu3ID);
                                                oyuncu3.putExtra("secenek", "oyuncu");
                                                startActivityForResult(oyuncu3, 3, activityOptions.toBundle());
                                            });
                                            String url4 = "https://api.themoviedb.org/3/person/" + dataCast.getMovieCast().get(3).getId() + "?api_key=" + TMDBApi.getApiKey();
                                            JsonObjectRequest request4 = new JsonObjectRequest(Request.Method.GET, url4, null,
                                                    response4 -> {
                                                        try {
                                                            oyuncuisim4.setText(response4.getString("name"));
                                                            oyuncu4ID = response4.getString("id").toString();
                                                            Picasso.get().load("https://image.tmdb.org/t/p/original" + response4.getString("profile_path")).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim4, new Callback() {
                                                                @Override
                                                                public void onSuccess() {

                                                                }

                                                                @Override
                                                                public void onError(Exception e) {
                                                                    try {
                                                                        Picasso.get().load("https://image.tmdb.org/t/p/original" + response4.getString("profile_path")).into(oyuncuresim4);
                                                                    } catch (JSONException ex) {
                                                                        ex.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        oyuncu4_relative.setOnClickListener(v -> {
                                                            Intent oyuncu4 = new Intent(DetailsActivity.this, OyuncuDetailsActivity.class);
                                                            pairs[0] = new Pair<View, String>(oyuncuresim4, "picture");
                                                            pairs[1] = new Pair<View, String>(oyuncuisim4, "text");
                                                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this, pairs);
                                                            oyuncu4.putExtra("person_id", oyuncu4ID);
                                                            oyuncu4.putExtra("secenek", "oyuncu");
                                                            startActivityForResult(oyuncu4, 3, activityOptions.toBundle());
                                                        });
                                                        String url5 = "https://api.themoviedb.org/3/person/" + dataCast.getMovieCast().get(4).getId() + "?api_key=" + TMDBApi.getApiKey();
                                                        JsonObjectRequest request5 = new JsonObjectRequest(Request.Method.GET, url5, null,
                                                                response5 -> {
                                                                    try {
                                                                        oyuncuisim5.setText(response5.getString("name"));
                                                                        oyuncu5ID = response5.getString("id").toString();
                                                                        Picasso.get().load("https://image.tmdb.org/t/p/original" + response5.getString("profile_path")).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim5, new Callback() {
                                                                            @Override
                                                                            public void onSuccess() {

                                                                            }

                                                                            @Override
                                                                            public void onError(Exception e) {
                                                                                try {
                                                                                    Picasso.get().load("https://image.tmdb.org/t/p/original" + response5.getString("profile_path")).into(oyuncuresim5);
                                                                                } catch (JSONException ex) {
                                                                                    ex.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    oyuncu5_relative.setOnClickListener(v -> {
                                                                        Intent oyuncu5 = new Intent(DetailsActivity.this, OyuncuDetailsActivity.class);
                                                                        pairs[0] = new Pair<View, String>(oyuncuresim5, "picture");
                                                                        pairs[1] = new Pair<View, String>(oyuncuisim5, "text");
                                                                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this, pairs);
                                                                        oyuncu5.putExtra("person_id", oyuncu5ID);
                                                                        oyuncu5.putExtra("secenek", "oyuncu");
                                                                        startActivityForResult(oyuncu5, 3, activityOptions.toBundle());
                                                                    });
                                                                    String url6 = "https://api.themoviedb.org/3/person/" + dataCast.getMovieCast().get(5).getId() + "?api_key=" + TMDBApi.getApiKey();
                                                                    JsonObjectRequest request6 = new JsonObjectRequest(Request.Method.GET, url6, null,
                                                                            response6 -> {
                                                                                try {
                                                                                    oyuncuisim6.setText(response6.getString("name"));
                                                                                    oyuncu6ID = response6.getString("id").toString();
                                                                                    Picasso.get().load("https://image.tmdb.org/t/p/original" + response6.getString("profile_path")).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim6, new Callback() {
                                                                                        @Override
                                                                                        public void onSuccess() {

                                                                                        }

                                                                                        @Override
                                                                                        public void onError(Exception e) {
                                                                                            try {
                                                                                                Picasso.get().load("https://image.tmdb.org/t/p/original" + response6.getString("profile_path")).into(oyuncuresim6);
                                                                                            } catch (JSONException ex) {
                                                                                                ex.printStackTrace();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                                oyuncu6_relative.setOnClickListener(v -> {
                                                                                    Intent oyuncu6 = new Intent(DetailsActivity.this, OyuncuDetailsActivity.class);
                                                                                    pairs[0] = new Pair<View, String>(oyuncuresim6, "picture");
                                                                                    pairs[1] = new Pair<View, String>(oyuncuisim6, "text");
                                                                                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this, pairs);
                                                                                    oyuncu6.putExtra("person_id", oyuncu6ID);
                                                                                    oyuncu6.putExtra("secenek", "oyuncu");
                                                                                    startActivityForResult(oyuncu6, 3, activityOptions.toBundle());
                                                                                });
                                                                            }, Throwable::printStackTrace);
                                                                    requestQueue.add(request6);
                                                                }, Throwable::printStackTrace);
                                                        requestQueue.add(request5);
                                                    }, Throwable::printStackTrace);
                                            requestQueue.add(request4);
                                        }, Throwable::printStackTrace);
                                requestQueue.add(request3);
                            }, Throwable::printStackTrace);
                    requestQueue.add(request2);
                }, Throwable::printStackTrace);
        requestQueue.add(request1);
    }

    private void getYonetmen(APIMovieCast dataCast) {
        String id = "";
        for (int i = 0; i < dataCast.getMovieCrew().size(); i++) {
            if (dataCast.getMovieCrew().get(i).getJob().equals("Director")) {
                id = dataCast.getMovieCrew().get(i).getId().toString();
                yonetmenID = id;
                textYonetmen.setText(dataCast.getMovieCrew().get(i).getName());
            }
        }
        String url = "https://api.themoviedb.org/3/person/" + id + "?api_key=" + TMDBApi.getApiKey();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Picasso.get().load("https://image.tmdb.org/t/p/original" + response.getString("profile_path")).networkPolicy(NetworkPolicy.OFFLINE).into(image_yonetmen, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                try {
                                    Picasso.get().load("https://image.tmdb.org/t/p/original" + response.getString("profile_path")).into(image_yonetmen);
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        yonetmen_relative.setOnClickListener(v -> {
                            Intent yonetmen = new Intent(DetailsActivity.this, OyuncuDetailsActivity.class);
                            pairs[0] = new Pair<View, String>(image_yonetmen, "picture");
                            pairs[1] = new Pair<View, String>(textYonetmen, "text");
                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this, pairs);
                            yonetmen.putExtra("person_id", yonetmenID);
                            yonetmen.putExtra("secenek", "yonetmen");
                            startActivityForResult(yonetmen, 3, activityOptions.toBundle());
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void getIncomingIntent() {
        movie.setFilm_yil(getIntent().getStringExtra("yil"));
        movie.setFilm_puan(getIntent().getStringExtra("puan"));
        movie.setFilm_image(getIntent().getStringExtra("image"));
        movie.setFilm_ozet(getIntent().getStringExtra("ozet"));
        movie.setFilm_sure(getIntent().getStringExtra("sure"));
        movie.setFilm_ozet_eng(getIntent().getStringExtra("ozet_eng"));
        movie.setFilm_sure_eng(getIntent().getStringExtra("sure_eng"));
        movie.setFilm_adi(getIntent().getStringExtra("ad"));
        movie.setFilm_id(getIntent().getStringExtra("id"));
        movie.setFilm_tur(getIntent().getStringExtra("tur"));
        movie.setFilm_tur_eng(getIntent().getStringExtra("tur_eng"));
        movie.setFilm_sinif(getIntent().getStringExtra("sinif"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Drawable favFab = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_beyaz, getTheme());
        assert favFab != null;
        Drawable whiteFav = Objects.requireNonNull(favFab.getConstantState()).newDrawable();
        whiteFav.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        fab.setImageDrawable(whiteFav);

        if (firebaseAuth.getCurrentUser() != null) {
            reference_favoriler.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    firebaseList.add(snapshot.getValue(MovieFirebase.class).getFilm_id());
                    firebaseKeys.add(snapshot.getKey());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference_favoriler.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (firebaseList.contains(movie.getFilm_id())) {
                        Drawable favFabFirebase = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_ekli, getTheme());
                        Drawable whiteFavFirebase = Objects.requireNonNull(favFabFirebase.getConstantState()).newDrawable();
                        whiteFavFirebase.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                        fab.setImageDrawable(whiteFavFirebase);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            if (!favorites.Kontrol(movie.getFilm_id())) {
                favFab = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_ekli, getTheme());
                whiteFav = Objects.requireNonNull(favFab.getConstantState()).newDrawable();
                whiteFav.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                fab.setImageDrawable(whiteFav);
            }
        }
    }
}
