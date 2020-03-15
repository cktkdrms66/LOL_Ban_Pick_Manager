package com.example.lol_ban_pick_manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentTeamActivity extends Fragment {

    RecyclerView recyclerView;
    ImageView imageView_setting;
    TeamAdapter adapter;

    static int posIndex;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team,container,false);

        imageView_setting = view.findViewById(R.id.team_imageView_setting);
        recyclerView = view.findViewById(R.id.team_recyclerView);


        imageView_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationClass.dialogHelp0Team(getActivity());
            }
        });


        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        adapter = new TeamAdapter(ApplicationClass.teams);
        recyclerView.setAdapter(adapter);

        //팀 클릭 시, 플러스이면 팀메이크팝업을, 아니면 팀디테일로 이동한다.
        adapter.setOnItemClickListener(new TeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if(ApplicationClass.teams.get(pos).type == 0){
                    Intent intent = new Intent(getContext(), PopupMakeTeamActivity.class);
                    startActivityForResult(intent, 0);
                }else{
                    if(pos == 1){
                        //기본팀이면 볼 수 없다.
                        return;
                    }
                    Intent intent = new Intent(getContext(), TeamDetailActivity.class);
                    intent.putExtra("teamIndex", pos);
                    startActivityForResult(intent, 1);
                }
            }
        });

        //팀 길게 누를 시, 삭제를 묻는다. 플러스나 기본팀은 해당사항없다.
        adapter.setOnLongClickListener(new TeamAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View v, int pos) {
                if(ApplicationClass.teams.get(pos).type == 0 || pos == 1){
                    //플러스나 기본팀일 경우 삭제할 수 없다.
                    return;
                }else{
                    posIndex = pos;
                    new AlertDialog.Builder(getContext()).setMessage("삭제하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(ApplicationClass.teams.get(posIndex).using > 0){
                                        ApplicationClass.showToast(getContext(),
                                                "이 팀을 포함한 매치가 있어 삭제할 수 없습니다.");
                                        return;
                                    }
                                    ApplicationClass.removeTeam(posIndex);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            }).show();
                }
            }
        });

        return  view;
    }

    //메이크팝업이나 팀디테일갔다가 다시 돌아왔을 때 리사이클러뷰 갱신
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode == -1){
                int isMake = data.getExtras().getInt("isMake");
                if(isMake == 1){
                    adapter.notifyItemInserted(ApplicationClass.teams.size()-1);
                }
            }
        }
        if(requestCode==1){
            if(resultCode == -1){
                boolean isChange = data.getExtras().getBoolean("isChange");
                if(isChange){
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    //리사이클러뷰 버그 해결을 위해 임시로 만든 클래스
    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {

            }
        }
    }
}
