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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class SocksActivity extends AppCompatActivity {


    //database reference
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




 @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_socks);


     thisSock = (ImageView) findViewById(R.id.sockImage);
     thisLegend = (TextView) findViewById(R.id.sockLegend);
     owner = (TextView) findViewById(R.id.sockOwner);
     thisRating = (TextView) findViewById(R.id.sockRating);
     ratingButton = (Button) findViewById(R.id.ratingButton);
     ratingBar = (RatingBar) findViewById(R.id.ratingBar);


     /**Retrieve the object**/

     Intent onStart = getIntent();
     final Chaussette result = onStart.getParcelableExtra("sock");

     String leg = result.getmLegende();
     String user = result.getmDisplayNameUser();

     url = result.getmImgChaussette();
     initialRate = result.getmNote();
     thisLegend.setText(leg);
     owner.setText(user);

     thisRating.setText(String.valueOf(initialRate));

     ratingButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             myRate = ratingBar.getRating();
             if (myRate == 0.0) {
                 Toast.makeText(SocksActivity.this, String.valueOf(myRate), Toast.LENGTH_LONG);
             } else {
                 upDateRate(result, myRate);
                 finish();
             }

         }
     });


     new SockImage(thisSock).execute(url);

 }



    public void upDateRate(Chaussette ratedSock, float rate){

        String uploadId = ratedSock.getmIdChaussette();
        String idU = ratedSock.getmIdUser();
        ratedSock.setmNote(ratedSock.getmNote()+rate);

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

