package com.example.amangoyal.funchat.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amangoyal.funchat.R;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private List<FriendsModelClass> friendsList;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FriendsModelClass friendsModelClass = friendsList.get(i);
        viewHolder.setdate( friendsModelClass.getDate());
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tdate = itemView.findViewById(R.id.list_desc);
        }

        public void setdate(String date){
            tdate.setText(date);
        }

    }
}
