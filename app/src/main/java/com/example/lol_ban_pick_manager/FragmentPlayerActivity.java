package com.example.lol_ban_pick_manager;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentPlayerActivity extends Fragment {

    RecyclerView recyclerView;
    ImageView imageView_setting;
    PlayerAdapter adapter;

    static int posIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player,container,false);

        imageView_setting = view.findViewById(R.id.player_imageView_setting);
        recyclerView = view.findViewById(R.id.player_recyclerView);

        imageView_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationClass.dialogHelp0Player(getActivity());
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapter = new PlayerAdapter(ApplicationClass.players);
        recyclerView.setAdapter(adapter);

        //플레이어 클릭 시, 플러스면 플레이어메이크팝업으로, 그 외엔 디테일로 간다.
        adapter.setOnItemClickListener(new PlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if(ApplicationClass.players.get(pos).type == 0){
                    Intent intent = new Intent(getContext(), PopupMakePlayerActivity.class);
                    startActivityForResult(intent, 1);
                } else{
                    if(pos == 1){
                        //기본 플레이어는 아무것도 안한다
                        return;
                    }
                    int playerIndex = pos;
                    Intent intent = new Intent(getContext(), PlayerDetailActivity.class);
                    intent.putExtra("where", 0);
                    intent.putExtra("playerIndex", playerIndex);
                    startActivityForResult(intent,0);
                }
            }
        });

        //길게 클릭 시 삭제한다
        adapter.setOnLongItemClickListener(new PlayerAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View v, int pos) {

                if(ApplicationClass.players.get(pos).type == 0 || pos == 1){
                    //플러스거나 기본 플레이어면 아무것도 안한다
                    return;

                }else{
                    posIndex = pos;
                    new AlertDialog.Builder(getContext()).setMessage("삭제하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(ApplicationClass.players.get(posIndex).using > 0){
                                        ApplicationClass.showToast(getContext(),
                                                "이 플레이어로 이루어진 팀이 있어 삭제할 수 없습니다.");
                                        return;

                                    }
                                    ApplicationClass.removePlayer(posIndex);
                                    adapter.notifyItemRemoved(posIndex);
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

    //다시 돌아왔을 때
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //0 데이터변경 1 추가
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode == -1){
                Boolean isChange = data.getExtras().getBoolean("isChange");
                if(isChange){
                    adapter.notifyDataSetChanged();
                }

            }
        }else if(requestCode == 1){
            if(resultCode == -1){
                Boolean isChange = data.getExtras().getBoolean("isChange");
                if(isChange){
                    adapter.notifyItemInserted(ApplicationClass.players.size()-1);
                }
            }
        }
    }
}
