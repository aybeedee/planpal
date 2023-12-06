package com.project.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.security.AccessControlContext;
import java.util.List;
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyViewHolder> {

    List<User1> friendsList;
    Context context;
    String userId;
    String ipAddress;
    String url;

    private void initializeIpAddress() {
        ipAddress = context.getString(R.string.ip_addr);
        url = "http://" + ipAddress;
    }

    public FriendsListAdapter(List<User1> friendsList, Context context, String userId) {
        this.friendsList = friendsList;
        this.context = context;
        this.userId = userId;
        initializeIpAddress();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View component = LayoutInflater.from(context).inflate(R.layout.friends_recycler_view, parent, false);
        return new MyViewHolder(component);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User1 user = friendsList.get(position);
        Log.d("user-in-rv", user.getId() + " " + user.getEmail() + " " + user.getFullName() + " " + user.getProfilePhotoUrl());
        holder.name.setText(friendsList.get(position).getFullName());
        holder.email.setText(friendsList.get(position).getEmail());
        Picasso.get().load(url + friendsList.get(position).getProfilePhotoUrl() + ".jpg").into(holder.profile_pic);
    }

    @Override
    public int getItemCount() {
        return (friendsList.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        ImageView profile_pic;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            profile_pic=itemView.findViewById(R.id.profile_pic);
        }

    }
}
