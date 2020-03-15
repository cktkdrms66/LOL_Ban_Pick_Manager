package com.example.lol_ban_pick_manager;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Team.Player> mItems;
    private OnItemClickListener mListener = null;
    private OnLongClickListener mLongListener = null;
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    public interface OnLongClickListener{
        void onLongClick(View v, int pos);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    public void setOnLongItemClickListener(OnLongClickListener listener){
        this.mLongListener = listener;
    }



    public class PlayerViewHolder extends RecyclerView.ViewHolder{
        TextView textView_name;
        ImageView imageView_tear_back;
        TextView textView_tear;
        ImageView imageView;
        public PlayerViewHolder(View view){
            super(view);

            textView_name = view.findViewById(R.id.cardView_player_name);
            textView_tear = view.findViewById(R.id.cardView_player_textView_tear);
            imageView_tear_back = view.findViewById(R.id.cardView_player_tear_back);
            imageView = view.findViewById(R.id.cardView_player_image);


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
    public PlayerAdapter(ArrayList<Team.Player> mItems){
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_player, parent, false);
        return new PlayerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlayerViewHolder new_holder = (PlayerViewHolder) holder;
        if(mItems.get(position).type == 0){
            new_holder.imageView.setImageResource(R.drawable.plus);
            new_holder.textView_name.setVisibility(View.INVISIBLE);
            new_holder.imageView_tear_back.setVisibility(View.INVISIBLE);
            new_holder.textView_tear.setVisibility(View.INVISIBLE);
        }else{
            new_holder.textView_name.setText(mItems.get(position).name);
            new_holder.textView_tear.setText(mItems.get(position).tear);
            new_holder.imageView_tear_back.setColorFilter(Team.tear_color(mItems.get(position).tear), PorterDuff.Mode.SRC_IN);
            if(mItems.get(position).most.size() == 0){
                new_holder.imageView.setImageResource(R.drawable.randomchampion);
            }else{
                new_holder.imageView.setImageResource(mItems.get(position).most.get(0).image);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
