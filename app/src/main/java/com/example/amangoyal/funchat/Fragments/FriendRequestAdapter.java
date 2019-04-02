package com.example.amangoyal.funchat.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amangoyal.funchat.ChatActivity;
import com.example.amangoyal.funchat.ProfileActivity;
import com.example.amangoyal.funchat.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.RequestViewHolder> {

    List<FriendRequestModelClass> reqList;
    Context context;
    private DatabaseReference mrootRef = FirebaseDatabase.getInstance().getReference();


    FriendRequestAdapter(Context context, List<FriendRequestModelClass> reqList) {
        this.context = context;
        this.reqList = reqList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_friend_request_layout, viewGroup, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestViewHolder holder, int i) {
        final FriendRequestModelClass friendRequestModelClass = reqList.get(i);
        holder.setTextName(friendRequestModelClass.getName());
        holder.setTextDate(friendRequestModelClass.getDate());
        holder.setImage(friendRequestModelClass.getThumb_image());

        final String currentUser = friendRequestModelClass.getCurrentUserUid();
        final String userId = friendRequestModelClass.getUid();


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                dateFormatter.setLenient(false);
                Date today = new Date();
                final String currentDate = dateFormatter.format(today);

                Map reqReceived = new HashMap();
                reqReceived.put("friends/" + currentUser + "/" + userId, currentDate);
                reqReceived.put("friends/" + userId + "/" + currentUser + "/", currentDate);

                reqReceived.put("friend_req/" + currentUser + "/" + userId + "/request_type", null);
                reqReceived.put("friend_req/" + userId + "/" + currentUser + "/request_type", null);
                reqReceived.put("friend_req/" + currentUser + "/" + userId + "/timestamp", null);
                reqReceived.put("friend_req/" + userId + "/" + currentUser + "/timestamp", null);

                mrootRef.updateChildren(reqReceived, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {


                        holder.accept.setVisibility(View.INVISIBLE);
                        holder.reject.setVisibility(View.INVISIBLE);
                        holder.tdate.setText("You are now friend with "+ friendRequestModelClass.getName());
                    }
                });
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map cancelReq = new HashMap();
                cancelReq.put("friend_req/" + currentUser + "/" + userId + "/request_type", null);
                cancelReq.put("friend_req/" + userId + "/" + currentUser + "/request_type", null);
                cancelReq.put("friend_req/" + currentUser + "/" + userId + "/timestamp", null);
                cancelReq.put("friend_req/" + userId + "/" + currentUser + "/timestamp", null);


                mrootRef.updateChildren(cancelReq, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        holder.accept.setVisibility(View.INVISIBLE);
                        holder.reject.setVisibility(View.INVISIBLE);
                        holder.tdate.setText("Friend Request Rejected");
                    }
                });
            }
        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_id", userId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reqList.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout root;
        TextView tname;
        TextView tdate;
        CircleImageView imageView;
        Button accept, reject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.list_root);
            tname = itemView.findViewById(R.id.list_title);
            tdate = itemView.findViewById(R.id.list_desc);
            accept = itemView.findViewById(R.id.single_friend_request_accept_button);
            reject = itemView.findViewById(R.id.single_friend_request_reject_button);
            imageView = itemView.findViewById(R.id.single_user_image);


        }


        public void setTextName(String name) {
            tname.setText(name);
        }

        public void setTextDate(String date) {
            tdate.setText(date);
        }

        public void setImage(String thumb_image) {
            Picasso.get().load(thumb_image).placeholder(R.drawable.default_avatar).into(imageView);


        }


    }
}
