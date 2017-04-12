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
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Navigation extends AppCompatActivity implements View.OnClickListener{

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;

    //view objects
    private TextView textViewUserName;
    private ImageButton buttonLogout;

    private CircleImageView civProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //initializing Firebase authentification objects
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("users_avatar");

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //if the user is not logged in that means current user will return null
        if(user == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, IdentificationActivity.class));
        }

        civProfilePic = (CircleImageView)findViewById(R.id.profile_image);

        //initializing views
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        buttonLogout=(ImageButton) findViewById(R.id.buttonLogout);
        textViewUserName=(TextView) findViewById(R.id.textViewUserName);

        //displaying logged in user name
        textViewUserName.setText("Welcome "+ user.getDisplayName());

        //adding listener to button
        buttonLogout.setOnClickListener(this);


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

            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, Flux.newInstance());
        transaction.commit();

        downloadPicture ();

    }

    private void downloadPicture () {

        final StorageReference userPicture = mStorageRef.child(firebaseAuth.getCurrentUser().getDisplayName()+"_avatar");
        userPicture.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Navigation.this)
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

    @Override
    public void onClick(View v) {
        if(v==buttonLogout){

                //logging out the user
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(getApplicationContext(),IdentificationActivity.class));

        }
    }


}
