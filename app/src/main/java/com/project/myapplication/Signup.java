package com.project.myapplication;

import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    String dpUrl;
    FirebaseAuth mAuth;
    Uri image;
    String encodedImage,imageName,pictureUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageView uploadPic= findViewById(R.id.circularBackground);
        EditText name = findViewById(R.id.name);
        EditText email= findViewById(R.id.email);
        AppCompatButton signUpButton = findViewById(R.id.Signupbutton);
        TextInputLayout password = findViewById(R.id.password);
        TextInputEditText passwordEditText = password.findViewById(R.id.password_edit_text);
        Button loginButton = findViewById(R.id.loginbutton);
        ImageButton toggleButton = findViewById(R.id.toggle_button);

        mAuth = FirebaseAuth.getInstance();

        Log.d("mAuth", mAuth.toString());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("email", email.getText().toString());
                Log.d("password", passwordEditText.getText().toString());

                // Retrieve IP address from strings.xml
                String ipAddress = getString(R.string.ip_addr);
                // Concatenate the retrieved IP address with the URL
                String url = "http://" + ipAddress + "/planpal/insert.php";

                mAuth.createUserWithEmailAndPassword(
                        email.getText().toString(), passwordEditText.getText().toString()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Toast.makeText(Signup.this, "Sign Up Successful", Toast.LENGTH_LONG).show();

                        String userId = mAuth.getUid().toString();

                        //Toast.makeText(Signup.this, userId, Toast.LENGTH_LONG).show();

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                        User1 user = new User1(userId, name.getText().toString(), email.getText().toString(),dpUrl);

                        mDatabase.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Log.d("databaseUsers", "users");

                                //dpUrl = uri.toString();
                                mDatabase.child("users").child(userId).child("profilePhotoUrl").setValue(pictureUrl);
                                //Toast.makeText(Signup.this, dpUrl, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Signup.this, Chats.class);
                                startActivity(intent);
                                finish();

                                //FIREBASE STORAGE CODE IF NEEDED IN COMMENT OPEN IT AT LINE 126

//                                FirebaseStorage storage = FirebaseStorage.getInstance();
//                                StorageReference storageRef = storage.getReference().child(mAuth.getUid() + Calendar.getInstance().getTimeInMillis() + "-dp.png");
//                                storageRef.putFile(image)
//                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                            @Override
//                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
//                                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                    @Override
//                                                    public void onSuccess(Uri uri) {
//
//                                                    }
//                                                });
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(Signup.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
//                                            }
//                                        });


                            }
                        });
                    }
                }
                ).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Signup.this, "Sign Up Failed"+e.toString(), Toast.LENGTH_LONG).show();
                    }
                });



            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Password is visible, hide it
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    // Password is hidden, show it
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                // Move the cursor to the end of the text to maintain the current cursor position
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
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
                                        Toast.makeText(Signup.this, pictureUrl, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Signup.this, res.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // Handle JSON parsing error gracefully
                                    Toast.makeText(Signup.this, "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    Toast.makeText(Signup.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
                RequestQueue queue = Volley.newRequestQueue(Signup.this);
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
}