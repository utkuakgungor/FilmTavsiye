package com.utkuakgungor.filmtavsiye.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.utkuakgungor.filmtavsiye.R;

import java.util.Objects;

public class SearchFragment extends Fragment {

    private Chip action,adventure,crime,mystery,drama,thriller,science,fantasy,western,war,comedy,history;
    private SearchResultFragment searchResultFragment;
    private EditText film_adi,film_yonetmen,film_oyuncu;
    private String actionS,adventureS,scienceS,fantasyS,thrillerS,dramaS,crimeS,mysteryS,westernS,warS,comedyS,historyS;
    private SharedPreferences sharedPreferences;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("Ayarlar", Context.MODE_PRIVATE);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.fragment_search, container, false);
        sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("Ayarlar",Context.MODE_PRIVATE);
        setHasOptionsMenu(true);
        FloatingActionButton button = v.findViewById(R.id.buttonsearch);
        searchResultFragment=new SearchResultFragment();
        action=v.findViewById(R.id.searchturaction);
        adventure=v.findViewById(R.id.searchturadventure);
        science=v.findViewById(R.id.searchturscience);
        fantasy=v.findViewById(R.id.searchturfantasy);
        thriller=v.findViewById(R.id.searchturthriller);
        drama=v.findViewById(R.id.searchturdrama);
        crime=v.findViewById(R.id.searchturcrime);
        mystery=v.findViewById(R.id.searchturmystery);
        western=v.findViewById(R.id.searchturwestern);
        war=v.findViewById(R.id.searchturwar);
        comedy=v.findViewById(R.id.searchturcomedy);
        history=v.findViewById(R.id.searchturhistory);
        film_adi=v.findViewById(R.id.searchedit1);
        film_yonetmen=v.findViewById(R.id.searchedit2);
        film_oyuncu=v.findViewById(R.id.searchedit3);

        film_adi.setOnKeyListener((v1, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                Search();
                return true;
            }
            return false;
        });

        film_oyuncu.setOnKeyListener((v12, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                Search();
                return true;
            }
            return false;
        });

        film_yonetmen.setOnKeyListener((v13, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                Search();
                return true;
            }
            return false;
        });

        button.setOnClickListener(v14 -> Search());
        return v;
    }

    private void Doldurma(){
        if(action.isChecked()){
            actionS=getResources().getString(R.string.turaction);
        }
        if(adventure.isChecked()){
            adventureS= getResources().getString(R.string.turadventure);
        }
        if(science.isChecked()){
            scienceS= getResources().getString(R.string.turscience);
        }
        if(fantasy.isChecked()){
            fantasyS = getResources().getString(R.string.turfantasy);
        }
        if(thriller.isChecked()){
            thrillerS= getResources().getString(R.string.turthriller);
        }
        if(drama.isChecked()){
            dramaS= getResources().getString(R.string.turdrama);
        }
        if(crime.isChecked()){
            crimeS= getResources().getString(R.string.turcrime);
        }
        if(mystery.isChecked()){
            mysteryS= getResources().getString(R.string.turmystery);
        }
        if(western.isChecked()){
            westernS=getResources().getString(R.string.turwestern);
        }
        if(war.isChecked()){
            warS=getResources().getString(R.string.turwar);
        }
        if(comedy.isChecked()){
            comedyS=getResources().getString(R.string.turcomedy);
        }
        if(history.isChecked()){
            historyS=getResources().getString(R.string.turhistory);
        }
    }

    private void Search(){
        String filmadi = film_adi.getText().toString();
        String yonetmen = film_yonetmen.getText().toString();
        String oyuncu = film_oyuncu.getText().toString();
        actionS = null ;
        adventureS=null;
        scienceS=null;
        fantasyS=null;
        thrillerS=null;
        dramaS=null;
        crimeS=null;
        mysteryS=null;
        westernS=null;
        warS=null;
        comedyS=null;
        historyS=null;
        Bundle bundle = new Bundle();
        bundle.putString("adi",filmadi);
        bundle.putString("yonetmen",yonetmen);
        bundle.putString("oyuncu",oyuncu);
        Doldurma();
        bundle.putString("action",actionS);
        bundle.putString("adventure",adventureS);
        bundle.putString("science",scienceS);
        bundle.putString("fantasy",fantasyS);
        bundle.putString("thriller",thrillerS);
        bundle.putString("drama",dramaS);
        bundle.putString("crime",crimeS);
        bundle.putString("mystery",mysteryS);
        bundle.putString("western",westernS);
        bundle.putString("war",warS);
        bundle.putString("comedy",comedyS);
        bundle.putString("history",historyS);
        searchResultFragment.setArguments(bundle);
        if(!TextUtils.isEmpty(filmadi) || !TextUtils.isEmpty(yonetmen) || !TextUtils.isEmpty(oyuncu) || action.isChecked() || adventure.isChecked() || science.isChecked() || fantasy.isChecked()
                || thriller.isChecked() || drama.isChecked() || crime.isChecked() || mystery.isChecked() || western.isChecked()
                || war.isChecked() || comedy.isChecked() || history.isChecked()){
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.main_frame,searchResultFragment,"Result")
                    .addToBackStack(null).commit();
        }
        else{
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()),getResources().getString(R.string.degergir),Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView snackbar_text = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            if(sharedPreferences.contains("Gece")){
                view.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.colorGray));
                snackbar_text.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
            }
            snackbar.show();
        }
    }

}
