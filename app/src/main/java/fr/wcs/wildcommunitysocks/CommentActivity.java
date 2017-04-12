package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_COMMENTS;

public class CommentActivity extends AppCompatActivity {

    private Chaussette sock;

    private ListView commentListView;


    private CommentAdapter mAdapter;
    private DatabaseReference mCommentsDataBase;
    private FirebaseAuth mAuth;


    private EditText commentEditText;
    private FloatingActionButton postCommentButton;
    //progress dialog
    private ProgressDialog progressDialog;
    private List<Comment> allItems;

    private String sockId;
    private String authorName;
    private String authorId;
    private String newComment;
    private String uploadId;
    private String removeId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        /**Retrieve the object**/

        Intent onStart = getIntent();
        sock = onStart.getParcelableExtra("sock");

        sockId = sock.getmIdChaussette();

        /**Display all the comments for this sock in the listView:*/
        //displaying progress dialog while fetching images
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        commentListView =(ListView) findViewById(R.id.commentsListView);

        mCommentsDataBase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS).child(DATABASE_PATH_COMMENTS).child(sockId);
        mAdapter = new CommentAdapter(mCommentsDataBase,this,R.layout.comment_item);
        commentListView.setAdapter(mAdapter);
        allItems = new ArrayList<>();

/***Adding an Event Listener
        mCommentsDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                allItems.clear();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Comment item = postSnapshot.getValue(Comment.class);
                    allItems.add(item);

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
/******************************************/
        /**Delete a comment by clicking it*/

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comment comment = (Comment) parent.getItemAtPosition(position);
                removeId = comment.getmIdComment();

                AlertDialog.Builder delete = new AlertDialog.Builder(CommentActivity.this);
                delete.setTitle(getString(R.string.longClick));
                delete.setMessage(getString(R.string.deleteComment));
                delete.setNegativeButton(getString(R.string.cancel), null);
                delete.setPositiveButton(getString(R.string.confirm), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //rowListItem.add(sock);
                        mCommentsDataBase.child(removeId).removeValue();
                    }
                });
                delete.show();
            }
        });


        /**Propose to add a comment :*/

        mAuth = FirebaseAuth.getInstance();
        authorName = mAuth.getCurrentUser().getDisplayName();
        authorId =mAuth.getCurrentUser().getUid();
        StorageReference userPicture = FirebaseStorage.getInstance().getReference("users_avatar").child(authorName+"_avatar");
        final String authorPic=userPicture.getDownloadUrl().toString();


        commentEditText = (EditText) findViewById(R.id.editTextComment);
        postCommentButton =(FloatingActionButton) findViewById(R.id.postCommentButton);


        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newComment = commentEditText.getText().toString().trim();
                if(newComment==""){
                    Toast.makeText(CommentActivity.this, getString(R.string.noCommentToast), Toast.LENGTH_SHORT).show();
                }else{
                    uploadId = mCommentsDataBase.push().getKey();
                    Comment newCom = new Comment(uploadId, authorId,authorName,sockId, newComment);

                    mCommentsDataBase.child(uploadId).setValue(newCom);
                }
                commentEditText.setText("");
            }
        });

    }
}
