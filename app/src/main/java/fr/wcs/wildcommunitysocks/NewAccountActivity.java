package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class NewAccountActivity extends AppCompatActivity implements View.OnClickListener{

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextUserName;
    private TextView textViewSignIn;
    private Button buttonSignup;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private String email;
    private String password;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if (firebaseAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), Navigation.class));
        }

        //initializing views
        buttonSignup = (Button) findViewById(R.id.button_new_confirm);
        textViewSignIn = (TextView) findViewById(R.id.alreadyRegistered);
        editTextEmail = (EditText) findViewById(R.id.editEmail);
        editTextPassword = (EditText) findViewById(R.id.editPwd);
        editTextUserName=(EditText) findViewById(R.id.editPseudo);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

    public void registerUser(){
        //getting email and password from edit texts
        email = editTextEmail.getText().toString().trim();
        password  = editTextPassword.getText().toString().trim();
        userName = editTextUserName.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(NewAccountActivity.this,getString(R.string.toastDefaultEmail),Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(NewAccountActivity.this,getString(R.string.toastDefaultPwd),Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage(getString(R.string.registrationPending));
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("TAG", "User profile update.");
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), Navigation.class));
                                    }
                                }
                            });
                        }else{
                            //display some message here
                           // Log.e(TAG, "Sign-in Failed: " + task.getException().getMessage());
                            // If the connection keeps failing un comment this :
                            //Toast.makeText(NewAccountActivity.this,"Sign-in Failed: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();

                            Toast.makeText(NewAccountActivity.this,getString(R.string.registrationFailed),Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }


    @Override
    public void onClick(View v) {
        if(v==buttonSignup){
            registerUser();
        }
        if(v==textViewSignIn){
            startActivity(new Intent(NewAccountActivity.this,IdentificationActivity.class));
        }
    }
}
