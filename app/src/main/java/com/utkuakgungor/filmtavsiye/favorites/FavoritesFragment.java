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
import com.utkuakgungor.filmtavsiye.privacy.PrivacyActivity;
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_privacy,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.menu_privacy:
                Intent privacyIntent = new Intent(getContext(), PrivacyActivity.class);
                startActivity(privacyIntent);
                return true;
            case R.id.menu_day_night:
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
                return true;
            default:
                return false;
        }
    }

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        SharedPreferences sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("Ayarlar",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        favori = new Favorites(v.getContext());
        dbList = favori.getDataFromDB();
        recyclerView = v.findViewById(R.id.firebaselist);
        setHasOptionsMenu(true);
        recyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,1);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());

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
            editor.commit();
        });

        btnCard.setOnClickListener(v1 ->{
            recyclerView.setLayoutManager(linearLayoutManager);
            btnDashboard.setImageResource(R.drawable.ic_dashboard);
            btnCard.setImageResource(R.drawable.ic_card_active);
            editor.putString("Card","Card");
            editor.commit();
        });

        adapter = new RecyclerAdapter(v.getContext(),dbList);
        recyclerView.setAdapter(adapter);
        if(dbList.isEmpty()){
            Snackbar snackbar = Snackbar.make(v,getResources().getString(R.string.favoribos),Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView snackbar_text = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            if(sharedPreferences.contains("Gece")){
                view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorGray));
                snackbar_text.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
            }
            snackbar.show();
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
