package com.project.myapplication;

import static com.android.volley.Request.Method.POST;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class creategroup1 extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String userId;
    Uri image;
    String encodedImage, imageName, pictureUrl;
    ImageView groupPhotoButton;
    AppCompatButton createGroupButton;
    EditText groupName, groupDescription, memberEmail;
    ImageButton addMemberButton;

    List<ObjectReference> groupMembers;

    RecyclerView friends_recycler;
    FriendsListAdapter friendsAdapter;
    List<User1> groupMemberUsers;


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
        setContentView(R.layout.activity_creategroup1);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        groupPhotoButton = findViewById(R.id.circularBackground);
        createGroupButton = findViewById(R.id.creategroupbutton);
        groupName = findViewById(R.id.groupname);
        groupDescription = findViewById(R.id.groupdesc);
        memberEmail = findViewById(R.id.addmember);
        addMemberButton = findViewById(R.id.plusButton);
        friends_recycler = findViewById(R.id.groupMembersRV);

        groupMembers = new ArrayList<>();
        groupMemberUsers = new ArrayList<>();

        friendsAdapter = new FriendsListAdapter(groupMemberUsers, creategroup1.this, userId);
        friends_recycler.setAdapter(friendsAdapter);
        RecyclerView.LayoutManager featuredLM = new LinearLayoutManager(creategroup1.this);
        friends_recycler.setLayoutManager(featuredLM);

        groupPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });

        addMemberButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String addMemberEmail = memberEmail.getText().toString();

                mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Boolean foundUser = false;
                        String foundUserId = "";

                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                            User1 userObject = userSnapshot.getValue(User1.class);
                            Log.d("user-log", userObject.getEmail());

                            if (addMemberEmail.equals(userObject.getEmail())) {

                                foundUser = true;
                                foundUserId = userObject.getId();

                                String finalFoundUserId = foundUserId;

                                mDatabase.child("userFriends").child(userId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        Boolean foundUserFriend = false;

                                        for (DataSnapshot userFriendsSnapshot : snapshot.getChildren()) {

                                            ObjectReference userFriendRef = userFriendsSnapshot.getValue(ObjectReference.class);

                                            if (finalFoundUserId.equals(userFriendRef.getId())) {

                                                foundUserFriend = true;

                                                groupMembers.add(userFriendRef);
                                                groupMemberUsers.add(userObject);
                                                friendsAdapter.notifyDataSetChanged();

                                                Toast.makeText(creategroup1.this, "User added: " + finalFoundUserId, Toast.LENGTH_LONG).show();

                                                break;
                                            }
                                        }

                                        if (!foundUserFriend) {

                                            Toast.makeText(creategroup1.this, "user is not your friend!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(creategroup1.this, "DB ERR: " + error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            }
                        }

                        if (!foundUser) {
                            Toast.makeText(creategroup1.this, "user email not found!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(creategroup1.this, "DB ERR: " + error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String groupId = mDatabase.child("groups").push().getKey();

                Group group = new Group(groupId, groupName.getText().toString(), groupDescription.getText().toString(), pictureUrl);

                // add new group to database
                mDatabase.child("groups").child(groupId).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            ObjectReference userSelfRef = new ObjectReference(userId);
                            groupMembers.add(userSelfRef);

                            for (int i = 0; i < groupMembers.size(); i++) {

                                String memberId = groupMembers.get(i).getId();

                                // add group to group member references
                                mDatabase.child("userGroups")
                                        .child(memberId)
                                        .child(groupId)
                                        .child("id")
                                        .setValue(groupId)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                }
                                                else {
                                                    Toast.makeText(creategroup1.this, "Group Creation Failed!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                // add group members to group reference
                                mDatabase.child("groupUsers")
                                        .child(groupId)
                                        .child(memberId)
                                        .child("id")
                                        .setValue(memberId)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                }
                                                else {
                                                    Toast.makeText(creategroup1.this, "Group Creation Failed!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                            }

                            Intent intent = new Intent(creategroup1.this, Chats.class);
                            startActivity(intent);
                            finish();
                        }

                        else {
                            Toast.makeText(creategroup1.this, "Group Creation Failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
                                        pictureUrl = "/planpal/pictures/" + imageName;
                                        Log.d("PictureURL", pictureUrl);
                                        Toast.makeText(creategroup1.this, pictureUrl, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(creategroup1.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error gracefully
                                    Toast.makeText(creategroup1.this, "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    Toast.makeText(creategroup1.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
                RequestQueue queue = Volley.newRequestQueue(creategroup1.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}