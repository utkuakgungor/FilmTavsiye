package com.utkuakgungor.filmtavsiye.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.utkuakgungor.filmtavsiye.R;
import com.utkuakgungor.filmtavsiye.utils.Favorites;
import com.utkuakgungor.filmtavsiye.models.Model;

import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private Favorites favorites;
    private List<Model> list;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextInputEditText passwordEdit = findViewById(R.id.editPassword);
        progressBar = findViewById(R.id.registerBar);
        TextInputEditText passwordValidEdit = findViewById(R.id.editPasswordValid);
        TextInputEditText emailEdit = findViewById(R.id.editEmail);
        FloatingActionButton btnRegister = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(v1 -> {
            progressBar.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(passwordEdit.getText()).toString().equals("")
                    || Objects.requireNonNull(passwordValidEdit.getText()).toString().equals("")
                    || Objects.requireNonNull(emailEdit.getText()).toString().equals("")) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(v1, getResources().getString(R.string.text_enter_fields), Snackbar.LENGTH_LONG).show();
            } else if (!passwordEdit.getText().toString().equals(passwordValidEdit.getText().toString())) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(v1, getResources().getString(R.string.text_passwords_same), Snackbar.LENGTH_LONG).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches()) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(v1, getResources().getString(R.string.text_enter_email), Snackbar.LENGTH_LONG).show();
            } else {
                mAuth.createUserWithEmailAndPassword(emailEdit.getText().toString().trim(), passwordEdit.getText().toString())
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
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
                                Toast.makeText(this, getResources().getString(R.string.text_register_success), Toast.LENGTH_LONG).show();
                                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                                settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(settingsIntent);
                            } else {
                                Snackbar.make(v1, getResources().getString(R.string.text_register_error), Snackbar.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            int index = 0;
                            if (e.getClass().equals(FirebaseAuthUserCollisionException.class)) {
                                FirebaseAuthUserCollisionException exception = (FirebaseAuthUserCollisionException) e;
                                if (exception.getErrorCode().equals("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL")) {
                                    Snackbar.make(v1, getResources().getString(R.string.text_user_already_registered), Snackbar.LENGTH_LONG).show();
                                    index++;
                                }
                            }
                            if (index == 0) {
                                Snackbar.make(v1, getResources().getString(R.string.text_register_error), Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
