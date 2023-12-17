package com.project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class groupDetailsMember extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    String userId;
    String imageServerURL;
    String groupId;

    TextView groupName, groupDescription;
    TextView planName, planTime, planDate, attendingCount, notAttendingCount;
    ImageView groupPhoto;
    AppCompatButton plansButton, viewPlanButton;

    RecyclerView membersRV;
    FriendsListAdapter membersAdapter;
    List<User1> membersList;

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
        setContentView(R.layout.activity_group_details_member);

        groupName = findViewById(R.id.groupName);
        groupDescription = findViewById(R.id.descriptionText);
        planName = findViewById(R.id.planName);
        planDate = findViewById(R.id.planDate);
        planTime = findViewById(R.id.planTime);
        attendingCount = findViewById(R.id.attendingCount);
        notAttendingCount = findViewById(R.id.notAttendingCount);
        groupPhoto = findViewById(R.id.profile_pic);
        plansButton = findViewById(R.id.plansButton);
        viewPlanButton = findViewById(R.id.viewPlanButton);
        membersRV = findViewById(R.id.peopleGroup);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        groupId = getIntent().getStringExtra("groupId");

        imageServerURL = "http://" + getString(R.string.ip_addr);

        final String[] nextPlanId = new String[1];
        nextPlanId[0] = "";

        mDatabase.child("groups").child(groupId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    Group groupObject = task.getResult().getValue(Group.class);
                    groupName.setText(groupObject.getName());
                    groupDescription.setText(groupObject.getDescription());
                    Picasso.get().load(imageServerURL + groupObject.getGroupPhotoUrl() + ".jpg").into(groupPhoto);

                    final Long[] nextPlanTimestamp = {Long.valueOf("2150115799850")};
                    Long currentTimestamp = System.currentTimeMillis();

                    mDatabase.child("groupPlans").child(groupId).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            final String[] x = new String[1];

                            for (DataSnapshot planSnapshot : snapshot.getChildren()) {

                                ObjectReference planObjectRef = planSnapshot.getValue(ObjectReference.class);

                                mDatabase.child("plans").child(planObjectRef.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                                        if (task.isSuccessful()) {

                                            Plan planObject = task.getResult().getValue(Plan.class);

                                            Long planTimestamp = planObject.getTimestamp();

                                            if ((planTimestamp >= currentTimestamp) && (planTimestamp <= nextPlanTimestamp[0])) {

                                                nextPlanId[0] = planObject.getId();
                                                Log.d("check", "reached here");
                                                Log.d("reached-id", nextPlanId[0] + "-apple");
                                                nextPlanTimestamp[0] = planTimestamp;

                                                planName.setText(planObject.getName());
                                                planDate.setText(planObject.getDate());
                                                attendingCount.setText(planObject.getAttendingCount().toString());
                                                notAttendingCount.setText(planObject.getNotAttendingCount().toString());

                                                try {
                                                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                                    Date _24HourDt = _24HourSDF.parse(planObject.getStartTime());
                                                    planTime.setText(_12HourSDF.format(_24HourDt));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        membersList = new ArrayList<>();
        membersAdapter = new FriendsListAdapter(membersList, groupDetailsMember.this, userId);
        membersRV.setAdapter(membersAdapter);
        RecyclerView.LayoutManager membersLM = new LinearLayoutManager(groupDetailsMember.this);
        membersRV.setLayoutManager(membersLM);

        mDatabase.child("groupUsers").child(groupId).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ObjectReference memberObjectRef = snapshot.getValue(ObjectReference.class);

                mDatabase.child("users").child(memberObjectRef.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (task.isSuccessful()) {

                            User1 memberObject = task.getResult().getValue(User1.class);
                            membersList.add(memberObject);
                            membersAdapter.notifyDataSetChanged();
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

        plansButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(groupDetailsMember.this, Plans.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();

        if (on) {

            winParams.flags |= bits;
        }

        else {

            winParams.flags &= ~bits;
        }

        win.setAttributes(winParams);
    }
}