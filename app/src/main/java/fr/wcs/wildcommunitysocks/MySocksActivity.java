package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MySocksActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase references
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    //Layout elements
    private ImageView thisSock, seeComments;
    private TextView thisLegend, owner, thisRating;
    private String url;
    private float initialRate;
    private Button modifyButton;
    private Button removeButton;
    private static Chaussette result;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_socks);

        thisSock = (ImageView) findViewById(R.id.sockImage);
        thisLegend = (TextView) findViewById(R.id.sockLegend);
        owner = (TextView) findViewById(R.id.sockOwner);
        thisRating = (TextView) findViewById(R.id.sockRating);
        seeComments =(ImageView) findViewById(R.id.imageViewComment);
        seeComments.setOnClickListener(this);

        modifyButton = (Button) findViewById(R.id.buttonModify);
        modifyButton.setOnClickListener(this);

        removeButton = (Button) findViewById(R.id.buttonRemove);
        removeButton.setOnClickListener(this);

        Intent onStart = getIntent();
        result = onStart.getParcelableExtra("sock");

        String leg = result.getmLegende();
        String author = result.getmDisplayNameUser();

        url = result.getmImgChaussette();
        initialRate = result.getmNote();
        thisLegend.setText(leg);
        owner.setText(author);

        thisRating.setText(String.valueOf(initialRate));


        new MySocksActivity.SockImage(thisSock).execute(url);

    }

    public Bitmap loadImageFromNetwork(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) { return null; }
    }


    private class SockImage extends AsyncTask<String, Void, Bitmap> {

        private ImageView miMv;

        public SockImage (ImageView iMv){
            miMv = iMv;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return loadImageFromNetwork(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            thisSock.setImageBitmap(bitmap);
        }
    }


    public void removeButton() {
       mAuth = FirebaseAuth.getInstance();
       FirebaseUser user = mAuth.getCurrentUser();
       mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
       Intent onStart = getIntent();
       Chaussette result = onStart.getParcelableExtra("sock");
       mDatabase.child(user.getUid()).child(Constants.DATABASE_PATH_UPLOADS).child(result.getmIdChaussette()).removeValue();
       mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(result.getmIdChaussette()).removeValue();

        StorageReference mStorageRef= FirebaseStorage.getInstance().getReference();
        StorageReference desertRef = mStorageRef.child(Constants.STORAGE_PATH_UPLOADS).child(result.getmIdChaussette());
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }


    @Override
    public void onClick (View v) {
        if(v==seeComments){
            Intent comments =new Intent(MySocksActivity.this,CommentActivity.class);
            comments.putExtra("sock",result);
            startActivity(comments);
        }


        if (v == modifyButton) {
            Intent intent = new Intent(this, ModifyMySocksActivity.class);
            intent.putExtra("sock", result);
            startActivity(intent);
        }


        if (v == removeButton) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.removeAlertDialogTitle))
                    .setContentText(getString(R.string.removeAlertDialogQuestion))
                    .setCancelText(getString(R.string.alertDialogueCancel))
                    .setConfirmText(getString(R.string.alertDialogConfirm))
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText(getString(R.string.removeAlertDialogGo))
                                    .setContentText("!!!")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            removeButton();
                            sDialog.setTitleText("Supprimer !")
                                    .setContentText("...")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                            //startActivity(new Intent(MySocksActivity.this,Navigation.class));
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    })
                    .show();

        }
    }

}