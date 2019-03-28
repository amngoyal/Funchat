package com.example.amangoyal.funchat.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;

import com.example.amangoyal.funchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatsFragment";

    private RecyclerView mRecyclerView;
    private DatabaseReference mrootRef;
    private String currentUser;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth mAuth;
    private List<ChatModelClass> chatList = new ArrayList<>();
    private ChatListAdapter adapter;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mrootRef = FirebaseDatabase.getInstance().getReference();
        mrootRef.keepSynced(true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapter = new ChatListAdapter(getContext(), chatList);
        fetch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.chat_recycler_view);
        mRecyclerView.hasFixedSize();
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mRecyclerView.setAdapter(adapter);
    }

    private void fetch() {

        /*mrootRef.child("messages").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    //final Query messageQuery = mRootRef.child("messages").orderByKey().limitToLast(1);
                    Log.d(TAG, "fetchCalled");

                    mrootRef.child("chat").child(currentUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                Log.d(TAG, "onDataChange: " + d);


                                ChatModelClass chatModelClass = new ChatModelClass(
                                        d.child("seen").getValue().toString(),
                                        d.child("timestamp").getValue().toString(),
                                        d.child("name").getValue().toString(),
                                        d.child("thumb_image").getValue().toString(),
                                        d.child("uid").getValue().toString()
                                );
                                chatList.add(chatModelClass);
                                Log.d(TAG, "data added to list");
                            }

                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "list size: " + chatList.size());
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
        });*/

        Query chatQuery = mrootRef.child("chat").child(currentUser).orderByChild("timestamp").limitToLast(20);

        chatQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d(TAG, "onChildAdded: "+dataSnapshot);
                ChatModelClass chatModelClass = dataSnapshot.getValue(ChatModelClass.class);
                chatList.add(chatModelClass);
               adapter.notifyDataSetChanged();
                adapter.notifyItemChanged(1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d(TAG, "onChildChanged: ");
                adapter.notifyDataSetChanged();
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

}
