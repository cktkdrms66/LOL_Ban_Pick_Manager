package com.example.lol_ban_pick_manager;

import java.io.Serializable;
import java.util.ArrayList;

public class Champion implements Serializable {
    int id;
    String name;
    int image;
    boolean isChampion = true;


    public Champion(int position){
        if(position == 0){
            name = "아무 챔피언";
            image = R.drawable.randomchampion;
        }else if(position == 1){
            name = "탑 챔피언";
            image = R.drawable.position_top;
        }else if(position == 2){
            name = "정글 챔피언";
            image = R.drawable.position_jg;
        }else if(position == 3){
            name = "미드 챔피언";
            image = R.drawable.position_mid;
        }else if(position == 4){
            name = "바텀 챔피언";
            image = R.drawable.position_bot;
        }else if(position == 5){
            name = "서포터 챔피언";
            image = R.drawable.position_sup;
        }
        isChampion = false;
    }
    public Champion(String name, int image, int id){
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public static Champion makePlus(){
        Champion champion = new Champion("추가", R.drawable.plus, -1);
        champion.isChampion = false;
        return champion;
    }

    public static int getChampionIndex(String name){
        int i;
        for(i = 0; i < ApplicationClass.champions.size(); i++){
            Champion champion = ApplicationClass.champions.get(i);
            if(champion.name == name){
                System.out.println(name);
                return i;
            }
        }
        System.out.println("no champ");
        return -1;
    }

    public static int getChampionImage(int pos){
        if(pos == -1){
            return -1;
        }
        Champion champion = ApplicationClass.champions.get(pos);
        return champion.image;
    }
    public static Champion getChampion(int pos){
        Champion champion = ApplicationClass.champions.get(pos);
        return new Champion(champion.name, champion.image, pos);
    }
    public static Champion getChampion(String name){
        for(int i = 0; i < ApplicationClass.champions.size(); i++){
            Champion champion = ApplicationClass.champions.get(i);
            if(champion.name == name){
                return new Champion(champion.name, champion.image, i);
            }
        }
        System.out.println("null champ");
        return null;
    }

    public static void championSetting(){
        int index = 0;
        ArrayList<Champion> champions = ApplicationClass.champions;
        champions.add(new Champion("가렌", R.drawable.garen, index++));
        champions.add(new Champion("갈리오", R.drawable.galio, index++));
        champions.add(new Champion("갱플랭크", R.drawable.gangplank, index++));
        champions.add(new Champion("그라가스", R.drawable.gragas, index++));
        champions.add(new Champion("그레이브즈", R.drawable.graves, index++));
        champions.add(new Champion("나르", R.drawable.gnar, index++));
        champions.add(new Champion("나미", R.drawable.nami, index++));
        champions.add(new Champion("나서스", R.drawable.nasus, index++));
        champions.add(new Champion("노틸러스", R.drawable.nautilus, index++));
        champions.add(new Champion("녹턴", R.drawable.noctume, index++));
        champions.add(new Champion("누누와 윌럼프", R.drawable.nunu, index++));
        champions.add(new Champion("니달리", R.drawable.nidalee, index++));
        champions.add(new Champion("니코", R.drawable.neeko, index++));
        champions.add(new Champion("다리우스", R.drawable.darius, index++));
        champions.add(new Champion("다이애나", R.drawable.diana, index++));
        champions.add(new Champion("드레이븐", R.drawable.draven, index++));
        champions.add(new Champion("라이즈", R.drawable.ryze, index++));
        champions.add(new Champion("라칸", R.drawable.rakan, index++));
        champions.add(new Champion("람머스", R.drawable.rammus, index++));
        champions.add(new Champion("럭스", R.drawable.lux, index++));
        champions.add(new Champion("럼블", R.drawable.rumble, index++));
        champions.add(new Champion("레넥톤", R.drawable.reneton, index++));
        champions.add(new Champion("레오나", R.drawable.leona, index++));
        champions.add(new Champion("렉사이", R.drawable.reksai, index++));
        champions.add(new Champion("렝가", R.drawable.rengar, index++));
        champions.add(new Champion("루시안", R.drawable.lucian, index++));
        champions.add(new Champion("룰루", R.drawable.lulu, index++));
        champions.add(new Champion("르블랑", R.drawable.leblanc, index++));
        champions.add(new Champion("리신", R.drawable.leesin, index++));
        champions.add(new Champion("리븐", R.drawable.riven, index++));
        champions.add(new Champion("리산드라", R.drawable.lissandra, index++));
        champions.add(new Champion("마스터이", R.drawable.masteryi, index++));
        champions.add(new Champion("마오카이", R.drawable.maokai, index++));
        champions.add(new Champion("말자하", R.drawable.malzahar, index++));
        champions.add(new Champion("말파이트", R.drawable.malphite, index++));
        champions.add(new Champion("모데카이저", R.drawable.mordekaiser, index++));
        champions.add(new Champion("모르가나", R.drawable.morgana, index++));
        champions.add(new Champion("문도 박사", R.drawable.mundo, index++));
        champions.add(new Champion("미스 포츈", R.drawable.missfortune, index++));
        champions.add(new Champion("바드", R.drawable.bard, index++));
        champions.add(new Champion("바루스", R.drawable.varus, index++));
        champions.add(new Champion("바이", R.drawable.vi, index++));
        champions.add(new Champion("베이가", R.drawable.veigar, index++));
        champions.add(new Champion("베인", R.drawable.vayne, index++));
        champions.add(new Champion("벨코즈", R.drawable.velkoz, index++));
        champions.add(new Champion("볼리베어", R.drawable.volibear, index++));
        champions.add(new Champion("브라움", R.drawable.braum, index++));
        champions.add(new Champion("브랜드", R.drawable.brand, index++));
        champions.add(new Champion("블라디미르", R.drawable.vladimir, index++));
        champions.add(new Champion("블리츠크랭크", R.drawable.blitzcrank, index++));
        champions.add(new Champion("빅토르", R.drawable.viktor, index++));
        champions.add(new Champion("뽀삐", R.drawable.poppy, index++));
        champions.add(new Champion("사이온", R.drawable.sion, index++));
        champions.add(new Champion("사일러스", R.drawable.sylas, index++));
        champions.add(new Champion("샤코", R.drawable.shaco, index++));
        champions.add(new Champion("세나", R.drawable.senna, index++));
        champions.add(new Champion("세주아니", R.drawable.sejuani, index++));
        champions.add(new Champion("세트", R.drawable.sett, index++));
        champions.add(new Champion("소나", R.drawable.sona, index++));
        champions.add(new Champion("소라카", R.drawable.soraka, index++));
        champions.add(new Champion("쉔", R.drawable.shen, index++));
        champions.add(new Champion("쉬바나", R.drawable.shyvana, index++));
        champions.add(new Champion("스웨인", R.drawable.swain, index++));
        champions.add(new Champion("스카너", R.drawable.skarner, index++));
        champions.add(new Champion("시비르", R.drawable.sivir, index++));
        champions.add(new Champion("신짜오", R.drawable.xinzhao, index++));
        champions.add(new Champion("신드라", R.drawable.sindra, index++));
        champions.add(new Champion("신지드", R.drawable.singed, index++));
        champions.add(new Champion("쓰레쉬", R.drawable.thresh, index++));
        champions.add(new Champion("아리", R.drawable.ahri, index++));
        champions.add(new Champion("아무무", R.drawable.amumu, index++));
        champions.add(new Champion("아우렐리온 솔", R.drawable.aurelion_sol, index++));
        champions.add(new Champion("아이번", R.drawable.ivern, index++));
        champions.add(new Champion("아지르", R.drawable.azir, index++));
        champions.add(new Champion("아칼리", R.drawable.akali, index++));
        champions.add(new Champion("아트록스", R.drawable.aatrox, index++));
        champions.add(new Champion("아펠리오스", R.drawable.aphelios, index++));
        champions.add(new Champion("알리스타", R.drawable.alistar, index++));
        champions.add(new Champion("애니", R.drawable.ani, index++));
        champions.add(new Champion("애니비아", R.drawable.anibia, index++));
        champions.add(new Champion("애쉬", R.drawable.ash, index++));
        champions.add(new Champion("야스오", R.drawable.yasuo, index++));
        champions.add(new Champion("에코", R.drawable.eco, index++));
        champions.add(new Champion("엘리스", R.drawable.elise, index++));
        champions.add(new Champion("오공", R.drawable.wukong, index++));
        champions.add(new Champion("오른", R.drawable.ornn, index++));
        champions.add(new Champion("오리아나", R.drawable.orianna, index++));
        champions.add(new Champion("올라프", R.drawable.olaf, index++));
        champions.add(new Champion("요릭", R.drawable.yorick, index++));
        champions.add(new Champion("우디르", R.drawable.udyr, index++));
        champions.add(new Champion("우르곳", R.drawable.urgot, index++));
        champions.add(new Champion("워윅", R.drawable.warwick, index++));
        champions.add(new Champion("유미", R.drawable.yuumi, index++));
        champions.add(new Champion("이렐리아", R.drawable.irelia, index++));
        champions.add(new Champion("이블린", R.drawable.evelynn, index++));
        champions.add(new Champion("이즈리얼", R.drawable.ezreal, index++));
        champions.add(new Champion("일라오이", R.drawable.illaoi, index++));
        champions.add(new Champion("자르반 4세", R.drawable.jarvaniv, index++));
        champions.add(new Champion("자야", R.drawable.xayah, index++));
        champions.add(new Champion("자이라", R.drawable.zyra, index++));
        champions.add(new Champion("자크", R.drawable.zac, index++));
        champions.add(new Champion("잔나", R.drawable.janna, index++));
        champions.add(new Champion("잭스", R.drawable.jax, index++));
        champions.add(new Champion("제드", R.drawable.zed, index++));
        champions.add(new Champion("제라스", R.drawable.xerath, index++));
        champions.add(new Champion("제이스", R.drawable.jayce, index++));
        champions.add(new Champion("조이", R.drawable.zoe, index++));
        champions.add(new Champion("직스", R.drawable.ziggs, index++));
        champions.add(new Champion("진", R.drawable.jhin, index++));
        champions.add(new Champion("질리언", R.drawable.zilean, index++));
        champions.add(new Champion("징크스", R.drawable.jinx, index++));
        champions.add(new Champion("초가스", R.drawable.chogath, index++));
        champions.add(new Champion("카르마", R.drawable.karma, index++));
        champions.add(new Champion("카밀", R.drawable.camile, index++));
        champions.add(new Champion("카사딘", R.drawable.kassadin, index++));
        champions.add(new Champion("카서스", R.drawable.karthus, index++));
        champions.add(new Champion("카시오페아", R.drawable.cassiopeia, index++));
        champions.add(new Champion("카이사", R.drawable.kaisa, index++));
        champions.add(new Champion("카직스", R.drawable.khazix, index++));
        champions.add(new Champion("카타리나", R.drawable.katarina, index++));
        champions.add(new Champion("칼리스타", R.drawable.kalista, index++));
        champions.add(new Champion("케넨", R.drawable.kennen, index++));
        champions.add(new Champion("케이틀린", R.drawable.caitlyn, index++));
        champions.add(new Champion("케인", R.drawable.kayn, index++));
        champions.add(new Champion("케일", R.drawable.kayle, index++));
        champions.add(new Champion("코그모", R.drawable.kogmaw, index++));
        champions.add(new Champion("코르키", R.drawable.corki, index++));
        champions.add(new Champion("퀸", R.drawable.queen, index++));
        champions.add(new Champion("클레드", R.drawable.kled, index++));
        champions.add(new Champion("키아나", R.drawable.qiyana, index++));
        champions.add(new Champion("킨드레드", R.drawable.kindred, index++));
        champions.add(new Champion("타릭", R.drawable.taric, index++));
        champions.add(new Champion("탈론", R.drawable.talon, index++));
        champions.add(new Champion("탈리야", R.drawable.taliyah, index++));
        champions.add(new Champion("탐 켄치", R.drawable.tahmkench, index++));
        champions.add(new Champion("트런들", R.drawable.trundle, index++));
        champions.add(new Champion("트리스타나", R.drawable.tristana, index++));
        champions.add(new Champion("트린다미어", R.drawable.tryndamere, index++));
        champions.add(new Champion("트위스티드 페이트", R.drawable.twistedpate, index++));
        champions.add(new Champion("트위치", R.drawable.twitch, index++));
        champions.add(new Champion("티모", R.drawable.teemo, index++));
        champions.add(new Champion("파이크", R.drawable.pyke, index++));
        champions.add(new Champion("판테온", R.drawable.pantheon, index++));
        champions.add(new Champion("피들스틱", R.drawable.fiddlestick, index++));
        champions.add(new Champion("피오라", R.drawable.fiora, index++));
        champions.add(new Champion("피즈", R.drawable.fizz, index++));
        champions.add(new Champion("하이머딩거", R.drawable.heimerdinger, index++));
        champions.add(new Champion("헤카림", R.drawable.hecarim, index++));

    }

}
