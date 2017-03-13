package fr.wcs.wildcommunitysocks;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MyProfil extends AppCompatActivity {

    private TextView mTextMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mTextMessage = (TextView) findViewById(R.id.messageView);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    mTextMessage.setText("Home");
                }
                if (tabId == R.id.tab_flux) {
                    mTextMessage.setText("Flux");
                }
                if (tabId == R.id.tab_add) {
                    mTextMessage.setText("Photo");
                }
                if (tabId == R.id.tab_classement) {
                    mTextMessage.setText("Classement");
                }
                if (tabId == R.id.tab_favoris) {
                    mTextMessage.setText("Favoris");
                }
            }
        });

    }

}
