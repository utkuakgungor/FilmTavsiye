package com.utkuakgungor.filmtavsiye.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.models.APIMovieCast;
import com.utkuakgungor.filmtavsiye.models.Oyuncu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

public class CustomDialogClass extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_cast,null);
        Bundle bundle=getArguments();
        APIMovieCast dataCast= (APIMovieCast) Objects.requireNonNull(bundle).getSerializable("cast");
        List<Oyuncu> oyuncuList = new ArrayList<>();
        for(int i = 0; i< Objects.requireNonNull(dataCast).getMovieCast().size(); i++){
            Oyuncu oyuncu = new Oyuncu();
            oyuncu.setOyuncuAdi(dataCast.getMovieCast().get(i).getName());
            oyuncu.setOyuncuID(dataCast.getMovieCast().get(i).getId());
            oyuncu.setOyuncuResim(dataCast.getMovieCast().get(i).getProfilePath());
            oyuncuList.add(oyuncu);
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        RecyclerView recyclerView = view.findViewById(R.id.castRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(getContext());
        lim.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lim);
        OyuncularAdapter adapter = new OyuncularAdapter(requireContext(), oyuncuList);
        recyclerView.setAdapter(adapter);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.castFab);
        floatingActionButton.setOnClickListener(v -> dismiss());
        return builder.create();
    }
}
