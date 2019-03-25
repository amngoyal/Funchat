package com.example.amangoyal.funchat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    public MessagesAdapter(List<Messages> messagesList) {
        this.messagesList = messagesList;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_single_layout,viewGroup,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {

        Messages message = messagesList.get(i);

        String messageType = message.getType();
        String fromUser = message.getFrom();

        if(messageType.equals("text")){
            messageViewHolder.messageLayout.setText(message.getMessage());
            messageViewHolder.messageImageLayout.setVisibility(View.INVISIBLE);

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

        private TextView messageLayout;
        private CircleImageView profile;
        public ImageView messageImageLayout;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageLayout = itemView.findViewById(R.id.message_text_layout);
            profile = itemView.findViewById(R.id.message_profile_layout);
            messageImageLayout = itemView.findViewById(R.id.message_image_layout);

        }
    }
}

