package fr.wcs.wildcommunitysocks;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class Navigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                Fragment selectedFragment = null;
                
                if (tabId == R.id.tab_flux) {
                    selectedFragment = Flux.newInstance();
                }
                else if (tabId == R.id.tab_add) {
                    selectedFragment = AddPhotos.newInstance();
                }
                else if (tabId == R.id.tab_classement) {
                    selectedFragment = Classement.newInstance();
                }
                else if (tabId == R.id.tab_favoris) {
                    selectedFragment = MyKickUp.newInstance();
                }
                else if (tabId == R.id.tab_home) {
                    selectedFragment = MyProfil.newInstance();
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();

            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, Flux.newInstance());
        transaction.commit();

    }
}
