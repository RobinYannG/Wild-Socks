package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static fr.wcs.wildcommunitysocks.R.id.imageView;

public class AddPhotos extends Fragment implements View.OnClickListener{

    //private StorageReference mStorageRef;
    private static final int PICK_PHOTO = 100;
    private static final int PICK_IMAGE_REQUEST=255;
    private static final int REQUEST_IMAGE_CAPTURE = 234;

    ImageView showPhoto;
    private Uri imageUri, newUri;
    private Button buttonTakePicture;
    private Button buttonSelectFromGallery;
    private Button buttonUpload;
    private String mCurrentPhotoPath;
    private StorageReference mStorageRef;
    private Chaussette mChaussette;
    private EditText mEditTextLegende;
    private FirebaseAuth firebaseAuth;
    private int key;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;


    public static AddPhotos newInstance() {
        AddPhotos fragment = new AddPhotos();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStorageRef= FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
        View view = inflater.inflate(R.layout.fragment_add_photos, container, false);

        showPhoto = (ImageView) view.findViewById(imageView);
        buttonSelectFromGallery = (Button) view.findViewById(R.id.galleryButton);
        buttonTakePicture=(Button) view.findViewById(R.id.cameraButton);
        buttonUpload=(Button) view.findViewById(R.id.buttonUpload);
        mEditTextLegende = (EditText) view.findViewById(R.id.editsockName);

        buttonTakePicture.setOnClickListener(this);
        buttonSelectFromGallery.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        return view;
    }



    private void openGallery() {
        Intent gallery = new Intent();

        gallery.setType("image/*");

        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE_REQUEST);


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /**Ensure there is a camera activity to handle the Intent*/
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                Uri photoURI = FileProvider.getUriForFile(getActivity(),"com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {

            imageUri = data.getData();
            try {
                Bitmap bitmapOrg = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                showPhoto.setImageBitmap(bitmapOrg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageUri=data.getData();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            showPhoto.setImageBitmap(imageBitmap);

            galleryAddPic();
            return;
        }
    }


    private File createImageFile() throws IOException {
       // Create an image file name
        String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + sdf + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



    private void uploadFile(){
        if(imageUri!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Chargement en cours...");
            progressDialog.show();


            StorageReference picRef = mStorageRef.child(Constants.STORAGE_PATH_UPLOADS+ System.currentTimeMillis()+"."+getFileExtension(imageUri));


            picRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Upload successfull", Toast.LENGTH_LONG);
                            String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String displayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                            mChaussette = new Chaussette(taskSnapshot.getDownloadUrl().toString(),
                                    mEditTextLegende.getText().toString().trim(),
                                    idUser,
                                    displayName,
                                    0);



                            //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mChaussette.setmIdChaussette(uploadId);
                            mDatabase.child(idUser).child(Constants.DATABASE_PATH_UPLOADS).child(uploadId).setValue(mChaussette);
                            mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(uploadId).setValue(mChaussette);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG);

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded :"+(int)progress+"%");

                        }


                    });

        }
        else{
            Toast.makeText(getActivity(),"Foirage", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonTakePicture) {
            dispatchTakePictureIntent();
        }
        if (v == buttonSelectFromGallery) {
            openGallery();
        }
        if (v == buttonUpload) {
            uploadFile();
        }

    }



}
