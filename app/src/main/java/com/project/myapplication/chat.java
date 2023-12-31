package com.project.myapplication;

import static com.android.volley.Request.Method.POST;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class chat extends AppCompatActivity {

    String userId, userName;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    String imageServerURL;
    String groupId;
    String text, imageUrl;

    Uri image;
    String encodedImage, imageName;

    ImageView groupPhoto;
    TextView groupName;
    ImageButton sendButton, addImageButton, backButton;
    EditText messageTextInput;
    AppCompatButton plansButton;

    RecyclerView messagesRV;
    MessagesAdapter messageAdapter;
    List<Message> messagesList;

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
        setContentView(R.layout.activity_chat);

        groupName = findViewById(R.id.groupName);
        groupPhoto = findViewById(R.id.groupPhoto);
        sendButton = findViewById(R.id.sendButton);
        addImageButton = findViewById(R.id.addImageButton);
        backButton = findViewById(R.id.backButton);
        plansButton = findViewById(R.id.plansButton);
        messageTextInput = findViewById(R.id.messageEditText);
        messagesRV = findViewById(R.id.chatRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        text = "";
        imageUrl = "";

        imageServerURL = "http://" + getString(R.string.ip_addr);

        groupId = getIntent().getStringExtra("groupId");

        mDatabase.child("groups").child(groupId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    Group groupObject = task.getResult().getValue(Group.class);
                    groupName.setText(groupObject.getName());
                    Picasso.get().load(imageServerURL + groupObject.getGroupPhotoUrl() + ".jpg").into(groupPhoto);
                }
            }
        });

        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    User1 userObject = task.getResult().getValue(User1.class);
                    userName = userObject.getFullName();
                }
            }
        });

        groupPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat.this, groupDetailsMember.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });

        messagesList = new ArrayList<>();
        messageAdapter = new MessagesAdapter(messagesList, chat.this, userId);
        messagesRV.setAdapter(messageAdapter);
        RecyclerView.LayoutManager messageLM = new LinearLayoutManager(chat.this);
        messagesRV.setLayoutManager(messageLM);

        mDatabase.child("messages").child(groupId).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Message messageObject = snapshot.getValue(Message.class);
                messagesList.add(messageObject);
                messageAdapter.notifyDataSetChanged();
                messagesRV.scrollToPosition(messagesList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                text = messageTextInput.getText().toString();

                String messageId = mDatabase.child("messages").child(groupId).push().getKey();
                Long timestamp = System.currentTimeMillis();

                Message message = new Message(messageId, text, imageUrl, timestamp, userId);

                mDatabase.child("messages").child(groupId).child(messageId).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            if (text.equals("") && !imageUrl.equals("")) {

                                mDatabase.child("groupUsers").child(groupId).addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        long totalMembers = snapshot.getChildrenCount();

                                        final long[] memberIndex = {0};

                                        for (DataSnapshot groupMemberSnapshot : snapshot.getChildren()) {

                                            ObjectReference groupMemberRef = groupMemberSnapshot.getValue(ObjectReference.class);

                                            mDatabase.child("users").child(groupMemberRef.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {

                                                    if (task.isSuccessful()) {

                                                        User1 userObject = task.getResult().getValue(User1.class);
                                                        String receiverFCMToken = userObject.getFcmToken();

                                                        if (receiverFCMToken != null) {

                                                            sendNotification((userName + ": sent a photo"), userObject.getFcmToken());
                                                        }

                                                        memberIndex[0]++;

                                                        if (memberIndex[0] == totalMembers) {

                                                            text = "";
                                                            imageUrl = "";

                                                            messageTextInput.setText("");
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

                            else {

                                mDatabase.child("groupUsers").child(groupId).addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        long totalMembers = snapshot.getChildrenCount();

                                        final long[] memberIndex = {0};

                                        for (DataSnapshot groupMemberSnapshot : snapshot.getChildren()) {

                                            ObjectReference groupMemberRef = groupMemberSnapshot.getValue(ObjectReference.class);

                                            mDatabase.child("users").child(groupMemberRef.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {

                                                    if (task.isSuccessful()) {

                                                        User1 userObject = task.getResult().getValue(User1.class);
                                                        String receiverFCMToken = userObject.getFcmToken();

                                                        if (receiverFCMToken != null) {

                                                            sendNotification((userName + ": " + text), userObject.getFcmToken());
                                                        }

                                                        memberIndex[0]++;

                                                        if (memberIndex[0] == totalMembers) {

                                                            text = "";
                                                            imageUrl = "";

                                                            messageTextInput.setText("");
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

                            Toast.makeText(chat.this, "Message Sent", Toast.LENGTH_LONG).show();
                        }

                        else {

                            Toast.makeText(chat.this, "Message could not be sent", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });

        plansButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(chat.this, Plans.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });

        getOnBackPressedDispatcher().addCallback(chat.this, new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {

                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            image = data.getData();

            try {
                InputStream imageStream = getContentResolver().openInputStream(image);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);
                imageName = getImageName(image);
                // Retrieve IP address from strings.xml
                String ipAddress = getString(R.string.ip_addr);
                // Concatenate the retrieved IP address with the URL
                String url = "http://" + ipAddress + "/planpal/insert_pic.php";
                StringRequest request = new StringRequest(POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject res = new JSONObject(response);
                                    if (res.getInt("status") == 1) {
                                        imageUrl = "/planpal/pictures/" + imageName;
                                        Log.d("PictureURL", imageUrl);
                                        Toast.makeText(chat.this, imageUrl, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(chat.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error gracefully
                                    Toast.makeText(chat.this, "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    Toast.makeText(chat.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("encoded_string", encodedImage);
                        params.put("image_name", imageName);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(chat.this);
                queue.add(request);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    private String getImageName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    result = cursor.getString(index);
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    void sendNotification(String message, String receiverFCMToken) {

        JSONObject jsonObject = new JSONObject();

        try {

            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title", groupName.getText().toString());
            notificationObject.put("body", message);

            JSONObject dataObject = new JSONObject();
            dataObject.put("groupId", groupId);

            jsonObject.put("notification", notificationObject);
            jsonObject.put("data", dataObject);
            jsonObject.put("to", receiverFCMToken);

            callAPI(jsonObject);
        }

        catch (Exception e) {

        }
    }

    void callAPI(JSONObject jsonObject) {

        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        okhttp3.Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAqnI8b0s:APA91bHzs6_yTZ_0mCpHVW7QvafTCPmMev3orBEAXB9QjOvnXxMHILEdFiYDXXRLjtY8XfG9DzpQT1MnZkqaBbBM8Gov9VlGsKD5cYja84LVWLysSk7LHk3BtKLLWl5Jm6TIp-8Gacao")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {

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