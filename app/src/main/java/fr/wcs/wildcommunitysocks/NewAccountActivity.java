package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        Button button_new_confirm =(Button) findViewById(R.id.button_new_confirm);
        Button button_new_cancel=(Button) findViewById(R.id.button_new_cancel);

        button_new_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_create= new Intent(NewAccountActivity.this, MyProfil.class);
                /**
                 * After checking the mandatory information is complete :
                 * Pseudo
                 * Mail
                 * PWD
                 *
                 * The new user is then redirected to his newly created Profile page
                 * where he will be able to complete (if he wishes to)
                 * Picture
                 * foot size
                 * sentence
                 */
                startActivity(intent_create);
            }
        });

        button_new_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_cancel= new Intent (NewAccountActivity.this, MainActivity.class);
                /**
                 * Possibly a pop-up to ask for confirmation
                 */
                startActivity(intent_cancel);
            }
        });
    }
}
