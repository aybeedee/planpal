package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.inappmessaging.model.Button;
import com.squareup.picasso.Picasso;

public class ProfileViewSelf extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String userId,email,name;
    ImageView profilePic;

    TextView email1,name1;
    ImageView friends,schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view_self);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();
        profilePic = findViewById(R.id.profile_pic);
        email1=findViewById(R.id.email);
        name1=findViewById(R.id.name);
        friends=findViewById(R.id.friends);
        schedule=findViewById(R.id.schedule);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {

                    Log.e("firebase", "Error getting data", task.getException());
                }

                else {
                    User1 userObject = task.getResult().getValue(User1.class);
                    Picasso.get().load(userObject.getProfilePhotoUrl()).into(profilePic);
                    email1.setText(userObject.getEmail());
                    name1.setText(userObject.getFullName());
                }
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileViewSelf.this, FriendsList.class);
                startActivity(intent);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileViewSelf.this, Schedule.class);
                startActivity(intent);
            }
        });


    }
}