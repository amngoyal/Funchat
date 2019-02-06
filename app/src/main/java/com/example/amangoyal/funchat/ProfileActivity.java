package com.example.amangoyal.funchat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ImageView mImage;
    private TextView tname, tstatus, tfriends;
    private Button friend_request_btn, decline_request_btn;
    private DatabaseReference userDatabaseReference;
    private ProgressDialog mProgress;
    private DatabaseReference friendReqDatabaseReference;
    private DatabaseReference friendsDatabaseReference;
    private DatabaseReference mNotificationReference;

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


        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        friendReqDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friend_req");
        friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friends");
        mNotificationReference = FirebaseDatabase.getInstance().getReference().child("notification");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                //    Toast.makeText(ProfileActivity.this, ""+image, Toast.LENGTH_SHORT).show();
                tname.setText(displayName);
                tstatus.setText(status);
                Picasso.get().load(image).into(mImage);


                //----------------------Friend request button state------------------
                friendReqDatabaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(userId)) {
                            String req_type = dataSnapshot.child(userId).child("request_type").getValue().toString();
                            if (req_type.equals("received")) {
                                currentState = "req_received";
                                friend_request_btn.setText("Accept friend request");
                                decline_request_btn.setEnabled(true);
                                decline_request_btn.setVisibility(View.VISIBLE);
                                Toast.makeText(ProfileActivity.this, "req_received", Toast.LENGTH_SHORT).show();

                            } else if (req_type.equals("sent")) {
                                currentState = "req_sent";
                                friend_request_btn.setText("Cancel friend request");
                                decline_request_btn.setVisibility(View.INVISIBLE);
                                decline_request_btn.setEnabled(false);
                                Toast.makeText(ProfileActivity.this, "req_sent", Toast.LENGTH_SHORT).show();

                            }

                            mProgress.dismiss();

                        } else {
                            decline_request_btn.setVisibility(View.INVISIBLE);
                            decline_request_btn.setEnabled(false);

                            friendsDatabaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(userId)) {
                                                friend_request_btn.setText("Unfriend this person");
                                                currentState = "friends";
                                                Toast.makeText(ProfileActivity.this, "friends", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ProfileActivity.this, "not_friends", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {


                                        }
                                    });
                            mProgress.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        friend_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                friend_request_btn.setEnabled(false);

                //----------------------Send friend request-------------
                if (currentState.equals("not_friends")) {
                    friendReqDatabaseReference.child(currentUser.getUid()).child(userId).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                friendReqDatabaseReference.child(userId).child(currentUser.getUid()).child("request_type")
                                        .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        currentState = "req_sent";
                                        friend_request_btn.setText("Cancel friend request");
                                        decline_request_btn.setVisibility(View.INVISIBLE);
                                        decline_request_btn.setEnabled(false);


                                        //Hashmap used to set notification data
                                        HashMap<String, String> notificationData = new HashMap<>();
                                        notificationData.put("from", currentUser.getUid());
                                        notificationData.put("type", "request");

                                        mNotificationReference.child(userId).push().setValue(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ProfileActivity.this, "Friend req sent", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.d(TAG, "onComplete Error: ");
                                                    task.getException().printStackTrace();
                                                    Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });

                                friend_request_btn.setEnabled(true);

                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed Sending request", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                //----------------------Cancel request---------------------
                if (currentState.equals("req_sent")) {


                    friendReqDatabaseReference.child(userId).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendReqDatabaseReference.child(currentUser.getUid()).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    friend_request_btn.setEnabled(true);
                                    friend_request_btn.setText("Send Friend request");
                                    currentState = "not_friends";
                                    decline_request_btn.setVisibility(View.INVISIBLE);
                                    decline_request_btn.setEnabled(false);
                                }
                            });

                        }
                    });

                }

                //----------------------------Accept friend request-----------------------
                if (currentState.equals("req_received")) {
                    DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                    dateFormatter.setLenient(false);
                    Date today = new Date();
                    final String currentDate = dateFormatter.format(today);
                    //   final String currentDat = DateFormat.getDateInstance().format(new Date());


                    friendsDatabaseReference.child(currentUser.getUid()).child(userId)
                            .setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            friendsDatabaseReference.child(userId).child(currentUser.getUid())
                                    .setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    friendReqDatabaseReference.child(userId).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            friendReqDatabaseReference.child(currentUser.getUid()).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    friend_request_btn.setEnabled(true);
                                                    friend_request_btn.setText("Unfriend this person");
                                                    currentState = "friends";
                                                    decline_request_btn.setVisibility(View.INVISIBLE);
                                                    decline_request_btn.setEnabled(false);

                                                }
                                            });

                                        }
                                    });
                                }
                            });
                        }
                    });
                }

                //--------------------------------Unfriend--------------------------------
                if (currentState.equals("friends")) {
                    friendsDatabaseReference.child(currentUser.getUid()).child(userId).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    friendsDatabaseReference.child(userId).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            friend_request_btn.setEnabled(true);
                                            friend_request_btn.setText("Send Friend request");
                                            currentState = "not_friends";
                                            decline_request_btn.setVisibility(View.INVISIBLE);
                                            decline_request_btn.setEnabled(false);
                                        }
                                    });
                                }
                            });

                }

            }
        });

        decline_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendReqDatabaseReference.child(userId).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        friendReqDatabaseReference.child(currentUser.getUid()).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                friend_request_btn.setEnabled(true);
                                friend_request_btn.setText("Send Friend request");
                                currentState = "not_friends";
                                decline_request_btn.setVisibility(View.INVISIBLE);
                                decline_request_btn.setEnabled(false);
                            }
                        });

                    }
                });
            }
        });
    }
}
