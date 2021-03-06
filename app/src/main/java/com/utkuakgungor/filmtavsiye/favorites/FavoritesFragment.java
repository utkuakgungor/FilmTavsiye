package com.utkuakgungor.filmtavsiye.favorites;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.models.MovieFirebase;
import com.utkuakgungor.filmtavsiye.settings.SettingsActivity;
import com.utkuakgungor.filmtavsiye.utils.Favorites;
import com.utkuakgungor.filmtavsiye.utils.FirebaseAdapter;
import com.utkuakgungor.filmtavsiye.models.Model;
import com.utkuakgungor.filmtavsiye.models.Movie;
import com.utkuakgungor.filmtavsiye.utils.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritesFragment extends Fragment {

    private RecyclerAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private List<Model> dbList;
    private List<MovieFirebase> dbListFirebase;
    private Favorites favori;
    private RecyclerView recyclerView;
    private View v;
    private FirebaseAdapter adapterFirebase;

    public FavoritesFragment() {
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = v.findViewById(R.id.firebaselist);
        setHasOptionsMenu(true);
        dbListFirebase = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            reference = FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail().replace(".", "").replace("#", "").replace("$", "").replace("[", "").replace("]", "")));
            adapterFirebase = new FirebaseAdapter("favorites", v.getContext(), dbListFirebase, firebaseAuth, reference);
            getFirebaseData();
            recyclerView.setAdapter(adapterFirebase);
        } else {
            favori = new Favorites(v.getContext());
            dbList = favori.getDataFromDB();
            adapter = new RecyclerAdapter(v.getContext(), dbList, firebaseAuth, reference);
            recyclerView.setAdapter(adapter);
            if (dbList.isEmpty()) {
                Snackbar.make(v, getResources().getString(R.string.favoribos), Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(v, getResources().getString(R.string.login_message), Snackbar.LENGTH_LONG).show();
            }
        }

        return v;
    }

    private void getFirebaseData() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                dbListFirebase.add(snapshot.getValue(MovieFirebase.class));
                adapterFirebase.notifyDataSetChanged();
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
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (dbListFirebase.isEmpty()) {
                    Snackbar.make(v, getResources().getString(R.string.favoribos), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onActivityResult(int requestCode) {
        if (requestCode == 1) {
            dbList=new ArrayList<>();
            dbList=favori.getDataFromDB();
            adapter=new RecyclerAdapter(v.getContext(),dbList,firebaseAuth,reference);
            recyclerView.swapAdapter(adapter,false);
        } else if (requestCode == 2) {
            dbListFirebase=new ArrayList<>();
            getFirebaseData();
            adapterFirebase=new FirebaseAdapter("favorites",v.getContext(),dbListFirebase,firebaseAuth,reference);
            recyclerView.swapAdapter(adapterFirebase,false);
        }
    }
}
