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

import com.example.amangoyal.funchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mrootRef;
    private String currentUser;
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
        fetch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mRecyclerView = view.findViewById(R.id.chat_recycler_view);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ChatListAdapter(getContext(), chatList);
        mRecyclerView.setAdapter(adapter);



        return view;
    }

    private void fetch() {

        mrootRef.child("messages").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    //final Query messageQuery = mrootRef.child("messages").orderByKey().limitToLast(1);
                    Log.d("chatFragment", "fetchCAlled");

                    mrootRef.child("chat").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                ChatModelClass chatModelClass = new ChatModelClass(
                                        d.child("seen").getValue().toString(),
                                        d.child("timestamp").getValue().toString(),
                                        d.child("name").getValue().toString(),
                                        d.child("thumb_image").getValue().toString()
                                );

                                chatList.add(chatModelClass);

                                Log.d("chatFragment", "data added to list");

                                adapter.notifyDataSetChanged();
                            }


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

    }

}
