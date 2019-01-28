package com.example.amangoyal.funchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView tname,tstatus,tfriends;
    private Button friend_request_btn;
    private DatabaseReference userDatabaseRefrence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userId = getIntent().getStringExtra("user_id");

        mImage = findViewById(R.id.imageView);
        tname = findViewById(R.id.profile_name);
        tstatus = findViewById(R.id.profile_status);
        tfriends = findViewById(R.id.profile_friends);
        friend_request_btn = findViewById(R.id.profile_send_request_button);

        userDatabaseRefrence = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userDatabaseRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                tname.setText(displayName);
                tstatus.setText(status);
                Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(mImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
