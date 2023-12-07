package com.project.myapplication;

// IncomingRequestsAdapter.java

import android.annotation.SuppressLint;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        User1 incomingUser = userList.get(position);
        String incomingUserId = incomingUser.getId();

        holder.name.setText(incomingUser.getFullName());
        Picasso.get().load(url + incomingUser.getProfilePhotoUrl() + ".jpg").into(holder.profile_pic);

        holder.acceptRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // add friend to user's friends list
                mDatabase.child("userFriends").child(userId).child(incomingUserId).child("id").setValue(incomingUserId).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            // add user to friend's friends list
                            mDatabase.child("userFriends").child(incomingUserId).child(userId).child("id").setValue(userId).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        // remove request from incoming of user
                                        mDatabase.child("userRequestsIncoming").child(userId).child(incomingUserId).removeValue();

                                        // remove request from outgoing of friend
                                        mDatabase.child("userRequestsOutgoing").child(incomingUserId).child(userId).removeValue();

                                        Toast.makeText(context, "Request Accepted Successfully!", Toast.LENGTH_LONG).show();
                                    }

                                    else {
                                        Toast.makeText(context, "Request Accept Failed!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                        else {

                            Toast.makeText(context, "Request Accept Failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        holder.rejectRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // remove from receiver's incoming requests list
                mDatabase.child("userRequestsIncoming").child(userId).child(incomingUserId).removeValue();

                // remove from sender's outgoing requests list
                mDatabase.child("userRequestsOutgoing").child(incomingUserId).child(userId).removeValue();

                Toast.makeText(context, "Request Rejected", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView profile_pic;
        ImageButton acceptRequest;
        ImageButton rejectRequest;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            name = itemView.findViewById(R.id.requestUserName);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            acceptRequest = itemView.findViewById(R.id.acceptButton);
            rejectRequest = itemView.findViewById(R.id.rejectButton);
        }
    }
}


