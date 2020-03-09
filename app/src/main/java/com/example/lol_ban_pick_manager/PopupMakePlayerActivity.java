package com.example.lol_ban_pick_manager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PopupMakePlayerActivity extends AppCompatActivity {

    ImageView imageView_tear;
    TextView textView_tear;
    EditText editText;
    RecyclerView recyclerView;
    ImageView imageView_ok;
    ConstraintLayout constraintLayout;

    String tear;
    ChampionAdapter adapter;
    ArrayList<Champion> champions;
    InputMethodManager imm;
    int selectIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_make_player);


        imageView_tear = findViewById(R.id.make_player_back);
        textView_tear = findViewById(R.id.make_player_tear);
        editText = findViewById(R.id.make_player_editText_input);
        recyclerView = findViewById(R.id.make_player_recycler);
        imageView_ok = findViewById(R.id.make_player_ok);
        constraintLayout = findViewById(R.id.make_player_layout);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });

        //티어 초기화, 티어 누를 시, 다이얼로그 띄우기.
        imageView_tear.setColorFilter(Team.tear_color("UN"), PorterDuff.Mode.SRC_IN);
        imageView_tear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(PopupMakePlayerActivity.this);
                customDialog.setSelectTear();
                customDialog.setOnClickListener(new CustomDialog.OnClickListener() {
                    @Override
                    public void onFirstClick(View v, int i) {
                        //언랭, 마스터~챌린저에 해당
                        imageView_tear.setColorFilter(Team.tear_color(i), PorterDuff.Mode.SRC_IN);
                        textView_tear.setText(Team.getTearFromInt(i));
                        tear = Team.getTearFromInt(i);
                    }

                    @Override
                    public void onSecondClick(View v, int tearIndex, int i) {
                        //아이언 ~ 다이아까지 해당
                        imageView_tear.setColorFilter(Team.tear_color(tearIndex), PorterDuff.Mode.SRC_IN);
                        textView_tear.setText(Team.getTearFromInt(tearIndex) + (i+1));
                        tear = Team.getTearFromInt(tearIndex) + (i+1);
                    }
                });
            }
        });


        tear = "UN";
        champions = new ArrayList<>();
        champions.add(Champion.makePlus());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new ChampionAdapter(this, champions);
        recyclerView.setAdapter(adapter);

        //플러스 클릭 시, 챔피언 선택으로 이동.
        adapter.setOnItemClickListener(new ChampionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, ImageView imageView) {
                if(champions.get(pos).isChampion ==false){
                    //플러스 클릭 시, 챔피언 선택으로 이동
                    Intent intent1 = new Intent(getApplicationContext(), SelectChampionActivity.class);
                    intent1.putExtra("where", 0);
                    intent1.putExtra("champions", champions);
                    startActivityForResult(intent1, 0);
                } else{
                    if(adapter.getIsClicked(pos) == false){
                        //선택했을 때
                        if(adapter.getmOnlyItemPosition() == -1){
                            //최초 선택 시 해당 색을 검게 칠한다.
                            adapter.setIsClicked(pos, true);
                            adapter.mOnlyItemPosition = pos;
                            adapter.notifyDataSetChanged();
                        }else{
                            //이미 다른 선택된 애가 있다면 그 친구와 체인지
                            Champion champion = Champion.getChampion(champions.get(pos).name);
                            champions.set(pos, Champion.getChampion(champions.get(adapter.getmOnlyItemPosition()).name));
                            champions.set(adapter.getmOnlyItemPosition(), Champion.getChampion(champion.name));
                            adapter.setIsClicked(adapter.mOnlyItemPosition, false);
                            adapter.mOnlyItemPosition = -1;
                            adapter.notifyDataSetChanged();
                        }
                    }else{
                        //이미 선택된 애 누르면 선택을 풀자.
                        adapter.setIsClicked(pos, false);
                        adapter.mOnlyItemPosition = -1;
                        adapter.notifyDataSetChanged();
                    }

                }

            }
        });

        //길게 누르면 삭제
        adapter.setOnLongClickListener(new ChampionAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int pos) {
                if(champions.get(pos).isChampion ==false){
                    //플러스는 삭제 못함
                    return;
                }
                selectIndex = pos;
                new AlertDialog.Builder(PopupMakePlayerActivity.this).setMessage("삭제하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                champions.remove(selectIndex);
                                adapter.mIsClicked.remove(selectIndex);
                                adapter.mIsPicked.remove(selectIndex);
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
        });

        //오케이 누르면
        imageView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().length() < 2){
                    ApplicationClass.showToast(getApplicationContext(), "이름을 2글자 이상 입력해주세요.");
                    return;
                }
                for(int i = 0; i < ApplicationClass.players.size(); i++){
                    if(editText.getText().toString().equals(ApplicationClass.players.get(i).name)){
                        ApplicationClass.showToast(getApplicationContext(), "중복된 이름입니다.");
                        return;
                    }
                }
                new AlertDialog.Builder(PopupMakePlayerActivity.this).setMessage("이대로 플레이어를 만드시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                champions.remove(champions.size()-1);
                                Team.makePlayer(editText.getText().toString(), tear, champions);
                                Intent intent = new Intent();
                                intent.putExtra("isChange", true);
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

    //챔피언 선택에서 되돌아왔을때


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                int championIndex = data.getExtras().getInt("championIndex");
                if(championIndex == -1){
                    return;
                }
                champions.add(champions.size()-1, Champion.getChampion(championIndex));
                adapter.mIsClicked.add(false);
                adapter.mIsPicked.add(false);
                adapter.notifyItemInserted(champions.size()-2);

            }
        }
    }

    @Override
    public void onBackPressed() {
        //뒤로 갈때 만약 리사이클러뷰를 변경해야하는지를 정해줌.
        Intent intent = new Intent();
        intent.putExtra("isChange", false);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
