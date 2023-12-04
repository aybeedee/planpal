package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Chats extends AppCompatActivity {

    String userId;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ImageView profileImage,group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        group=findViewById(R.id.group);
        profileImage=findViewById(R.id.profilePhoto);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {

                    Log.e("firebase", "Error getting data", task.getException());
                }

                else {
                    User1 userObject = task.getResult().getValue(User1.class);
                    Picasso.get().load(userObject.getProfilePhotoUrl()).into(profileImage);
                }
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open the new activity
                Intent intent = new Intent(Chats.this, creategroup1.class);
                // Start the new activity
                startActivity(intent);
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open the new activity
                Intent intent = new Intent(Chats.this, ProfileViewSelf.class);
                // Start the new activity
                startActivity(intent);
            }
        });
    }
}