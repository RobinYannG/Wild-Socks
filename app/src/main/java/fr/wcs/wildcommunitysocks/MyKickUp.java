package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MyKickUp extends AppCompatActivity {

    private Intent MyKickUpToHome;
    private Intent MyKickUpToFlux;
    private Intent MyKickUptoAdd;
    private Intent MyKickUptoClassement;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_kick_up);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mTextMessage = (TextView) findViewById(R.id.messageViewMyKickUp);
        mTextMessage.setText("JE SUIS SUR LA PAGE KICKUP");
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    MyKickUpToHome = new Intent(MyKickUp.this, MyProfil.class);
                    startActivity(MyKickUpToHome);
                }
                if (tabId == R.id.tab_flux) {
                    MyKickUpToFlux = new Intent(MyKickUp.this, Flux.class);
                    startActivity(MyKickUpToFlux);
                }
                if (tabId == R.id.tab_add) {
                    MyKickUptoAdd = new Intent(MyKickUp.this, AddPhotos.class);
                    startActivity(MyKickUptoAdd);
                }
                if (tabId == R.id.tab_classement) {
                    MyKickUptoClassement = new Intent(MyKickUp.this, Classement.class);
                    startActivity(MyKickUptoClassement);
                }
            }
        });
    }
}
