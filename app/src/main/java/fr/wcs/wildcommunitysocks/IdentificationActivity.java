package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IdentificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        Button identification = (Button) findViewById(R.id.id_confirm);
        Button cancel_id=(Button) findViewById(R.id.id_cancel);

        identification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When an existing user identifies, he arrives on the main stream : Flux
                Intent intent_identification = new Intent(IdentificationActivity.this,Flux.class);
                /**
                 * here will be detailed the connexion checking :
                 * - at first : if there is a name and a password
                 * - when connected to a database : if the user exists
                 */
                startActivity(intent_identification);


            }
        });

        cancel_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_back = new Intent(IdentificationActivity.this,MainActivity.class);
                /**
                 * Possibly a pop-up to ask for confirmation
                 */
                startActivity(intent_back);
            }
        });
    }
}
