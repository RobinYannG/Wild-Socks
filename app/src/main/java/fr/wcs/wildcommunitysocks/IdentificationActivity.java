package fr.wcs.wildcommunitysocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class IdentificationActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton buttonLogIn;
    private ImageView imageViewSignUp;
    private EditText editTextUserPwd;
    private EditText editTextuserEmail;
    private ImageView imageViewModifyPwd;


    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if (firebaseAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), Navigation.class));
        }


        buttonLogIn = (ImageButton) findViewById(R.id.imageButtonConnect);
        imageViewSignUp = (ImageView) findViewById(R.id.imageViewCreateAccount);
        editTextUserPwd =(EditText) findViewById(R.id.editUserPwd);
        editTextuserEmail=(EditText) findViewById(R.id.editUserEmail);
        imageViewModifyPwd = (ImageView) findViewById(R.id.imageViewPasswordForget);

        progressDialog = new ProgressDialog(this);

        buttonLogIn.setOnClickListener(this);
        imageViewSignUp.setOnClickListener(this);
        imageViewModifyPwd.setOnClickListener(this);
    }

    public void userLogin(){
        //getting email and password from edit texts
        String email = editTextuserEmail.getText().toString().trim();
        String password  = editTextUserPwd.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(IdentificationActivity.this,getString(R.string.toastDefaultEmail),Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(IdentificationActivity.this,getString(R.string.toastDefaultPwd),Toast.LENGTH_LONG).show();
            return;
        }
        //if the email and password are not empty
        //displaying a progress dialog
        progressDialog.setMessage(getString(R.string.signInPending));
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), Navigation.class));
                        }else{
                            Toast.makeText(getApplicationContext(),getString(R.string.signInFailed),Toast.LENGTH_SHORT);
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v==buttonLogIn){
            userLogin();
        }
        if(v==imageViewSignUp){
            startActivity(new Intent(IdentificationActivity.this,NewAccountActivity.class));
        }
        if (v == imageViewModifyPwd) {
            startActivity(new Intent(IdentificationActivity.this,ModifyPassword.class));
        }
    }
}
