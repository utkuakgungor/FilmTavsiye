package com.utkuakgungor.filmtavsiye.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.details.OyuncuDetailsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

public class OyuncularAdapter extends RecyclerView.Adapter<OyuncularAdapter.ViewHolder> {

    private List<Oyuncu> list;
    private Pair[] pairs;
    private RequestQueue requestQueue;

    OyuncularAdapter(Context context, List<Oyuncu> list) {
        this.list = new ArrayList<>();
        this.list = list;
        requestQueue= Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public OyuncularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.oyuncular, null);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OyuncularAdapter.ViewHolder holder, final int position) {
        final Oyuncu user = list.get(position);
        pairs = new Pair[2];
        holder.txt_oyuncu.setText(user.getOyuncu_adi());
        String urlBasPerson = "https://api.themoviedb.org/3/person/";
        String urlSonPerson = "?api_key=" + TMDBApi.getApiKey() + "&language=en-US";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlBasPerson + user.getPerson_id() + urlSonPerson, null,
                response -> {
                    try {
                        if (Objects.equals(response.getString("profile_path"), "null") || Objects.equals(response.getString("profile_path"), "")) {
                            Picasso.get().load(R.drawable.ic_person).into(holder.txt_image);
                        } else {
                            Picasso.get().load("https://image.tmdb.org/t/p/original" + response.getString("profile_path")).networkPolicy(NetworkPolicy.OFFLINE).into(holder.txt_image, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    try {
                                        Picasso.get().load("https://image.tmdb.org/t/p/original" + response.getString("profile_path")).into(holder.txt_image);
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);

        holder.txt_relative.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), OyuncuDetailsActivity.class);
            pairs[0] = new Pair<View, String>(holder.txt_image, "picture");
            pairs[1] = new Pair<View, String>(holder.txt_oyuncu, "text");
            intent.putExtra("oyuncu", list.get(position).getOyuncu_adi());
            intent.putExtra("secenek", "oyuncu");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(), pairs);
            v.getContext().startActivity(intent, activityOptions.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_oyuncu;
        ImageView txt_image;
        CardView txt_relative;

        private ViewHolder(View itemView) {
            super(itemView);

            txt_image = itemView.findViewById(R.id.oyuncularResim);
            txt_oyuncu = itemView.findViewById(R.id.oyuncularText);
            txt_relative = itemView.findViewById(R.id.oyuncularRelative);
        }
    }

}
