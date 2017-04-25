package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Credits extends AppCompatActivity implements View.OnClickListener{

    private ImageButton imageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==imageButtonBack){
            finish();
        }
    }

}
