package com.project.myapplication;

// OutgoingRequestsAdapter.java

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

public class OutgoingRequestsAdapter extends RecyclerView.Adapter<OutgoingRequestsAdapter.RequestViewHolder> {

    private List<FriendRequestModel> requests;
    private Context context;

    public OutgoingRequestsAdapter(List<FriendRequestModel> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outgoing_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        FriendRequestModel request = requests.get(position);

        // Set data to views
        holder.userName.setText(request.getUserName());
        // Set more data as needed

        // Set click listener
        holder.cancelButton.setOnClickListener(v -> {
            // Handle cancel button click
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName;
        ImageButton cancelButton;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.outgoingProfileImage);
            userName = itemView.findViewById(R.id.outgoingUserName);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
}

