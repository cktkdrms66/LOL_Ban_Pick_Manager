package com.example.lol_ban_pick_manager;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


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
    //여러 텍스트뷰 중에서 해당 인덱스 찾기
    static int findIndexInImageView(View view, TextView[] textViews){
        int i = 0;
        TextView nowView = (TextView)view;
        for(TextView TextView : textViews){
            if(nowView == TextView ){
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
        if(bitmap == null){
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

    //도움말
    public static void dialogHelp0Match(final Context context){
        new AlertDialog.Builder(context).setTitle("도움말")
                .setMessage("두 개의 팀을 이뤄 매치를 만들 수 있습니다. " +
                        "해당 매치를 눌러 밴픽을 추가하거나, 다른 밴픽 게임을 다시 볼 수 있습니다." +
                        " 만약 해당 매치를 삭제하고 싶다면 길게 누르세요.")
                .setNeutralButton("다음", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogHelp1Match(context);
                    }
                }).show();
    }

    public static void dialogHelp1Match(Context context){
        new AlertDialog.Builder(context).setTitle("도움말")
                .setMessage("밴픽으로 들어간다면, 팀 로고를 눌러 해당 팀의 핵심 챔피언, 플레이어 정보를 볼 수 있습니다." +
                        " 만약 플레이어에 대한 더 많은 정보를 얻고 싶다면 플레이어를 누르세요.")
                .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).show();
    }
    public static void dialogHelp0Team(Context context){
        new AlertDialog.Builder(context).setTitle("도움말")
                .setMessage("플레이어 5명을 모아 팀을 만들 수 있습니다. 만든 팀은 짧게 눌러 언제든지" +
                        " 수정이 가능합니다. 만약 삭제하고 싶다면 해당 팀을 길게 누르세요.")
                .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).show();
    }
    public static void dialogHelp0Player(Context context){
        new AlertDialog.Builder(context).setTitle("도움말")
                .setMessage("플레이어를 만들 수 있습니다. 모스트 챔피언과 티어를 설정하세요. 물론 언제든지 " +
                        "수정이 가능합니다. 만약 삭제하고 싶다면 해당 플레이어를 길게 누르세요.")
                .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).show();
    }

}
