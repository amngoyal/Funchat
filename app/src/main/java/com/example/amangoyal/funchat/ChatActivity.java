package com.example.amangoyal.funchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    private String mChatUser;
    private Toolbar chatToolbar;
    private DatabaseReference mRootRef;
    private String userName;
    private TextView nameView, lastSeenView;
    private CircleImageView circleImageView;
    private FirebaseAuth mAuth;
    private String mCurrentUser;
    private ImageButton chatSendButton,chatAddButton;
    private EditText chatEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatUser = getIntent().getStringExtra("user_id");
        userName = getIntent().getStringExtra("userName");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser().getUid();

        chatSendButton  = findViewById(R.id.chat_send_button);
        chatAddButton = findViewById(R.id.chat_Add_button);
        chatEditText = findViewById(R.id.chat_edit_text);


        mRootRef = FirebaseDatabase.getInstance().getReference();

        chatToolbar = findViewById(R.id.chatToolbar);
        chatToolbar.setTitle("");
        setSupportActionBar(chatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.custom_action_bar, null);
        actionBar.setCustomView(action_bar_view);

        //-----Custom layout attributes------
        nameView = findViewById(R.id.custom_bar_name);
        lastSeenView = findViewById(R.id.last_seen);
        circleImageView = findViewById(R.id.custom_action_image);

        nameView.setText(userName);

        mRootRef.child("users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String lastOnlineTime = dataSnapshot.child("online").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                if (lastOnlineTime.equals("true")) {
                    lastSeenView.setText("Online");
                } else {


                    long lastTime = Long.parseLong(lastOnlineTime);
                    String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, getApplicationContext());

                    lastSeenView.setText(lastSeenTime);
                }

                Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRootRef.child("chat").child(mCurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(mChatUser)) {
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chat/"+mCurrentUser+"/"+mChatUser,chatAddMap);
                    chatUserMap.put("chat/"+mChatUser+"/"+mCurrentUser,chatAddMap);

                    mRootRef.updateChildren(chatAddMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            Log.d("chatError",databaseError.getMessage().toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = chatEditText.getText().toString();
        if(TextUtils.isEmpty(message)){
            String currentUserRef = "messages/"+mCurrentUser+"/"+mChatUser;
            String chatUserRef = "message/"+mChatUser+"/"+mCurrentUser;

            Map messageMap = new HashMap();
            messageMap.put(chatUserRef+"/message",message);
            messageMap.put(chatUserRef+"/send",false);
        }
    }

}

