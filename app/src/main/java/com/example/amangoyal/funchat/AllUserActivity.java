package com.example.amangoyal.funchat;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.CircularPropagation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amangoyal.funchat.R;
import com.example.amangoyal.funchat.UsersModelClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference;
    private ProgressDialog mProgress;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

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
    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout root;
        public TextView tname;
        public TextView tstatus;
        public CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.list_root);
            tname = itemView.findViewById(R.id.list_title);
            tstatus = itemView.findViewById(R.id.list_desc);
            imageView = itemView.findViewById(R.id.single_user_image);

        }

        public void setTextName(String name) {
            tname.setText(name);
        }

        public void setTextStatus(String status) {
            tstatus.setText(status);
        }
        public void setImage(String thumb_image){
            Picasso.get().load(thumb_image).placeholder(R.drawable.default_avatar).into(imageView);

        }


    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseRecyclerOptions<UsersModelClass> options =
                new FirebaseRecyclerOptions.Builder<UsersModelClass>()
                        .setQuery(query, new SnapshotParser<UsersModelClass>() {
                            @NonNull
                            @Override
                            public UsersModelClass parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new UsersModelClass(snapshot.child("name").getValue().toString(),
                                        snapshot.child("status").getValue().toString(),snapshot.child("thumb_image").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<UsersModelClass, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_user_layout, parent, false);
                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, UsersModelClass model) {
                holder.setTextName(model.getName());
                holder.setTextStatus(model.getStatus());
                holder.setImage(model.getThumb_image());

                final String user_id = getRef(position).getKey();

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(AllUserActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AllUserActivity.this,ProfileActivity.class);
                        intent.putExtra("user id", user_id);
                        startActivity(intent);
                    }
                });
            }

        };
        mRecyclerView.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}





   /* @Override
    protected void onStart() {
        super.onStart();

        mProgress.setMessage("Please wait");
        mProgress.setTitle("loading all users");
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.keepSynced(true);
        Query query = databaseReference.orderByKey();

        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<UsersModelClass>().setQuery(query, UsersModelClass.class).build();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerAdapter<UsersModelClass, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UsersModelClass, UsersViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull UsersModelClass model) {
                holder.setName(model.getName());
                holder.setUserStatus(model.getStatus());
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_user_layout, viewGroup, false);
                return new UsersViewHolder(view);
            }
        };


        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        mProgress.dismiss();


    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {

            TextView userNameView = mView.findViewById(R.id.single_user_name);
            userNameView.setText(name);
        }
        public void setUserStatus(String status){
            TextView userStatusView = mView.findViewById(R.id.single_user_status);
            userStatusView.setText(status);
        }
    }*/
