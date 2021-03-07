package com.utkuakgungor.filmtavsiye.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.models.MovieFirebase;
import com.utkuakgungor.filmtavsiye.settings.SettingsActivity;
import com.utkuakgungor.filmtavsiye.utils.FirebaseAdapter;
import com.utkuakgungor.filmtavsiye.models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class SearchResultFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private String action, adventure, science, fantasy, thriller, drama, crime, mystery, western, war, comedy, history, firebase_oyuncu, firebase_turler;
    private List<MovieFirebase> result;
    private FirebaseAdapter adapter;
    private RelativeLayout sonucyok_Relative;
    private Bundle bundle;
    private DatabaseReference reference, referenceFavoriler;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent privacyIntent = new Intent(getContext(), SettingsActivity.class);
        startActivity(privacyIntent);
        return true;
    }

    private void getData() {
        action = bundle.getString("action");
        adventure = bundle.getString("adventure");
        science = bundle.getString("science");
        fantasy = bundle.getString("fantasy");
        thriller = bundle.getString("thriller");
        drama = bundle.getString("drama");
        crime = bundle.getString("crime");
        mystery = bundle.getString("mystery");
        western = bundle.getString("western");
        war = bundle.getString("war");
        comedy = bundle.getString("comedy");
        history = bundle.getString("history");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_result, container, false);
        setHasOptionsMenu(true);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            referenceFavoriler = FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail().replace(".", "").replace("#", "").replace("$", "").replace("[", "").replace("]", "")));
        }
        sonucyok_Relative = v.findViewById(R.id.aramasonucRelative);
        bundle = getArguments();
        getData();
        if (TextUtils.isEmpty(action)) {
            action = "";
        }
        if (TextUtils.isEmpty(adventure)) {
            adventure = "";
        }
        if (TextUtils.isEmpty(science)) {
            science = "";
        }
        if (TextUtils.isEmpty(fantasy)) {
            fantasy = "";
        }
        if (TextUtils.isEmpty(thriller)) {
            thriller = "";
        }
        if (TextUtils.isEmpty(drama)) {
            drama = "";
        }
        if (TextUtils.isEmpty(crime)) {
            crime = "";
        }
        if (TextUtils.isEmpty(mystery)) {
            mystery = "";
        }
        if (TextUtils.isEmpty(western)) {
            western = "";
        }
        if (TextUtils.isEmpty(war)) {
            war = "";
        }
        if (TextUtils.isEmpty(comedy)) {
            comedy = "";
        }
        if (TextUtils.isEmpty(history)) {
            history = "";
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Filmler");
        reference.keepSynced(true);
        result = new ArrayList<>();
        RecyclerView recyclerView = v.findViewById(R.id.firebaselist);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);

        adapter = new FirebaseAdapter("search", getContext(), result, firebaseAuth, referenceFavoriler);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);

        updateList(action, adventure, science, fantasy, thriller, drama, crime, mystery, western, war, comedy, history);

        return v;
    }

    private void updateList(final String action, final String adventure, final String science, final String fantasy,
                            final String thriller, final String drama, final String crime, final String mystery,
                            final String western, final String war, final String comedy, final String history) {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (Objects.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    firebase_turler = Objects.requireNonNull(dataSnapshot.getValue(Movie.class)).getFilm_tur_eng().replace(",", "");
                } else {
                    firebase_turler = Objects.requireNonNull(dataSnapshot.getValue(Movie.class)).getFilm_tur().replace(",", "");
                }
                if (firebase_turler.contains(action)
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
                        && firebase_turler.contains(history)) {
                    result.add(dataSnapshot.getValue(MovieFirebase.class));
                    sonucyok_Relative.setVisibility(View.GONE);
                } else if (result.isEmpty()) {
                    sonucyok_Relative.setVisibility(View.VISIBLE);
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
