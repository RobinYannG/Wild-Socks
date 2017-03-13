package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class Classement extends AppCompatActivity {

    private Intent classementToHome;
    private Intent classementToMyKickUp;
    private Intent classementToAdd;
    private Intent classementToFlux;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classement);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mTextMessage = (TextView) findViewById(R.id.messageViewClassement);
        mTextMessage.setText("JE SUIS SUR LA PAGE CLASSEMENT");
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    classementToHome = new Intent(Classement.this, MyProfil.class);
                    startActivity(classementToHome);
                }
                if (tabId == R.id.tab_flux) {
                    classementToFlux = new Intent(Classement.this, Flux.class);
                    startActivity(classementToFlux);
                }
                if (tabId == R.id.tab_add) {
                    classementToAdd = new Intent(Classement.this, AddPhotos.class);
                    startActivity(classementToAdd);
                }

                if (tabId == R.id.tab_favoris) {
                    classementToMyKickUp = new Intent(Classement.this, MyKickUp.class);
                    startActivity(classementToMyKickUp);
                }
            }
        });
    }
}
