package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;


public class Flux extends AppCompatActivity {

    private Intent fluxToHome;
    private Intent fluxToMyKickUp;
    private Intent fluxToAdd;
    private Intent fluxToClassement;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flux);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mTextMessage = (TextView) findViewById(R.id.messageViewFlux);
        mTextMessage.setText("JE SUIS SUR LA PAGE FLUX");
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    fluxToHome = new Intent(Flux.this, MyProfil.class);
                    startActivity(fluxToHome);
                }
                if (tabId == R.id.tab_add) {
                    fluxToAdd = new Intent(Flux.this, AddPhotos.class);
                    startActivity(fluxToAdd);
                }
                if (tabId == R.id.tab_classement) {
                    fluxToClassement = new Intent(Flux.this, Classement.class);
                    startActivity(fluxToClassement);
                }
                if (tabId == R.id.tab_favoris) {
                    fluxToMyKickUp = new Intent(Flux.this, MyKickUp.class);
                    startActivity(fluxToMyKickUp);
                }
            }
        });
    }
}
