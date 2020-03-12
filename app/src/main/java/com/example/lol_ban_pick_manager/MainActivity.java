package com.example.lol_ban_pick_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.paperdb.Paper;

import static com.example.lol_ban_pick_manager.ApplicationClass.isNeedToSetting;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentMatchActivity fragmentMatchActivity = new FragmentMatchActivity();
    private FragmentTeamActivity fragmentTeamActivity = new FragmentTeamActivity();
    private FragmentPlayerActivity fragmentPlayerActivity = new FragmentPlayerActivity();
    private AdView mAdView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // 광고가 제대로 로드 되는지 테스트 하기 위한 코드입니다.
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragmentMatchActivity).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());


    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.item_match:
                    transaction.replace(R.id.framelayout, fragmentMatchActivity).commitAllowingStateLoss();
                    break;
                case R.id.item_team:
                    transaction.replace(R.id.framelayout, fragmentTeamActivity).commitAllowingStateLoss();
                    break;
                case R.id.item_player:
                    transaction.replace(R.id.framelayout, fragmentPlayerActivity).commitAllowingStateLoss();
                    break;

            }
            return true;
        }
    }

    boolean isFinish = false;
    @Override
    public void onBackPressed() {
        if(isFinish){
            super.onBackPressed();
        }else{
            new AlertDialog.Builder(this).setMessage("앱을 종료하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isFinish = true;
                            onBackPressed();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    }).show();
        }

    }
}
