package com.project.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.Calendar;

public class Signup extends AppCompatActivity {
    String dpUrl;
    FirebaseAuth mAuth;
    Uri image;


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

        mAuth = FirebaseAuth.getInstance();

        Log.d("mAuth", mAuth.toString());

        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("email", email.getText().toString());
                Log.d("password", passwordEditText.getText().toString());
                mAuth.createUserWithEmailAndPassword(

                        email.getText().toString(), passwordEditText.getText().toString()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Toast.makeText(Signup.this, "Sign Up Successful", Toast.LENGTH_LONG).show();

                        String userId = mAuth.getUid().toString();

                        Toast.makeText(Signup.this, userId, Toast.LENGTH_LONG).show();

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                        User1 user = new User1(userId, name.getText().toString(), email.getText().toString(),dpUrl);

                        mDatabase.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Log.d("databaseUsers", "users");

                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReference().child(mAuth.getUid() + Calendar.getInstance().getTimeInMillis() + "-dp.png");
                                storageRef.putFile(image)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        dpUrl = uri.toString();
                                                        mDatabase.child("users").child(userId).child("profilePhotoUrl").setValue(dpUrl);
                                                        Toast.makeText(Signup.this, dpUrl, Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Signup.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                Intent intent = new Intent(Signup.this, Chats.class);
                                startActivity(intent);
                                finish();
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
        }
    }
}