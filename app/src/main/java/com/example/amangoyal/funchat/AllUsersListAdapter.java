package com.example.amangoyal.funchat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersListAdapter extends RecyclerView.Adapter<AllUsersListAdapter.ViewHolder> {

    private List<UsersModelClass> usersList;
    private Context context;

    public AllUsersListAdapter(Context context, List<UsersModelClass> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        UsersModelClass user = usersList.get(i);
        holder.setTextName(user.getName());
        holder.setTextStatus(user.getStatus());
        holder.setImage(user.getThumb_image());

        final String user_id = user.getUid();

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_id", user_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

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

        public void setImage(String thumb_image) {
            Picasso.get().load(thumb_image).placeholder(R.drawable.default_avatar).into(imageView);

        }


    }
}
