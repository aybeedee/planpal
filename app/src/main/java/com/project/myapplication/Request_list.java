package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Request_list extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    TextView request,friendText;

    String userId;

    List<User1> UsersList;
    List<User1> UsersList2;

    RecyclerView incoming_recycler,outgoing_recycler;
    IncomingRequestsAdapter incomingAdapter;
    OutgoingRequestsAdapter outgoingAdapter;

    TextView emptyRecyclerViewMessage1,emptyRecyclerViewMessage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        request=findViewById(R.id.requestsText);
        friendText=findViewById(R.id.friendsText);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();
        incoming_recycler=findViewById(R.id.incomingrecyclerView);
        outgoing_recycler=findViewById(R.id.outgoingrecyclerView);
        emptyRecyclerViewMessage1=findViewById(R.id.emptyRecyclerViewMessage1);
        emptyRecyclerViewMessage2=findViewById(R.id.emptyRecyclerViewMessage2);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve IP address from strings.xml
        String ipAddress = getString(R.string.ip_addr);
        // Concatenate the retrieved IP address with the URL
        String url = "http://" + ipAddress;

        DatabaseReference IncomingRequestRef = mDatabase.child("userRequestsIncoming").child(userId);

        // Initialize a list to store user friend data
        UsersList = new ArrayList<>();

        incomingAdapter = new IncomingRequestsAdapter(UsersList ,Request_list.this, userId);
        incoming_recycler.setAdapter(incomingAdapter);
        RecyclerView.LayoutManager featuredLM = new LinearLayoutManager(Request_list.this);
        incoming_recycler.setLayoutManager(featuredLM);

        IncomingRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                String requestId = dataSnapshot.getKey(); // Get each friend ID

                DatabaseReference userRef = mDatabase.child("users").child(requestId);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User1 user = dataSnapshot.getValue(User1.class); // Assuming User class exists
                        if (user != null) {
                            UsersList.add(user);
                            incomingAdapter.notifyDataSetChanged();

//                            // Check if the adapter's item count is zero and manage visibility accordingly
//                            if (incomingAdapter.getItemCount() == 0) {
//                                incoming_recycler.setVisibility(View.GONE);
//                                emptyRecyclerViewMessage1.setVisibility(View.VISIBLE);
//                            } else {
//                                incoming_recycler.setVisibility(View.VISIBLE);
//                                emptyRecyclerViewMessage1.setVisibility(View.GONE);
//                            }
                            // Notify adapter or perform operations with the fetched user data
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });
            }

            // Other overridden methods of ChildEventListener
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        DatabaseReference OutgoingRequestRef = mDatabase.child("userRequestsOutgoing").child(userId);

        // Initialize a list to store user friend data
        UsersList2 = new ArrayList<>();

        outgoingAdapter = new OutgoingRequestsAdapter(UsersList2 ,Request_list.this, userId);
        outgoing_recycler.setAdapter(outgoingAdapter);
        RecyclerView.LayoutManager featuredLM1 = new LinearLayoutManager(Request_list.this);
        outgoing_recycler.setLayoutManager(featuredLM1);

        OutgoingRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                String requestId = dataSnapshot.getKey(); // Get each friend ID

                DatabaseReference userRef = mDatabase.child("users").child(requestId);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User1 user = dataSnapshot.getValue(User1.class); // Assuming User class exists
                        if (user != null) {
                            UsersList2.add(user);
                            outgoingAdapter.notifyDataSetChanged();
                            // Notify adapter or perform operations with the fetched user data
//                            // Check if the adapter's item count is zero and manage visibility accordingly
//                            if (outgoingAdapter.getItemCount() == 0) {
//                                outgoing_recycler.setVisibility(View.GONE);
//                                emptyRecyclerViewMessage2.setVisibility(View.VISIBLE);
//                            } else {
//                                outgoing_recycler.setVisibility(View.VISIBLE);
//                                emptyRecyclerViewMessage2.setVisibility(View.GONE);
//                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });
            }

            // Other overridden methods of ChildEventListener
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });







        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Request_list.this, Request_list.class);
                startActivity(intent);
            }
        });

        friendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Request_list.this, FriendsList.class);
                startActivity(intent);
            }
        });
    }
}