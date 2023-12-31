package com.project.myapplication;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Chats extends AppCompatActivity {

    String userId;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ImageView profileImage, group;
    EditText groupSearchInput;

    RecyclerView groupsRV;
    GroupsAdapter groupsAdapter;
    List<Group> groupsList;

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
        setContentView(R.layout.activity_chats);


        group = findViewById(R.id.group);
        profileImage = findViewById(R.id.profilePhoto);
        groupsRV = findViewById(R.id.contactRecyclerView);
        groupSearchInput = findViewById(R.id.search_bar);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve IP address from strings.xml
        String ipAddress = getString(R.string.ip_addr);
        // Concatenate the retrieved IP address with the URL
        String url = "http://" + ipAddress;

        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {

                    Log.e("firebase", "Error getting data", task.getException());
                }

                else {
                    User1 userObject = task.getResult().getValue(User1.class);
                    String fullProfilePath = url + userObject.getProfilePhotoUrl() + ".jpg";
                    Log.d("dp-path", fullProfilePath);
                    Picasso.get().load(fullProfilePath).into(profileImage);
                }
            }
        });

        groupsList = new ArrayList<>();
        groupsAdapter = new GroupsAdapter(groupsList, Chats.this, userId);
        groupsRV.setAdapter(groupsAdapter);
        RecyclerView.LayoutManager groupsLM = new LinearLayoutManager(Chats.this);
        groupsRV.setLayoutManager(groupsLM);

        mDatabase.child("userGroups").child(userId).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ObjectReference groupObjectRef = snapshot.getValue(ObjectReference.class);

                mDatabase.child("groups").child(groupObjectRef.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (task.isSuccessful()) {

                            Group groupObject = task.getResult().getValue(Group.class);
                            groupsList.add(groupObject);
                            groupsAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        groupSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    groupsList.clear();

                    mDatabase.child("userGroups").child(userId).addChildEventListener(new ChildEventListener() {

                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            ObjectReference groupObjectRef = snapshot.getValue(ObjectReference.class);

                            mDatabase.child("groups").child(groupObjectRef.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {

                                    if (task.isSuccessful()) {

                                        Group groupObject = task.getResult().getValue(Group.class);

                                        if (groupObject.getName().toLowerCase().contains(groupSearchInput.getText().toString().toLowerCase())) {

                                            groupsList.add(groupObject);
                                            groupsAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    return true;
                }

                return false;
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

        getOnBackPressedDispatcher().addCallback(Chats.this, new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {

                groupsList.clear();

                mDatabase.child("userGroups").child(userId).addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        ObjectReference groupObjectRef = snapshot.getValue(ObjectReference.class);

                        mDatabase.child("groups").child(groupObjectRef.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {

                                if (task.isSuccessful()) {

                                    Group groupObject = task.getResult().getValue(Group.class);
                                    groupsList.add(groupObject);
                                    groupsAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        getFCMToken();
    }

    void getFCMToken() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {

            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (task.isSuccessful()) {

                    String token = task.getResult();
                    Log.d("Token", token);
                    mDatabase.child("users").child(mAuth.getUid().toString()).child("fcmToken").setValue(token);
                }
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