package com.example.lol_ban_pick_manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

public class PopupMakeTeamActivity extends AppCompatActivity {

    String teamName;
    Bitmap teamLogo;
    ArrayList<Champion> most;
    Team.Player[] players = new Team.Player[5];
    int[] playerIndexes = new int[5];
    int playerIndexForChange;
    InputMethodManager imm;

    EditText editText_teamName;
    ImageView imageView_teamLogo;
    ImageView[] imageViews = new ImageView[5];
    TextView[] textViews = new TextView[5];
    RecyclerView recyclerView;
    ImageView imageView_ok;
    TextView textView_imageX;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_make_team);


        editText_teamName = findViewById(R.id.make_team_editText_input);
        imageView_teamLogo = findViewById(R.id.make_team_team_logo);
        imageView_ok = findViewById(R.id.make_team_ok);
        recyclerView = findViewById(R.id.make_team_recycler);
        imageViews[0] = findViewById(R.id.make_team_player_image0);
        imageViews[1] = findViewById(R.id.make_team_player_image1);
        imageViews[2] = findViewById(R.id.make_team_player_image2);
        imageViews[3] = findViewById(R.id.make_team_player_image3);
        imageViews[4] = findViewById(R.id.make_team_player_image4);
        textViews[0] = findViewById(R.id.make_team_player_name0);
        textViews[1] = findViewById(R.id.make_team_player_name1);
        textViews[2] = findViewById(R.id.make_team_player_name2);
        textViews[3] = findViewById(R.id.make_team_player_name3);
        textViews[4] = findViewById(R.id.make_team_player_name4);
        textView_imageX = findViewById(R.id.make_team_imageX);
        constraintLayout = findViewById(R.id.make_team_layout);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(editText_teamName.getWindowToken(), 0);
            }
        });

        //플레이어와 팀로고 초기화
        for(int i = 0; i < 5; i++){
            playerIndexes[i] = -1;
            players[i] = ApplicationClass.players.get(1);
        }
        teamLogo = null;

        //각 플레이어들의 이미지 텍스트 초기화
       for(int i = 0; i < 5; i ++){

           imageViews[i].setImageResource(R.drawable.randomchampion);
           textViews[i].setText("이름없음");
           imageViews[i].setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //각 플레이어 이미지 누르면 플레이어선택으로 이동
                   int i = ApplicationClass.findIndexInImageView(view, imageViews);
                   Intent intent = new Intent(getApplicationContext(), SelectPlayerActivity.class);
                   playerIndexForChange = i;
                   startActivityForResult(intent, 1);
               }
           });
        }


        //사진첩으로 이동
        imageView_teamLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0);
            }
        });

        //이미지를 삭제
        textView_imageX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamLogo = null;
                imageView_teamLogo.setImageResource(R.drawable.no);
            }
        });


        //오케이 누르면 팀 만들기.
        imageView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamName = editText_teamName.getText().toString();
                if(teamName.length() < 2){
                    ApplicationClass.showToast(getApplicationContext(), "이름을 2글자 이상으로 설정해주세요.");
                    return;
                }else if(teamName.length() > 10){
                    ApplicationClass.showToast(getApplicationContext(), "이름을 10글자 이하로 설정해주세요.");
                    return;
                }
                for(int i = 1; i < ApplicationClass.teams.size(); i++){
                    if(ApplicationClass.teams.get(i).name.equals(teamName)){
                        ApplicationClass.showToast(getApplicationContext(), "중복된 이름입니다.");
                        return;
                    }
                }
                new AlertDialog.Builder(PopupMakeTeamActivity.this).setMessage("이대로 팀을 만드시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for(int j = 0; j < 5; j++){
                                    if(playerIndexes[j] == -1){
                                        players[j] = ApplicationClass.players.get(1);
                                    }else{
                                        players[j] = ApplicationClass.players.get(playerIndexes[j]);
                                    }
                                }
                                most = new ArrayList<>();
                                Team.makeTeam(teamName, teamLogo, players, most);
                                Intent intent = new Intent();
                                intent.putExtra("isMake", 1);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        }).show();

            }
        });





    }

    //다시 되돌아왔을때 0 사진첩에서 돌아왔을떄, 1 플레이어 선택에서 돌아왔을때
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            //0 사진첩에서 돌아왔을 때
            if(resultCode==RESULT_OK){
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imageView_teamLogo.setImageBitmap(img);
                    teamLogo = img;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(resultCode == RESULT_CANCELED){
                return;
            }
        }else if(requestCode == 1){
            //1 플레이어 선택에서 돌아왔을 때
            if(resultCode == RESULT_OK){
                int playerIndex = data.getExtras().getInt("playerIndex");
                if(playerIndex == -1){
                    //선택 안했다면 아무것도 안하기
                    return;
                }else{
                    Team.Player player = ApplicationClass.players.get(playerIndex);
                    if(player.most.size() == 0){
                        imageViews[playerIndexForChange].setImageResource(R.drawable.randomchampion);
                    }else{
                        imageViews[playerIndexForChange].setImageResource(player.most.get(0).image);
                    }
                    String name = player.name;
                    if(player.name.length() > 5){
                        name = name.substring(0,4);
                        name+= "..";
                    }
                    textViews[playerIndexForChange].setText(name);
                    playerIndexes[playerIndexForChange] = playerIndex;

                }

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

    //뒤로 가기. 리사이클러뷰 바뀔 필요가 없음을 전달
    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("isMake", 0);
        setResult(RESULT_OK, intent);
        finish();
    }
}