package com.project.myapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private List<Friend> contacts;

    public ContactsAdapter(List<Friend> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Friend contact = contacts.get(position);

        holder.profileImage.setImageResource(contact.getProfileImageResId());
        holder.contactName.setText(contact.getName());
        holder.contactEmail.setText(contact.getEmail());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView contactName;
        TextView contactEmail;

        ContactViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.contactProfileImage);
            contactName = itemView.findViewById(R.id.contactName);
            contactEmail = itemView.findViewById(R.id.contactEmail);
        }
    }
}

