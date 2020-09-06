package com.utkuakgungor.filmtavsiye.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.utkuakgungor.filmtavsiye.details.DetailsActivity;
import com.utkuakgungor.filmtavsiye.R;

import java.util.List;

public class FirebaseAdapter extends RecyclerView.Adapter<FirebaseAdapter.UserViewHolder>{

    private List<Movie> list;

    private int sira;

    private Snackbar snackbar;

    private View view;

    private TextView snackbar_text;

    private Context context;

    private String filmadi,filmyili,filmpuani,filmozeti,filmfoto,filmyoutube,filmsure,filmozeteng,filmsureeng,textcolor,filmoyuncular,filmtur,filmtureng,filmyonetmen,filmsinif,filmresimler;

    public FirebaseAdapter(Context context,List<Movie> list){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false));
    }
    
    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position ){
        final Movie user=list.get(position);
        SharedPreferences sharedPreferences=context.getSharedPreferences("Ayarlar",Context.MODE_PRIVATE);
        holder.txt_film_adi.setText(user.getFilm_adi());
        holder.txt_film_puani.setText(user.getFilm_puani());
        final Favorites favorites = new Favorites(context);
        if(!favorites.Kontrol(user.getFilm_adi())){
            holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);
        }
        else{
            holder.txt_fav.setImageResource(R.drawable.ic_favorite);
        }
        Picasso.get().load(user.getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.txt_image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(user.getImage()).into(holder.txt_image);
            }
        });
        holder.txt_fav.setOnClickListener(v -> {
            filmadi=list.get(position).getFilm_adi();
            filmyili=list.get(position).getFilm_yili();
            filmpuani=list.get(position).getFilm_puani();
            filmozeti=list.get(position).getOzet();
            filmfoto=list.get(position).getImage();
            filmyoutube=list.get(position).getYoutube();
            filmsure=list.get(position).getSure();
            filmozeteng=list.get(position).getFilm_ozet_eng();
            filmsureeng=list.get(position).getFilm_sure_eng();
            textcolor=list.get(position).getText_color();
            filmoyuncular=list.get(position).getFilm_oyuncular();
            filmtur=list.get(position).getFilm_tur();
            filmtureng=list.get(position).getFilm_tur_eng();
            filmyonetmen=list.get(position).getFilm_yonetmen();
            filmsinif=list.get(position).getFilm_sinif();
            filmresimler=list.get(position).getFilm_resimler();
            if (favorites.Kontrol(filmadi)){
                favorites.ekle(filmadi,filmyili,filmpuani,filmfoto,filmyoutube,filmozeti,filmsure,filmozeteng,filmsureeng,textcolor,filmoyuncular,filmtur,filmtureng,filmyonetmen,filmsinif,filmresimler);
                favorites.close();
                snackbar = Snackbar.make(v,v.getResources().getString(R.string.favoriekle),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(context,R.color.colorBlack));
                }
                snackbar.show();
                holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);

            }else {
                favorites.deleteData(filmadi);
                snackbar = Snackbar.make(v,v.getResources().getString(R.string.favoricikti),Snackbar.LENGTH_LONG);
                view=snackbar.getView();
                snackbar_text = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                if(sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(context,R.color.colorBlack));
                }
                snackbar.show();
                holder.txt_fav.setImageResource(R.drawable.ic_favorite);

            }
        });
        holder.txt_layout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            sira=position;
            intent.putExtra("ad", list.get(position).getFilm_adi());
            intent.putExtra("yil", list.get(position).getFilm_yili());
            intent.putExtra("puan", list.get(position).getFilm_puani());
            intent.putExtra("image",list.get(position).getImage());
            intent.putExtra("youtube",list.get(position).getYoutube());
            intent.putExtra("ozet",list.get(position).getOzet());
            intent.putExtra("sure",list.get(position).getSure());
            intent.putExtra("ozet_eng",list.get(position).getFilm_ozet_eng());
            intent.putExtra("sure_eng",list.get(position).getFilm_sure_eng());
            intent.putExtra("color",list.get(position).getText_color());
            intent.putExtra("oyuncular",list.get(position).getFilm_oyuncular());
            intent.putExtra("tur",list.get(position).getFilm_tur());
            intent.putExtra("tur_eng",list.get(position).getFilm_tur_eng());
            intent.putExtra("yonetmen",list.get(position).getFilm_yonetmen());
            intent.putExtra("sinif",list.get(position).getFilm_sinif());
            intent.putExtra("resimler",list.get(position).getFilm_resimler());
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View,String>(holder.txt_image,"cardPicture");
            pairs[1] = new Pair<View,String>(holder.txt_film_adi,"cardTitle1");
            pairs[2] = new Pair<View,String>(holder.txt_film_puani,"cardPuan");
            ActivityOptions activityOptions=ActivityOptions.
                    makeSceneTransitionAnimation((Activity)v.getContext(),pairs);
            v.getContext().startActivity(intent,activityOptions.toBundle() );
        });
    }

    public int getSira(){
        return sira;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView txt_film_adi,txt_film_puani;
        ImageButton txt_fav;
        ImageView txt_image;
        RelativeLayout txt_layout;

        private UserViewHolder(View itemView){
            super(itemView);

            txt_fav= itemView.findViewById(R.id.button_fav);
            txt_film_adi=itemView.findViewById(R.id.cardTitle1);
            txt_film_puani=itemView.findViewById(R.id.cardTitle2);
            txt_image=itemView.findViewById(R.id.cardPicture);
            txt_layout=itemView.findViewById(R.id.cardLayout);
        }
    }
}
