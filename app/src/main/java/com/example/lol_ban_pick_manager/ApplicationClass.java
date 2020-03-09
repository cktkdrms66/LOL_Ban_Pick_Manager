package com.example.lol_ban_pick_manager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.TreeSet;

import io.paperdb.Paper;


public class ApplicationClass extends Application {

    //데이터들의 개수
    static int totalMatchNum = 0;
    static int totalTeamNum = 0;
    static int totalPlayerNum = 0;

    static ArrayList<Champion> champions = new ArrayList<>();
    static ArrayList<Team> teams = new ArrayList<>();
    static ArrayList<Match> matches = new ArrayList<>();
    static ArrayList<Team.Player> players = new ArrayList<>();

    static boolean isNeedToSetting = true;

    static DataReadWrite task;

    @Override
    public void onCreate() {
        super.onCreate();
        Champion.championSetting();
        Paper.init(this);
        //Paper.book().destroy();

        //플러스 추가
        Team.Player.makePlus();
        Team.makePlus();
        Match.makePlus();

        //디폴트 플레이어, 팀 추가
        Team.Player.makeDefaultPlayer();
        Team.makeDefaultTeam();

        //데이터 로드
        loadData();
    }


    //데이터 저장 불러오기 함수들, (저장, 삭제, 다시 저장)

    //매치
    public static void saveMatch(int matchIndex){
        task = new DataReadWrite();
        task.execute(matchIndex, 0);
    }
    public static void saveReMatch(int matchIndex){
        task = new DataReadWrite();
        task.execute(matchIndex, 1);
    }
    public static void removeMatch(int matchIndex){
        task = new DataReadWrite();
        task.execute(matchIndex, 2);
    }
    //매치에 게임을 추가하고 다시 저장
    public static void addGame(int matchIndex, Match.Game game){
        ApplicationClass.matches.get(matchIndex).games.add(game);
        ApplicationClass.matches.get(matchIndex).gameNum++;
        saveReMatch(matchIndex);
    }

    //팀
    public static void saveTeam(int teamIndex){
        task = new DataReadWrite();
        task.execute(teamIndex, 3);
    }
    public static void saveReTeam(Team team){
        for(int i = 0; i < ApplicationClass.teams.size(); i++){
            if(team == ApplicationClass.teams.get(i)){
                saveReTeam(i);
                return;
            }
        }
    }
    public static void saveReTeam(int teamIndex){
        task = new DataReadWrite();
        task.execute(teamIndex, 4);
    }
    public static void removeTeam(int teamIndex){
        task = new DataReadWrite();
        task.execute(teamIndex, 5);
    }

    //플레이어
    public static void savePlayer(int playerIndex){
        task = new DataReadWrite();
        task.execute(playerIndex, 6);
    }
    public static void saveRePlayer(Team.Player player){
        for(int i =0 ; i < players.size(); i++){
            if(player == players.get(i)){
                saveRePlayer(i);
                return;
            }
        }
    }
    public static void saveRePlayer(int playerIndex){
        task = new DataReadWrite();
        task.execute(playerIndex, 7);
    }
    public static void removePlayer(int playerIndex){
        task = new DataReadWrite();
        task.execute(playerIndex, 8);
    }

    //데이터 로드
    public static void loadData(){
        task = new DataReadWrite();
        task.execute(0, 9);
    }


    //토스트 띄우기 함수
    static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    //여러 이미지뷰 중에서 해당 인덱스 찾기
    static int findIndexInImageView(View view, ImageView[] imageViews){
        int i = 0;
        ImageView nowView = (ImageView)view;
        for(ImageView imageView : imageViews){
            if(nowView == imageView ){
                break;
            }
            i++;
        }
        return i;
    }

    //스트링을 비트맵으로 변환시켜주는 함수
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage(); return null;
        }
    }
    //비트맵을 스트링으로 변환시켜주는 함수
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

}
