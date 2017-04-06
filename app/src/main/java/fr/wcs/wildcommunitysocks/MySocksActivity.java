package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MySocksActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase references
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    //progress dialog
    private ProgressDialog progressDialog;

    //Layout elements
    private ImageView thisSock;
    private TextView thisLegend, owner, thisRating;
    private RatingBar ratingBar;
    private String url;
    private Button ratingButton;
    private float myRate=0;
    private float initialRate;
    private Button modifyButton;
    private Button removeButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socks);

     /**   //initializing Firebase authentification objects
        mAuth = FirebaseAuth.getInstance();

        //if the user is not logged in that means current user will return null
        if(mAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, IdentificationActivity.class));
        }  */




        thisSock = (ImageView) findViewById(R.id.sockImage);
        thisLegend = (TextView) findViewById(R.id.sockLegend);
        owner = (TextView) findViewById(R.id.sockOwner);
        thisRating = (TextView) findViewById(R.id.sockRating);

        modifyButton = (Button) findViewById(R.id.buttonModify);
        modifyButton.setOnClickListener(this);

        removeButton = (Button) findViewById(R.id.buttonRemove);
        removeButton.setOnClickListener(this);




       /** ratingButton = (Button) findViewById(R.id.ratingButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);  */


        /**Retrieve the object**/

        Intent onStart = getIntent();
        final Chaussette result = onStart.getParcelableExtra("sock");

        String leg = result.getmLegende();
        String author = result.getmDisplayNameUser();

        url = result.getmImgChaussette();
        initialRate = result.getmNote();
        thisLegend.setText(leg);
        owner.setText(author);

        thisRating.setText(String.valueOf(initialRate));



        new MySocksActivity.SockImage(thisSock).execute(url);

    }



  /**  public void upDateRate(Chaussette ratedSock, float rate){

        String uploadId = ratedSock.getmIdChaussette();
        String idU = ratedSock.getmIdUser();
        ratedSock.setmNote(ratedSock.getmNote()+rate);
        ratedSock.setmSubNote(ratedSock.getmSubNote()-rate);

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
        mDatabase.child(idU).child(Constants.DATABASE_PATH_UPLOADS).child(uploadId).setValue(ratedSock);
        mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(uploadId).setValue(ratedSock);
        initialRate = ratedSock.getmNote();
        ratingBar.setRating(0);
        thisRating.setText(String.valueOf(initialRate));
    }  */



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

        Intent onStart = getIntent();
        final Chaussette result = onStart.getParcelableExtra("sock");

        mDatabase.child(mAuth.getCurrentUser().getUid()).child(Constants.DATABASE_PATH_UPLOADS).child(result.getmIdChaussette()).removeValue();
    }


    @Override
    public void onClick (View v) {
        if (v == modifyButton) {
            startActivity(new Intent(this, ModifyMySocksActivity.class));
        }
        if (v == removeButton) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Tu changes de Chaussettes ?")
                    .setContentText("Es tu sûr de vouloir supprimer ta photo ?")
                    .setCancelText("Non")
                    .setConfirmText("Oui")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText("TOP")
                                    .setContentText("!!!")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                            // or you can new a SweetAlertDialog to show
                               /* sDialog.dismiss();
                                new SweetAlertDialog(SampleActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Cancelled!")
                                        .setContentText("Your imaginary file is safe :)")
                                        .setConfirmText("OK")
                                        .show();*/
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
                                            startActivity(new Intent(MySocksActivity.this,MyProfil.class));
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    })
                    .show();

        }
    }

}