package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MyProfil extends AppCompatActivity {

    private Intent myProfilToFlux;
    private Intent myProfilToAdd;
    private Intent myProfilToClassement;
    private Intent myProfilToMyKickUp;
    private TextView mTextMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mTextMessage = (TextView) findViewById(R.id.messageViewMyProfil);
        mTextMessage.setText("JE SUIS SUR LA PAGE PROFIL");
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_flux) {
                    myProfilToFlux = new Intent(MyProfil.this, Flux.class);
                    startActivity(myProfilToFlux);
                    MyProfil.this.finish();
                }
                else if (tabId == R.id.tab_add) {
                    myProfilToAdd = new Intent(MyProfil.this, AddPhotos.class);
                    startActivity(myProfilToAdd);
                }
                else if (tabId == R.id.tab_classement) {
                    myProfilToClassement = new Intent(MyProfil.this, Classement.class);
                    startActivity(myProfilToClassement);
                }
                else if (tabId == R.id.tab_favoris) {
                    myProfilToMyKickUp = new Intent(MyProfil.this, MyKickUp.class);
                    startActivity(myProfilToMyKickUp);
                }

            }
        });




    }

}
