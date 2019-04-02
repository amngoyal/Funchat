package com.example.amangoyal.funchat;

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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Messages> messagesList;
    Context context;

    public MessagesAdapter(Context context,List<Messages> messagesList) {
        this.messagesList = messagesList;
        this.context = context;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_single_layout,viewGroup,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, final int i) {

        Messages message = messagesList.get(i);

        String messageType = message.getType();
        String fromUser = message.getFrom();
        String time = DateUtils.formatDateTime(context, message.getTime(), DateUtils.FORMAT_SHOW_TIME);


        if(messageType.equals("text")){
            messageViewHolder.messageLayout.setText(message.getMessage());
            messageViewHolder.messageImageLayout.setVisibility(View.INVISIBLE);
            messageViewHolder.messageTimeLayout.setText(time);

        }else{
            messageViewHolder.messageLayout.setVisibility(View.INVISIBLE);
            Picasso.get().load(message.getMessage()).into(messageViewHolder.messageImageLayout);
        }


    }


    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView messageLayout,messageTimeLayout;
        private CircleImageView profile;
        public ImageView messageImageLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageLayout = itemView.findViewById(R.id.message_text_layout);
            profile = itemView.findViewById(R.id.message_profile_layout);

            messageImageLayout = itemView.findViewById(R.id.message_image_layout);
            messageTimeLayout = itemView.findViewById(R.id.message_time_layout);

        }
    }
}

