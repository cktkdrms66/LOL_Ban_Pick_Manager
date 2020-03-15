package com.example.lol_ban_pick_manager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;



public class CustomDialog {

    private Context context;
    private OnClickListener mClickListener;

    public CustomDialog(Context context) {
        this.context = context;
    }

    public interface OnClickListener{
        void onFirstClick(View v, int i);
        void onSecondClick(View v, int tear, int i);
    }
    public void setOnClickListener(OnClickListener listener){
        mClickListener = listener;
    }

    //티어 선택
    public void setSelectTear(){
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_tear);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final ImageView[] imageViews = new ImageView[10];
        imageViews[0] = dlg.findViewById(R.id.dialog_tear0);
        imageViews[1] = dlg.findViewById(R.id.dialog_tear1);
        imageViews[2] = dlg.findViewById(R.id.dialog_tear2);
        imageViews[3] = dlg.findViewById(R.id.dialog_tear3);
        imageViews[4] = dlg.findViewById(R.id.dialog_tear4);
        imageViews[5] = dlg.findViewById(R.id.dialog_tear10);
        imageViews[6] = dlg.findViewById(R.id.dialog_tear11);
        imageViews[7] = dlg.findViewById(R.id.dialog_tear12);
        imageViews[8] = dlg.findViewById(R.id.dialog_tear13);
        imageViews[9] = dlg.findViewById(R.id.dialog_tear14);

        final TextView[] textViews = new TextView[10];
        textViews[0] = dlg.findViewById(R.id.dialog_tear_text0);
        textViews[1] = dlg.findViewById(R.id.dialog_tear_text1);
        textViews[2] = dlg.findViewById(R.id.dialog_tear_text2);
        textViews[3] = dlg.findViewById(R.id.dialog_tear_text3);
        textViews[4] = dlg.findViewById(R.id.dialog_tear_text4);
        textViews[5] = dlg.findViewById(R.id.dialog_tear_text5);
        textViews[6] = dlg.findViewById(R.id.dialog_tear_text6);
        textViews[7] = dlg.findViewById(R.id.dialog_tear_text7);
        textViews[8] = dlg.findViewById(R.id.dialog_tear_text8);
        textViews[9] = dlg.findViewById(R.id.dialog_tear_text9);


        //10개 티어의 색깔과 클릭 이벤트를 설정한다.
        for(int i = 0 ; i < 10; i++){
            imageViews[i].setColorFilter(Team.tear_color(textViews[i].getText().toString()), PorterDuff.Mode.SRC_IN);
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                    int i = ApplicationClass.findIndexInImageView(view, imageViews);
                    if(0 < i && i < 7){
                        //브론즈부터 다이아까지는 세부 티어 다이얼로그를 보여준다
                        CustomDialog customDialog = new CustomDialog(context);
                        customDialog.setSelectTearDetail(i, mClickListener);
                    }else{
                        //언랭, 마스터, 그마, 챌린저는 닫는다
                        if(mClickListener != null){
                            mClickListener.onFirstClick(view, i);
                        }

                    }

                }
            });
        }


    }
    //티어 세부 선택
    public void setSelectTearDetail(final int tear, OnClickListener listener){
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_tear_level);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final ImageView[] imageViews = new ImageView[4];
        imageViews[0] = dlg.findViewById(R.id.dialog_tear0);
        imageViews[1] = dlg.findViewById(R.id.dialog_tear1);
        imageViews[2] = dlg.findViewById(R.id.dialog_tear2);
        imageViews[3] = dlg.findViewById(R.id.dialog_tear3);

        final TextView[] textViews = new TextView[4];
        textViews[0] = dlg.findViewById(R.id.dialog_tear_text0);
        textViews[1] = dlg.findViewById(R.id.dialog_tear_text1);
        textViews[2] = dlg.findViewById(R.id.dialog_tear_text2);
        textViews[3] = dlg.findViewById(R.id.dialog_tear_text3);

        setOnClickListener(listener);

        for(int i = 0 ; i < 4; i++){
            imageViews[i].setColorFilter(Team.tear_color(tear), PorterDuff.Mode.SRC_IN);
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickListener != null){
                        int i = ApplicationClass.findIndexInImageView(view, imageViews);
                        mClickListener.onSecondClick(view, tear, i);
                        dlg.dismiss();
                    }

                }
            });
        }

    }
    
    //게임 디테일 보기
    public void setLookGameDetail(int matchIndex, int gameIndex){
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_popup_game_more_detail);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        ImageView[] imageViews_team0 = new ImageView[5];
        ImageView[] imageViews_team1 = new ImageView[5];
        TextView textView_team0;
        TextView textView_team1;
        ImageView[] imageViews_ban0 = new ImageView[5];
        ImageView[] imageViews_ban1 = new ImageView[5];
        imageViews_team0[0] = dlg.findViewById(R.id.game_imageView10);
        imageViews_team0[1] = dlg.findViewById(R.id.game_imageView11);
        imageViews_team0[2] = dlg.findViewById(R.id.game_imageView12);
        imageViews_team0[3] = dlg.findViewById(R.id.game_imageView13);
        imageViews_team0[4] = dlg.findViewById(R.id.game_imageView14);

        imageViews_team1[0] = dlg.findViewById(R.id.game_imageView15);
        imageViews_team1[1] = dlg.findViewById(R.id.game_imageView16);
        imageViews_team1[2] = dlg.findViewById(R.id.game_imageView17);
        imageViews_team1[3] = dlg.findViewById(R.id.game_imageView18);
        imageViews_team1[4] = dlg.findViewById(R.id.game_imageView19);

        imageViews_ban0[0] = dlg.findViewById(R.id.game_imageView0);
        imageViews_ban0[1] = dlg.findViewById(R.id.game_imageView1);
        imageViews_ban0[2] = dlg.findViewById(R.id.game_imageView2);
        imageViews_ban0[3] = dlg.findViewById(R.id.game_imageView3);
        imageViews_ban0[4] = dlg.findViewById(R.id.game_imageView4);

        imageViews_ban1[0] = dlg.findViewById(R.id.game_imageView5);
        imageViews_ban1[1] = dlg.findViewById(R.id.game_imageView6);
        imageViews_ban1[2] = dlg.findViewById(R.id.game_imageView7);
        imageViews_ban1[3] = dlg.findViewById(R.id.game_imageView8);
        imageViews_ban1[4] = dlg.findViewById(R.id.game_imageView9);
        
        textView_team0 = dlg.findViewById(R.id.textView);
        textView_team1 = dlg.findViewById(R.id.textView2);

        //
        Match match = ApplicationClass.matches.get(matchIndex);
        //팀이름을 설정한다
        textView_team0.setText(ApplicationClass.matches.get(matchIndex).team0.name);
        textView_team1.setText(ApplicationClass.matches.get(matchIndex).team1.name);
        
        //만약 아군이 레드팀이면 색을 바꾼다
        if(!ApplicationClass.matches.get(matchIndex).isTeam0Blue){
            textView_team0.setBackgroundColor(0x2196F3);
            textView_team1.setBackgroundColor(0xF44336);
        }


        Match.SwapPhaseClass swapPhaseClass = (Match.SwapPhaseClass) match.games.get(gameIndex).gameElements.get(20);
        Match.Game game = match.games.get(gameIndex);
        for(int i = 0; i < 5; i++){
            imageViews_team0[i].setImageBitmap(ApplicationClass.StringToBitmap(swapPhaseClass.bitmapsNew0[i]));
            imageViews_team1[i].setImageBitmap(ApplicationClass.StringToBitmap(swapPhaseClass.bitmapsNew1[i]));
        }
        if(match.isTeam0Blue){
            imageViews_ban0[0].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(0)).championIndex));
            imageViews_ban0[1].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(2)).championIndex));
            imageViews_ban0[2].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(4)).championIndex));
            imageViews_ban0[3].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(13)).championIndex));
            imageViews_ban0[4].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(15)).championIndex));
            
            imageViews_ban1[0].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(1)).championIndex));
            imageViews_ban1[1].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(3)).championIndex));
            imageViews_ban1[2].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(5)).championIndex));
            imageViews_ban1[3].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(12)).championIndex));
            imageViews_ban1[4].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(14)).championIndex));
            
        }else{
            imageViews_ban1[0].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(0)).championIndex));
            imageViews_ban1[1].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(2)).championIndex));
            imageViews_ban1[2].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(4)).championIndex));
            imageViews_ban1[3].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(13)).championIndex));
            imageViews_ban1[4].setImageResource(Champion.getChampionImage(((Match.PickClass) game.gameElements.get(15)).championIndex));

            imageViews_ban0[0].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(1)).championIndex));
            imageViews_ban0[1].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(3)).championIndex));
            imageViews_ban0[2].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(5)).championIndex));
            imageViews_ban0[3].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(12)).championIndex));
            imageViews_ban0[4].setImageResource(Champion.getChampionImage(((Match.PickClass)game.gameElements.get(14)).championIndex));

        }
        
    }




}
