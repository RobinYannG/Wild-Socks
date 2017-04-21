package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3500;
    private TextView textViewCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textViewCommunity = (TextView) findViewById(R.id.textViewCommunity);

        Typeface communityType = Typeface.createFromAsset(getAssets(),"OleoScript-Bold.ttf");
        textViewCommunity.setTypeface(communityType);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        }, SPLASH_TIME_OUT);
    }
}
