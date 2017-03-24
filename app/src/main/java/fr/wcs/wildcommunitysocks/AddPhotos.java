package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AddPhotos extends Fragment implements View.OnClickListener{

    //private StorageReference mStorageRef;
    private static final int PICK_PHOTO = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    ImageView showPhoto;
    private Uri imageUri;
    private Button buttonTakePicture;
    private Button buttonSelectFromGallery;
    private Button buttonUpload;
    private String mCurrentPhotoPath;


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

       // mStorageRef= FirebaseStorage.getInstance().getReference();
        View view = inflater.inflate(R.layout.fragment_add_photos, container, false);

        showPhoto = (ImageView) view.findViewById(R.id.imageView);
        buttonSelectFromGallery = (Button) view.findViewById(R.id.galleryButton);
        buttonTakePicture=(Button) view.findViewById(R.id.cameraButton);
        buttonUpload=(Button) view.findViewById(R.id.buttonUpload);

        buttonTakePicture.setOnClickListener(this);
        buttonSelectFromGallery.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        return view;
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");

        gallery.putExtra("crop", "true");
        gallery.putExtra("scale", "true");
        gallery.putExtra("outputX", 200);
        gallery.putExtra("outputY", 200);
        gallery.putExtra("aspectX", 1);
        gallery.putExtra("aspectY", 1);
        gallery.putExtra("return-data", "true");

        startActivityForResult(gallery, PICK_PHOTO);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /**Ensure there is a camera activity to handle the Intent*/
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        /**    takePictureIntent.setType("image/*");
            takePictureIntent.putExtra("crop", "true");
            takePictureIntent.putExtra("scale", "true");
            takePictureIntent.putExtra("outputX", 200);
            takePictureIntent.putExtra("outputY", 200);
            takePictureIntent.putExtra("aspectX", 1);
            takePictureIntent.putExtra("aspectY", 1);
            takePictureIntent.putExtra("return-data", "true");**/
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
                Uri photoURI;
                try {
                    photoURI = FileProvider.getUriForFile(getActivity(),"com.example.android.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                //Uri photoURI = FileProvider.getUriForFile(getActivity(),
                 //       "com.example.android.fileprovider", photoFile);
               // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**NB modified from 'protected' to 'public' to avoid conflict**/
        if (resultCode == RESULT_OK && requestCode == PICK_PHOTO) {
            Bundle extras = data.getExtras();
            imageUri = data.getData();
            showPhoto.setImageURI(imageUri);
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //showPhoto.setImageBitmap(imageBitmap);
            //galleryAddPic();
            return;
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageUri=data.getData();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            /**scaling the image
            // Get the dimensions of the View
            int targetW = 200;
            int targetH = 200;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            showPhoto.setImageBitmap(bitmap);
            //**/
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

    private void uploadFile(){

    }

    @Override
    public void onClick(View v) {
        if(v==buttonTakePicture){
            dispatchTakePictureIntent();
        }
        if(v==buttonSelectFromGallery){
            openGallery();
        }
        if(v==buttonUpload){
            uploadFile();
        }

    }


}
