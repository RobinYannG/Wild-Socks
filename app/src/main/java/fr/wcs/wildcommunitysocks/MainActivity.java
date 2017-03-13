package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_new = (Button) findViewById(R.id.button_start_new);
        Button button_id = (Button) findViewById(R.id.button_start_id);

        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_start_new = new Intent(MainActivity.this,NewAccountActivity.class);
                startActivity(intent_start_new);
            }
        });

        button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_start_id=new Intent(MainActivity.this, IdentificationActivity.class);
                startActivity(intent_start_id);
            }
        });
    }
}
