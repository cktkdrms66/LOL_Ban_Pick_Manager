package com.example.lol_ban_pick_manager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayerDetailActivity extends Activity {

    ImageView imageView_tear;
    TextView textView_tear;
    TextView textView_name;
    RecyclerView recyclerView;
    Team.Player player;
    Team team;
    ImageView[] imageViews = new ImageView[3];
    TextView[] textViews = new TextView[3];

    ChampionAdapter adapter;
    ArrayList<Champion> arrayList;
    static int selectIndex;


    boolean isChange = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);


        imageView_tear = findViewById(R.id.player_detail_back);
        textView_tear = findViewById(R.id.player_detail_tear);
        textView_name = findViewById(R.id.player_detail_name);
        recyclerView = findViewById(R.id.player_detail_recycler);
        imageViews[0] = findViewById(R.id.cardView_champion_image0);
        imageViews[1] = findViewById(R.id.cardView_champion_image1);
        imageViews[2] = findViewById(R.id.cardView_champion_image2);
        textViews[0] = findViewById(R.id.cardView_champion_name0);
        textViews[1] = findViewById(R.id.cardView_champion_name1);
        textViews[2] = findViewById(R.id.cardView_champion_name2);

        final Intent intent = getIntent();
        final int playerIndex = intent.getExtras().getInt("playerIndex");
        final int isOktoModify = intent.getIntExtra("isOktoModify", 1);


        //where ===== 0 FragmentPlayer,BanPick     1 TeamDetail
        if(intent.getExtras().getInt("where") == 0){
            //만약 플레이어 창에서 왔다면 해당 플레이어의 정보를 획득한다.
            player = ApplicationClass.players.get(playerIndex);
        }else{
            //만약 팀 디테일에서 왔다면 해당 팀과 플레이어의 정보를 획득한다.
            team = ApplicationClass.teams.get(intent.getExtras().getInt("teamIndex"));
            player = team.players[playerIndex];
        }
        textView_tear.setText(player.tear);
        textView_name.setText(player.name);

        //간판 모스트 픽의 모습을 설정한다.
        int size = (player.most.size() > 3 ? 3 : player.most.size());
        for(int i = 0; i < size; i++){
            imageViews[i].setImageResource(player.most.get(i).image);
            textViews[i].setText(player.most.get(i).name);
        }
        //티어 누를 시 이벤트 설정
        imageView_tear.setColorFilter(Team.tear_color(player.tear), PorterDuff.Mode.SRC_IN);
        if(isOktoModify == 1){
            imageView_tear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialog customDialog = new CustomDialog(PlayerDetailActivity.this);
                    customDialog.setSelectTear();
                    customDialog.setOnClickListener(new CustomDialog.OnClickListener() {
                        @Override
                        public void onFirstClick(View v, int i) {
                            //언랭, 마스터 그마 챌 이벤트
                            imageView_tear.setColorFilter(Team.tear_color(i), PorterDuff.Mode.SRC_IN);
                            textView_tear.setText(Team.getTearFromInt(i));
                            player.tear = Team.getTearFromInt(i);
                            isChange = true;
                            ApplicationClass.saveRePlayer(player);
                        }

                        @Override
                        public void onSecondClick(View v, int tear, int i) {
                            //아이언 ~ 다이아 이벤트
                            imageView_tear.setColorFilter(Team.tear_color(tear), PorterDuff.Mode.SRC_IN);
                            textView_tear.setText(Team.getTearFromInt(tear) + (i+1));
                            player.tear = Team.getTearFromInt(tear) + (i+1);
                            isChange = true;
                            ApplicationClass.saveRePlayer(player);
                        }
                    });
                }
            });

        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

        //해당 플레이어의 모스트들을 넣는다 플러스와 함께
        arrayList = new ArrayList<>();
        for(int i = 0; i < player.most.size(); i++){
            arrayList.add(player.most.get(i));
        }
        if(isOktoModify == 1){
            arrayList.add(Champion.makePlus());
        }

        adapter = new ChampionAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

        //챔피언 클릭 이벤트
        if(isOktoModify ==1){
            adapter.setOnItemClickListener(new ChampionAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int pos, ImageView imageView) {
                    if(arrayList.get(pos).isChampion ==false){
                        //플러스면 챔피언 선택으로 이동한다
                        Intent intent1 = new Intent(getApplicationContext(), SelectChampionActivity.class);
                        intent1.putExtra("where", 1);
                        int[] championIndexes = new int[arrayList.size()-1];
                        for(int i = 0; i < championIndexes.length; i++){
                            championIndexes[i] = arrayList.get(i).id;
                        }
                        intent1.putExtra("championIndexes", championIndexes);
                        startActivityForResult(intent1, 0);
                    } else{
                        if(adapter.getIsClicked(pos) == false){
                            //챔피언을 누르면 순서 변경
                            if(adapter.getmOnlyItemPosition() == -1){
                                //만약 최초 선택이면 검게 칠한다
                                adapter.setIsClicked(pos, true);
                                adapter.mOnlyItemPosition = pos;
                                adapter.notifyDataSetChanged();
                            }else{
                                //두번째 선택이면 다른 그 애와 바꾼다
                                Champion champion = Champion.getChampion(player.most.get(pos).name);
                                player.most.set(pos, Champion.getChampion(player.most.get(adapter.getmOnlyItemPosition()).name));
                                arrayList.set(pos, Champion.getChampion(player.most.get(adapter.getmOnlyItemPosition()).name));
                                player.most.set(adapter.getmOnlyItemPosition(), champion);
                                arrayList.set(adapter.getmOnlyItemPosition(), Champion.getChampion(champion.name));
                                int size = player.most.size() > 3 ? 3 : player.most.size();
                                for(int i = 0; i < size; i++){
                                    imageViews[i].setImageResource(player.most.get(i).image);
                                    textViews[i].setText(player.most.get(i).name);
                                }
                                for(int i = size; i < 3; i++){
                                    imageViews[i].setImageResource(R.drawable.randomchampion);
                                    textViews[i].setText("");
                                }
                                isChange = true;
                                ApplicationClass.saveRePlayer(player);

                                adapter.setIsClicked(adapter.mOnlyItemPosition, false);
                                adapter.mOnlyItemPosition = -1;
                                adapter.notifyDataSetChanged();

                            }
                        }else{
                            //선택했던걸 다시 선택하면 검은 칠을 뺸다.
                            adapter.setIsClicked(pos, false);
                            adapter.mOnlyItemPosition = -1;
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            });


            //챔피언 길게 누를 시 삭제한다
            adapter.setOnLongClickListener(new ChampionAdapter.OnLongClickListener() {
                @Override
                public void onLongClick(View view, int pos) {
                    if(arrayList.get(pos).isChampion ==false){
                        //플러스면 아무것도 안 한다
                        return;
                    }
                    selectIndex = pos;
                    new AlertDialog.Builder(getApplicationContext()).setMessage("삭제하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //삭제한다
                                    arrayList.remove(selectIndex);
                                    adapter.mIsClicked.remove(selectIndex);
                                    adapter.mIsPicked.remove(selectIndex);
                                    player.most.remove(selectIndex);
                                    if (selectIndex < 3) {// 0 1 2
                                        int size = player.most.size() > 3 ? 3 : player.most.size();
                                        for(int j= 0; j < size; j++){
                                            imageViews[j].setImageResource(player.most.get(j).image);
                                            textViews[j].setText(player.most.get(j).name);
                                        }
                                        for(int j = player.most.size(); j < 3; j++){
                                            imageViews[j].setImageResource(R.drawable.randomchampion);
                                            textViews[j].setText("");
                                        }
                                    }
                                    adapter.notifyItemRemoved(selectIndex);
                                    isChange = true;
                                    ApplicationClass.saveRePlayer(player);
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



    }


    @Override
    public void onBackPressed() {
        //이 플레이어에 대해 바뀐 것이 있는지를 정보를 준다
        Intent intent = new Intent();
        intent.putExtra("isChange", isChange);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    //되돌아 올때 이벤트 설정
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                //챔피언 선택에서 되돌아왔을 떄
                int championIndex = data.getExtras().getInt("championIndex");
                if(championIndex == -1){
                    return;
                }

                Champion champion = Champion.getChampion(championIndex);
                arrayList.add(arrayList.size()-1,champion);
                player.most.add(champion);
                adapter.mIsClicked.add(false);
                adapter.mIsPicked.add(false);

                adapter.notifyItemInserted(arrayList.size()-2);
                isChange = true;
                int size = player.most.size() > 3 ? 3 : player.most.size();
                for(int i = 0; i < size; i++){
                    imageViews[i].setImageResource(player.most.get(i).image);
                    textViews[i].setText(player.most.get(i).name);
                }
                ApplicationClass.saveRePlayer(player);

            }
        }
    }
}
