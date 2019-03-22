package com.example.amangoyal.funchat.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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


public class FriendsFragment extends Fragment {


    private RecyclerView mFriendlist;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private View mMainView;
    private DatabaseReference usersDatabaseRef;
    private List<FriendsModelClass> arrayList = new ArrayList<>();
    private FriendListAdapter friendListAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);
        mFriendlist = mMainView.findViewById(R.id.friends_recycler_view);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friends");
        mDatabaseReference.keepSynced(true);
        usersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersDatabaseRef.keepSynced(true);


        mFriendlist.setHasFixedSize(true);
        mFriendlist.setLayoutManager(new LinearLayoutManager(getContext()));
        fetch();

        return mMainView;
    }

    public void fetch() {

        mDatabaseReference.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot data : dataSnapshot.getChildren()) {

                    Log.d("aaaa", data.getKey());

                    final String friendUserId = data.getKey();

                    Log.d("bbbb", data.getValue().toString());

                    final String date = data.getValue().toString();


                    usersDatabaseRef.child(friendUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.d("alldata", dataSnapshot.getValue().toString());

                            FriendsModelClass friend = new FriendsModelClass(
                                    dataSnapshot.child("name").getValue().toString(),
                                    dataSnapshot.child("image").getValue().toString(),
                                    date,
                                    dataSnapshot.child("thumb_image").getValue().toString(),
                                    friendUserId,
                                    dataSnapshot.child("online").getValue().toString()

                            );

                            Log.d("alldata", dataSnapshot.child("online").getValue().toString());
                            arrayList.add(friend);
                            friendListAdapter.notifyDataSetChanged();
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

        friendListAdapter = new FriendListAdapter(getContext(), arrayList);
        mFriendlist.setAdapter(friendListAdapter);

    }

}