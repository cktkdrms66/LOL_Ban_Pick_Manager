package com.example.lol_ban_pick_manager;

import android.app.Activity;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_make_game);

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