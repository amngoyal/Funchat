package com.example.amangoyal.funchat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private static final int GALLERY_PICK = 1;
    private String mChatUser;
    private Toolbar chatToolbar;
    private DatabaseReference mRootRef;
    private String userName;
    private TextView nameView, lastSeenView;
    private CircleImageView circleImageView;
    private FirebaseAuth mAuth;
    private String mCurrentUser;
    private ImageButton chatSendButton, chatAddButton;
    private EditText chatEditText;
    private RecyclerView chatMessageListLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Messages> messagesList = new ArrayList<>();
    private MessagesAdapter mAdapter;
    public static final int TOTAL_ITEMS_TO_LOAD = 10;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int mCurrentPages = 1;
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    private StorageReference mImageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatUser = getIntent().getStringExtra("user_id");
        userName = getIntent().getStringExtra("userName");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser().getUid();

        chatSendButton = findViewById(R.id.chat_send_button);
        chatAddButton = findViewById(R.id.chat_Add_button);
        chatEditText = findViewById(R.id.chat_edit_text);
        chatMessageListLayout = findViewById(R.id.chat_message_list);
        swipeRefreshLayout = findViewById(R.id.chat_message_swipe_layout);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();


        mLinearLayoutManager = new LinearLayoutManager(this);
        chatMessageListLayout.setHasFixedSize(true);
        chatMessageListLayout.setLayoutManager(mLinearLayoutManager);

        mAdapter = new MessagesAdapter(messagesList);
        chatMessageListLayout.setAdapter(mAdapter);
        loadMessages();


        chatToolbar = findViewById(R.id.chatToolbar);
        chatToolbar.setTitle("");
        setSupportActionBar(chatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);


        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.custom_action_bar, null);
        actionBar.setCustomView(action_bar_view);

        //-----Custom layout attributes------
        nameView = findViewById(R.id.custom_bar_name);
        lastSeenView = findViewById(R.id.last_seen);
        circleImageView = findViewById(R.id.custom_action_image);

        nameView.setText(userName);

        mRootRef.child("users").child(mChatUser).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    mRootRef.child("users").child(mChatUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map chatAddMap = new HashMap();
                            chatAddMap.put("seen", "false");
                            chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
                            chatAddMap.put("name", dataSnapshot.child("name").getValue());
                            chatAddMap.put("thumb_image", dataSnapshot.child("thumb_image").getValue());
                            chatAddMap.put("uid", dataSnapshot.getKey());

                            Map chatUserMap = new HashMap();
                            chatUserMap.put("chat/" + mCurrentUser + "/" + mChatUser, chatAddMap);
                            chatUserMap.put("chat/" + mChatUser + "/" + mCurrentUser, chatAddMap);

                            mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                    if (databaseError != null)
                                        Log.d("chatError", databaseError.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrentPages++;
                itemPos = 0;
                loadMoreMessages();
                // chatMessageListLayout.scrollToPosition(1);


            }
        });

        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                chatEditText.setText("");

            }
        });

        chatAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });
    }

    private void loadMoreMessages() {

        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrentUser).child(mChatUser);
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages = dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();

                if (!mPrevKey.equals(messageKey)) {
                    messagesList.add(itemPos++, messages);
                } else {
                    mPrevKey = mLastKey;
                }

                if (itemPos == 1) {
                    mLastKey = messageKey;
                }

                Log.d("Total keys:", "last key:" + mLastKey + " |prev key:" + mPrevKey + " |message key:" + messageKey);
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                mLinearLayoutManager.scrollToPositionWithOffset(10, 0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadMessages() {

        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrentUser).child(mChatUser);
        Query messageQuery = messageRef.limitToLast(mCurrentPages * TOTAL_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Messages messages = dataSnapshot.getValue(Messages.class);
                itemPos++;
                String messageKey = dataSnapshot.getKey();
                if (itemPos == 1) {
                    mLastKey = messageKey;
                    mPrevKey = messageKey;
                }

                Log.d("last key", mLastKey);
                messagesList.add(messages);
                mAdapter.notifyDataSetChanged();
                chatMessageListLayout.scrollToPosition(messagesList.size() - 1);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String message = chatEditText.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            String currentUserRef = "messages/" + mCurrentUser + "/" + mChatUser;
            String chatUserRef = "messages/" + mChatUser + "/" + mCurrentUser;

            DatabaseReference userPushId = FirebaseDatabase.getInstance().getReference().child("message")
                    .child(mCurrentUser).child(mChatUser).push();
            String pushId = userPushId.getKey();

            Map timestamp = ServerValue.TIMESTAMP;
            Map chatTimeStamp = new HashMap();
            chatTimeStamp.put("chat/" + mCurrentUser + "/" + mChatUser + "/timestamp", timestamp);
            chatTimeStamp.put("chat/" + mChatUser + "/" + mCurrentUser + "/timestamp", timestamp);
            mRootRef.updateChildren(chatTimeStamp, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d("messageUserMap", databaseError.getMessage());
                    }
                }
            });

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", timestamp);
            messageMap.put("from", mCurrentUser);

            Map messageUserMap = new HashMap();
            messageUserMap.put(currentUserRef + "/" + pushId, messageMap);
            messageUserMap.put(chatUserRef + "/" + pushId, messageMap);

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d("messageUserMap", databaseError.getMessage());
                    }
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            final String currentUserRef = "messages/" + mCurrentUser + "/" + mChatUser;
            final String chatUserRef = "messages/" + mChatUser + "/" + mCurrentUser;

            DatabaseReference userPushId = FirebaseDatabase.getInstance().getReference().child("message")
                    .child(mCurrentUser).child(mChatUser).push();
            final String pushId = userPushId.getKey();

            final StorageReference imageRef = mImageStorage.child("message_images").child(pushId + ".jpg");
            imageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {

                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadURL = uri.toString();

                                Map messageMap = new HashMap();
                                messageMap.put("message", downloadURL);
                                messageMap.put("seen", false);
                                messageMap.put("type", "image");
                                messageMap.put("time", ServerValue.TIMESTAMP);
                                messageMap.put("from", mCurrentUser);

                                Map messageUserMap = new HashMap();
                                messageUserMap.put(currentUserRef + "/" + pushId, messageMap);
                                messageUserMap.put(chatUserRef + "/" + pushId, messageMap);

                                mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Log.d("messageUserMap", databaseError.getMessage());
                                        }
                                    }
                                });

                            }
                        });
                    }
                }
            });
        }
    }
}

