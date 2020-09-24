package com.utkuakgungor.filmtavsiye.main;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.utkuakgungor.filmtavsiye.favorites.FavoritesFragment;
import com.utkuakgungor.filmtavsiye.home.HomeFragment;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.search.SearchFragment;


public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private FavoritesFragment favoritesFragment;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences=getSharedPreferences("Ayarlar",MODE_PRIVATE);
        if(sharedPreferences.contains("Dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else if(sharedPreferences.contains("Light")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        homeFragment = new HomeFragment();
        favoritesFragment = new FavoritesFragment();
        searchFragment=new SearchFragment();
        setTitle(getResources().getString(R.string.nav_home));
        setFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.nav_history:
                    setTitle(getResources().getString(R.string.nav_home));
                    setFragment(homeFragment);
                    return true;
                case R.id.nav_search:
                    setTitle(getResources().getString(R.string.navsearch));
                    setFragment(searchFragment);
                    return true;
                case R.id.nav_favorites:
                    setTitle(getResources().getString(R.string.nav_favorites));
                    setFragment(favoritesFragment);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentManager.popBackStack();
        fragmentTransaction.commit();
    }
}
