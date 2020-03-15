package com.example.lol_ban_pick_manager;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class PopupGameActivity extends AppCompatActivity {

    TextView textView_title;
    ImageView imageView_plus;
    ImageView imageView_ok;
    RecyclerView recyclerView_teams;
    ImageView imageView_more;
    static int selectIndex;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_game);

        MobileAds.initialize(this, "ca-app-pub-9554390275341876/9954370046");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9554390275341876/9954370046");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        final Intent intent = getIntent();
        final int matchIndex = intent.getExtras().getInt("matchIndex");

        imageView_plus = findViewById(R.id.game_plus);
        textView_title = findViewById(R.id.game_textView_title);
        imageView_ok = findViewById(R.id.game_imageView_ok);
        recyclerView_teams = findViewById(R.id.game_recyclerView_games);
        imageView_more = findViewById(R.id.game_imgaeView_more);



        recyclerView_teams.setHasFixedSize(true);
        recyclerView_teams.setLayoutManager(new LinearLayoutManager(this));

        //밴픽 게임들을 나열
        final GameAdapter adapter = new GameAdapter(ApplicationClass.matches.get(matchIndex).games);
        recyclerView_teams.setAdapter(adapter);

        //좀더 보기를 누르면 게임 디테일로 감
        imageView_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gameIndex = adapter.getmOnlyItemPosition();
                if(gameIndex == -1){
                    return;
                }
                CustomDialog customDialog = new CustomDialog(PopupGameActivity.this);
                customDialog.setLookGameDetail(matchIndex, gameIndex);
            }
        });

        //오케이 누르면 그 밴픽으로 이동
        imageView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gameIndex = adapter.getmOnlyItemPosition();
                if(gameIndex == -1){
                    return;
                }

                Intent intent2 = new Intent(getApplicationContext(), BanPickActivity.class);
                intent2.putExtra("matchIndex", matchIndex);
                intent2.putExtra("gameIndex", adapter.getmOnlyItemPosition());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }
            }
        });

        //플러스를 누르면 스택 다 없애고 다시 밴픽 액티비티로 감
        adapter.setOnItemClickListener(new GameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), BanPickActivity.class);
                intent1.putExtra("matchIndex", matchIndex);
                intent1.putExtra("gameIndex", 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }
            }
        });

        //길게 누르면 삭제
        adapter.setOnLongClickListener(new GameAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View v, int pos) {
                if(pos == 0){
                    return;
                }else{
                    selectIndex = pos;
                    new androidx.appcompat.app.AlertDialog.Builder(PopupGameActivity.this).setMessage("삭제하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ApplicationClass.matches.get(matchIndex).games.remove(selectIndex);
                                    ApplicationClass.saveReMatch(matchIndex);
                                    adapter.mIsClicked.remove(selectIndex);
                                    adapter.notifyItemRemoved(selectIndex);
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
        });







    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
