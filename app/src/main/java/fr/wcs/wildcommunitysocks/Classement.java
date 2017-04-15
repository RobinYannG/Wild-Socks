package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_CATEGORY_1INT;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_CATEGORY_2INT;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_CATEGORY_3INT;
import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_NO_CATEGORY;

public class Classement extends Fragment implements View.OnClickListener {
    public static Classement newInstance() {
        Classement fragment = new Classement();
        return fragment;
    }

    private ImageView imageViewAllRanking;
    private ImageView imageViewCategory1;
    private ImageView imageViewCategory2;
    private ImageView imageViewCategory3;
    private CircleImageView profile_image;
    private TextView textViewDisplayName;
    private TextView textViewPointure;
    private TextView textViewPublish;
    private TextView textViewNbrKickUp;
    private Button buttonModifyProfil;

    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_classement, container, false);

        imageViewAllRanking = (ImageView) view.findViewById(R.id.imageViewAllRanking);
        imageViewCategory1 = (ImageView) view.findViewById(R.id.imageViewCategory1);
        imageViewCategory2 = (ImageView) view.findViewById(R.id.imageViewCategory2);
        imageViewCategory3 = (ImageView) view.findViewById(R.id.imageViewCategory3);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        textViewDisplayName =(TextView) view.findViewById(R.id.textViewDisplayName);
        textViewPointure = (TextView) view.findViewById(R.id.textViewPointure);
        textViewPublish = (TextView) view.findViewById(R.id.textViewPublish);
        textViewNbrKickUp = (TextView) view.findViewById(R.id.textViewNbrKickUp);
        buttonModifyProfil = (Button) view.findViewById(R.id.buttonModifyProfil);


        imageViewAllRanking.setOnClickListener(this);
        imageViewCategory1.setOnClickListener(this);
        imageViewCategory2.setOnClickListener(this);
        imageViewCategory3.setOnClickListener(this);
        buttonModifyProfil.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("users_avatar");

        FirebaseUser user = firebaseAuth.getCurrentUser();
        textViewDisplayName.setText(user.getDisplayName());

        downloadPicture();

        return view;
    }

    public void downloadPicture () {

        final StorageReference userPicture = mStorageRef.child(firebaseAuth.getCurrentUser().getDisplayName()+"_avatar");
        userPicture.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Classement.this)
                        .load(uri)
                        .into(profile_image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

    }

    /*
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
            task.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot state) {
                    //call a user defined function to handle the event.
                }
            });
        }
    } */

    public void onClick(View v) {
        if (v == imageViewAllRanking){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a0");
            startActivity(intent);
        }
        if (v == imageViewCategory1){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a1");
            startActivity(intent);
        }
        if (v == imageViewCategory2){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a2");
            startActivity(intent);
        }
        if (v == imageViewCategory3){
            Intent intent = new Intent(getActivity(), RankingResult.class);
            intent.putExtra("I_CAME_FROM", "a3");
            startActivity(intent);
        }
        if (v == buttonModifyProfil ) {
            startActivity(new Intent(getActivity(), ModifyProfil.class));
        }
    }




}