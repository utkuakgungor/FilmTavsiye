package com.utkuakgungor.filmtavsiye.details;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.models.MovieFirebase;
import com.utkuakgungor.filmtavsiye.utils.FirebaseAdapter;
import com.utkuakgungor.filmtavsiye.models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TurlerDetailsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String tur, turler;
    private DatabaseReference reference, referenceFavoriler;
    private List<MovieFirebase> result;
    private FirebaseAdapter adapter;
    private ImageView imageView;

    private String url = "https://source.unsplash.com/858x480/?movie,camera";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            referenceFavoriler = FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail().replace(".", "").replace("#", "").replace("$", "").replace("[", "").replace("]", "")));
        }
        setContentView(R.layout.activity_turlerdetails);
        getIncomingIntent();
        Toolbar toolbar = findViewById(R.id.detay_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        TextView imageTextview = findViewById(R.id.ImageViewText);
        imageView = findViewById(R.id.detayImage);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Filmler");
        reference.keepSynced(true);
        result = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.firebaselist);
        recyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);

        imageTextview.setText(tur);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    collapsingToolbarLayout.setTitle(tur);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        Picasso.get().load(url).into(imageView);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new FirebaseAdapter("turler", TurlerDetailsActivity.this, result, firebaseAuth, referenceFavoriler);
        recyclerView.setAdapter(adapter);
        updateList();

    }

    private void updateList() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (Objects.equals(Locale.getDefault().getDisplayLanguage(), "English")) {
                    turler = Objects.requireNonNull(dataSnapshot.getValue(MovieFirebase.class)).getFilm_tur_eng().replace(",", "");
                } else {
                    turler = Objects.requireNonNull(dataSnapshot.getValue(MovieFirebase.class)).getFilm_tur().replace(",", "");
                }
                if (turler.contains(tur)) {
                    result.add(dataSnapshot.getValue(MovieFirebase.class));
                    adapter.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyItemChanged(adapter.getSira());
    }

    private void getIncomingIntent() {
        tur = getIntent().getStringExtra("tur");
    }
}
