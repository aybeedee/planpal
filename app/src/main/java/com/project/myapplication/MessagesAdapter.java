package com.project.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Message> messagesList;
    Context context;
    String userId;
    String imageServerURL;

    public MessagesAdapter(List<Message> messagesList, Context context, String userId) {

        this.messagesList = messagesList;
        this.context = context;
        this.userId = userId;
        this.imageServerURL = "http://" + context.getString(R.string.ip_addr);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {

            return new SenderTextVH(LayoutInflater.from(context).inflate(R.layout.message_sent_component, parent, false));
        }

        else if (viewType == 1) {

            return new SenderImgVH(LayoutInflater.from(context).inflate(R.layout.message_sent_img_component, parent, false));
        }

        else if (viewType == 2) {

            return new ReceiverTextVH(LayoutInflater.from(context).inflate(R.layout.message_received_component, parent, false));
        }

        return new ReceiverImgVH(LayoutInflater.from(context).inflate(R.layout.message_received_img_component, parent, false));
    }

    @Override
    public int getItemViewType(int position) {

        if (messagesList.get(position).getSenderId().equals(userId)) {

            if (messagesList.get(position).getText().equals("")) {

                if (!messagesList.get(position).getImageUrl().equals("")) {

                    return 1;
                }
            }

            else {

                return 0;
            }
        }

        else {

            if (messagesList.get(position).getText().equals("")) {

                if (!messagesList.get(position).getImageUrl().equals("")) {

                    return 3;
                }
            }

            else {

                return 2;
            }
        }

        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (holder.getItemViewType() == 0) {

            SenderTextVH senderTextVH = (SenderTextVH) holder;

            String text = messagesList.get(position).getText();
            Long timestamp = messagesList.get(position).getTimestamp();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date(timestamp);
            String formattedTime = simpleDateFormat.format(date);

            senderTextVH.text.setText(text);
            senderTextVH.time.setText(formattedTime);
        }

        else if (holder.getItemViewType() == 1) {

            SenderImgVH senderImgVH = (SenderImgVH) holder;

            String imageUrl = messagesList.get(position).getImageUrl();
            Long timestamp = messagesList.get(position).getTimestamp();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date(timestamp);
            String formattedTime = simpleDateFormat.format(date);

            Picasso.get().load(imageServerURL + imageUrl + ".jpg").into(senderImgVH.img);
            senderImgVH.time.setText(formattedTime);
        }

        else if (holder.getItemViewType() == 2) {

            ReceiverTextVH receiverTextVH = (ReceiverTextVH) holder;

            String senderId = messagesList.get(position).getSenderId();
            String text = messagesList.get(position).getText();
            Long timestamp = messagesList.get(position).getTimestamp();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date(timestamp);
            String formattedTime = simpleDateFormat.format(date);

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("users").child(senderId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if (task.isSuccessful()) {

                        User1 userObject = task.getResult().getValue(User1.class);
                        receiverTextVH.senderName.setText(userObject.getFullName());
                    }

                    else {

                        Toast.makeText(context, "Could not fetch user", Toast.LENGTH_LONG).show();
                    }
                }
            });

            receiverTextVH.text.setText(text);
            receiverTextVH.time.setText(formattedTime);
        }

        else {

            ReceiverImgVH receiverImgVH = (ReceiverImgVH) holder;

            String senderId = messagesList.get(position).getSenderId();
            String imageUrl = messagesList.get(position).getImageUrl();
            Long timestamp = messagesList.get(position).getTimestamp();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date(timestamp);
            String formattedTime = simpleDateFormat.format(date);

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("users").child(senderId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if (task.isSuccessful()) {

                        User1 userObject = task.getResult().getValue(User1.class);
                        receiverImgVH.senderName.setText(userObject.getFullName());
                    }

                    else {

                        Toast.makeText(context, "Could not fetch user", Toast.LENGTH_LONG).show();
                    }
                }
            });

            Picasso.get().load(imageServerURL + imageUrl + ".jpg").into(receiverImgVH.img);
            receiverImgVH.time.setText(formattedTime);
        }
    }

    @Override
    public int getItemCount() {

        return (messagesList.size());
    }

    // viewType 0
    public class SenderTextVH extends RecyclerView.ViewHolder {

        TextView time, text;

        public SenderTextVH(@NonNull View itemView) {

            super(itemView);

            time = itemView.findViewById(R.id.timestamp);
            text = itemView.findViewById(R.id.text);
        }
    }

    // viewType 1
    public class SenderImgVH extends RecyclerView.ViewHolder {

        TextView time;
        ImageView img;

        public SenderImgVH(@NonNull View itemView) {

            super(itemView);

            time = itemView.findViewById(R.id.timestamp);
            img = itemView.findViewById(R.id.img);
        }
    }

    // viewType 2
    public class ReceiverTextVH extends RecyclerView.ViewHolder {

        TextView senderName, time, text;

        public ReceiverTextVH(@NonNull View itemView) {

            super(itemView);

            senderName = itemView.findViewById(R.id.senderName);
            time = itemView.findViewById(R.id.timestamp);
            text = itemView.findViewById(R.id.text);
        }
    }

    // viewType 3
    public class ReceiverImgVH extends RecyclerView.ViewHolder {

        TextView senderName, time;
        ImageView img;

        public ReceiverImgVH(@NonNull View itemView) {

            super(itemView);

            senderName = itemView.findViewById(R.id.senderName);
            time = itemView.findViewById(R.id.timestamp);
            img = itemView.findViewById(R.id.img);
        }
    }
}