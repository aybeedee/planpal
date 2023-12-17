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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlanDetails extends AppCompatActivity {

    DatabaseReference mDatabase;

    String groupId, planId;

    TextView planName, planLocation, planDate, planStartTime, planEndTime;
    TextView allFreeSchedule, bestFitSchedule;

    List<UserSchedule> userSchedules;
    String thisPlanDate, thisPlanStartTime, thisPlanEndTime;

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

                    thisPlanDate = planObject.getDate();
                    thisPlanStartTime = planObject.getStartTime();
                    thisPlanEndTime = planObject.getEndTime();

                    planDate.setText(thisPlanDate);

                    try {
                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                        Date _24HourStartTime = _24HourSDF.parse(thisPlanStartTime);
                        Date _24HourEndTime = _24HourSDF.parse(thisPlanEndTime);
                        planStartTime.setText(_12HourSDF.format(_24HourStartTime));
                        planEndTime.setText(_12HourSDF.format(_24HourEndTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        userSchedules = new ArrayList<>();

        // fetch schedules of all group members
        mDatabase.child("groupUsers").child(groupId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long totalSchedules = snapshot.getChildrenCount();

                final long[] scheduleIndex = {0};

                for (DataSnapshot groupMemberSnapshot : snapshot.getChildren()) {

                    ObjectReference groupMemberRef = groupMemberSnapshot.getValue(ObjectReference.class);

                    mDatabase.child("userSchedules").child(groupMemberRef.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                            if (task.isSuccessful()) {

                                UserSchedule scheduleObject = task.getResult().getValue(UserSchedule.class);
                                userSchedules.add(scheduleObject);

                                scheduleIndex[0]++;

                                if (scheduleIndex[0] == totalSchedules) {

                                    String[] allFreeResult = AllFreeScheduler();

                                    if (allFreeResult != null) {

                                        Log.d("all-free-start", allFreeResult[0]);
                                        Log.d("all-free-end", allFreeResult[1]);
                                    }

                                    try {

                                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                        Date _24HourStartTime = _24HourSDF.parse(allFreeResult[0]);
                                        Date _24HourEndTime = _24HourSDF.parse(allFreeResult[1]);
                                        String _12HourStartTime = _12HourSDF.format(_24HourStartTime);
                                        String _12HourEndTime = _12HourSDF.format(_24HourEndTime);

                                        allFreeSchedule.setText(_12HourStartTime + " - " + _12HourEndTime);

                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }

                                    String[] bestFitResult = BestFitScheduler();

                                    if (bestFitResult != null) {

                                        Log.d("best-fit-start", bestFitResult[0]);
                                        Log.d("best-fit-end", bestFitResult[1]);
                                    }

                                    try {

                                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                        Date _24HourStartTime = _24HourSDF.parse(bestFitResult[0]);
                                        Date _24HourEndTime = _24HourSDF.parse(bestFitResult[1]);
                                        String _12HourStartTime = _12HourSDF.format(_24HourStartTime);
                                        String _12HourEndTime = _12HourSDF.format(_24HourEndTime);

                                        bestFitSchedule.setText(_12HourStartTime + " - " + _12HourEndTime);

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

    private String[] AllFreeScheduler() {

        Log.d("plan-schedule", thisPlanDate + " | " + thisPlanStartTime + " | " + thisPlanEndTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {

            Date planStartDate = dateFormat.parse(thisPlanDate + " " + thisPlanStartTime);
            Date planEndDate = dateFormat.parse(thisPlanDate + " " + thisPlanEndTime);

            Date largestFreeStartTime = planStartDate;
            Date largestFreeEndTime = planEndDate;

            Log.d("all-free-before-running", largestFreeStartTime.toString() + " | " + largestFreeEndTime.toString());

            for (int i = 0; i < userSchedules.size(); i++) {

                Log.d("user-schedule", userSchedules.get(i).getDate() + " | " + userSchedules.get(i).getStartTime() + " | " + userSchedules.get(i).getEndTime());

                if (userSchedules.get(i).getDate().equals(thisPlanDate)) {

                    Date userStartTime = dateFormat.parse(thisPlanDate + " " + userSchedules.get(i).getStartTime());
                    Date userEndTime = dateFormat.parse(thisPlanDate + " " + userSchedules.get(i).getEndTime());

                    if (userStartTime.after(largestFreeStartTime)) {
                        largestFreeStartTime = userStartTime;
                    }

                    if (userEndTime.before(largestFreeEndTime)) {
                        largestFreeEndTime = userEndTime;
                    }
                }
            }

            Log.d("all-free-after-running", largestFreeStartTime.toString() + " | " + largestFreeEndTime.toString());

            // Check if there is a common free time
            if (largestFreeStartTime.before(largestFreeEndTime)) {

                return new String[]{
                        dateFormat.format(largestFreeStartTime),
                        dateFormat.format(largestFreeEndTime)
                };

            } else {

                // No common free time found
                return null;
            }

        } catch (ParseException e) {

            e.printStackTrace();
            return null;
        }
    }

    private String[] BestFitScheduler() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try {
            Date planStart = sdf.parse(thisPlanStartTime);
            Date planEnd = sdf.parse(thisPlanEndTime);

            long bestFitOverlap = Long.MAX_VALUE;
            String[] bestFitPeriod = null;

            for (int i = 0; i < userSchedules.size(); i++) {

                if (userSchedules.get(i).getDate().equals(thisPlanDate)) {

                    Date userStart = sdf.parse(userSchedules.get(i).getStartTime());
                    Date userEnd = sdf.parse(userSchedules.get(i).getEndTime());

                    // get the common time overlap between the user's schedule and the plan timings, sets 0 if no overlap
                    long overlap = Math.max(0, Math.min(userEnd.getTime(), planEnd.getTime()) - Math.max(userStart.getTime(), planStart.getTime()));

                    if (overlap < bestFitOverlap) {
                        bestFitOverlap = overlap;

                        if (bestFitPeriod == null) {

                            bestFitPeriod = new String[] {
                                    userSchedules.get(i).getStartTime(),
                                    userSchedules.get(i).getEndTime()
                            };
                        }

                        else {
                            bestFitPeriod[0] = userSchedules.get(i).getStartTime();
                            bestFitPeriod[1] = userSchedules.get(i).getEndTime();
                        }
                    }
                }
            }

            return bestFitPeriod;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}