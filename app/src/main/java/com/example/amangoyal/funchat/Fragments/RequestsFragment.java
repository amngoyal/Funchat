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
    private FirebaseAuth mAuth;
    private List<FriendRequestModelClass> requestList = new ArrayList<>();
    private FriendRequestAdapter adapter;
    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mrootRef = FirebaseDatabase.getInstance().getReference();
        mrootRef.keepSynced(true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adapter = new FriendRequestAdapter(getContext(),requestList);
        fetch();
    }

    private void fetch() {
        mrootRef.child("friend_req").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot data : dataSnapshot.getChildren()) {

                    final String friendUserId = data.getKey();

                    final String date = data.getValue().toString();


                    mrootRef.child("users").child(friendUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.d("alldata", dataSnapshot.getValue().toString());

                            FriendRequestModelClass friend = new FriendRequestModelClass(
                                    dataSnapshot.child("name").getValue().toString(),
                                    dataSnapshot.child("image").getValue().toString(),
                                    date,
                                    dataSnapshot.child("thumb_image").getValue().toString(),
                                    friendUserId,
                                    dataSnapshot.child("online").getValue().toString()

                            );

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
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

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
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mRecyclerView.setAdapter(adapter);
    }
}
