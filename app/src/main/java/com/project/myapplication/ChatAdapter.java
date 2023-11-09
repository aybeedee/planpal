package com.project.myapplication;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<Chatmessage> messages;
    private MediaPlayer mediaPlayer; // MediaPlayer for playing voice notes
    private int playingPosition = -1;

    public ChatAdapter(List<Chatmessage> messages) {
        this.messages = messages;
        mediaPlayer = new MediaPlayer();
    }

    public void addMessages(List<Chatmessage> newMessages) {
        messages.addAll(newMessages);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Chatmessage message = messages.get(holder.getAdapterPosition());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timestamp = sdf.format(message.getTimestamp());

        if ("text".equals(message.getType())) {
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);

            holder.messageTextView.setText(message.getText());
        } else if ("image".equals(message.getType())) {
            holder.messageTextView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);

            Picasso.get().load(message.getText()).into(holder.imageView);

            // Load and display the image using Picasso (as in your previous code)
        }

        holder.timestampTextView.setText(timestamp);
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView timestampTextView;
        public ImageView imageView;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


}

