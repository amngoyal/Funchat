package com.example.amangoyal.funchat.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amangoyal.funchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mrootRef;
    private String currentUser;
    private LinearLayoutManager linearLayoutManager;
    private List<FriendRequestModelClass> requestList = new ArrayList<>();
    private FriendRequestAdapter adapter;
    private TextView tEmptyList;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mrootRef = FirebaseDatabase.getInstance().getReference();
        mrootRef.keepSynced(true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapter = new FriendRequestAdapter(getContext(), requestList);
        fetch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.request_recycler_view);
        mRecyclerView.hasFixedSize();
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        tEmptyList = view.findViewById(R.id.request_fragment_empty_list_text_layout);

        mRecyclerView.setAdapter(adapter);

    }

    private void fetch() {
        /*mrootRef.child("friend_req").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot data : dataSnapshot.getChildren()) {

                    final String friendUserId = data.getKey();
                    if (dataSnapshot.child(friendUserId).child("request_type").getValue().toString().equals("received")) {

                        long timestamp = Long.parseLong(data.child("timestamp").getValue().toString());
                        final String date = DateUtils.formatDateTime(getContext(), timestamp, DateUtils.FORMAT_SHOW_DATE);


                        mrootRef.child("users").child(friendUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                FriendRequestModelClass friend = new FriendRequestModelClass(
                                        dataSnapshot.child("name").getValue().toString(),
                                        dataSnapshot.child("image").getValue().toString(),
                                        date,
                                        dataSnapshot.child("thumb_image").getValue().toString(),
                                        friendUserId,
                                        currentUser);

                                Log.d("alldata", dataSnapshot.child("online").getValue().toString());
                                requestList.add(friend);
                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });*/

        mrootRef.child("friend_req").child(currentUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String friendUserId = dataSnapshot.getKey();
                if (dataSnapshot.child("request_type").getValue().toString().equals("received")) {

                    long timestamp = Long.parseLong(dataSnapshot.child("timestamp").getValue().toString());
                    final String date = DateUtils.formatDateTime(getContext(), timestamp, DateUtils.FORMAT_SHOW_DATE);


                    mrootRef.child("users").child(friendUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            FriendRequestModelClass friend = new FriendRequestModelClass(
                                    dataSnapshot.child("name").getValue().toString(),
                                    dataSnapshot.child("image").getValue().toString(),
                                    date,
                                    dataSnapshot.child("thumb_image").getValue().toString(),
                                    friendUserId,
                                    currentUser);

                            Log.d("alldata", dataSnapshot.child("online").getValue().toString());
                            requestList.add(friend);
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
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


}

