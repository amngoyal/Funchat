package com.example.amangoyal.funchat.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amangoyal.funchat.AllUsersListAdapter;
import com.example.amangoyal.funchat.R;
import com.example.amangoyal.funchat.UsersModelClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {

    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference;
    private ProgressDialog mProgress;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private AllUsersListAdapter allUsersListAdapter;
    private List<UsersModelClass> usersList = new ArrayList<>();

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

}
