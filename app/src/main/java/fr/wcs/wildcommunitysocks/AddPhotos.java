package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class AddPhotos extends AppCompatActivity {

    private Intent addToHome;
    private Intent addToMyKickUp;
    private Intent addToClassement;
    private Intent addToFlux;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mTextMessage = (TextView) findViewById(R.id.messageViewAdd);
        mTextMessage.setText("JE SUIS SUR LA PAGE AJOUTER TOF");
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    addToHome = new Intent(AddPhotos.this, MyProfil.class);
                    startActivity(addToHome);
                }
                if (tabId == R.id.tab_flux) {
                    addToFlux = new Intent(AddPhotos.this, Flux.class);
                    startActivity(addToFlux);
                }
                if (tabId == R.id.tab_classement) {
                    addToClassement = new Intent(AddPhotos.this, Classement.class);
                    startActivity(addToClassement);
                }
                if (tabId == R.id.tab_favoris) {
                    addToMyKickUp = new Intent(AddPhotos.this, MyKickUp.class);
                    startActivity(addToMyKickUp);
                }
            }
        });
    }
}
