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
import com.utkuakgungor.filmtavsiye.details.DetailsActivity;
import com.utkuakgungor.filmtavsiye.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.UserViewHolder>{

    private List<Model> list;
    private int sira;
    private Context context;
    private String filmadi,filmyili,filmpuani,filmozeti,filmfoto,filmyoutube,filmsure,filmozeteng,filmsureeng,textcolor,filmoyuncular,filmtur,filmtureng,filmyonetmen,filmsinif,filmresimler;
    private Snackbar snackbar;
    private View view;
    private TextView snackbar_text;

    public RecyclerAdapter(Context context,List<Model> list) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        SharedPreferences sharedPreferences=context.getSharedPreferences("Ayarlar",Context.MODE_PRIVATE);
        holder.txt_film_adi.setText(list.get(position).getFilm_adi());
        holder.txt_film_puani.setText(list.get(position).getFilm_puan());
        Picasso.get().load(list.get(position).getFilm_image()).into(holder.txt_image);
        final Favorites favorites = new Favorites(context);
        if(!favorites.Kontrol(list.get(position).getFilm_adi())){
            holder.txt_fav.setImageResource(R.drawable.ic_favorite_ekli);
        }
        holder.txt_fav.setOnClickListener(v -> {
            filmadi=list.get(position).getFilm_adi();
            filmyili=list.get(position).getFilm_yili();
            filmpuani=list.get(position).getFilm_puan();
            filmozeti=list.get(position).getFilm_ozet();
            filmfoto=list.get(position).getFilm_image();
            filmyoutube=list.get(position).getFilm_youtube();
            filmsure=list.get(position).getFilm_sure();
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
                if (sharedPreferences.contains("Gece")){
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
                if (sharedPreferences.contains("Gece")){
                    view.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGray));
                    snackbar_text.setTextColor(ContextCompat.getColor(context,R.color.colorBlack));
                }
                snackbar.show();
                list.remove(position);
                notifyDataSetChanged();

            }
        });
        holder.txt_layout.setOnClickListener(v -> {
            sira=position;
            Intent intent = new Intent(v.getContext(),DetailsActivity.class);
            intent.putExtra("ad", list.get(position).getFilm_adi());
            intent.putExtra("yil", list.get(position).getFilm_yili());
            intent.putExtra("puan", list.get(position).getFilm_puan());
            intent.putExtra("image",list.get(position).getFilm_image());
            intent.putExtra("youtube",list.get(position).getFilm_youtube());
            intent.putExtra("ozet",list.get(position).getFilm_ozet());
            intent.putExtra("sure",list.get(position).getFilm_sure());
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
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        ImageView txt_image;
        TextView txt_film_adi,txt_film_puani;
        ImageButton txt_fav;
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