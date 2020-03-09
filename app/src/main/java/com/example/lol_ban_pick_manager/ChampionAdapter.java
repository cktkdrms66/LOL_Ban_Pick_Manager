package com.example.lol_ban_pick_manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChampionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context context;
    ArrayList<Champion> unFilteredList;
    ArrayList<Champion> filteredList;
    ArrayList<Boolean> mIsClicked = new ArrayList<>();
    ArrayList<Boolean> mIsPicked = new ArrayList<>();
    int mOnlyItemPosition = -1;
    OnItemClickListener mListener = null;
    OnLongClickListener mLongListener = null;
    public int getmOnlyItemPosition() {
        return mOnlyItemPosition;
    }

    public Champion getChampion(int pos){
        return filteredList.get(pos);
    }
    public boolean getIsClicked(int pos){
        return mIsClicked.get(pos);
    }
    public void setIsClicked(int pos, boolean b){
        mIsClicked.set(pos,b);
    }
    public interface OnItemClickListener{
        void onItemClick(int pos, ImageView imageView);
    }
    public interface OnLongClickListener{
        void onLongClick(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    public void setOnLongClickListener(OnLongClickListener listener){
        this.mLongListener = listener;
    }
    public void setClickClear(boolean isReturn, boolean isNext, int championIndex){
        if(isReturn ==false){
            mIsClicked.set(mOnlyItemPosition, false);
            mIsPicked.set(mOnlyItemPosition, true);

            mOnlyItemPosition = -1;
        }else{
            if(mOnlyItemPosition != -1){
                setIsClicked(mOnlyItemPosition, false);
            }
            if(isNext == false){
                mIsPicked.set(championIndex, false);
                mIsClicked.set(championIndex, true);
                mOnlyItemPosition = championIndex;
            } else{
                mIsPicked.set(championIndex, true);
                mIsClicked.set(championIndex, false);
                mOnlyItemPosition = -1;

            }
        }
        notifyDataSetChanged();


    }


    public void setOnlyClick(int pos, boolean isClick){
        if(isClick){
            if(mOnlyItemPosition != -1){
                mIsClicked.set(mOnlyItemPosition, false);
            }
            mOnlyItemPosition = pos;
            notifyDataSetChanged();
        }else{
            mOnlyItemPosition = -1;;
        }
    }

    public class ChampionViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ChampionViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.cardView_champion_image);
            textView = view.findViewById(R.id.cardView_champion_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            if(pos < mIsPicked.size()){
                                if(mIsPicked.get(pos)){
                                    return;
                                }
                            }


                            mListener.onItemClick(pos, imageView);
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
                            if(pos < mIsPicked.size()){
                                if(mIsPicked.get(pos)){
                                    return true;
                                }
                            }
                            mLongListener.onLongClick(view, pos);
                        }
                    }
                    return true;
                }
            });
        }
    }

    public ChampionAdapter(Context context, ArrayList<Champion> champions){
        filteredList = champions;
        unFilteredList = champions;
        this.context = context;
        for(int i = 0; i < filteredList.size(); i++){
            mIsClicked.add(false);
            mIsPicked.add(false);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cardview_champion, parent, false);
        return new ChampionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChampionViewHolder new_holder = (ChampionViewHolder) holder;
        new_holder.textView.setText(filteredList.get(position).name);
        new_holder.imageView.setImageResource(filteredList.get(position).image);
        if(position < mIsClicked.size()){
            if(mIsPicked.get(position)){
                new_holder.imageView.setColorFilter(Color.parseColor("#D81B60"), PorterDuff.Mode.DST);
                new_holder.imageView.setColorFilter(Color.parseColor("#696969"), PorterDuff.Mode.MULTIPLY);
            } else{
                if(mIsClicked.get(position)){
                    new_holder.imageView.setColorFilter(Color.parseColor("#FFABB5B8"), PorterDuff.Mode.MULTIPLY);
                }else{
                    new_holder.imageView.setColorFilter(Color.parseColor("#FFABB5B8"), PorterDuff.Mode.DST);
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()) {
                    filteredList = unFilteredList;
                } else {
                    ArrayList<Champion> filteringList = new ArrayList<>();
                    for(Champion champion : unFilteredList) {
                        String name = champion.name;
                        if(name.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(champion);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                filteredList = (ArrayList<Champion>)results.values;
                notifyDataSetChanged();
            }
        };

    }
}
