package com.example.lol_ban_pick_manager;

import android.animation.Animator;
import android.os.AsyncTask;

import java.util.ArrayList;

import io.paperdb.Paper;

public class DataReadWrite extends AsyncTask<Integer, Integer, Integer> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        if(integers[1] == 0){
            //match save 0
            saveMatch(integers[0]);
        } else if(integers[1] == 1){
            //match resave 1
            saveReMatch(integers[0]);
        } else if(integers[1] == 2){
            //match remove 2
            removeMatch(integers[0]);
        } else if(integers[1] == 3){
            //team save 3
            saveTeam(integers[0]);
        } else if(integers[1] == 4){
            //team resave 4
            saveReTeam(integers[0]);
        } else if(integers[1] == 5){
            //team remove 5
            removeTeam(integers[0]);
        } else if(integers[1] == 6){
            //player save 6
            savePlayer(integers[0]);
        } else if(integers[1] == 7){
            //player resave 7
            saveRePlayer(integers[0]);
        } else if(integers[1] == 8){
            //player remove 8
            removePlayer(integers[0]);
        } else{//9
            loadData();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


    public void saveMatch(int matchIndex){
        int index = ApplicationClass.totalMatchNum++;
        String string = "Match" + index;
        Paper.book().write(string, ApplicationClass.matches.get(matchIndex));
        Paper.book().write("totalMatchNum", ApplicationClass.totalMatchNum);
    }
    public void saveReMatch(int matchIndex){
        String string = "Match" + (matchIndex-1);
        Paper.book().write(string, ApplicationClass.matches.get(matchIndex));
    }
    public void removeMatch(int matchIndex){
        ApplicationClass.matches.get(matchIndex).team0.using--;
        saveReTeam(ApplicationClass.matches.get(matchIndex).team0);
        ApplicationClass.matches.get(matchIndex).team1.using--;
        saveReTeam(ApplicationClass.matches.get(matchIndex).team1);
        ApplicationClass.matches.remove(matchIndex);
        Paper.book().delete("Match" + (matchIndex-1));
        for(int i = 1; i < ApplicationClass.matches.size(); i++){
            String string = "Match" + (i-1);
            Paper.book().write(string, ApplicationClass.matches.get(i));
        }
        ApplicationClass.totalMatchNum--;
        Paper.book().write("totalMatchNum", ApplicationClass.totalMatchNum);

    }
    public void saveTeam(int teamIndex){
        int index = ApplicationClass.totalTeamNum++;
        ApplicationClass.teams.get(teamIndex).id = index + 2;
        Paper.book().write("Team" + index, ApplicationClass.teams.get(teamIndex));
        Paper.book().write("totalTeamNum", ApplicationClass.totalTeamNum);
    }
    public void saveReTeam(int teamIndex){
        String string = "Team" + (teamIndex-2);
        ApplicationClass.teams.get(teamIndex).id = teamIndex;
        Paper.book().write(string, ApplicationClass.teams.get(teamIndex));

    }
    public void saveReTeam(Team team){
        for(int i = 2; i < ApplicationClass.teams.size(); i++){
            if(ApplicationClass.teams.get(i) == team){
                String string = "Team" + (i-2);
                Paper.book().write(string, team);
                break;
            }
        }
    }
    public void removeTeam(int teamIndex){
        for(int i = 0; i < 5; i++){
            if(ApplicationClass.teams.get(teamIndex).players[i].type == 1){
                continue;
            };
            ApplicationClass.teams.get(teamIndex).players[i].using--;
            saveRePlayer(ApplicationClass.teams.get(teamIndex).players[i]);
        }
        ApplicationClass.teams.remove(teamIndex);
        Paper.book().delete("Team" + (teamIndex-2));
        for(int i = 2; i < ApplicationClass.teams.size(); i++){
            saveReTeam(i);
        }
        ApplicationClass.totalTeamNum--;
        Paper.book().write("totalTeamNum", ApplicationClass.totalTeamNum);
    }


    public void savePlayer(int playerIndex){
        int index = ApplicationClass.totalPlayerNum++;
        ApplicationClass.players.get(playerIndex).id = index + 2;
        Paper.book().write("Player" + index, ApplicationClass.players.get(playerIndex));
        Paper.book().write("totalPlayerNum", ApplicationClass.totalPlayerNum);
    }
    public  void saveRePlayer(Team.Player player){
        for(int i = 2; i < ApplicationClass.players.size(); i++){
            if(ApplicationClass.players.get(i) == player){
                String string = "Player" + (i-2);
                Paper.book().write(string, player);
                break;
            }
        }
    }
    public void saveRePlayer(int playerIndex){
        String string = "Player" + (playerIndex-2);
        ApplicationClass.players.get(playerIndex).id = playerIndex;
        Paper.book().write(string, ApplicationClass.players.get(playerIndex));

    }
    public void removePlayer(int PlayerIndex){
        ApplicationClass.players.remove(PlayerIndex);//0 1 2
        Paper.book().delete("Player" + (PlayerIndex-2));
        for(int i = 2; i < ApplicationClass.players.size(); i++){
            saveRePlayer(i);
        }
        ApplicationClass.totalPlayerNum--;
        Paper.book().write("totalPlayerNum", ApplicationClass.totalPlayerNum);
    }

    public void loadData(){

        String first = Paper.book().read("first");
        if(first == null){
            ApplicationClass.totalMatchNum = 0;
            ApplicationClass.totalPlayerNum = 0;
            ApplicationClass.totalTeamNum = 0;
            Paper.book().write("totalPlayerNum", ApplicationClass.totalPlayerNum);
            Paper.book().write("totalMatchNum", ApplicationClass.totalMatchNum);
            Paper.book().write("totalTeamNum", ApplicationClass.totalTeamNum);
            Paper.book().write("first", "not first");
        }else{
            ApplicationClass.totalPlayerNum = Paper.book().read("totalPlayerNum");
            ApplicationClass.totalTeamNum = Paper.book().read("totalTeamNum");
            ApplicationClass.totalMatchNum = Paper.book().read("totalMatchNum");

            loadPlayer();
            loadTeam();
            loadMatch();
        }
    }
    public void loadMatch(){
        for(int i = 0; i < ApplicationClass.totalMatchNum; i++){
            Match match = Paper.book().read("Match" + i);
            match.team0 = ApplicationClass.teams.get(match.team0.id);
            match.team1 = ApplicationClass.teams.get(match.team1.id);
            ApplicationClass.matches.add(match);
        }
    }
    public void loadTeam(){
        for(int i = 0; i < ApplicationClass.totalTeamNum; i++){
            Team team = Paper.book().read("Team" + i);
            for(int j = 0; j < 5; j++){
                team.players[j] = ApplicationClass.players.get(team.players[j].id);
            }
            ApplicationClass.teams.add(team);
        }
    }
    public void loadPlayer(){
        for(int i = 0; i < ApplicationClass.totalPlayerNum; i++){
            Team.Player player = Paper.book().read("Player" + i);
            ApplicationClass.players.add(player);
        }
    }


}
