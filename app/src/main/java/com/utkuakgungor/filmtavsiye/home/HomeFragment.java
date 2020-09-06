package com.utkuakgungor.filmtavsiye.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Objects;

public class HomeFragment extends Fragment {

    private List<Movie> result;
    private DatabaseReference reference;
    private FirebaseAdapter adapter;

    public HomeFragment() {
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
            editor.apply();
            Intent intent =getActivity().getIntent();
            getActivity().finish();
            getActivity().startActivity(intent);
        }
        else{
            getActivity().setTheme(R.style.AppThemeDark);
            editor.putString("Gece","Gece");
            editor.apply();
            Intent intent =getActivity().getIntent();
            getActivity().finish();
            getActivity().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("Ayarlar",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        setHasOptionsMenu(true);
        reference= FirebaseDatabase.getInstance().getReference("Filmler");
        reference.keepSynced(true);
        result =new ArrayList<>();
        final RecyclerView recyclerView = v.findViewById(R.id.firebaselist);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,1);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        ImageButton btnDashboard = v.findViewById(R.id.btnDashboard);
        ImageButton btnCard = v.findViewById(R.id.btnCard);

        if(sharedPreferences.contains("Card")){
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
            editor.apply();
        });

        btnCard.setOnClickListener(v1 ->{
            recyclerView.setLayoutManager(linearLayoutManager);
            btnDashboard.setImageResource(R.drawable.ic_dashboard);
            btnCard.setImageResource(R.drawable.ic_card_active);
            editor.putString("Card","Card");
            editor.apply();
        });

        adapter =new FirebaseAdapter(v.getContext(),result);

        recyclerView.setAdapter(adapter);
        updateList();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyItemChanged(adapter.getSira());
    }

    private void updateList(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                result.add(dataSnapshot.getValue(Movie.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                result.add(dataSnapshot.getValue(Movie.class));
                adapter.notifyDataSetChanged();
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

}
