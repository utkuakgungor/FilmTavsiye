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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.utkuakgungor.filmtavsiye.details.DetailsActivity;
import com.utkuakgungor.filmtavsiye.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.UserViewHolder> {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private List<Model> list;
    private int sira;
    private Context context;

    public RecyclerAdapter(Context context, List<Model> list, FirebaseAuth firebaseAuth, DatabaseReference reference) {
        this.list = list;
        this.firebaseAuth = firebaseAuth;
        this.context = context;
        this.reference = reference;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.txt_film_adi.setText(list.get(position).getFilm_adi());
        holder.txt_film_puani.setText(list.get(position).getFilm_puan());
        Picasso.get().load(list.get(position).getFilm_image()).into(holder.txt_image);
        final Favorites favorites = new Favorites(context);
        holder.txt_fav.setImageResource(R.drawable.ic_favorite);
        if (firebaseAuth.getCurrentUser() != null) {
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (Objects.requireNonNull(snapshot.getValue(Movie.class)).getFilm_adi().equals(list.get(position).getFilm_adi())) {
                        holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);
                    }
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
        } else {
            if (!favorites.Kontrol(list.get(position).getFilm_adi())) {
                holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);
            }
        }

        holder.txt_fav.setOnClickListener(v -> {
            if (firebaseAuth.getCurrentUser() != null) {
                final int[] movieNumber = {0};
                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(Objects.requireNonNull(snapshot.getValue(Movie.class)).getFilm_adi().equals(list.get(position).getFilm_adi())){
                            reference.child(Objects.requireNonNull(snapshot.getKey())).removeValue();
                            movieNumber[0]++;
                        }
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
                if(movieNumber[0] ==0){
                    String id=reference.push().getKey();
                    reference.child(Objects.requireNonNull(id)).setValue(list.get(position));
                }
            } else {
                if (favorites.Kontrol(list.get(position).getFilm_adi())) {
                    favorites.ekleModel(list.get(position));
                    favorites.close();
                    Snackbar.make(v, v.getResources().getString(R.string.favoriekle), Snackbar.LENGTH_LONG).show();
                    holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);
                } else {
                    favorites.deleteData(list.get(position).getFilm_adi());
                    list.remove(position);
                    holder.txt_fav.setImageResource(R.drawable.ic_favorite);
                    Snackbar.make(v, v.getResources().getString(R.string.favoricikti), Snackbar.LENGTH_LONG).show();
                    notifyDataSetChanged();
                }
            }
        });
        holder.txt_layout.setOnClickListener(v ->
        {
            sira = position;
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            intent.putExtra("ad", list.get(position).getFilm_adi());
            intent.putExtra("yil", list.get(position).getFilm_yili());
            intent.putExtra("puan", list.get(position).getFilm_puan());
            intent.putExtra("image", list.get(position).getFilm_image());
            intent.putExtra("youtube", list.get(position).getFilm_youtube());
            intent.putExtra("ozet", list.get(position).getFilm_ozet());
            intent.putExtra("sure", list.get(position).getFilm_sure());
            intent.putExtra("ozet_eng", list.get(position).getFilm_ozet_eng());
            intent.putExtra("sure_eng", list.get(position).getFilm_sure_eng());
            intent.putExtra("color", list.get(position).getText_color());
            intent.putExtra("oyuncular", list.get(position).getFilm_oyuncular());
            intent.putExtra("tur", list.get(position).getFilm_tur());
            intent.putExtra("tur_eng", list.get(position).getFilm_tur_eng());
            intent.putExtra("yonetmen", list.get(position).getFilm_yonetmen());
            intent.putExtra("sinif", list.get(position).getFilm_sinif());
            intent.putExtra("resimler", list.get(position).getFilm_resimler());
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(holder.txt_image, "cardPicture");
            pairs[1] = new Pair<View, String>(holder.txt_film_adi, "cardTitle1");
            pairs[2] = new Pair<View, String>(holder.txt_film_puani, "cardPuan");
            ActivityOptions activityOptions = ActivityOptions.
                    makeSceneTransitionAnimation((Activity) v.getContext(), pairs);
            v.getContext().startActivity(intent, activityOptions.toBundle());
        });

    }

    public int getSira() {
        return sira;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView txt_image;
        TextView txt_film_adi, txt_film_puani;
        ImageButton txt_fav;
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