package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_COMMENTS;

public class SocksActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener, View.OnClickListener {

    @BindView(R.id.layoutForm) LinearLayout layoutForm;
    @BindView(R.id.sockImage) ImageView thisSock;
    @BindView(R.id.sockLegend) TextView thisLegend;
    @BindView(R.id.sockOwner) TextView owner;
    @BindView(R.id.sockRating) TextView thisRating;
    //@BindView(R.id.textViewComment) TextView seeComments;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.btnSubmit) Button  btnSubmit;
    @BindView(R.id.lblWeHearFeedback) TextView lblWeHearFeedback;
    @BindView(R.id.txtComments) EditText txtComments;
    @BindView(R.id.lblThanksFeedback) TextView txtThanks;
    @BindView(R.id.note) TextView note;
    @BindView(R.id.reportImage) ImageButton reportImage;


    //Firebase references
    private DatabaseReference mDatabase;
    public DatabaseReference mCommentsDataBase;
    public FirebaseAuth mAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    //Layout elements
    private String url;
    private float myRate=0;
    private float initialRate;
    private static Chaussette result;


    public static String uploadId;
    public String sockId;
    public String userName;
    public String userId;
    public Chaussette sock;
    public ImageButton buttonViewComments;

    boolean isAnimated = false;
    public boolean isModerator = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socks);

        ButterKnife.bind(this);
        btnSubmit.setOnClickListener(this);

        buttonViewComments = (ImageButton) findViewById(R.id.commentView);
        buttonViewComments.setOnClickListener(this);

        reportImage =(ImageButton) findViewById(R.id.reportImage);
        reportImage.setOnClickListener(this);



        //initializing Firebase authentification objects
        mAuth = FirebaseAuth.getInstance();

        //if the user is not logged in that means current user will return null
        if(mAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, IdentificationActivity.class));
        }else{
            userId=mAuth.getCurrentUser().getUid();
        }
        initializeUI();
        for(int i=0;i<Constants.WILD_SOCKS_MODERATOR.length;i++){
            if(Constants.WILD_SOCKS_MODERATOR[i].equals(userId)){
                isModerator=true;
            }
        }

        /* seeComments.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent comments = new Intent(SocksActivity.this,CommentActivity.class);
                 comments.putExtra("sock",result);
                 startActivity(comments);
             }
         });*/
    }

    public void initializeUI() {
        // Setting Initial Settings for UIs
        ratingBar.setRating(0);
        layoutForm.setVisibility(View.INVISIBLE);

        // Setting listeners
        ratingBar.setOnRatingBarChangeListener(this);

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

        new SockImage(thisSock).execute(url);

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float value, boolean b) {
        if (isAnimated == false) {
            // show hidden views
            layoutForm.setVisibility(View.VISIBLE);

            // Work out animations
            ViewAnimator

                    .animate(note)
                    .dp().translationY(0, -100)
                    .alpha(1,0)
                    .duration(320)
                    .interpolator(new LinearOutSlowInInterpolator())
                    // Rating bar
                    .andAnimate(ratingBar)
                    .dp().translationY(0, -96)
                    .duration(1700)
                    .interpolator(new LinearOutSlowInInterpolator())
                    // Layout Form
                    .andAnimate(layoutForm)
                    .dp().translationY(0, -96)
                    .singleInterpolator(new LinearOutSlowInInterpolator())
                    .duration(450)
                    .alpha(0,1)
                    .interpolator(new FastOutSlowInInterpolator())
                    // Label feedback of form
                    .andAnimate(lblWeHearFeedback)
                    .dp().translationY(0,-20)
                    .interpolator(new LinearOutSlowInInterpolator())
                    .duration(300)
                    .alpha(0,1)
                    // Commects edittext
                    .andAnimate(txtComments)
                    .dp().translationY(30,-30)
                    .interpolator(new LinearOutSlowInInterpolator())
                    .duration(550)
                    .alpha(0,1)
                    // Submit button
                    .andAnimate(btnSubmit)
                    .dp().translationY(60,-35)
                    .interpolator(new LinearOutSlowInInterpolator())
                    .duration(800)
                    .alpha(0,1)
                    .onStop(new AnimationListener.Stop() {
                        @Override
                        public void onStop() {
                            isAnimated = true;
                        }
                    })
                    .start();
        }
    }

    public void reportSock(){


        String uploadId = result.getmIdChaussette();
        String idU = result.getmIdUser();
        result.setmReported(true);
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_REPORTS).child(result.getmIdChaussette());
        mDatabase.setValue(result);
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
        mDatabase.child(idU).child(Constants.DATABASE_PATH_UPLOADS).child(uploadId).setValue(result);
        mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(uploadId).setValue(result);
        if(result.getmCategory().equals("")){
            mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(result.getmCategory()).child(uploadId).setValue(result);
        }

    }


    public void removeButton() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
        String category =result.getmCategory();

        mDatabase.child(user.getUid()).child(Constants.DATABASE_PATH_UPLOADS).child(result.getmIdChaussette()).removeValue();
        mDatabase.child(Constants.DATABASE_PATH_ALL_UPLOADS).child(result.getmIdChaussette()).removeValue();

        if(result.ismReported()){
            mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_REPORTS);
            mDatabase.child(result.getmIdChaussette()).removeValue();
        }
       if(category!=""){
           mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
           mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(category).child(result.getmIdChaussette()).removeValue();
        }
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

    public void onClick(View v) {
        if (v == btnSubmit) {
            onSubmitClick();
        }
        if (v == buttonViewComments) {
           Intent intent = new Intent(SocksActivity.this,CommentActivity.class);
            intent.putExtra("sock",result);
            startActivity(intent);
        }

        if(v==reportImage){
            if(isModerator){
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.moderatorAlertTitle))
                        .setContentText(getString(R.string.moderatorAlertRemove))
                        .setCancelText(getString(R.string.alertDialogueCancel))
                        .setConfirmText(getString(R.string.alertDialogConfirm))
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // reuse previous dialog instance, keep widget user state, reset them if you need
                                sDialog.setTitleText(getString(R.string.moderatorAlertNoRemove))
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
                return;
            }
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.reportAlertDialogTitle))
                    .setContentText(getString(R.string.reportAlertDialogQuestion))
                    .setCancelText(getString(R.string.alertDialogueCancel))
                    .setConfirmText(getString(R.string.alertDialogConfirm))
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText(getString(R.string.reportAlertDialogBack))
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
                            reportSock();
                            sDialog.setTitleText(getString(R.string.reportAlertDialogEnd))
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



    public void onSubmitClick() {

        myRate = ratingBar.getRating();
        String comments = txtComments.getText().toString().trim();
        if (TextUtils.isEmpty(comments)) {

            upDateRate(result, myRate);

            /**Get the photo to the MyKicks node*******************************/

            //getting current user
            FirebaseUser user = mAuth.getCurrentUser();

            //Reaching the MyKick file
            mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);

            //Uploading the object
            mDatabase.child(user.getUid()).child(Constants.DATABASE_PATH_MYKICKS).child(result.getmIdChaussette()).setValue(result);


        } else {
            //Increase the rate attribute of the object
            upDateRate(result, myRate);
            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
            mDatabase.child(user.getUid()).child(Constants.DATABASE_PATH_MYKICKS).child(result.getmIdChaussette()).setValue(result);

            /**Retrieve the object**/

            Intent onStart = getIntent();
            sock = onStart.getParcelableExtra("sock");

            sockId = sock.getmIdChaussette();

            mAuth = FirebaseAuth.getInstance();
            mCommentsDataBase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS).child(DATABASE_PATH_COMMENTS).child(sockId);

            userName = mAuth.getCurrentUser().getDisplayName();
            uploadId = mCommentsDataBase.push().getKey();
            userId = mAuth.getCurrentUser().getUid();

            StorageReference userPicture = FirebaseStorage.getInstance().getReference("users_avatar").child(userName+"_avatar");
            final String userPic=userPicture.getDownloadUrl().toString();
            Comment newCom = new Comment(uploadId, userId,userName,sockId, comments);
            mCommentsDataBase.child(uploadId).setValue(newCom);

        }

        if (isAnimated) {
            // Feedback has been written
            txtThanks.setVisibility(View.VISIBLE);

            // Perfrom Animations
            ViewAnimator
                    .animate(ratingBar)
                    .dp().translationY(-50, -50)
                    .interpolator(new LinearOutSlowInInterpolator())
                    .duration(800)
                    .alpha(1,0)
                    .andAnimate(lblWeHearFeedback)
                    .dp().translationY(-20, -90)
                    .interpolator(new LinearOutSlowInInterpolator())
                    .duration(250)
                    .alpha(1,0)
                    .andAnimate(txtComments)
                    .dp().translationY(-30, -120)
                    .interpolator(new LinearOutSlowInInterpolator())
                    .duration(300)
                    .alpha(1,0)
                    .andAnimate(btnSubmit)
                    .dp().translationY(-35, -200)
                    .interpolator(new LinearOutSlowInInterpolator())
                    .duration(340)
                    .alpha(1,0)
                    .andAnimate(txtThanks)
                    .dp().translationY(0, -200)
                    .interpolator(new LinearOutSlowInInterpolator())
                    .duration(600)
                    .start();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);

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
        } catch (IOException e) {
            return null;
        }
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

