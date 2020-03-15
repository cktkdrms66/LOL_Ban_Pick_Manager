package com.example.lol_ban_pick_manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SelectChampionActivity extends Activity {
    ImageView imageView_search;
    RecyclerView recyclerView;
    EditText editText_search;
    ConstraintLayout constraintLayout;
    ImageView[] imageViews = new ImageView[5];
    ImageView imageView_team;

    Intent intent;
    ArrayList<Champion> champions;
    int clickedPositionIndex;
    boolean isTeamClicked;
    int[] championIndexes;
    int[] playerIndexs;
    ArrayList<Boolean> mIsPicked;
    ChampionAdapter adapter;
    InputMethodManager imm;
    int where;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);

        imageView_search = findViewById(R.id.champion_imageView_search2);
        recyclerView = findViewById(R.id.champion_recyclerView);
        editText_search = findViewById(R.id.champion_editText);
        constraintLayout = findViewById(R.id.champion_layout);
        imageViews[0] = findViewById(R.id.champion_top);
        imageViews[1] = findViewById(R.id.champion_jg);
        imageViews[2] = findViewById(R.id.champion_mid);
        imageViews[3] = findViewById(R.id.champion_bot);
        imageViews[4] = findViewById(R.id.champion_sup);
        imageView_team = findViewById(R.id.champion_team);

        intent = getIntent();
        where = intent.getExtras().getInt("where");
        if(where == 0){
            //플레이어 만들기
            champions = (ArrayList<Champion>)intent.getSerializableExtra("champions");
        }
        if(where == 1){
            //현재 플레이어
            champions = (ArrayList<Champion>)intent.getSerializableExtra("champions");
        }else if(where == 2){
            //밴픽에서
            mIsPicked = (ArrayList<Boolean>)intent.getSerializableExtra("mIsPicked");
            championIndexes = intent.getExtras().getIntArray("championIndexes");
        }else if(where == 3){
            //팀 만들기에서 혹은 팀 세부에서
            imageView_team.setImageResource(R.drawable.ic_people_black_24dp);
            champions = (ArrayList<Champion>)intent.getSerializableExtra("champions");
            playerIndexs = intent.getIntArrayExtra("playerIndexs");
            for(int i = 0 ; i < 5; i++){
                if(playerIndexs[i] == -1){
                    playerIndexs[i] = 1;
                }
            }
        }


        //키보드 내리기
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
            }
        });

        //각 포지션 이미지 누르면 해당 챔피언들 나오게
        clickedPositionIndex = -1;
        for(int i = 0; i < 5; i++){
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = ApplicationClass.findIndexInImageView(view, imageViews);
                    if(isTeamClicked){
                        if(clickedPositionIndex == index){
                            imageViews[index].setColorFilter(0x69696969, PorterDuff.Mode.DST);
                            clickedPositionIndex = -1;
                            adapter.getAll(where, editText_search.getText().toString(),clickedPositionIndex);
                        }else{
                            for(int i = 0; i < 5; i++){
                                if(index == i){
                                    imageViews[i].setColorFilter(0x69696969, PorterDuff.Mode.MULTIPLY);
                                }else{
                                    imageViews[i].setColorFilter(0x69696969, PorterDuff.Mode.DST);
                                }

                            }
                            clickedPositionIndex = index;
                            adapter.setTeamChampion(clickedPositionIndex, playerIndexs);
                        }
                    }else{
                        if(clickedPositionIndex == index){
                            imageViews[index].setColorFilter(0x69696969, PorterDuff.Mode.DST);
                            adapter.getAll(where, editText_search.getText().toString(),clickedPositionIndex);
                            clickedPositionIndex = -1;
                        }else{
                            for(int i = 0; i < 5; i++){
                                if(index == i){
                                    imageViews[i].setColorFilter(0x69696969, PorterDuff.Mode.MULTIPLY);
                                }else{
                                    imageViews[i].setColorFilter(0x69696969, PorterDuff.Mode.DST);
                                }

                            }
                            adapter.getFilter(index, where, editText_search.getText().toString());
                            clickedPositionIndex = index;
                        }
                    }

                }
            });
        }

        //포지션 팀 클릭 이벤트 처리
        imageView_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(where == 3){
                    if(isTeamClicked){
                        isTeamClicked = false;
                        imageView_team.setColorFilter(0x69696969, PorterDuff.Mode.DST);
                        if(clickedPositionIndex != -1){
                            adapter.getFilter(clickedPositionIndex, where, editText_search.getText().toString());
                        }else{
                            adapter.getAll(where, editText_search.getText().toString(),clickedPositionIndex);
                        }
                    }else{
                        isTeamClicked = true;
                        imageView_team.setColorFilter(0x69696969, PorterDuff.Mode.MULTIPLY);
                        adapter.setTeamChampion(clickedPositionIndex, playerIndexs);

                    }
                }
            }
        });


        //리사이클러뷰 챔피언 설정
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        adapter = new ChampionAdapter(this, ApplicationClass.champions);
        recyclerView.setAdapter(adapter);
        if(where == 2){
            adapter.setmIsPicked(mIsPicked);
        }

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
            }
        });
        adapter.setOnItemClickListener(new ChampionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, ImageView imageView) {
                int index = adapter.filteredList.get(pos).id;
                if(where ==0 || where == 3){
                    for(int i = 0; i < champions.size(); i++){
                        if(champions.get(i).id == index){
                            ApplicationClass.showToast(getApplicationContext(), "모스트에 이미 해당 챔피언이 존재합니다");
                            return;
                        }
                    }
                } else if(where == 1){
                    for(int i = 0; i < champions.size(); i++){
                        if(champions.get(i).id == index){
                            ApplicationClass.showToast(getApplicationContext(), "모스트에 이미 해당 챔피언이 존재합니다");
                            return;
                        }
                    }

                } else if(where == 2){
                    for(int i = 0; i < 20; i++){
                        if(championIndexes[i] == index){
                            ApplicationClass.showToast(getApplicationContext(), "이미 해당 챔피언이 존재합니다");
                            return;
                        }
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("championIndex", index);
                setResult(RESULT_OK, intent);
                imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
                finish();
            }
        });

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("championIndex", -1);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
