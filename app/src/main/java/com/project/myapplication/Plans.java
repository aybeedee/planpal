package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Plans extends AppCompatActivity {

    String userId;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    String groupId;

    AppCompatButton newPlanButton;

    RecyclerView plansRV;
    PlansAdapter plansAdapter;
    List<Plan> plansList;

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
        setContentView(R.layout.activity_plans);

        newPlanButton = findViewById(R.id.newPlanButton);
        plansRV = findViewById(R.id.PlanRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        groupId = getIntent().getStringExtra("groupId");

        plansList = new ArrayList<>();
        plansAdapter = new PlansAdapter(plansList, Plans.this, userId, groupId);
        plansRV.setAdapter(plansAdapter);
        RecyclerView.LayoutManager plansLM = new LinearLayoutManager(Plans.this);
        plansRV.setLayoutManager(plansLM);

        mDatabase.child("groupPlans").child(groupId).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ObjectReference planObjectRef = snapshot.getValue(ObjectReference.class);
                Log.d("PLAN FETCHED: ", planObjectRef.getId());

                mDatabase.child("plans").child(planObjectRef.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (task.isSuccessful()) {

                            Plan planObject = task.getResult().getValue(Plan.class);
                            Log.d("PLAN NAME: ", planObject.getName());
                            plansList.add(planObject);
                            plansAdapter.notifyDataSetChanged();
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

        newPlanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Plans.this, PostPlan.class);
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