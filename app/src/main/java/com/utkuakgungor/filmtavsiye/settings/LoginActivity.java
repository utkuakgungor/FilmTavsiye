package com.utkuakgungor.filmtavsiye.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.utils.Favorites;
import com.utkuakgungor.filmtavsiye.models.Model;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CoordinatorLayout coordinatorLayout;
    private GoogleSignInClient googleSignInClient;
    private ProgressBar progressBar;
    private Favorites favorites;
    private DatabaseReference reference;
    private List<Model> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        coordinatorLayout = findViewById(R.id.loginLayout);
        mAuth = FirebaseAuth.getInstance();
        MaterialButton loginButton = findViewById(R.id.btn_mail);
        MaterialButton registerButton = findViewById(R.id.btn_register);
        ImageButton googleButton = findViewById(R.id.googleLogin);
        ImageButton githubButton = findViewById(R.id.githubLogin);
        progressBar = findViewById(R.id.loginBar);
        ImageButton twitterButton = findViewById(R.id.twitterLogin);
        TextInputEditText usernameEdit = findViewById(R.id.loginUsername);
        TextInputEditText passwordEdit = findViewById(R.id.loginPassword);
        loginButton.setOnClickListener(v12 -> {
            progressBar.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(usernameEdit.getText()).toString().equals("") || Objects.requireNonNull(passwordEdit.getText()).toString().equals("")) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(v12, getResources().getString(R.string.text_enter_username_password), Snackbar.LENGTH_LONG).show();
            } else {
                mAuth.signInWithEmailAndPassword(Objects.requireNonNull(usernameEdit.getText()).toString(), Objects.requireNonNull(passwordEdit.getText()).toString())
                        .addOnSuccessListener(authResult -> {
                            favorites=new Favorites(this);
                            list=favorites.getDataFromDB();
                            if(list.size()>0){
                                reference= FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName()));
                                for(int i=0;i<list.size();i++){
                                    String id=reference.push().getKey();
                                    reference.child(Objects.requireNonNull(id)).setValue(list.get(i));
                                    favorites.deleteData(list.get(i).getFilm_id());
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            Intent settingsInten = new Intent(this, SettingsActivity.class);
                            settingsInten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(settingsInten);
                        }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(v12, getResources().getString(R.string.text_username_password), Snackbar.LENGTH_LONG).show();
                });

            }
        });
        registerButton.setOnClickListener(v13 -> {
            Intent registerActivity = new Intent(this, RegisterActivity.class);
            startActivity(registerActivity);
        });
        twitterButton.setOnClickListener(v1 -> {
            progressBar.setVisibility(View.VISIBLE);
            OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
            signInWithProvider(provider);
        });
        googleButton.setOnClickListener(v1 -> {
            progressBar.setVisibility(View.VISIBLE);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(this, gso);
            googleSignIn();
        });
        githubButton.setOnClickListener(v1 -> {
            progressBar.setVisibility(View.VISIBLE);
            OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
            signInWithProvider(provider);
        });
    }

    private void signInWithProvider(OAuthProvider.Builder provider) {
        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask.addOnSuccessListener(
                    authResult -> {
                        favorites=new Favorites(this);
                        list=favorites.getDataFromDB();
                        if(list.size()>0){
                            reference= FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName()));
                            for(int i=0;i<list.size();i++){
                                String id=reference.push().getKey();
                                reference.child(Objects.requireNonNull(id)).setValue(list.get(i));
                                favorites.deleteData(list.get(i).getFilm_id());
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        Intent settingsInten = new Intent(this, SettingsActivity.class);
                        settingsInten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(settingsInten);
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        int index = 0;
                        if (e.getClass().equals(FirebaseAuthUserCollisionException.class)) {
                            FirebaseAuthUserCollisionException exception = (FirebaseAuthUserCollisionException) e;
                            if (exception.getErrorCode().equals("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL")) {
                                Snackbar.make(coordinatorLayout, getResources().getString(R.string.text_user_already_registered), Snackbar.LENGTH_LONG).show();
                                index++;
                            }
                        }
                        if (index == 0) {
                            Snackbar.make(coordinatorLayout, getResources().getString(R.string.text_register_error), Snackbar.LENGTH_LONG).show();
                        }
                    });
        } else {
            mAuth.startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener(
                            authResult -> {
                                favorites=new Favorites(this);
                                list=favorites.getDataFromDB();
                                if(list.size()>0){
                                    reference= FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName()));
                                    for(int i=0;i<list.size();i++){
                                        String id=reference.push().getKey();
                                        reference.child(Objects.requireNonNull(id)).setValue(list.get(i));
                                        favorites.deleteData(list.get(i).getFilm_id());
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                Intent settingsInten = new Intent(this, SettingsActivity.class);
                                settingsInten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(settingsInten);
                            })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        int index = 0;
                        if (e.getClass().equals(FirebaseAuthUserCollisionException.class)) {
                            FirebaseAuthUserCollisionException exception = (FirebaseAuthUserCollisionException) e;
                            if (exception.getErrorCode().equals("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL")) {
                                Snackbar.make(coordinatorLayout, getResources().getString(R.string.text_user_already_registered), Snackbar.LENGTH_LONG).show();
                                index++;
                            }
                        }
                        if (index == 0) {
                            Snackbar.make(coordinatorLayout, getResources().getString(R.string.text_register_error), Snackbar.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void googleSignIn() {
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, getResources().getString(R.string.text_register_success), Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(Objects.requireNonNull(account));
        } catch (ApiException e) {
            Snackbar.make(coordinatorLayout, getResources().getString(R.string.text_register_error), Snackbar.LENGTH_LONG).show();
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential)
                .addOnSuccessListener(authResult -> {
                    favorites=new Favorites(this);
                    list=favorites.getDataFromDB();
                    if(list.size()>0){
                        reference= FirebaseDatabase.getInstance().getReference("Favoriler").child(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName()));
                        for(int i=0;i<list.size();i++){
                            String id=reference.push().getKey();
                            reference.child(Objects.requireNonNull(id)).setValue(list.get(i));
                            favorites.deleteData(list.get(i).getFilm_id());
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    Intent settingsInten = new Intent(coordinatorLayout.getContext(), SettingsActivity.class);
                    settingsInten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(settingsInten);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    int index = 0;
                    if (e.getClass().equals(FirebaseAuthUserCollisionException.class)) {
                        FirebaseAuthUserCollisionException exception = (FirebaseAuthUserCollisionException) e;
                        if (exception.getErrorCode().equals("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL")) {
                            Snackbar.make(coordinatorLayout, getResources().getString(R.string.text_user_already_registered), Snackbar.LENGTH_LONG).show();
                            index++;
                        }
                    }
                    if (index == 0) {
                        Snackbar.make(coordinatorLayout, getResources().getString(R.string.text_register_error), Snackbar.LENGTH_LONG).show();
                    }
                });
    }
}
