package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ModifyProfil extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userName;
    private Button modifyProfil;
    private ImageView userImg;
    private TextView deleteProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profil);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        userEmail = (EditText) findViewById(R.id.editTextChangeEmail);

        userName = (EditText) findViewById(R.id.editTextChangePseudo);
        userImg = (ImageView) findViewById(R.id.imageViewProfil);

        String pseudo = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        userEmail.setText(email);
        userName.setText(pseudo);
        userImg.setImageURI(photoUrl);

        modifyProfil = (Button) findViewById(R.id.buttonModify);
        modifyProfil.setOnClickListener(this);

        deleteProfil = (TextView) findViewById(R.id.textViewDeleteProfil);
        deleteProfil.setOnClickListener(this);

    }

    public void changeProfil() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String newEmail = userEmail.getText().toString().trim();
        String newPseudo = userName.getText().toString().trim();
        //Uri newPhoto = userImg.

        //Update Pseudo & Photo
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newPseudo)
                //.setPhotoUri(newPhoto)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        });

        //Update Email
        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        });

    }

    public void deleteProfil() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        });
    }

    @Override
    public void onClick (View v) {
        if (v == modifyProfil) {
            changeProfil();
            finish();
            startActivity(new Intent(this, Navigation.class));
        }
        if (v == deleteProfil) {

            // Boite de dialogue confirmation suppression
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Tu veux nous quitter ?")
                    .setContentText("Êtes-vous sûr de vouloir supprimer votre compte ?")
                    .setCancelText("Non")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText("Super choix !")
                                    .setContentText("Nous sommes ravis que tu veuilles continuer l'aventure avec nous !")
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            deleteProfil();
                            sDialog.setTitleText("Suppression !")
                                    .setContentText("Vous ne faites plus partie de la communauté Wild Socks!")
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                            startActivity(new Intent(ModifyProfil.this,MainActivity.class));
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        }

                    })
                    .show();
        }
    }
}
