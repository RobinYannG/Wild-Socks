package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_new;
    private Button button_id;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if (firebaseAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), Navigation.class));
        }

        //Initializing the views
        button_new = (Button) findViewById(R.id.button_start_new);
        button_id = (Button) findViewById(R.id.button_start_id);

        //Adding clickListeners
        button_id.setOnClickListener(this);
        button_new.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==button_id){
            startActivity(new Intent(MainActivity.this,IdentificationActivity.class));
        }
        if(v==button_new){
            startActivity(new Intent(MainActivity.this,NewAccountActivity.class));
        }

    }
}
