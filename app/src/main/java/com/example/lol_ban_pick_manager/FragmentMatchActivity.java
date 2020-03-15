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



public class FragmentMatchActivity extends Fragment {
    private RecyclerView recyclerView;
    private ImageView imageView_setting;

    static int posIndex;
    MatchAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match,container,false);

        imageView_setting = view.findViewById(R.id.match_imageView_setting);
        recyclerView = view.findViewById(R.id.team_recyclerView);


        imageView_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationClass.dialogHelp0Match(getActivity());
            }
        });


        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        adapter = new MatchAdapter(ApplicationClass.matches);
        recyclerView.setAdapter(adapter);

        //매치 클릭 시, 플러스이면 매치메이크팝업으로, 그 외는 게임의 리스트를 보여준다.
        adapter.setOnItemClickListener(new MatchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent;
                if(ApplicationClass.matches.get(pos).type == 0){
                    intent = new Intent(getContext(), PopupMakeMatchActivity.class);
                }else{
                    intent = new Intent(getContext(), PopupGameActivity.class);
                    intent.putExtra("matchIndex", pos);
                }
                startActivity(intent);
            }
        });

        //길게 누를 시, 해당 매치를 삭제한다.
        adapter.setOnLongClickListener(new MatchAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View v, int pos) {
                posIndex = pos;
                if(posIndex == 0){
                    return;
                }
                new AlertDialog.Builder(getContext()).setMessage("삭제하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ApplicationClass.removeMatch(posIndex);
                                adapter.notifyItemRemoved(posIndex);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .show();
            }
        });

        return  view;
    }

    //임시 클래스
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
