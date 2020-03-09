package com.example.lol_ban_pick_manager;

import android.app.Activity;
import android.content.Context;
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

public class PopupGameActivity extends AppCompatActivity {

    TextView textView_title;
    ImageView imageView_plus;
    ImageView imageView_ok;
    RecyclerView recyclerView_teams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_game);


        final Intent intent = getIntent();
        final int matchIndex = intent.getExtras().getInt("matchIndex");

        imageView_plus = findViewById(R.id.game_plus);
        textView_title = findViewById(R.id.game_textView_title);
        imageView_ok = findViewById(R.id.game_imageView_ok);
        recyclerView_teams = findViewById(R.id.game_recyclerView_games);



        recyclerView_teams.setHasFixedSize(true);
        recyclerView_teams.setLayoutManager(new LinearLayoutManager(this));

        final GameAdapter adapter = new GameAdapter(ApplicationClass.matches.get(matchIndex).games);
        recyclerView_teams.setAdapter(adapter);

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
            }
        });

        adapter.setOnItemClickListener(new GameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), BanPickActivity.class);
                intent1.putExtra("matchIndex", matchIndex);
                intent1.putExtra("gameIndex", 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
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
