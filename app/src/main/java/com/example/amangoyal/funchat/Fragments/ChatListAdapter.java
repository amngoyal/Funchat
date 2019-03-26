package com.example.amangoyal.funchat.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amangoyal.funchat.ChatActivity;
import com.example.amangoyal.funchat.R;
import com.example.amangoyal.funchat.loginAndRegisterActivity.StartActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.PriorityQueue;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private List<ChatModelClass> chatList;
    private Context context;

    public ChatListAdapter(Context context,List<ChatModelClass> chatList){
        this.chatList = chatList;
        this.context = context;

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user_layout,viewGroup,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        ChatModelClass chatModelClass = chatList.get(i);
        chatViewHolder.setName(chatModelClass.getName());
        chatViewHolder.setProfile(chatModelClass.getThumbImage());

        chatViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tname;
        CircleImageView tprofileImage;
        LinearLayout root;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

             tprofileImage = itemView.findViewById(R.id.single_user_image);
             tname =itemView.findViewById(R.id.list_title);

        }

        public void setName(String name){
            tname.setText(name);
        }

        public void setProfile(String profile){

            Picasso.get().load(profile).placeholder(R.drawable.default_avatar).into(tprofileImage);
        }

    }


}


