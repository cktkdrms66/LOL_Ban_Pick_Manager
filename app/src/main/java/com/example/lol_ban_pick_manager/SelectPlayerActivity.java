package com.example.lol_ban_pick_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectPlayerActivity extends Activity {

    private TextView textView;
    private RecyclerView recyclerView;
    private ImageView imageView_search;

    ArrayList<Team.Player> arrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_player);

        textView = findViewById(R.id.player_textView_title);
        imageView_search = findViewById(R.id.player_imageView_setting);
        recyclerView = findViewById(R.id.player_recyclerView);

        imageView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        arrayList = new ArrayList<>();
        for(int i = 1; i < ApplicationClass.players.size(); i++){
            arrayList.add(ApplicationClass.players.get(i));
        }
        PlayerAdapter adapter = new PlayerAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = getIntent();
                int playerIndex = pos+1;
                intent.putExtra("playerIndex", playerIndex);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("playerIndex", -1);
        setResult(RESULT_OK, intent);
        finish();
    }
}
