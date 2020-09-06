package com.utkuakgungor.filmtavsiye.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.utils.FirebaseAdapter;
import com.utkuakgungor.filmtavsiye.utils.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class SearchResultFragment extends Fragment {

    private String adi,yonetmen,oyuncu,action,adventure,science,fantasy,thriller,drama,crime,mystery,western,war,comedy,history,firebase_oyuncu,firebase_turler;
    private List<Movie> result;
    private FirebaseAdapter adapter;
    private ImageView sonucyok_resim;
    private TextView sonucyok_text;
    private Bundle bundle;
    private DatabaseReference reference;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("Ayarlar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(sharedPreferences.contains("Gece")){
            getActivity().setTheme(R.style.AppTheme);
            editor.remove("Gece");
            editor.commit();
            Intent intent =getActivity().getIntent();
            getActivity().finish();
            getActivity().startActivity(intent);
        }
        else{
            getActivity().setTheme(R.style.AppThemeDark);
            editor.putString("Gece","Gece");
            editor.commit();
            Intent intent =getActivity().getIntent();
            getActivity().finish();
            getActivity().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        adi = bundle.getString("adi");
        yonetmen = bundle.getString("yonetmen");
        oyuncu = bundle.getString("oyuncu");
        action=bundle.getString("action");
        adventure=bundle.getString("adventure");
        science=bundle.getString("science");
        fantasy=bundle.getString("fantasy");
        thriller=bundle.getString("thriller");
        drama=bundle.getString("drama");
        crime=bundle.getString("crime");
        mystery=bundle.getString("mystery");
        western=bundle.getString("western");
        war=bundle.getString("war");
        comedy=bundle.getString("comedy");
        history=bundle.getString("history");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_search_result, container, false);
        setHasOptionsMenu(true);
        SharedPreferences sharedPreference= Objects.requireNonNull(getActivity()).getSharedPreferences("Ayarlar",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        sonucyok_text=v.findViewById(R.id.sonucyok_yazi);
        sonucyok_resim=v.findViewById(R.id.sonucyok_resim);
        ImageButton btnCard =v.findViewById(R.id.btnCard);
        ImageButton btnDashboard =v.findViewById(R.id.btnDashboard);
        bundle=getArguments();
        getData();
        if(TextUtils.isEmpty(adi)){
            adi="";
        }
        if(TextUtils.isEmpty(yonetmen)){
            yonetmen="";
        }
        if(TextUtils.isEmpty(oyuncu)){
            oyuncu = "";
        }
        if(TextUtils.isEmpty(action)){
            action = "";
        }
        if(TextUtils.isEmpty(adventure)){
            adventure = "";
        }
        if(TextUtils.isEmpty(science)){
            science = "";
        }
        if(TextUtils.isEmpty(fantasy)){
            fantasy = "";
        }
        if(TextUtils.isEmpty(thriller)){
            thriller = "";
        }
        if(TextUtils.isEmpty(drama)){
            drama = "";
        }
        if(TextUtils.isEmpty(crime)){
            crime = "";
        }
        if(TextUtils.isEmpty(mystery)){
            mystery = "";
        }
        if(TextUtils.isEmpty(western)){
            western="";
        }
        if(TextUtils.isEmpty(war)){
            war="";
        }
        if(TextUtils.isEmpty(comedy)){
            comedy="";
        }
        if(TextUtils.isEmpty(history)){
            history="";
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference= database.getReference("Filmler");
        reference.keepSynced(true);
        result =new ArrayList<>();
        RecyclerView recyclerView = v.findViewById(R.id.firebaselist);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,1);

        if(sharedPreference.contains("Card")){
            recyclerView.setLayoutManager(linearLayoutManager);
            btnCard.setImageResource(R.drawable.ic_card_active);
            btnDashboard.setImageResource(R.drawable.ic_dashboard);
        }
        else {
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            btnCard.setImageResource(R.drawable.ic_card);
            btnDashboard.setImageResource(R.drawable.ic_dashboard_active);
        }

        btnDashboard.setOnClickListener(v12 ->{
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            btnCard.setImageResource(R.drawable.ic_card);
            btnDashboard.setImageResource(R.drawable.ic_dashboard_active);
            editor.remove("Card");
            editor.commit();
        });

        btnCard.setOnClickListener(v1 ->{
            recyclerView.setLayoutManager(linearLayoutManager);
            btnDashboard.setImageResource(R.drawable.ic_dashboard);
            btnCard.setImageResource(R.drawable.ic_card_active);
            editor.putString("Card","Card");
            editor.commit();
        });

        adapter =new FirebaseAdapter(getContext(),result);

        recyclerView.setAdapter(adapter);

        updateList(adi,yonetmen,oyuncu,action,adventure,science,fantasy,thriller,drama,crime,mystery,western,war,comedy,history);

        return v;
    }

    private void updateList(final String filmadi,final String yonetmen,final String oyuncu,final String action,final String adventure,final String science,
                            final String fantasy,final String thriller,final String drama,final String crime,final String mystery,final String western,
                            final String war,final String comedy,final String history){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                firebase_oyuncu=Objects.requireNonNull(dataSnapshot.getValue(Movie.class)).getFilm_oyuncular().toLowerCase().replace(",","");
                if(Objects.equals(Locale.getDefault().getDisplayLanguage(),"English")){
                    firebase_turler = Objects.requireNonNull(dataSnapshot.getValue(Movie.class)).getFilm_tur_eng().replace(",","");
                }
                else{
                    firebase_turler=Objects.requireNonNull(dataSnapshot.getValue(Movie.class)).getFilm_tur().replace(",","");
                }
                if(Objects.requireNonNull(dataSnapshot.getValue(Movie.class)).getFilm_adi().toLowerCase().contains(filmadi.toLowerCase())
                        && Objects.requireNonNull(dataSnapshot.getValue(Movie.class)).getFilm_yonetmen().toLowerCase().contains(yonetmen.toLowerCase())
                        && firebase_oyuncu.contains(oyuncu.toLowerCase())
                        && firebase_turler.contains(action)
                        && firebase_turler.contains(adventure)
                        && firebase_turler.contains(science)
                        && firebase_turler.contains(fantasy)
                        && firebase_turler.contains(thriller)
                        && firebase_turler.contains(drama)
                        && firebase_turler.contains(crime)
                        && firebase_turler.contains(mystery)
                        && firebase_turler.contains(western)
                        && firebase_turler.contains(war)
                        && firebase_turler.contains(comedy)
                        && firebase_turler.contains(history)){
                    result.add(dataSnapshot.getValue(Movie.class));
                    sonucyok_resim.setVisibility(View.GONE);
                    sonucyok_text.setVisibility(View.GONE);
                }
                else if(result.isEmpty()) {
                    sonucyok_text.setVisibility(View.VISIBLE);
                    sonucyok_resim.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyItemChanged(adapter.getSira());
    }
}
