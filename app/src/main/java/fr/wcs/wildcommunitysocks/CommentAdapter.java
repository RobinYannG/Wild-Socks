package fr.wcs.wildcommunitysocks;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wilder on 10/04/17.
 */

public class CommentAdapter extends FirebaseListAdapter<Comment> {
    // The mUsername for this client. We use this to indicate which messages originated from this user
    private int layout;
    private Context context;
    private CircleImageView circleAuthorAvatar;
    private TextView textViewauthor;
    private TextView textViewComment;

    private StorageReference mStorageRef;

    public CommentAdapter(Query ref, Activity activity, int layout) {
        super(ref, Comment.class, layout, activity);
    }



    @Override
    protected void populateView(View convertView, Comment model) {
        // Map an ItineraryModel object to an entry in our listview

        context = convertView.getContext().getApplicationContext();
        String author = model.getmAuthorName();
        textViewauthor = (TextView) convertView.findViewById(R.id.comment_author);
        textViewauthor.setText(author);

        String comment = model.getmComment();
        textViewComment =(TextView) convertView.findViewById(R.id.comment_line);
        textViewComment.setText(comment);

        circleAuthorAvatar =(CircleImageView) convertView.findViewById(R.id.profile_image);
        downloadPicture(model,circleAuthorAvatar);

    }

    private void downloadPicture (Comment coco, final CircleImageView circleAvatar) {
        mStorageRef = FirebaseStorage.getInstance().getReference("users_avatar");
        //child(Constants.DATABASE_PATH_ALL_UPLOADS).child(uploadId)

        StorageReference userPicture = mStorageRef.child(coco.getmAuthorName()+"_avatar");
        userPicture.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(circleAvatar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

    }


}

