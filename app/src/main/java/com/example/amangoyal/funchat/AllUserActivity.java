package com.example.amangoyal.funchat;


import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllUserActivity extends AppCompatActivity {

    private static final String TAG = "AllUserActivity";


    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference;
    private ProgressDialog mProgress;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private AllUsersListAdapter allUsersListAdapter;
    private List<UsersModelClass> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        // Setting up the toolbar
        toolbar = findViewById(R.id.alluser_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.recyclerview);
        mProgress = new ProgressDialog(this);

        //Setting up the recycler view
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);


        fetch();


    }


    //ViewHolder to handle out views in each position.


    private void fetch() {

        Log.d(TAG,"Fetch function called");
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    UsersModelClass usersModelClass = new UsersModelClass(
                            data.child("name").getValue().toString(),
                            data.child("image").getValue().toString(),
                            data.child("status").getValue().toString(),
                            data.child("thumb_image").getValue().toString()
                            , data.getKey()
                    );
                    usersList.add(usersModelClass);
                    Log.d(TAG,"FetchCalled "+ usersList.size());


                }
                allUsersListAdapter = new AllUsersListAdapter(AllUserActivity.this, usersList);
                mRecyclerView.setAdapter(allUsersListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: An error occurred while" + databaseError.getMessage());
            }
        });

    }
}