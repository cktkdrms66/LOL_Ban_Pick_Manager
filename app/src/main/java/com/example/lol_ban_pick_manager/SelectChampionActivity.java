package com.example.lol_ban_pick_manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.ConcurrentModificationException;
import java.util.Locale;

public class SelectChampionActivity extends Activity {
    ImageView imageView_search;
    RecyclerView recyclerView;
    EditText editText_search;
    ConstraintLayout constraintLayout;

    Intent intent;
    ArrayList<Champion> champions;
    int[] championIndexes;
    ChampionAdapter adapter;
    InputMethodManager imm;

    int where;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);

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
            championIndexes = intent.getExtras().getIntArray("championIndexes");
        }
        imageView_search = findViewById(R.id.champion_imageView_search2);
        recyclerView = findViewById(R.id.champion_recyclerView);
        editText_search = findViewById(R.id.champion_editText);
        constraintLayout = findViewById(R.id.champion_layout);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        adapter = new ChampionAdapter(this, ApplicationClass.champions);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChampionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, ImageView imageView) {
                int index = adapter.filteredList.get(pos).id;
                if(where ==0){
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
