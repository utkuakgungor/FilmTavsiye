package com.utkuakgungor.filmtavsiye.details;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.utils.SaveImageHelper;

import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

public class ImageDetailsActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private String image;
    private PhotoView photoView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String filename = UUID.randomUUID().toString()+".jpg";
            Picasso.get().load(image).into(new SaveImageHelper(
                    getApplicationContext().getContentResolver(),
                    filename,
                    image));
            Snackbar.make(coordinatorLayout,getResources().getString(R.string.resimindirme),Snackbar.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        FloatingActionButton back_fab=findViewById(R.id.detailimage_back_fab);
        getImage();
        photoView=findViewById(R.id.imagedetails_image);
        FloatingActionButton fab = findViewById(R.id.detailimage_fab);
        coordinatorLayout=findViewById(R.id.imagedetails_layout);
        Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(photoView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).into(photoView);
            }
        });
        Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_white,Objects.requireNonNull(getTheme()));
        assert myFabSrc != null;
        Drawable willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        back_fab.setImageDrawable(willBeWhite);
        myFabSrc = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_file_download,Objects.requireNonNull(getTheme()));
        assert myFabSrc != null;
        willBeWhite = Objects.requireNonNull(myFabSrc.getConstantState()).newDrawable();
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        fab.setImageDrawable(willBeWhite);
        back_fab.setOnClickListener(v -> onBackPressed());
        fab.setOnClickListener(v -> {
            if(ActivityCompat.checkSelfPermission(ImageDetailsActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },PERMISSION_REQUEST_CODE);
            }
            else {
                String filename = UUID.randomUUID().toString()+".jpg";
                Picasso.get().load(image).into(new SaveImageHelper(
                        getApplicationContext().getContentResolver(),
                        filename,
                        image));
                Snackbar.make(coordinatorLayout,getResources().getString(R.string.resimindirme),Snackbar.LENGTH_LONG).show();
            }
        });

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.colorBlack));
    }

    private void getImage(){
        if(getIntent().hasExtra("image")){
            image=getIntent().getStringExtra("image");
        }
    }
}
