package com.example.amangoyal.funchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Messages> messagesList;
    Context context;
    private DatabaseReference mRootRef;

    String currentUser;

    public MessagesAdapter(Context context, List<Messages> messagesList, String currentUser) {
        this.messagesList = messagesList;
        this.context = context;
        this.currentUser = currentUser;
        Log.d(TAG, "MessagesAdapter: ");


    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        Log.d(TAG, "onCreateViewHolder: " + messagesList.get(i).getFrom() + " ");

        Messages tmessage = messagesList.get(i);

        if (tmessage.getFrom().equals(currentUser)) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_single_item_out_layout, viewGroup, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_single_item_in_layout, viewGroup, false);
            return new MessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, final int i) {

        final Messages message = messagesList.get(i);
        Log.d(TAG, "onBindViewHolder: ");
        String fromUser = message.getFrom();
        final String messageType = message.getType();
        final String time = DateUtils.formatDateTime(context, message.getTime(), DateUtils.FORMAT_SHOW_TIME);

        mRootRef = FirebaseDatabase.getInstance().getReference();


        mRootRef.child("users").child(fromUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileUri = dataSnapshot.child("thumb_image").getValue().toString();

                messageViewHolder.setProfile(profileUri);

                if (messageType.equals("text")) {

                    messageViewHolder.messageLayout.setText(message.getMessage());
                    messageViewHolder.messageImageLayout.setVisibility(View.INVISIBLE);
                    messageViewHolder.messageTimeLayout.setText(time);
                 //   messageViewHolder.setMessageNameLayout(dataSnapshot.child("name").getValue().toString());
                } else {
                    messageViewHolder.messageLayout.setVisibility(View.INVISIBLE);
                    Picasso.get().load(message.getMessage()).into(messageViewHolder.messageImageLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return (messagesList.size());
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView messageLayout, messageTimeLayout, messageNameLayout;
        private CircleImageView profile;
        private ImageView messageImageLayout;


        TextView chatUserMessageLayout, chatUserTimeLayout;
        ImageView chatUserImageMessageLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageLayout = itemView.findViewById(R.id.message_text_layout);

            profile = itemView.findViewById(R.id.message_profile_layout);
           // messageNameLayout = itemView.findViewById(R.id.message_name_layout);

            messageImageLayout = itemView.findViewById(R.id.message_image_layout);
            messageTimeLayout = itemView.findViewById(R.id.message_time_layout);

        }

        public void setProfile(String uri) {
            Picasso.get().load(uri).placeholder(R.drawable.default_avatar).into(profile);
        }


    }
}

