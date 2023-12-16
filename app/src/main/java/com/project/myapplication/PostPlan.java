package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PostPlan extends AppCompatActivity {

    DatabaseReference mDatabase;

    TextView dateEditText, startTimeEditText, endTimeEditText;
    AppCompatButton createPlanButton;
    EditText planNameEditText, locationEditText;

    String groupId;

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
        setContentView(R.layout.activity_postplan);

        dateEditText = findViewById(R.id.dateEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        createPlanButton = findViewById(R.id.createPlanButton);
        planNameEditText = findViewById(R.id.planNameEditText);
        locationEditText = findViewById(R.id.locationEditText);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        groupId = getIntent().getStringExtra("groupId");

        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PostPlan.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                },
                year, month, day);

                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(93, 57, 201));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(93, 57, 201));
            }
        });

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(PostPlan.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                startTimeEditText.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);

                timePickerDialog.show();
                timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(93, 57, 201));
                timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(93, 57, 201));
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(PostPlan.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        endTimeEditText.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);

                timePickerDialog.show();
                timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(93, 57, 201));
                timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(93, 57, 201));
            }
        });

        createPlanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String planId = mDatabase.child("plans").push().getKey();

                String planName = planNameEditText.getText().toString();
                String planLocation = locationEditText.getText().toString();
                String planDate = dateEditText.getText().toString();
                String planStartTime = startTimeEditText.getText().toString();
                String planEndTime = endTimeEditText.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                Date date = null;

                try {

                    date = dateFormat.parse(planDate);

                }

                catch (ParseException e) {

                    e.printStackTrace();
                }

                Long planTimestamp = date.getTime();

                Plan plan = new Plan(planId, planName, planLocation, planDate, planTimestamp, planStartTime, planEndTime, 0, 0);

                // add new plan to database
                mDatabase.child("plans").child(planId).setValue(plan).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mDatabase.child("groupPlans").child(groupId).child(planId).child("id").setValue(planId).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Toast.makeText(PostPlan.this, "Plan Created Successfully", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(PostPlan.this, Plans.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    else {

                                        Toast.makeText(PostPlan.this, "Plan Creation Failed!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                        else {

                            Toast.makeText(PostPlan.this, "Plan Creation Failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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