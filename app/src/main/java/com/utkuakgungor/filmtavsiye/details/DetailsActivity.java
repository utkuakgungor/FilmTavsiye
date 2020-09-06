package com.utkuakgungor.filmtavsiye.details;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Pair;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;
import android.widget.Button;
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
import com.utkuakgungor.filmtavsiye.utils.CustomDialogClass;
import com.utkuakgungor.filmtavsiye.utils.Favorites;
import com.utkuakgungor.filmtavsiye.utils.Oyuncu;
import com.utkuakgungor.filmtavsiye.utils.ViewPagerAdapter;
import com.utkuakgungor.filmtavsiye.utils.Yonetmen;

import java.util.Locale;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private String ad,yil,puan,image,youtube,ozet,sure,ozet_eng,sure_eng,textcolor,oyuncular,tur,tur_eng,yonetmen,sinif,oyuncu1_resim,oyuncu2_resim,oyuncu3_resim,oyuncu4_resim,oyuncu5_resim,oyuncu6_resim,yonetmen_resim,resimler;
    private TextView oyuncuisim1,oyuncuisim2,oyuncuisim3,oyuncuisim4,oyuncuisim5,oyuncuisim6;
    private ImageView imageView;
    private CircleImageView image_yonetmen,oyuncuresim1,oyuncuresim2,oyuncuresim3,oyuncuresim4,oyuncuresim5,oyuncuresim6;
    private Favorites favorites;
    private FloatingActionButton fab;
    private String[] oyuncu;
    private int dotscount;
    private ImageView[] dots;
    private int sayac=0;
    private Snackbar snackbar;
    private View view;
    private TextView snackbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences=getSharedPreferences("Ayarlar",MODE_PRIVATE);
        if(sharedPreferences.contains("Gece")){
            setTheme(R.style.AppThemeDarkNoActionBar);
        }
        else{
            setTheme(R.style.AppThemeNoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);
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
        Button diger = findViewById(R.id.detaydiger);
        oyuncu=oyuncular.split(", ");
        String[] resim = resimler.split(", ");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(DetailsActivity.this, resim);
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
        String[] turler = tur.split(", ");
        String[] turler_eng = tur_eng.split(", ");

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(youtube,0);
            }
        });


        for(sayac=0; sayac< turler.length; sayac++){
            if(Objects.equals(Locale.getDefault().getDisplayLanguage(),"Türkçe" )){
                final Chip chip = new Chip(chipGroup.getContext());
                chip.setText(turler[sayac]);
                chip.setChipBackgroundColorResource(R.drawable.chipbackground);
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
                chip.setChipBackgroundColorResource(R.drawable.chipbackground);
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

        if(!favorites.Kontrol(ad)){
            Drawable favFab = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_favorite_ekli,getTheme());
            assert favFab != null;
            Drawable whiteFav = Objects.requireNonNull(favFab.getConstantState()).newDrawable();
            whiteFav.mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
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
            bundle.putStringArray("oyuncular",oyuncu);
            customDialogClass.setArguments(bundle);
            customDialogClass.show(getSupportFragmentManager(),"Custom Dialog");
        });

        if(Objects.equals(Locale.getDefault().getDisplayLanguage(),"Türkçe" )){
            textOzet.setText(ozet);
            textSure.setText(sure);
        }
        else {
            textSure.setText(sure_eng);
            textOzet.setText(ozet_eng);
        }
        if(Objects.equals(textcolor,"evet")){
            int color = Color.parseColor("#000000");
            textFilmAdi.setExpandedTitleColor(color);
        }
        textYonetmen.setText(yonetmen);
        textFilmAdi.setTitle(ad);
        textFilmYili.setText(yil);
        textPuan.setText(puan);
        imageView.setOnClickListener(v -> {
            Intent imageActivity = new Intent(DetailsActivity.this,ImageDetails.class);
            imageActivity.putExtra("image",image);
            Pair[] pair = new Pair[1];
            pair[0] = new Pair<View,String>(imageView,"cardPicture");
            ActivityOptions activityOptions=ActivityOptions
                    .makeSceneTransitionAnimation(DetailsActivity.this,pair);
            startActivity(imageActivity,activityOptions.toBundle());
        });
        Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).into(imageView);
            }
        });

        fab.setOnClickListener(v -> {
            if (favorites.Kontrol(ad)){
                favorites.ekle(ad,yil,puan,image,youtube,ozet,sure,ozet_eng,sure_eng,textcolor,oyuncular,tur,tur_eng,yonetmen,sinif,"");
                snackbar = Snackbar.make(v,getResources().getString(R.string.favoriekle),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
                Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_favorite_ekli,getTheme());
                assert myFabSrc != null;
                Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
                willBeWhite.mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                fab.setImageDrawable(willBeWhite);
            }else {
                favorites.deleteData(ad);
                snackbar = Snackbar.make(v,getResources().getString(R.string.favoricikti),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
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
                if(Objects.equals(yonetmen,Objects.requireNonNull(dataSnapshot.getValue(Yonetmen.class)).getYonetmen_adi())){
                    yonetmen_resim=Objects.requireNonNull(dataSnapshot.getValue(Yonetmen.class)).getYonetmen_resim_url();
                    Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Yonetmen.class)).getYonetmen_resim_url()).into(image_yonetmen, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Yonetmen.class)).getYonetmen_resim_url()).into(image_yonetmen);
                        }
                    });
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
                    if(Objects.equals(oyuncu[sayac],Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_adi())){
                        if(Objects.equals(oyuncuisim1.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim1.setText(oyuncu[sayac]);
                            oyuncu1_resim=Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url();
                            Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim1, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).into(oyuncuresim1);
                                }
                            });
                        }
                        else if(Objects.equals(oyuncuisim2.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim2.setText(oyuncu[sayac]);
                            oyuncu2_resim=Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url();
                            Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim2, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).into(oyuncuresim2);
                                }
                            });
                        }
                        else if(Objects.equals(oyuncuisim3.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim3.setText(oyuncu[sayac]);
                            oyuncu3_resim=Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url();
                            Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim3, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).into(oyuncuresim3);
                                }
                            });
                        }
                        else if(Objects.equals(oyuncuisim4.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim4.setText(oyuncu[sayac]);
                            oyuncu4_resim=Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url();
                            Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim4, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).into(oyuncuresim4);
                                }
                            });

                        }
                        else if(Objects.equals(oyuncuisim5.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim5.setText(oyuncu[sayac]);
                            oyuncu5_resim=Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url();
                            Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim5, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).into(oyuncuresim5);
                                }
                            });

                        }
                        else if(Objects.equals(oyuncuisim6.getText(),getResources().getString(R.string.degisken))){
                            oyuncuisim6.setText(oyuncu[sayac]);
                            oyuncu6_resim=Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url();
                            Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).networkPolicy(NetworkPolicy.OFFLINE).into(oyuncuresim6, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_resim_url()).into(oyuncuresim6);
                                }
                            });
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

        sinif=sinif.replace(",","");
        if(sinif.contains("Cinsellik")){
            image_cinsellik.setVisibility(View.VISIBLE);
            image_cinsellik.setOnClickListener(v -> {
                snackbar = Snackbar.make(v,getResources().getString(R.string.cinsellik),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
            });
        }
        else{
            image_cinsellik.setVisibility(View.GONE);
        }
        if(sinif.contains("Şiddet")){
            image_siddet.setVisibility(View.VISIBLE);
            image_siddet.setOnClickListener(v -> {
                snackbar = Snackbar.make(v,getResources().getString(R.string.siddet),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
            });
        }
        else{
            image_siddet.setVisibility(View.GONE);
        }
        if(sinif.contains("Olumsuz")){
            image_olumsuz.setVisibility(View.VISIBLE);
            image_olumsuz.setOnClickListener(v -> {
                snackbar = Snackbar.make(v,getResources().getString(R.string.olmsuz),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
            });
        }
        else{
            image_olumsuz.setVisibility(View.GONE);
        }
        if(sinif.contains("Aile")){
            image_aile.setVisibility(View.VISIBLE);
            image_aile.setOnClickListener(v -> {
                snackbar = Snackbar.make(v,getResources().getString(R.string.aile),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
            });
        }
        else{
            image_aile.setVisibility(View.GONE);
        }
        if(sinif.contains("7")){
            image_7.setVisibility(View.VISIBLE);
            image_7.setOnClickListener(v -> {
                snackbar = Snackbar.make(v,getResources().getString(R.string.yas7),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
            });
        }
        else{
            image_7.setVisibility(View.GONE);
        }
        if(sinif.contains("13")){
            image_13.setVisibility(View.VISIBLE);
            image_13.setOnClickListener(v -> {
                snackbar = Snackbar.make(v,getResources().getString(R.string.yas13),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
            });
        }
        else{
            image_13.setVisibility(View.GONE);
        }
        if(sinif.contains("15")){
            image_15.setVisibility(View.VISIBLE);
            image_15.setOnClickListener(v ->{
                snackbar = Snackbar.make(v,getResources().getString(R.string.yas15),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
            });
        }
        else{
            image_15.setVisibility(View.GONE);
        }
        if(sinif.contains("18")){
            image_18.setVisibility(View.VISIBLE);
            image_18.setOnClickListener(v -> {
                snackbar = Snackbar.make(v,getResources().getString(R.string.yas18),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text =  view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(DetailsActivity.this,R.color.colorBlack));
                }
                snackbar.show();
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

    private void getIncomingIntent(){

        if(getIntent().hasExtra("sure") && getIntent().hasExtra("ad") && getIntent().hasExtra("yil") && getIntent().hasExtra("puan")
                && getIntent().hasExtra("image") && getIntent().hasExtra("youtube") && getIntent().hasExtra("ozet")
                && getIntent().hasExtra("ozet_eng") && getIntent().hasExtra("sure_eng") && getIntent().hasExtra("color")
                && getIntent().hasExtra("oyuncular") && getIntent().hasExtra("tur") && getIntent().hasExtra("yonetmen")
                && getIntent().hasExtra("tur_eng") && getIntent().hasExtra("sinif") && getIntent().hasExtra("resimler")){

            ad = getIntent().getStringExtra("ad");
            yil=getIntent().getStringExtra("yil");
            puan=getIntent().getStringExtra("puan");
            image = getIntent().getStringExtra("image");
            youtube = getIntent().getStringExtra("youtube");
            ozet = getIntent().getStringExtra("ozet");
            sure=getIntent().getStringExtra("sure");
            ozet_eng=getIntent().getStringExtra("ozet_eng");
            sure_eng=getIntent().getStringExtra("sure_eng");
            textcolor=getIntent().getStringExtra("color");
            oyuncular=getIntent().getStringExtra("oyuncular");
            tur=getIntent().getStringExtra("tur");
            tur_eng=getIntent().getStringExtra("tur_eng");
            yonetmen=getIntent().getStringExtra("yonetmen");
            sinif=getIntent().getStringExtra("sinif");
            resimler=getIntent().getStringExtra("resimler");

        }
    }
}
