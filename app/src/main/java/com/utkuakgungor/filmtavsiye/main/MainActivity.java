package com.utkuakgungor.filmtavsiye.main;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        if(sharedPreferences.contains("Gece")){
            setTheme(R.style.AppThemeDark);
        }
        else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_nav);
        homeFragment = new HomeFragment();
        favoritesFragment = new FavoritesFragment();
        searchFragment=new SearchFragment();
        setFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.nav_history:
                    setFragment(homeFragment);
                    return true;
                case R.id.nav_search:
                    setFragment(searchFragment);
                    return true;
                case R.id.nav_favorites:
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
