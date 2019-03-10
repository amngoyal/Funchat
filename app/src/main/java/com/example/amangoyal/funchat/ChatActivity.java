package com.example.amangoyal.funchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    private String mChatUser;
    private Toolbar chatToolbar;
    private DatabaseReference mRootRef;
    private String userName;
    private TextView nameView, lastSeenView;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatUser = getIntent().getStringExtra("user_id");
        userName = getIntent().getStringExtra("userName");

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
                    String lastSeenTime =  GetTimeAgo.getTimeAgo(lastTime,getApplicationContext());

                    lastSeenView.setText(lastSeenTime);
                }

                Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

