package com.project.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectedMembersAdapter extends RecyclerView.Adapter<SelectedMembersAdapter.MemberViewHolder> {
    private List<selectedmember> members;
    private OnDeleteClickListener deleteClickListener; // Listener for delete actions

    public SelectedMembersAdapter(List<selectedmember> members, OnDeleteClickListener deleteClickListener) {
        this.members = members;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        selectedmember member = members.get(position);
        holder.memberNameTextView.setText(member.getName());
        holder.memberProfileImageView.setImageResource(member.getProfileImageResource());

        // Set a click listener for the delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    // Interface for handling delete button clicks
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        ImageView memberProfileImageView;
        TextView memberNameTextView;
        ImageButton deleteButton;

        MemberViewHolder(View itemView) {
            super(itemView);
            memberProfileImageView = itemView.findViewById(R.id.contactProfileImage);
            memberNameTextView = itemView.findViewById(R.id.resultName);
            deleteButton = itemView.findViewById(R.id.plusButton);
        }
    }
}
