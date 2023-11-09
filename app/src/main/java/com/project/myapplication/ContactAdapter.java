package com.project.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contactList;

    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        // Bind data to the views
        holder.contactName.setText(contact.getName());
        holder.latestChat.setText(contact.getLatestChat());
        holder.chatTime.setText(contact.getChatTime());
        holder.contactProfileImage.setImageResource(contact.getProfileImageResource());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView contactProfileImage;
        TextView contactName, latestChat, chatTime;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactProfileImage = itemView.findViewById(R.id.contactProfileImage);
            contactName = itemView.findViewById(R.id.contactName);
            latestChat = itemView.findViewById(R.id.latestChat);
            chatTime = itemView.findViewById(R.id.chatTime);
        }
    }
}

