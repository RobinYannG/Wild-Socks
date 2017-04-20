package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Navigation extends AppCompatActivity implements View.OnClickListener {


    private BottomBar bottomBar;

    private TextView textViewDisplayName;

    private TextView textViewPublish;
    private TextView textViewNbrKickUp;
    private Button buttonModifyProfil;
    private CircleImageView profile_image;
    private ImageView imageViewLogOut;
    private TextView textViewPointure;
    private DatabaseReference mDatabase;
    private static String idUser;

    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        textViewDisplayName =(TextView) findViewById(R.id.textViewDisplayName);
        textViewPointure = (TextView) findViewById(R.id.textViewPointure);
        textViewPublish = (TextView) findViewById(R.id.textViewPublish);
        textViewNbrKickUp = (TextView) findViewById(R.id.textViewNbrKickUp);
        buttonModifyProfil = (Button) findViewById(R.id.buttonModifyProfil);
        imageViewLogOut = (ImageView) findViewById(R.id.imageViewLogOut);
        textViewPointure = (TextView) findViewById(R.id.textViewPointure);

        buttonModifyProfil.setOnClickListener(this);
        imageViewLogOut.setOnClickListener(this);
        //profile_image.setOnClickListener(this);

        //initializing Firebase authentification objects
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("users_avatar");

        FirebaseUser user = firebaseAuth.getCurrentUser();
        textViewDisplayName.setText(user.getDisplayName());
        

        downloadPicture();

        //if the user is not logged in that means current user will return null
        if(user == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, IdentificationActivity.class));
        }


        //initializing views
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.selectTabAtPosition(1);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                Fragment selectedFragment = null;

                if (tabId == R.id.tab_flux) {
                    selectedFragment = Flux.newInstance();
                }
                else if (tabId == R.id.tab_add) {
                    selectedFragment = AddPhotos.newInstance();
                }
                else if (tabId == R.id.tab_classement) {
                    selectedFragment = Classement.newInstance();
                }
                else if (tabId == R.id.tab_favoris) {
                    selectedFragment = MyKickUp.newInstance();
                }
                else if (tabId == R.id.tab_home) {
                    selectedFragment = MyProfil.newInstance();
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();

                idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS);
                mDatabase.child(idUser).child("mPointure").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userPointure = dataSnapshot.getValue(String.class);
                        textViewPointure.setText(userPointure);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabase.child(idUser).child(Constants.DATABASE_PATH_UPLOADS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        textViewPublish.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, Flux.newInstance());
        transaction.commit();

    }

    public void downloadPicture () {

        final StorageReference userPicture = mStorageRef.child(firebaseAuth.getCurrentUser().getDisplayName()+"_avatar");
        userPicture.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Navigation.this)
                        .load(uri)
                        .into(profile_image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's an upload in progress, save the reference so you can query it later
        if (mStorageRef != null) {
            outState.putString(firebaseAuth.getCurrentUser().getDisplayName()+"_avatar", mStorageRef.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was an upload in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString(firebaseAuth.getCurrentUser().getDisplayName()+"_avatar");
        if (stringRef == null) {
            return;
        }
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all UploadTasks under this StorageReference (in this example, there should be one)
        List<UploadTask> tasks = mStorageRef.getActiveUploadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the upload
            UploadTask task = tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot state) {
                    //call a user defined function to handle the event.
                }
            });
        }
    }

    public void onClick (View v) {
        if (v == buttonModifyProfil ) {
            startActivity(new Intent(this, ModifyProfil.class));
        }
        if ( v == imageViewLogOut) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent (this, MainActivity.class));
            finish();
        }
    }



}
