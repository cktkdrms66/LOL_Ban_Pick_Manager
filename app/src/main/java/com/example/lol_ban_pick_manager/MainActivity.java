package com.example.lol_ban_pick_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;

import android.os.Bundle;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentMatchActivity fragmentMatchActivity = new FragmentMatchActivity();
    private FragmentTeamActivity fragmentTeamActivity = new FragmentTeamActivity();
    private FragmentPlayerActivity fragmentPlayerActivity = new FragmentPlayerActivity();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
