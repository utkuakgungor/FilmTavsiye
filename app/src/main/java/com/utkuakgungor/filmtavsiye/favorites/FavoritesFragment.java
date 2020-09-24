package com.utkuakgungor.filmtavsiye.favorites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.settings.SettingsActivity;
import com.utkuakgungor.filmtavsiye.utils.Favorites;
import com.utkuakgungor.filmtavsiye.utils.Model;
import com.utkuakgungor.filmtavsiye.utils.RecyclerAdapter;

import java.util.List;
import java.util.Objects;

public class FavoritesFragment extends Fragment {

    private RecyclerAdapter adapter;
    private List<Model> dbList;
    private Favorites favori;
    private RecyclerView recyclerView;

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
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        SharedPreferences sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("Ayarlar",Context.MODE_PRIVATE);
        favori = new Favorites(v.getContext());
        dbList = favori.getDataFromDB();
        recyclerView = v.findViewById(R.id.firebaselist);
        setHasOptionsMenu(true);
        recyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new RecyclerAdapter(v.getContext(),dbList);
        recyclerView.setAdapter(adapter);
        if(dbList.isEmpty()){
            Snackbar.make(v,getResources().getString(R.string.favoribos),Snackbar.LENGTH_LONG).show();
        }

        return v;
    }

    @Override
    public void onResume() {
        if(!dbList.isEmpty()){
            if(favori.Kontrol(dbList.get(adapter.getSira()).getFilm_adi())){
                dbList.remove(adapter.getSira());
                adapter.notifyDataSetChanged();
                adapter.notifyItemRangeChanged(adapter.getSira(),adapter.getItemCount());
            }
        }
        super.onResume();
    }
}
