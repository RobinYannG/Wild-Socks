package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static fr.wcs.wildcommunitysocks.Constants.DATABASE_PATH_COMMENTS;

public class CommentActivity extends AppCompatActivity {

    public Chaussette sock;

    public ListView commentListView;


    public CommentAdapter mAdapter;
    public DatabaseReference mCommentsDataBase;
    public FirebaseAuth mAuth;


    public EditText commentEditText;
    public FloatingActionButton postCommentButton;

    public String sockId;
    public String userName;
    public static String newComment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        /**Retrieve the object**/

        Intent onStart = getIntent();
        sock = onStart.getParcelableExtra("sock");

        sockId = sock.getmIdChaussette();

        /**Display all the comments for this sock in the listView:*/

        commentListView =(ListView) findViewById(R.id.commentsListView);

        mCommentsDataBase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_SOCKS).child(DATABASE_PATH_COMMENTS);

        mAdapter = new CommentAdapter(mCommentsDataBase,this,R.layout.comment_item);

        commentListView.setAdapter(mAdapter);

        /**Propose to add a comment :*/

        mAuth = FirebaseAuth.getInstance();
        userName = mAuth.getCurrentUser().getDisplayName();

        commentEditText = (EditText) findViewById(R.id.editTextComment);
        postCommentButton =(FloatingActionButton) findViewById(R.id.postCommentButton);


        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newComment = commentEditText.getText().toString().trim();
                if(newComment==""){
                    Toast.makeText(CommentActivity.this, getString(R.string.noCommentToast), Toast.LENGTH_SHORT).show();
                }else{
                    Comment newCom = new Comment(userName,sockId, newComment);
                    mCommentsDataBase.push().setValue(newCom);
                }
                commentEditText.setText("");
            }
        });

    }
}
