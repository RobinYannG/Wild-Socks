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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyProfil extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText userEmail;
    private EditText userName;
    private Button modifyProfil;
    private ImageView userImg;
    private TextView deleteProfil;
    private TextView modifyPassword;
    private TextView modifyPhoto;
    private CircleImageView civProfilePic;


    private Uri imageUri;
    private StorageReference mStorageRef;

    private static final int PICK_IMAGE_REQUEST = 256;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profil);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference("users_avatar");

        userEmail = (EditText) findViewById(R.id.editTextChangeEmail);
        civProfilePic = (CircleImageView)findViewById(R.id.profile_image);
        userName = (EditText) findViewById(R.id.editTextChangePseudo);
        userImg = (ImageView) findViewById(R.id.imageViewProfil);

        String pseudo = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        userEmail.setText(email);
        userName.setText(pseudo);
        civProfilePic.setImageURI(photoUrl);

        modifyProfil = (Button) findViewById(R.id.buttonModify);
        modifyProfil.setOnClickListener(this);

        deleteProfil = (TextView) findViewById(R.id.textViewDeleteProfil);
        deleteProfil.setOnClickListener(this);

        modifyPassword = (TextView) findViewById(R.id.textViewModifyPassword);
        modifyPassword.setOnClickListener(this);

        modifyPhoto = (TextView) findViewById(R.id.textViewModifyPhoto);
        modifyPhoto.setOnClickListener(this);

        downloadPicture();
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

    private void openGallery() {
        Intent gallery = new Intent();

        gallery.setType("image/*");

        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {

            if (data == null) {
                //Display an error
                return;
            }
            imageUri = data.getData();
            uploadPicture(imageUri);
            downloadPicture();

        }
    }

    private void uploadPicture(final Uri uri) {

        StorageReference picRef = mStorageRef.child(mAuth.getCurrentUser().getDisplayName()+ "_avatar");

        picRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUrl)
                                .build();

                        user.updateProfile(profileUpdates);
                        downloadPicture();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    private void downloadPicture () {

        StorageReference userPicture = mStorageRef.child(mAuth.getCurrentUser().getDisplayName()+"_avatar");
        userPicture.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ModifyProfil.this)
                        .load(uri)
                        .into(civProfilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
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
        if (v == modifyPassword){
            finish();
            startActivity(new Intent(ModifyProfil.this,ModifyPassword.class));
        }
        if (v == modifyPhoto) {
            openGallery();
        }
    }
}
