package com.utkuakgungor.filmtavsiye.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.utkuakgungor.filmtavsiye.details.DetailsActivity;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.models.APIMovie;
import com.utkuakgungor.filmtavsiye.models.MovieFirebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseAdapter extends RecyclerView.Adapter<FirebaseAdapter.UserViewHolder> {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private List<MovieFirebase> list;
    private int sira;
    private RequestQueue requestQueue;
    private List<String> firebaseList, firebaseKeys;
    private Context context;
    private String fragmentName;

    public FirebaseAdapter(String fragmentName,Context context, List<MovieFirebase> list, FirebaseAuth firebaseAuth, DatabaseReference reference) {
        this.list = list;
        this.reference = reference;
        this.firebaseAuth = firebaseAuth;
        this.context = context;
        this.fragmentName=fragmentName;
        requestQueue = Volley.newRequestQueue(context);
        firebaseKeys = new ArrayList<>();
        firebaseList = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        String url = "https://image.tmdb.org/t/p/original";
        String requestUrl = "https://api.themoviedb.org/3/movie/" + list.get(position).getFilm_id() + "?api_key=" + TMDBApi.getApiKey() + "&language=en-US";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                response -> {
                    String jsonString = response.toString();
                    Gson gson = new Gson();
                    APIMovie data = gson.fromJson(jsonString, APIMovie.class);
                    String requestUrlTR = "https://api.themoviedb.org/3/movie/" + list.get(position).getFilm_id() + "?api_key=" + TMDBApi.getApiKey() + "&language=tr-TR";
                    JsonObjectRequest requestTR = new JsonObjectRequest(Request.Method.GET, requestUrlTR, null,
                            responseTR -> {
                                String jsonStringTR = responseTR.toString();
                                Gson gsonTR = new Gson();
                                APIMovie dataTR = gsonTR.fromJson(jsonStringTR, APIMovie.class);
                                holder.txt_film_adi.setText(data.getTitle());
                                holder.txt_film_puani.setText(data.getVoteAverage());
                                final Favorites favorites = new Favorites(context);
                                holder.txt_fav.setImageResource(R.drawable.ic_favorite);
                                if (firebaseAuth.getCurrentUser() != null) {
                                    reference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            firebaseList.add(snapshot.getValue(MovieFirebase.class).getFilm_id());
                                            firebaseKeys.add(snapshot.getKey());
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
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (firebaseList.contains(list.get(position).getFilm_id())) {
                                                holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } else {
                                    if (!favorites.Kontrol(list.get(position).getFilm_id())) {
                                        holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);
                                    }
                                }
                                Picasso.get().load(url + data.getPosterPath()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.txt_image, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get().load(url + data.getPosterPath()).into(holder.txt_image);
                                    }
                                });
                                holder.txt_fav.setOnClickListener(v -> {
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        firebaseList = new ArrayList<>();
                                        firebaseKeys = new ArrayList<>();
                                        reference.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                firebaseList.add(snapshot.getValue(MovieFirebase.class).getFilm_id());
                                                firebaseKeys.add(snapshot.getKey());
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
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (firebaseList.contains(list.get(position).getFilm_id())) {
                                                    reference.child(firebaseKeys.get(firebaseList.indexOf(list.get(position).getFilm_id()))).removeValue();
                                                    holder.txt_fav.setImageResource(R.drawable.ic_favorite);
                                                    if(fragmentName.equals("favorites")){
                                                        list.remove(position);
                                                        notifyDataSetChanged();
                                                    }
                                                    Snackbar.make(v, v.getResources().getString(R.string.favoricikti), Snackbar.LENGTH_LONG).show();
                                                } else {
                                                    String id = reference.push().getKey();
                                                    MovieFirebase movieFirebase = list.get(position);
                                                    reference.child(id).setValue(movieFirebase);
                                                    Snackbar.make(v, v.getResources().getString(R.string.favoriekle), Snackbar.LENGTH_LONG).show();
                                                    holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        if (favorites.Kontrol(list.get(position).getFilm_id())) {
                                            favorites.ekleFirebase(list.get(position));
                                            favorites.close();
                                            Snackbar.make(v, v.getResources().getString(R.string.favoriekle), Snackbar.LENGTH_LONG).show();
                                            holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);
                                        } else {
                                            favorites.deleteData(list.get(position).getFilm_id());
                                            holder.txt_fav.setImageResource(R.drawable.ic_favorite);
                                            Snackbar.make(v, v.getResources().getString(R.string.favoricikti), Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                holder.txt_layout.setOnClickListener(v -> {
                                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                                    sira = position;
                                    intent.putExtra("id", list.get(position).getFilm_id());
                                    intent.putExtra("ad", data.getTitle());
                                    intent.putExtra("yil", data.getReleaseDate().split("-")[0]);
                                    intent.putExtra("puan", data.getVoteAverage().toString());
                                    intent.putExtra("image", url + data.getPosterPath());
                                    intent.putExtra("ozet", dataTR.getOverview());
                                    intent.putExtra("sure", Integer.parseInt(data.getRuntime()) / 60 + " saat " + Integer.parseInt(data.getRuntime()) % 60 + " dakika");
                                    intent.putExtra("ozet_eng", data.getOverview());
                                    intent.putExtra("sure_eng", Integer.parseInt(data.getRuntime()) / 60 + " hours " + Integer.parseInt(data.getRuntime()) % 60 + " minutes");
                                    intent.putExtra("tur", list.get(position).getFilm_tur());
                                    intent.putExtra("tur_eng", list.get(position).getFilm_tur_eng());
                                    intent.putExtra("sinif", list.get(position).getFilm_sinif());
                                    Pair[] pairs = new Pair[3];
                                    pairs[0] = new Pair<View, String>(holder.txt_image, "cardPicture");
                                    pairs[1] = new Pair<View, String>(holder.txt_film_adi, "cardTitle1");
                                    pairs[2] = new Pair<View, String>(holder.txt_film_puani, "cardPuan");
                                    ActivityOptions activityOptions = ActivityOptions.
                                            makeSceneTransitionAnimation((Activity) v.getContext(), pairs);
                                    ((Activity) context).startActivityForResult(intent,2, activityOptions.toBundle());
                                });
                            }, Throwable::printStackTrace);
                    requestQueue.add(requestTR);
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    public int getSira() {
        return sira;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txt_film_adi, txt_film_puani;
        ImageButton txt_fav;
        ImageView txt_image;
        RelativeLayout txt_layout;

        private UserViewHolder(View itemView) {
            super(itemView);

            txt_fav = itemView.findViewById(R.id.button_fav);
            txt_film_adi = itemView.findViewById(R.id.cardTitle1);
            txt_film_puani = itemView.findViewById(R.id.cardTitle2);
            txt_image = itemView.findViewById(R.id.cardPicture);
            txt_layout = itemView.findViewById(R.id.cardLayout);
        }
    }
}
