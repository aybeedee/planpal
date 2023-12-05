package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsList extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    RecyclerView friends_recycler;
    FriendsListAdapter friendsAdapter;
    String userId;
    List<User1> UsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();
        friends_recycler=findViewById(R.id.friendsrecyclerView);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve IP address from strings.xml
        String ipAddress = getString(R.string.ip_addr);
        // Concatenate the retrieved IP address with the URL
        String url = "http://" + ipAddress;

        // Reference to the userFriends node for the specific user
        DatabaseReference userFriendsRef = mDatabase.child("userFriends").child(userId);

        // Initialize a list to store user friend data
        UsersList = new ArrayList<>();

        friendsAdapter = new FriendsListAdapter(UsersList , FriendsList.this, userId);
        friends_recycler.setAdapter(friendsAdapter);
        RecyclerView.LayoutManager featuredLM = new LinearLayoutManager(FriendsList.this);
        friends_recycler.setLayoutManager(featuredLM);

        // Retrieve user friend IDs


//        userFriendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String friendId = snapshot.getKey(); // Get each friend ID
//                    // Use the friend ID to fetch user data from the 'users' node
//                    DatabaseReference userRef = mDatabase.child("users").child(friendId);
//                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            // Retrieve user data and add it to the list
//                            User1 user = dataSnapshot.getValue(User1.class); // Assuming User class exists
//                            if (user != null) {
//                                UsersList.add(user);
//                                friendsAdapter.notifyDataSetChanged();
//                                // Notify adapter or perform operations with the fetched user data
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle any errors
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle any errors
//            }
//        });

        userFriendsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                String friendId = dataSnapshot.getKey(); // Get each friend ID

                DatabaseReference userRef = mDatabase.child("users").child(friendId);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User1 user = dataSnapshot.getValue(User1.class); // Assuming User class exists
                        if (user != null) {
                            UsersList.add(user);
                            friendsAdapter.notifyDataSetChanged();
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




    }
}