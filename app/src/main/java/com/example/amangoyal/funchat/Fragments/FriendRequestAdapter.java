package com.example.amangoyal.funchat.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amangoyal.funchat.ChatActivity;
import com.example.amangoyal.funchat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.RequestViewHolder> {

    List<FriendRequestModelClass> reqList;
    Context context;


     FriendRequestAdapter(Context context, List<FriendRequestModelClass> reqList){
        this.context = context;
        this.reqList = reqList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user_layout,viewGroup,false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int i) {
        final FriendRequestModelClass friendRequestModelClass = reqList.get(i);
        holder.setTextName(friendRequestModelClass.getName());
        holder.setTextDate(friendRequestModelClass.getDate());
        holder.setImage(friendRequestModelClass.getThumb_image());
        holder.setOnlineStatus(friendRequestModelClass.getOnlineStatus());

    }

    @Override
    public int getItemCount() {
        return reqList.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout root;
        TextView tname;
        TextView tdate;
        CircleImageView imageView;
        ImageView onlineStatusImage;

        public RequestViewHolder(@NonNull View itemView) {
                super(itemView);

            root = itemView.findViewById(R.id.list_root);
            tname = itemView.findViewById(R.id.list_title);
            tdate = itemView.findViewById(R.id.list_desc);


            imageView = itemView.findViewById(R.id.single_user_image);
            onlineStatusImage = itemView.findViewById(R.id.single_user_online_dot);

        }


        public void setTextName(String name) {
            tname.setText(name);
        }

        public void setTextDate(String date) {
            tdate.setText(date);
        }

        public void setImage(String thumb_image) {
            Picasso.get().load(thumb_image).placeholder(R.drawable.default_avatar).into(imageView);

        }

        public void setOnlineStatus(String onlineStatus){

            if(onlineStatus.equals("true")){
                onlineStatusImage.setVisibility(View.VISIBLE);
            }

        }


    }
}
