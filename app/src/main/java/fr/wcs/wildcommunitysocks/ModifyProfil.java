package fr.wcs.wildcommunitysocks;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyProfil extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText userEmail;
    private EditText userName;
    private EditText userPointure;
    private ImageButton modifyProfil;
    private ImageButton deleteProfil;
    private ImageButton modifyPassword;
    private ImageButton modifyPhoto;
    private CircleImageView civProfilePic;
    private String mCurrentPhotoPath;


    private static final int REQUEST_IMAGE_CAPTURE = 234;


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

        userEmail = (EditText) findViewById(R.id.editTextModifyEmail);
        userName = (EditText) findViewById(R.id.editTextModifyPseudo);
        userPointure = (EditText) findViewById(R.id.editTextModifyEmail);

        civProfilePic = (CircleImageView)findViewById(R.id.profile_image);
        civProfilePic.setOnClickListener(this);



        String pseudo = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        userEmail.setText(email);
        userName.setText(pseudo);
        civProfilePic.setImageURI(photoUrl);

        modifyProfil = (ImageButton) findViewById(R.id.imageButtonRegister);
        modifyProfil.setOnClickListener(this);

        deleteProfil = (ImageButton) findViewById(R.id.imageButtonRemoveAccount);
        deleteProfil.setOnClickListener(this);

        modifyPassword = (ImageButton) findViewById(R.id.imageButtonModifyPassword);
        modifyPassword.setOnClickListener(this);

        modifyPhoto = (ImageButton) findViewById(R.id.imageButtonModifyPicture);
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            imageUri=data.getData();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            civProfilePic.setImageBitmap(imageBitmap);

            galleryAddPic();
            uploadPicture(imageUri);
            downloadPicture();

            return;
        }
    }

    private void uploadPicture(final Uri uri) {

        if (uri!=null) {
            StorageReference picRef = mStorageRef.child(mAuth.getCurrentUser().getDisplayName() + "_avatar");

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
        else{

            civProfilePic.setDrawingCacheEnabled(true);
            Bitmap imagebitmap = civProfilePic.getDrawingCache();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference picRef = mStorageRef.child(mAuth.getCurrentUser().getDisplayName() + "_avatar");



            UploadTask uploadTask = picRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                            Toast.makeText(ModifyProfil.this, "Raté", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /**Ensure there is a camera activity to handle the Intent*/
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            /** Create the file where the photo should go
             */
            File photoFile=null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            }


        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + sdf + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
        if (v == modifyPassword || v == civProfilePic){
            finish();
            startActivity(new Intent(ModifyProfil.this,ModifyPassword.class));
        }
        if (v == modifyPhoto) {


            new SweetAlertDialog(this)
                    .setTitleText("Alors tu ressembles à quoi!")
                    .setCancelText("Gallery")
                    .setConfirmText("Camera")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            openGallery();
                            sDialog.cancel();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            dispatchTakePictureIntent();
                            sDialog.cancel();

                        }

                    })
                    .show();
        }
    }
}
