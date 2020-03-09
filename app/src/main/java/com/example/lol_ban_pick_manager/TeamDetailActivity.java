package com.example.lol_ban_pick_manager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


        final Intent intent = getIntent();
        teamIndex = intent.getExtras().getInt("teamIndex");
        team = ApplicationClass.teams.get(teamIndex);

        if(team.logo == null){
            imageView_teamLogo.setImageResource(R.drawable.no);
        }else{
            imageView_teamLogo.setImageBitmap(ApplicationClass.StringToBitmap(team.logo));
        }
        textView_teamName.setText(team.name);

        for(int i = 0; i < 5; i++){
            imageViews_tear[i].setColorFilter(Team.tear_color(team.players[i].tear), PorterDuff.Mode.SRC_IN);
            textViews_tear[i].setText(team.players[i].tear);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ChampionAdapter adapter = new ChampionAdapter(this, team.most);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ChampionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, ImageView imageView) {
                return;
            }
        });

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
                change = true;

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
