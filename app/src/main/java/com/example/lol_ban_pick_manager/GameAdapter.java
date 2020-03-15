package com.example.lol_ban_pick_manager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Match.Game> mItems;
    ArrayList<Boolean> mIsClicked = new ArrayList<>();
    private int mOnlyItemPosition = -1;
    private OnItemClickListener mListener = null;
    private OnLongClickListener mLongListener = null;
    public interface OnItemClickListener{
        void onItemClick(View v);
    }
    public interface OnLongClickListener{
        void onLongClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public void setOnLongClickListener(OnLongClickListener listener){
        mLongListener = listener;
    }

    public void setOnlyClick(int pos, boolean isClick){
        if(isClick){
            if(mOnlyItemPosition != -1){
                mIsClicked.set(mOnlyItemPosition, false);
            }
            mOnlyItemPosition = pos;
            notifyDataSetChanged();
        }else{
            mOnlyItemPosition = -1;
        }
    }
    public int getmOnlyItemPosition(){
        return mOnlyItemPosition;
    }

    public Match.Game getGame(){
        if(mOnlyItemPosition == -1){
            return mItems.get(1);
        }
        return mItems.get(mOnlyItemPosition);
    }

    public class GameViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        TextView textView_name;
        TextView textView;
        ImageView imageView;
        TextView textView_back;
        ImageView[] imageViews = new ImageView[5];
        ImageView imageView_plus;
        public GameViewHolder(View view){
            super(view);
            textView_back = view.findViewById(R.id.game_back);
            imageView_plus = view.findViewById(R.id.game_plus);
            textView = view.findViewById(R.id.game_textView);
            constraintLayout = view.findViewById(R.id.cardView_game_layout);
            textView_name = view.findViewById(R.id.game_title);
            imageView = view.findViewById(R.id.game_logo);
            imageViews[0] = view.findViewById(R.id.game_star0);
            imageViews[1] = view.findViewById(R.id.game_star1);
            imageViews[2] = view.findViewById(R.id.game_star2);
            imageViews[3] = view.findViewById(R.id.game_star3);
            imageViews[4] = view.findViewById(R.id.game_star4);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(pos == 0) {
                            if (mListener != null) {
                                mListener.onItemClick(view);
                            }
                        }else{
                            if(mIsClicked.get(pos)){
                                mIsClicked.set(pos, false);
                                setOnlyClick(pos, false);
                                constraintLayout.setBackgroundColor(0xFFFFFFFF);

                            }else{
                                mIsClicked.set(pos, true);
                                setOnlyClick(pos, true);
                                constraintLayout.setBackgroundColor(0x69696969);

                            }
                        }

                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getAdapterPosition();
                    if(mLongListener != null){
                        mLongListener.onLongClick(view, pos);
                    }
                    return true;
                }
            });
        }
    }

    public GameAdapter(ArrayList<Match.Game> mItems){
        this.mItems = mItems;
        for(int i = 0; i < mItems.size(); i++){
            mIsClicked.add(false);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_game, parent, false);
        return new GameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GameViewHolder new_holder = (GameViewHolder) holder;
        if(mItems.get(position).type == 0){
            new_holder.textView.setVisibility(View.INVISIBLE);
            new_holder.imageView_plus.setImageResource(R.drawable.plus);
            new_holder.textView_back.setVisibility(View.INVISIBLE);
            for(int i = 0; i <5; i++){
                new_holder.imageViews[i].setVisibility(View.INVISIBLE);
            }

        }
        if(mItems.get(position).victoryTeamIndex == 0){
            new_holder.textView.setText("설정 안함");
        }else{
            new_holder.textView.setText(ApplicationClass.teams.get(mItems.get(position).victoryTeamIndex).name);
        }
        if(mItems.get(position).victoryTeamColor == 0){
            new_holder.textView.setTextColor(0xFF2196F3);
        }else if(mItems.get(position).victoryTeamColor == 1){
            new_holder.textView.setTextColor(0xFFF44336);
        }

        new_holder.textView_name.setText(mItems.get(position).name);

        new_holder.imageView.setImageBitmap(ApplicationClass.StringToBitmap(ApplicationClass.teams.get(mItems.get(position).victoryTeamIndex).logo));

        int size = mItems.get(position).star;
        for(int i = 0; i < size; i++){
            new_holder.imageViews[i].setColorFilter(0xFFFFB503);
        }

        if(mItems.get(position).type != 0){
            if(mIsClicked.get(position)){
                new_holder.constraintLayout.setBackgroundColor(0x69696969);
            }else{
                new_holder.constraintLayout.setBackgroundColor(0xFFFFFFFF);
            }
        }



    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
