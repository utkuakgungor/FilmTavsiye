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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.settings.SettingsActivity;
import com.utkuakgungor.filmtavsiye.utils.Favorites;
import com.utkuakgungor.filmtavsiye.utils.FirebaseAdapter;
import com.utkuakgungor.filmtavsiye.utils.Model;
import com.utkuakgungor.filmtavsiye.utils.Movie;
import com.utkuakgungor.filmtavsiye.utils.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritesFragment extends Fragment {

    private RecyclerAdapter adapter;
    private FirebaseAdapter firebaseAdapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private List<Model> dbList;
    private List<Movie> dbListFirebase;
    private Favorites favori;
    private RecyclerView recyclerView;
    private View v;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action,menu);
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
        dbListFirebase=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            reference= FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getDisplayName()));
            firebaseAdapter = new FirebaseAdapter(v.getContext(),dbListFirebase,firebaseAuth,reference);
            getFirebaseData();
            recyclerView.setAdapter(firebaseAdapter);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(dbListFirebase.isEmpty()){
                        Snackbar.make(v,getResources().getString(R.string.favoribos),Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            favori = new Favorites(v.getContext());
            dbList = favori.getDataFromDB();
            adapter = new RecyclerAdapter(v.getContext(),dbList,firebaseAuth,reference);
            recyclerView.setAdapter(adapter);
            if(dbList.isEmpty()){
                Snackbar.make(v,getResources().getString(R.string.favoribos),Snackbar.LENGTH_LONG).show();
            }
            else{
                Snackbar.make(v,getResources().getString(R.string.login_message),Snackbar.LENGTH_LONG).show();
            }
        }

        return v;
    }

    private void getFirebaseData() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                dbListFirebase.add(snapshot.getValue(Movie.class));
                adapter.notifyDataSetChanged();
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
    }


    @Override
    public void onResume() {
        if (firebaseAuth.getCurrentUser() != null) {
            reference= FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getDisplayName()));
            firebaseAdapter = new FirebaseAdapter(v.getContext(),dbListFirebase,firebaseAuth,reference);
            getFirebaseData();
            recyclerView.setAdapter(firebaseAdapter);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(dbListFirebase.isEmpty()){
                        Snackbar.make(v,getResources().getString(R.string.favoribos),Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            if(!dbList.isEmpty()){
                if(favori.Kontrol(dbList.get(adapter.getSira()).getFilm_adi())){
                    dbList.remove(adapter.getSira());
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemRangeChanged(adapter.getSira(),adapter.getItemCount());
                }
            }
        }
        super.onResume();
    }
}
