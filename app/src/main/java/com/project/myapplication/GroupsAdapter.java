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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {

    List<Group> groupsList;
    Context context;
    String userId;
    String imageServerURL;

    public GroupsAdapter(List<Group> groupsList, Context context, String userId) {

        this.groupsList = groupsList;
        this.context = context;
        this.userId = userId;
        this.imageServerURL = "http://" + context.getString(R.string.ip_addr);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View component = LayoutInflater.from(context).inflate(R.layout.group_item_component, parent, false);
        return new MyViewHolder(component);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Group groupObject = groupsList.get(position);

        holder.groupName.setText(groupObject.getName());
        holder.groupDescription.setText(groupObject.getDescription().substring(0, Math.min(groupObject.getDescription().length(), 36)));
        Picasso.get().load(imageServerURL + groupObject.getGroupPhotoUrl() + ".jpg").into(holder.groupPhoto);

        holder.groupItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, chat.class);
                intent.putExtra("groupId", groupObject.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return (groupsList.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView groupPhoto;
        TextView groupName, groupDescription;
        LinearLayout groupItem;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            groupPhoto = itemView.findViewById(R.id.groupPhoto);
            groupName = itemView.findViewById(R.id.groupName);
            groupDescription = itemView.findViewById(R.id.groupDescription);
            groupItem = itemView.findViewById(R.id.groupItem);
        }
    }
}
