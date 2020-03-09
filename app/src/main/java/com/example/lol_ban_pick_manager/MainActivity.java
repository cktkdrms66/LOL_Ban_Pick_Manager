package com.example.lol_ban_pick_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

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
}
