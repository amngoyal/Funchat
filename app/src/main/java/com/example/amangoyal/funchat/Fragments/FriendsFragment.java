package com.example.amangoyal.funchat.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amangoyal.funchat.AllUsersListAdapter;
import com.example.amangoyal.funchat.R;
import com.example.amangoyal.funchat.UsersModelClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class FriendsFragment extends Fragment {


    private RecyclerView mFriendlist;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private View mMainView;


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
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friends").child(mCurrentUserId);

        mFriendlist.setHasFixedSize(true);
        mFriendlist.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }

    public void fetch(){

    }

    @Override
    public void onStart() {
        super.onStart();

        Query queuy = FirebaseDatabase.getInstance().getReference().child("friends");

        FirebaseRecyclerOptions<Friends> friendsFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(queuy, new SnapshotParser<Friends>() {
                    @NonNull
                    @Override
                    public Friends parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Friends(snapshot.child(mCurrentUserId).);
                    }
                });
        FirebaseRecyclerAdapter<Friends,FriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                Friends.class,
                R.layout.single_user_layout,
                mDatabaseReference,
                FriendsViewHolder.class


        ) {
            @Override
            protected void onBindViewHolder(@NonNull FriendsViewHolder holder, int position, @NonNull Friends model) {

            }

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return null;
            }
        };
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setDate(String date){
            TextView userNameView = mView.findViewById(R.id.list_desc);
            userNameView.setText(date);
        }
    }
}
