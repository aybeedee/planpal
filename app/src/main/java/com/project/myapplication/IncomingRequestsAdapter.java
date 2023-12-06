package com.project.myapplication;

// IncomingRequestsAdapter.java

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class IncomingRequestsAdapter extends RecyclerView.Adapter<IncomingRequestsAdapter.MyViewHolder> {
    List<User1> userList;
    Context context;
    String userId;
    String ipAddress;
    String url;

    public IncomingRequestsAdapter(List<User1> userList, Context context, String userId) {
        this.userList = userList;
        this.context = context;
        this.userId = userId;
        initializeIpAddress();
    }

    private void initializeIpAddress() {
        ipAddress = context.getString(R.string.ip_addr);
        url = "http://" + ipAddress;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View component = LayoutInflater.from(context).inflate(R.layout.item_friend_request, parent, false);
        return new MyViewHolder(component);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(userList.get(position).getFullName());
        Picasso.get().load(url + userList.get(position).getProfilePhotoUrl() + ".jpg").into(holder.profile_pic);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView profile_pic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.requestUserName);
            profile_pic = itemView.findViewById(R.id.profile_pic);
        }
    }
}


