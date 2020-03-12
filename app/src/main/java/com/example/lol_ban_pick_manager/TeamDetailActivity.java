package com.example.lol_ban_pick_manager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TeamDetailActivity extends Activity {

    ImageView imageView_teamLogo;
    TextView textView_teamName;
    RecyclerView recyclerView;
    ImageView[] imageViews = new ImageView[5];
    TextView[] textViews = new TextView[5];
    TextView[] textViews_tear = new TextView[5];
    ImageView[] imageViews_tear = new ImageView[5];

    Team team;
    int teamIndex;
    int isOktoModify;
    int[] playerIndexes = new int[5];
    ChampionAdapter adapter;

    static boolean change;
    static int selectIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        imageView_teamLogo = findViewById(R.id.team_detail_teamlogo);
        textView_teamName = findViewById(R.id.team_detail_title);
        recyclerView = findViewById(R.id.team_detail_recyclerview);
        imageViews[0] = findViewById(R.id.cardView_player_image0);
        imageViews[1] = findViewById(R.id.cardView_player_image1);
        imageViews[2] = findViewById(R.id.cardView_player_image2);
        imageViews[3] = findViewById(R.id.cardView_player_image3);
        imageViews[4] = findViewById(R.id.cardView_player_image4);
        textViews[0] = findViewById(R.id.cardView_player_name0);
        textViews[1] = findViewById(R.id.cardView_player_name1);
        textViews[2] = findViewById(R.id.cardView_player_name2);
        textViews[3] = findViewById(R.id.cardView_player_name3);
        textViews[4] = findViewById(R.id.cardView_player_name4);
        imageViews_tear[0] = findViewById(R.id.cardView_player_tear_back0);
        imageViews_tear[1] = findViewById(R.id.cardView_player_tear_back1);
        imageViews_tear[2] = findViewById(R.id.cardView_player_tear_back2);
        imageViews_tear[3] = findViewById(R.id.cardView_player_tear_back3);
        imageViews_tear[4] = findViewById(R.id.cardView_player_tear_back4);
        textViews_tear[0] = findViewById(R.id.cardView_player_textView_tear0);
        textViews_tear[1] = findViewById(R.id.cardView_player_textView_tear1);
        textViews_tear[2] = findViewById(R.id.cardView_player_textView_tear2);
        textViews_tear[3] = findViewById(R.id.cardView_player_textView_tear3);
        textViews_tear[4] = findViewById(R.id.cardView_player_textView_tear4);

        //수정가능한지 가져온다
        final Intent intent = getIntent();
        isOktoModify = intent.getIntExtra("isOktoModify", 1);

        //팀 정보를 가져온다
        teamIndex = intent.getExtras().getInt("teamIndex");
        team = ApplicationClass.teams.get(teamIndex);

        //플레이어 인덱스를 넣는다
        for(int i = 0 ; i < 5; i++){
            playerIndexes[i] = team.players[i].id;
        }

        //팀 로고를 세팅한다
        if(team.logo == null){
            imageView_teamLogo.setImageResource(R.drawable.no);
        }else{
            imageView_teamLogo.setImageBitmap(ApplicationClass.StringToBitmap(team.logo));
        }
        //팀 로고를 누르면 메뉴가 뜬다 바꿀 거냐 아니면 삭제할거냐
        if(isOktoModify ==1){
            imageView_teamLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharSequence[] charSequences = new CharSequence[]{"변경", "이미지 삭제"};
                    new AlertDialog.Builder(TeamDetailActivity.this).setItems(charSequences, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //이미지 변경 사진첩으로 이동
                            if(i == 0){
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 3);

                            }else{
                                //이미지 삭제
                                imageView_teamLogo.setImageResource(R.drawable.no);
                                team.logo = null;
                                ApplicationClass.saveReTeam(teamIndex);
                                change = true;
                            }
                        }
                    }).show();
                }
            });
        }
        textView_teamName.setText(team.name);

        //플레이어들의 티어 색깔을 정한다
        for(int i = 0; i < 5; i++){
            imageViews_tear[i].setColorFilter(Team.tear_color(team.players[i].tear), PorterDuff.Mode.SRC_IN);
            textViews_tear[i].setText(team.players[i].tear);
        }

        //핵심 챔피언 모스트들을 설정한다.
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        if(isOktoModify == 0){
            ArrayList<Champion> champions = new ArrayList<>();
            for(int i = 0; i < team.most.size() -1; i++){
                champions.add(team.most.get(i));
            }
            adapter = new ChampionAdapter(this, champions);
        }else{
            adapter = new ChampionAdapter(this, team.most);
        }
        recyclerView.setAdapter(adapter);

        if(isOktoModify == 1){
            //모스트 누를 시, 챔피언 선택으로 이동
            adapter.setOnItemClickListener(new ChampionAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int pos, ImageView imageView) {
                    if(team.most.get(pos).isChampion ==false){
                        //플러스 클릭 시, 챔피언 선택으로 이동
                        Intent intent1 = new Intent(getApplicationContext(), SelectChampionActivity.class);
                        intent1.putExtra("where", 3);
                        intent1.putExtra("champions", team.most);
                        intent1.putExtra("playerIndexs", playerIndexes);
                        startActivityForResult(intent1, 2);
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
                                Champion champion = Champion.getChampion(team.most.get(pos).name);
                                team.most.set(pos, Champion.getChampion(team.most.get(adapter.getmOnlyItemPosition()).name));
                                team.most.set(adapter.getmOnlyItemPosition(), Champion.getChampion(champion.name));
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
                    if(team.most.get(pos).isChampion ==false){
                        //플러스는 삭제 못함
                        return;
                    }
                    selectIndex = pos;
                    new androidx.appcompat.app.AlertDialog.Builder(TeamDetailActivity.this).setMessage("삭제하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    team.most.remove(selectIndex);
                                    ApplicationClass.saveReTeam(teamIndex);
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
        }

        

        //각 플레이어 클릭시 이벤트 설정
        for(int i = 0; i < 5; i++){
            if(team.players[i].most.size() == 0){
                imageViews[i].setImageResource(R.drawable.randomchampion);
            }else{
                imageViews[i].setImageResource(team.players[i].most.get(0).image);
            }
            textViews[i].setText(team.players[i].name);
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isOktoModify == 1){
                        int i = 0;
                        ImageView nowView = (ImageView) view;
                        for(ImageView imageView : imageViews){
                            if(nowView == imageView){
                                break;
                            }
                            i++;
                        }

                        selectIndex = i;
                        show();
                    }
                }
            });
        }
    }

    void show(){
        final List<String> list = new ArrayList<>();
        list.add("변경");
        if(team.players[selectIndex].type != 1){
            list.add("자세히 보기");
        }
        final CharSequence[] items =  list.toArray(new String[list.size()]);
        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            Intent intent1 = new Intent(getApplicationContext(), SelectPlayerActivity.class);
                            startActivityForResult(intent1, 1);
                        }else{
                            Intent intent1 = new Intent(getApplicationContext(), PlayerDetailActivity.class);
                            intent1.putExtra("teamIndex", teamIndex);
                            intent1.putExtra("playerIndex", selectIndex);
                            intent1.putExtra("where", 1);
                            startActivityForResult(intent1, 0);
                        }
                    }
                })
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            //플레이어 디테일에서 되돌아왔을떄
            if(resultCode == RESULT_OK){
                Boolean isChange = data.getExtras().getBoolean("isChange");
                if(isChange){
                    for(int i = 0; i < 5; i++) {
                        if (team.players[i].type == 1) {
                            continue;
                        }
                        if (team.players[i].most.size() == 0) {
                            imageViews[i].setImageResource(R.drawable.randomchampion);
                        } else {
                            imageViews[i].setImageResource(team.players[i].most.get(0).image);
                        }
                        imageViews_tear[i].setColorFilter(Team.tear_color(team.players[i].tear), PorterDuff.Mode.SRC_IN);
                        textViews_tear[i].setText(team.players[i].tear);
                    }
                    ApplicationClass.saveReTeam(team);
                    change = true;
                }
            }
        }else if(requestCode == 1){
            //플레이어선택에서 되돌아왔을떄
            if(resultCode == RESULT_OK){
                int playerIndex = data.getExtras().getInt("playerIndex");
                if(playerIndex == -1 || playerIndex == 1){
                    return;
                }
                if(team.players[selectIndex] == ApplicationClass.players.get(playerIndex)){
                    return;
                }
                if(team.players[selectIndex].type != 1){
                    team.players[selectIndex].using--;
                    ApplicationClass.saveRePlayer(team.players[selectIndex]);

                }

                imageViews_tear[selectIndex].setColorFilter(Team.tear_color(ApplicationClass.players.get(playerIndex).tear),
                        PorterDuff.Mode.SRC_IN);
                textViews_tear[selectIndex].setText(ApplicationClass.players.get(playerIndex).tear);
                if(ApplicationClass.players.get(playerIndex).most.size() == 0){
                    imageViews[selectIndex].setImageResource(R.drawable.randomchampion);
                }else{
                    imageViews[selectIndex].setImageResource(ApplicationClass.players.get(playerIndex).most.get(0).image);
                }
                textViews[selectIndex].setText(ApplicationClass.players.get(playerIndex).name);
                team.players[selectIndex] = ApplicationClass.players.get(playerIndex);
                team.players[selectIndex].using++;
                ApplicationClass.saveRePlayer(team.players[selectIndex]);
                ApplicationClass.saveReTeam(team);
                playerIndexes[selectIndex] = playerIndex;
                change = true;

            }
        }else if(requestCode == 2){
            //챔피언 선택에서 돌아왔을 때
            if(resultCode == RESULT_OK){
                int championIndex = data.getExtras().getInt("championIndex");
                if(championIndex == -1){
                    return;
                }
                team.most.add(team.most.size()-1, Champion.getChampion(championIndex));
                adapter.mIsClicked.add(false);
                adapter.mIsPicked.add(false);
                adapter.notifyItemInserted(team.most.size()-2);
                ApplicationClass.saveReTeam(team);
                change = true;

            }
        }else if(requestCode == 3){
            //3 사진첩에서 돌아왔을 때
            if(resultCode == RESULT_OK){
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imageView_teamLogo.setImageBitmap(img);
                    team.logo = ApplicationClass.BitmapToString(img);
                    ApplicationClass.saveReTeam(teamIndex);
                    change = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isChange", change);
        intent.putExtra("teamIndex", teamIndex);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
