package com.project.myapplication;

// OutgoingRequestsAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OutgoingRequestsAdapter extends RecyclerView.Adapter<OutgoingRequestsAdapter.MyViewHolder> {

    List<User1> userList;
    Context context;
    String userId;
    String ipAddress;
    String url;

    public OutgoingRequestsAdapter(List<User1> userList, Context context, String userId) {
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
        View component = LayoutInflater.from(context).inflate(R.layout.item_outgoing_request, parent, false);
        return new MyViewHolder(component);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User1 incomingUser = userList.get(position);
        String incomingUserId = incomingUser.getId();

        holder.name.setText(incomingUser.getFullName());
        Picasso.get().load(url + incomingUser.getProfilePhotoUrl() + ".jpg").into(holder.profile_pic);

        holder.cancelRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // remove from receiver's incoming requests list
                mDatabase.child("userRequestsIncoming").child(incomingUserId).child(userId).removeValue();

                // remove from sender's outgoing requests list
                mDatabase.child("userRequestsOutgoing").child(userId).child(incomingUserId).removeValue();

                Toast.makeText(context, "Request Cancelled", Toast.LENGTH_LONG).show();
            }
        });
    }

    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView profile_pic;
        ImageButton cancelRequest;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            name = itemView.findViewById(R.id.outgoingUserName);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            cancelRequest = itemView.findViewById(R.id.cancelButton);
        }
    }
}

