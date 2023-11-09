package com.project.myapplication;

// IncomingRequestsAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IncomingRequestsAdapter extends RecyclerView.Adapter<IncomingRequestsAdapter.RequestViewHolder> {

    private List<FriendRequestModel> requests;
    private Context context;

    public IncomingRequestsAdapter(List<FriendRequestModel> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        FriendRequestModel request = requests.get(position);

        // Set data to views
        holder.userName.setText(request.getUserName());
        // Set more data as needed

        // Set click listeners
        holder.acceptButton.setOnClickListener(v -> {
            // Handle accept button click
        });

        holder.rejectButton.setOnClickListener(v -> {
            // Handle reject button click
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName;
        ImageButton acceptButton;
        ImageButton rejectButton;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.requestProfileImage);
            userName = itemView.findViewById(R.id.requestUserName);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}

