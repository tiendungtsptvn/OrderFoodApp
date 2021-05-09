package com.example.orderfoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.example.orderfoodapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    EditText edtPhone, edtName, edtPassword;
    Button btnSignUp;
    FirebaseDatabase database;
    DatabaseReference listUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPhone = findViewById(R.id.edt_phone_signup);
        edtName = findViewById(R.id.edt_name_signup);
        edtPassword = findViewById(R.id.edt_password_signup);
        btnSignUp = findViewById(R.id.btn_signup);

        database = FirebaseDatabase.getInstance();
        listUser = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPhone = edtPhone.getText().toString();
                String mName = edtName.getText().toString();
                String mPassword = edtPassword.getText().toString();
                if(!mPhone.equals("") && !mName.equals("") && !mPassword.equals("")){
                    ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                    mDialog.setMessage("Waitting...");
                    mDialog.show();
                    listUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mDialog.dismiss();
                            if(snapshot.child(edtPhone.getText().toString()).exists()){
                                Toast.makeText(SignUpActivity.this,
                                        "This phone number already exists !", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                User user = new User(mName, mPassword);
                                listUser.child(edtPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUpActivity.this,
                                        "Register success!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this,
                            "Please do not leave it blank !", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}