package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlanDetails extends AppCompatActivity {

    DatabaseReference mDatabase;

    String groupId, planId;

    TextView planName, planLocation, planDate, planStartTime, planEndTime;
    TextView allFreeSchedule, bestFitSchedule;

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
        setContentView(R.layout.activity_plan_details);

        planName = findViewById(R.id.planName);
        planLocation = findViewById(R.id.planLocation);
        planDate = findViewById(R.id.planDate);
        planStartTime = findViewById(R.id.planStartTime);
        planEndTime = findViewById(R.id.planEndTime);
        allFreeSchedule = findViewById(R.id.allFreeSchedule);
        bestFitSchedule = findViewById(R.id.bestFitSchedule);

        groupId = getIntent().getStringExtra("groupId");
        planId = getIntent().getStringExtra("planId");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("plans").child(planId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    Plan planObject = task.getResult().getValue(Plan.class);

                    Long planTimestamp = planObject.getTimestamp();

                    planName.setText(planObject.getName());
                    planLocation.setText(planObject.getLocation());
                    planDate.setText(planObject.getDate());

                    try {
                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                        Date _24HourStartTime = _24HourSDF.parse(planObject.getStartTime());
                        Date _24HourEndTime = _24HourSDF.parse(planObject.getEndTime());
                        planStartTime.setText(_12HourSDF.format(_24HourStartTime));
                        planEndTime.setText(_12HourSDF.format(_24HourEndTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
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