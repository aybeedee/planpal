package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendsList extends AppCompatActivity {

    ImageView addFriendButton;
    String userId;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        addFriendButton = findViewById(R.id.addFriend);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                final AlertDialog alertDialog = new AlertDialog.Builder(FriendsList.this).create();
//                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                Dialog dialog = new Dialog(FriendsList.this);
                View customLayout = getLayoutInflater().inflate(R.layout.add_friend_dialouge, null);

                Button addFriendButton = customLayout.findViewById(R.id.addFriendButton);
                EditText emailEditText = customLayout.findViewById(R.id.emailEditText);

                addFriendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendFriendRequest(emailEditText.getText().toString());
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(customLayout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    private void sendFriendRequest(String friendEmail) {

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Boolean foundUser = false;
                String foundUserId = "";

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User1 userObject = userSnapshot.getValue(User1.class);
                    Log.d("user-log", userObject.getEmail());
                    if (friendEmail.equals(userObject.getEmail())) {
                        foundUser = true;
                        foundUserId = userObject.getId();
                        mDatabase.child("userRequestsOutgoing")
                                .child(userId)
                                .child(foundUserId)
                                .child("id")
                                .setValue(foundUserId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(FriendsList.this, "Friend Request Sent 1", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(FriendsList.this, "Friend Request Failed!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        mDatabase.child("userRequestsIncoming")
                                .child(foundUserId)
                                .child(userId)
                                .child("id")
                                .setValue(userId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(FriendsList.this, "Friend Request Sent 2", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(FriendsList.this, "Friend Request Failed!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }

                if (!foundUser) {
                    Toast.makeText(FriendsList.this, "email not found!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FriendsList.this, "DB ERR: " + error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}