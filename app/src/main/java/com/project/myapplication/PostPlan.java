package com.project.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.util.Calendar;

public class PostPlan extends AppCompatActivity {

    TextView dateEditText, startTimeEditText, endTimeEditText;
    AppCompatButton createPlanButton;

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