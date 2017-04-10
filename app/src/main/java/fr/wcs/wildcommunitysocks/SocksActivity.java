package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class SocksActivity extends AppCompatActivity {


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
    private TextView seeComments;
    private float myRate=0;
    private float initialRate;
    private static Chaussette result;




 @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_socks);

     //initializing Firebase authentification objects
     mAuth = FirebaseAuth.getInstance();

     //if the user is not logged in that means current user will return null
     if(mAuth.getCurrentUser() == null){
         //closing this activity
         finish();
         //starting login activity
         startActivity(new Intent(this, IdentificationActivity.class));
     }




     thisSock = (ImageView) findViewById(R.id.sockImage);
     thisLegend = (TextView) findViewById(R.id.sockLegend);
     owner = (TextView) findViewById(R.id.sockOwner);
     thisRating = (TextView) findViewById(R.id.sockRating);
     ratingButton = (Button) findViewById(R.id.ratingButton);
     ratingBar = (RatingBar) findViewById(R.id.ratingBar);
     seeComments =(TextView) findViewById(R.id.textViewComment);


     /**Retrieve the object**/

     Intent onStart = getIntent();
     result = onStart.getParcelableExtra("sock");

     String leg = result.getmLegende();
     String author = result.getmDisplayNameUser();

     url = result.getmImgChaussette();
     initialRate = result.getmNote();
     thisLegend.setText(leg);
     owner.setText(author);

     thisRating.setText(String.valueOf(initialRate));

     ratingButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             myRate = ratingBar.getRating();
             if (myRate == 0.0) {
                 Toast.makeText(SocksActivity.this, R.string.noRate, Toast.LENGTH_LONG).show();
             } else {
                 //Increase the rate attribute of the object
                 upDateRate(result, myRate);

                 /**Get the photo to the MyKicks node*******************************/

                 //getting current user
                 FirebaseUser user = mAuth.getCurrentUser();

                 //Reaching the MyKick file
                 mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);

                 //Uploading the object
                 mDatabase.child(user.getUid()).child(Constants.DATABASE_PATH_MYKICKS).child(result.getmIdChaussette()).setValue(result);
                 finish();
             }

         }
     });

     seeComments.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent comments = new Intent(SocksActivity.this,CommentActivity.class);
             comments.putExtra("sock",result);
             startActivity(comments);
         }
     });


     new SockImage(thisSock).execute(url);

 }



    public void upDateRate(Chaussette ratedSock, float rate){

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


    private class SockImage extends AsyncTask<String, Void, Bitmap>{

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
}

