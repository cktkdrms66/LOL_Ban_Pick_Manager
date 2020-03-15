package com.example.lol_ban_pick_manager;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Team> mItems;
    private OnItemClickListener mListener = null;
    private OnLongClickListener mLongListener = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    public interface OnLongClickListener {
        void onLongClick(View v, int pos);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    public void setOnLongClickListener(OnLongClickListener listener){
        this.mLongListener = listener;
    }


    public class TeamViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        TextView textView_name;
        TextView textView_backgroud;
        ImageView imageView_logo;
        ImageView imageView_color[] = new ImageView[5];
        TextView textView_player[] = new TextView[5];
        ImageView imageView_plus;
        public TeamViewHolder(View view){
            super(view);
            constraintLayout = view.findViewById(R.id.cardView_game_layout);
            imageView_plus = view.findViewById(R.id.cardView_team_plus);
            textView_name = view.findViewById(R.id.cardView_team_name);
            textView_backgroud = view.findViewById(R.id.cardView_team_logo_backgroud);
            imageView_logo = view.findViewById(R.id.cardView_team_logo);
            imageView_color[0] = view.findViewById(R.id.cardView_team_color0);
            imageView_color[1] = view.findViewById(R.id.cardView_team_color1);
            imageView_color[2] = view.findViewById(R.id.cardView_team_color2);
            imageView_color[3] = view.findViewById(R.id.cardView_team_color3);
            imageView_color[4] = view.findViewById(R.id.cardView_team_color4);
            textView_player[0] = view.findViewById(R.id.cardView_team_player0);
            textView_player[1] = view.findViewById(R.id.cardView_team_player1);
            textView_player[2] = view.findViewById(R.id.cardView_team_player2);
            textView_player[3] = view.findViewById(R.id.cardView_team_player3);
            textView_player[4] = view.findViewById(R.id.cardView_team_player4);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mLongListener != null){
                            mLongListener.onLongClick(view, pos);
                        }
                    }
                    return true;
                }
            });
        }
    }

    public TeamAdapter(ArrayList<Team> mItems){
        this.mItems = mItems;
        
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_team, parent, false);
        return new TeamViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TeamViewHolder new_holder = (TeamViewHolder) holder;
            new_holder.textView_name.setText(mItems.get(position).name);
            if(mItems.get(position).type == 0){
                new_holder.textView_backgroud.setBackgroundResource(R.drawable.nothing);
                new_holder.imageView_plus.setImageResource(R.drawable.plus);
            }else{
                new_holder.imageView_plus.setImageResource(R.drawable.nothing);
            }

            if(mItems.get(position).type ==1){
                new_holder.imageView_logo.setImageResource(R.drawable.no);
            }else{
                new_holder.imageView_logo.setImageBitmap(ApplicationClass.StringToBitmap(mItems.get(position).logo));
            }

            for(int i = 0; i < 5; i++){
                String tear = mItems.get(position).players[i].tear;
                new_holder.textView_player[i].setText(tear);
                new_holder.imageView_color[i].setColorFilter(Team.tear_color(tear), PorterDuff.Mode.SRC_IN);
            }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
