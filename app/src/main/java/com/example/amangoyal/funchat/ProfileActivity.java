package com.example.amangoyal.funchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView tname,tstatus,tfriends;
    private Button friend_request_btn,decline_request_btn;
    private DatabaseReference userDatabaseRefrence;
    private ProgressDialog mProgress;
    private DatabaseReference friendDatabaseRefrence;
    private FirebaseUser currentUser;
    private String currentState = "not_friends";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String userId = getIntent().getStringExtra("user_id");

        mImage = findViewById(R.id.imageView);
        tname = findViewById(R.id.profile_name);
        tstatus = findViewById(R.id.profile_status);
        tfriends = findViewById(R.id.profile_friends);
        friend_request_btn = findViewById(R.id.profile_send_request_button);
        decline_request_btn = findViewById(R.id.profile_decline_request_button);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Fetching data from database");
        mProgress.setTitle("Please Wait");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();


        userDatabaseRefrence = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        friendDatabaseRefrence = FirebaseDatabase.getInstance().getReference().child("friend_req");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        userDatabaseRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

            //    Toast.makeText(ProfileActivity.this, ""+image, Toast.LENGTH_SHORT).show();
                tname.setText(displayName);
                tstatus.setText(status);
                Picasso.get().load(image).into(mImage);
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        friend_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentState.equals("not_friends")){
                    friendDatabaseRefrence.child(currentUser.getUid()).child(userId).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                friendDatabaseRefrence.child(userId).child(currentUser.getUid()).child("request_type")
                                        .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileActivity.this, "Friend request sent", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(ProfileActivity.this, "Failed Sending request", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}
