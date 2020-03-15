package com.example.lol_ban_pick_manager;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


public class PopupMakeMatchActivity extends AppCompatActivity {

    String matchName;
    boolean isDefaultPosition = true;
    int team0Index;
    int team1Index;
    InputMethodManager imm;

    ImageView imageView_ok;
    TextView textView_team0_name;
    TextView textView_team1_name;
    ImageView imageView_team0_logo;
    ImageView imageView_team1_logo;
    EditText editText_match_name;
    ImageView imageView_change;
    ConstraintLayout constraintLayout;
    ImageView[] imageView_team0_back = new ImageView[5];
    ImageView[] imageView_team1_back = new ImageView[5];
    TextView[] textView_team0_tear = new TextView[5];
    TextView[] textView_team1_tear = new TextView[5];
    ImageView[][] imageView_team0_most = new ImageView[5][3];
    ImageView[][] imageView_team1_most = new ImageView[5][3];

    private InterstitialAd mInterstitialAd;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_make_game);


        MobileAds.initialize(this, "ca-app-pub-9554390275341876/9954370046");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9554390275341876/9954370046");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        team0Index = 1;
        team1Index = 1;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imageView_ok = findViewById(R.id.make_game_ok);
        textView_team0_name = findViewById(R.id.make_game_textView_team0_name);
        textView_team1_name = findViewById(R.id.make_game_textView_team1_name);
        imageView_team0_logo = findViewById(R.id.make_game_team0_logo);
        imageView_team1_logo = findViewById(R.id.make_game_team1_logo);
        editText_match_name = findViewById(R.id.make_game_editText_input);
        imageView_change = findViewById(R.id.make_game_imageView_change);
        constraintLayout = findViewById(R.id.make_game_layout);


        imageView_team0_back[0] = findViewById(R.id.make_match_tear_back0);
        imageView_team0_back[1] = findViewById(R.id.make_match_tear_back1);
        imageView_team0_back[2] = findViewById(R.id.make_match_tear_back2);
        imageView_team0_back[3] = findViewById(R.id.make_match_tear_back3);
        imageView_team0_back[4] = findViewById(R.id.make_match_tear_back4);

        imageView_team1_back[0] = findViewById(R.id.make_match_tear_back5);
        imageView_team1_back[1] = findViewById(R.id.make_match_tear_back6);
        imageView_team1_back[2] = findViewById(R.id.make_match_tear_back7);
        imageView_team1_back[3] = findViewById(R.id.make_match_tear_back8);
        imageView_team1_back[4] = findViewById(R.id.make_match_tear_back9);

        textView_team0_tear[0] = findViewById(R.id.make_match_tear0);
        textView_team0_tear[1] = findViewById(R.id.make_match_tear1);
        textView_team0_tear[2] = findViewById(R.id.make_match_tear2);
        textView_team0_tear[3] = findViewById(R.id.make_match_tear3);
        textView_team0_tear[4] = findViewById(R.id.make_match_tear4);
        
        textView_team1_tear[0] = findViewById(R.id.make_match_tear5);
        textView_team1_tear[1] = findViewById(R.id.make_match_tear6);
        textView_team1_tear[2] = findViewById(R.id.make_match_tear7);
        textView_team1_tear[3] = findViewById(R.id.make_match_tear8);
        textView_team1_tear[4] = findViewById(R.id.make_match_tear9);


        imageView_team0_most[0][0] = findViewById(R.id.make_match_most00);
        imageView_team0_most[0][1] = findViewById(R.id.make_match_most01);
        imageView_team0_most[0][2] = findViewById(R.id.make_match_most02);

        imageView_team0_most[1][0] = findViewById(R.id.make_match_most10);
        imageView_team0_most[1][1] = findViewById(R.id.make_match_most11);
        imageView_team0_most[1][2] = findViewById(R.id.make_match_most12);

        imageView_team0_most[2][0] = findViewById(R.id.make_match_most20);
        imageView_team0_most[2][1] = findViewById(R.id.make_match_most21);
        imageView_team0_most[2][2] = findViewById(R.id.make_match_most22);

        imageView_team0_most[3][0] = findViewById(R.id.make_match_most30);
        imageView_team0_most[3][1] = findViewById(R.id.make_match_most31);
        imageView_team0_most[3][2] = findViewById(R.id.make_match_most32);


        imageView_team0_most[4][0] = findViewById(R.id.make_match_most40);
        imageView_team0_most[4][1] = findViewById(R.id.make_match_most41);
        imageView_team0_most[4][2] = findViewById(R.id.make_match_most42);



        imageView_team1_most[0][0] = findViewById(R.id.make_match_most50);
        imageView_team1_most[0][1] = findViewById(R.id.make_match_most51);
        imageView_team1_most[0][2] = findViewById(R.id.make_match_most52);

        imageView_team1_most[1][0] = findViewById(R.id.make_match_most60);
        imageView_team1_most[1][1] = findViewById(R.id.make_match_most61);
        imageView_team1_most[1][2] = findViewById(R.id.make_match_most62);

        imageView_team1_most[2][0] = findViewById(R.id.make_match_most70);
        imageView_team1_most[2][1] = findViewById(R.id.make_match_most71);
        imageView_team1_most[2][2] = findViewById(R.id.make_match_most72);

        imageView_team1_most[3][0] = findViewById(R.id.make_match_most80);
        imageView_team1_most[3][1] = findViewById(R.id.make_match_most81);
        imageView_team1_most[3][2] = findViewById(R.id.make_match_most82);


        imageView_team1_most[4][0] = findViewById(R.id.make_match_most90);
        imageView_team1_most[4][1] = findViewById(R.id.make_match_most91);
        imageView_team1_most[4][2] = findViewById(R.id.make_match_most92);



        for(int i = 0; i < 5; i++){
            imageView_team0_back[i].setColorFilter(Team.tear_color("UN"));
            imageView_team1_back[i].setColorFilter(Team.tear_color("UN"));
        }

        

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(editText_match_name.getWindowToken(), 0);
            }
        });

        editText_match_name.setText("매치 " + (ApplicationClass.totalMatchNum + 1));

        imageView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchName = editText_match_name.getText().toString();
                if(matchName.length() < 2){
                    ApplicationClass.showToast(getApplicationContext(), "이름을 2글자 이상으로 설정해주세요.");
                    return;
                }else if(matchName.length() > 15){
                    ApplicationClass.showToast(getApplicationContext(), "이름을 15글자 이하로 설정해주세요.");
                    return;
                }
                for(int i = 1; i < ApplicationClass.matches.size(); i++){
                    if(ApplicationClass.matches.get(i).name.equals(matchName)){
                        ApplicationClass.showToast(getApplicationContext(), "중복된 이름입니다.");
                        return;
                    }
                }

                new AlertDialog.Builder(PopupMakeMatchActivity.this).setMessage("이대로 진행하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(), BanPickActivity.class);
                                matchName = editText_match_name.getText().toString();
                                Team team0 = ApplicationClass.teams.get(team0Index);
                                Team team1 = ApplicationClass.teams.get(team1Index);
                                Match.makeMatch(matchName, team0, team1, isDefaultPosition, 0, Match.makeDefaultgames());
                                intent.putExtra("matchIndex", ApplicationClass.matches.size()-1);
                                intent.putExtra("gameIndex", 0);
                                startActivity(intent);
                                if(mInterstitialAd.isLoaded()){
                                    mInterstitialAd.show();
                                }
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .show();

            }
        });

        //진형 변경
        imageView_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int blueColor = ContextCompat.getColor(getApplicationContext(), R.color.colorBlueTeam);
                int redColor = ContextCompat.getColor(getApplicationContext(), R.color.colorRedTeam);
                if(textView_team0_name.getCurrentTextColor() == blueColor){
                    textView_team0_name.setTextColor(redColor);
                    textView_team1_name.setTextColor(blueColor);
                    isDefaultPosition = false;
                } else{
                    textView_team0_name.setTextColor(blueColor);
                    textView_team1_name.setTextColor(redColor);
                    isDefaultPosition = true;
                }
            }
        });

        //팀로고 클릭 시, 팀 선택으로 이동
        imageView_team0_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectTeamActivity.class);
                intent.putExtra("isOurTeam", true);
                startActivityForResult(intent, 0);
            }
        });

        imageView_team1_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectTeamActivity.class);
                intent.putExtra("isOurTeam", false);
                startActivityForResult(intent, 1);
            }
        });


    }

    void setTeamPlayer(int teamIndex, ImageView[] tear_backs, TextView[] tears, ImageView[][] mosts){
        Team team = ApplicationClass.teams.get(teamIndex);
        for(int i = 0; i < 5; i++){
            tears[i].setText(team.players[i].tear);
            tear_backs[i].setColorFilter(Team.tear_color(team.players[i].tear));
            int size = team.players[i].most.size() > 3 ? 3 : team.players[i].most.size();
            for(int j = 0; j < size; j++){
                mosts[i][j].setImageResource(team.players[i].most.get(j).image);
            }
            for(int j = size; j < 3; j++){
                mosts[i][j].setImageResource(R.drawable.randomchampion);
            }
        }
    }


    //다시 돌아오면, teamIndex를 받는다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode==RESULT_OK){
                //우리팀 진형
                team0Index = data.getExtras().getInt("teamIndex");
                if(ApplicationClass.teams.get(team0Index).type == 1){
                    imageView_team0_logo.setImageResource(R.drawable.no);
                }else{
                    Bitmap bitmap = ApplicationClass.StringToBitmap(ApplicationClass.teams.get(team0Index).logo);
                    imageView_team0_logo.setImageBitmap(bitmap);
                }
                textView_team0_name.setText(ApplicationClass.teams.get(team0Index).name);
                setTeamPlayer(team0Index, imageView_team0_back, textView_team0_tear, imageView_team0_most);

            }
        }else if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //적팀 진형
                team1Index = data.getExtras().getInt("teamIndex");
                if(ApplicationClass.teams.get(team1Index).type ==1){
                    imageView_team1_logo.setImageResource(R.drawable.no);
                }else{
                    Bitmap bitmap = ApplicationClass.StringToBitmap(ApplicationClass.teams.get(team1Index).logo);
                    imageView_team1_logo.setImageBitmap(bitmap);
                }
                textView_team1_name.setText(ApplicationClass.teams.get(team1Index).name);
                setTeamPlayer(team1Index, imageView_team1_back, textView_team1_tear, imageView_team1_most);
            }
        }
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