package com.utkuakgungor.filmtavsiye.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.utkuakgungor.filmtavsiye.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomDialogClass extends AppCompatDialogFragment {

    private DatabaseReference databaseReference;
    private OyuncularAdapter adapter;
    private int sayac=0;
    private String[] oyuncular;
    private List<Oyuncu> list;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Oyuncular");
        Bundle bundle;
        bundle=getArguments();
        assert bundle != null;
        oyuncular=bundle.getStringArray("oyuncular");
        databaseReference.keepSynced(true);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_cast,null);
        builder.setView(view);
        list= new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.castRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(getContext());
        lim.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lim);
        updateList();
        adapter=new OyuncularAdapter(requireContext(),list);
        recyclerView.setAdapter(adapter);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.castFab);
        floatingActionButton.setOnClickListener(v -> dismiss());

        return builder.create();

    }

    private void updateList(){
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(sayac=0;sayac<oyuncular.length;sayac++){
                    if(Objects.equals(oyuncular[sayac],Objects.requireNonNull(dataSnapshot.getValue(Oyuncu.class)).getOyuncu_adi())){
                        list.add(dataSnapshot.getValue(Oyuncu.class));
                        adapter.notifyDataSetChanged();
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
    }
}
