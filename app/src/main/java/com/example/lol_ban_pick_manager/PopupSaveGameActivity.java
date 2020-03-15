package com.example.lol_ban_pick_manager;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PopupSaveGameActivity extends AppCompatActivity {

    ImageView imageView_ok;
    ImageView imageView_team_logo;
    TextView textView_team_name;
    ImageView[] imageViews = new ImageView[5];
    EditText editText;
    ConstraintLayout constraintLayout;

    int star = 1;
    Match match;
    String gameName;
    Bitmap team0Image, team1Image;
    String team0Name, team1Name;
    int matchIndex;
    int victoryType = 2;
    InputMethodManager imm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_save_game);

        final Intent intent = getIntent();
        matchIndex = intent.getExtras().getInt("matchIndex");
        match = ApplicationClass.matches.get(matchIndex);
        team0Name = intent.getExtras().getString("team0Name");
        team1Name = intent.getExtras().getString("team1Name");
        if(match.team0.type == 1){
            team0Image = null;
        }else{
            team0Image = ApplicationClass.StringToBitmap(match.team0.logo);
        }
        if(match.team1.type == 1){
            team1Image = null;
        }else{
            team1Image = ApplicationClass.StringToBitmap(match.team1.logo);
        }

        editText = findViewById(R.id.save_game_editText_input);
        textView_team_name = findViewById(R.id.save_game_team_name);
        imageView_ok = findViewById(R.id.save_game_ok);
        imageView_team_logo = findViewById(R.id.save_game_image);
        imageViews[0] = findViewById(R.id.save_game_star0);
        imageViews[1] = findViewById(R.id.save_game_star1);
        imageViews[2] = findViewById(R.id.save_game_star2);
        imageViews[3] = findViewById(R.id.save_game_star3);
        imageViews[4] = findViewById(R.id.save_game_star4);
        constraintLayout = findViewById(R.id.save_game_layout);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
        editText.setText("밴픽 " + match.games.size());
        imageView_team_logo.setImageResource(R.drawable.no);
        imageViews[0].setImageResource(R.drawable.starlight);

        for(int i = 0; i < 5; i++){
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = 0;
                    ImageView nowView = (ImageView)view;
                    for(ImageView imageView : imageViews){
                        if(imageView == nowView){
                            break;
                        }
                        i++;
                    }
                    for(int j = 0; j <= i; j++){
                        imageViews[j].setImageResource(R.drawable.starlight);
                    }
                    for(int j = i+1; j < 5; j++){
                        imageViews[j].setImageResource(R.drawable.star);
                    }
                    star = i + 1;
                }
            });
        }

        imageView_team_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] items = new CharSequence[]{team0Name, team1Name, "설정 안함"};
                new AlertDialog.Builder(PopupSaveGameActivity.this).setTitle("승리팀 설정")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    imageView_team_logo.setImageBitmap(team0Image);
                                    textView_team_name.setText(team0Name);
                                    victoryType = 0;
                                }else if(i == 1){
                                    imageView_team_logo.setImageBitmap(team1Image);
                                    textView_team_name.setText(team1Name);
                                    victoryType = 1;
                                }else{
                                    imageView_team_logo.setImageResource(R.drawable.no);
                                    textView_team_name.setText("설정 안함");
                                    victoryType = 2;
                                }
                            }
                        }).show();

            }
        });

        imageView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = editText.getText().toString();
                if(gameName.length() < 2){
                    ApplicationClass.showToast(getApplicationContext(), "이름을 2글자 이상으로 설정해주세요.");
                    return;
                }else if(gameName.length() > 15){
                    ApplicationClass.showToast(getApplicationContext(), "이름을 15글자 이하로 설정해주세요.");
                    return;
                }
                Match match = ApplicationClass.matches.get(matchIndex);
                for(int i = 1; i < match.games.size(); i++){
                    if(match.games.get(i).name.equals(gameName)){
                        ApplicationClass.showToast(getApplicationContext(), "중복된 이름입니다.");
                        return;
                    }
                }
                Intent intent1 = new Intent();
                intent1.putExtra("star", star);
                intent1.putExtra("gameName", gameName);
                intent1.putExtra("victoryType", victoryType);
                setResult(RESULT_OK, intent1);
                finish();
            }
        });

    }
}
