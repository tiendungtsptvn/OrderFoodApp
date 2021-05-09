package com.example.orderfoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderfoodapp.model.Common;
import com.example.orderfoodapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn;
    FirebaseDatabase database;
    DatabaseReference listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = findViewById(R.id.edt_phone_signin);
        edtPassword = findViewById(R.id.edt_password_signin);
        btnSignIn = findViewById(R.id.btn_signin);

        database = FirebaseDatabase.getInstance();
        listUser = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("Waitting...");
                progressDialog.show();
                listUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        if (snapshot.child(edtPhone.getText().toString()).exists()) {
                            User user = snapshot.child(edtPhone.getText().toString()).
                                    getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                Toast.makeText(SignInActivity.this,
                                        "Success!", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(SignInActivity.this,
                                        MenuActivity.class);
                                Common.currentUser = user;
                                Common.currentPhone = edtPhone.getText().toString();
                                startActivity(home);
                            } else
                                Toast.makeText(SignInActivity.this,
                                        "Wrong Password!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(SignInActivity.this,
                                    "Acount not exists!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}