package com.project.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.MyViewHolder> {

    List<Plan> plansList;
    Context context;
    String userId;
    String groupId;
    String imageServerURL;

    public PlansAdapter(List<Plan> plansList, Context context, String userId, String groupId) {

        this.plansList = plansList;
        this.context = context;
        this.userId = userId;
        this.groupId = groupId;
        this.imageServerURL = "http://" + context.getString(R.string.ip_addr);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View component = LayoutInflater.from(context).inflate(R.layout.completed_plan_recycler_view, parent, false);
        return new MyViewHolder(component);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Plan planObject = plansList.get(position);

        Log.d("IN PLANS LIST ADAPTER: ", planObject.getName());

        holder.planName.setText(planObject.getName());
        holder.planDate.setText(planObject.getDate());
        holder.attendingCount.setText(planObject.getAttendingCount().toString());
        holder.notAttendingCount.setText(planObject.getNotAttendingCount().toString());

        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(planObject.getStartTime());
            holder.planTime.setText(_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (planObject.getNotAttendingCount() > planObject.getAttendingCount()) {
            holder.planStatus.setText("CANCELLED");
        }

        holder.planItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PlanDetails.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("planId", planObject.getId());
                context.startActivity(intent);
            }
        });

//        Group groupObject = groupsList.get(position);
//
//        holder.groupName.setText(groupObject.getName());
//        holder.groupDescription.setText(groupObject.getDescription().substring(0, Math.min(groupObject.getDescription().length(), 36)));
//        Picasso.get().load(imageServerURL + groupObject.getGroupPhotoUrl() + ".jpg").into(holder.groupPhoto);
//
//        holder.groupItem.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(context, chat.class);
//                intent.putExtra("groupId", groupObject.getId());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {

        return (plansList.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatButton planStatus;
        TextView planName, planTime, planDate, attendingCount, notAttendingCount;
        LinearLayout planItem;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            planStatus = itemView.findViewById(R.id.planStatus);
            planName = itemView.findViewById(R.id.planName);
            planTime = itemView.findViewById(R.id.planTime);
            planDate = itemView.findViewById(R.id.planDate);
            attendingCount = itemView.findViewById(R.id.attendingCount);
            notAttendingCount = itemView.findViewById(R.id.notAttendingCount);
            planItem = itemView.findViewById(R.id.planItem);
        }
    }
}
