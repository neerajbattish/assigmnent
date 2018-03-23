package com.example.neera.assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neera.assignment.helper.UserSessionManager;
import com.example.neera.assignment.model.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextname;
    private EditText editTextPassword;
    private TextView textViewSignin;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabase;
    String eml, pass, namee, id;
    UserSessionManager session;
    FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextname = (EditText) findViewById(R.id.editTextname);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        session = new UserSessionManager(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        }
        if (view == textViewSignin) {
            Intent a=new Intent(Signup.this,LogIn.class);
            startActivity(a);
         }
    }

    private void registerUser() {
        try {
            progressDialog.setMessage("Registering User....");
            progressDialog.show();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String name = editTextname.getText().toString().trim();
            eml = email;
            pass = password;
            namee = name;
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter the Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter the Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Please enter the Name", Toast.LENGTH_SHORT).show();
                return;
            }
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                id = user.getUid();
                                // Write a users to the database
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                mDatabase = firebaseDatabase.getReference().child("user");
                                UserInformation user1 = new UserInformation(namee, eml, pass);
                                mDatabase.child(id).setValue(user1);
                                firebaseAuth.getCurrentUser().sendEmailVerification();
                                clear();
                                progressDialog.hide();
                                toastMessage("Registered Successfully And Verification Link is Send to your ID");
                                Intent a=new Intent(Signup.this,LogIn.class);
                                startActivity(a);
                            } else {
                                progressDialog.hide();
                                toastMessage("Problem With Registration");
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void clear()
    {
        editTextEmail.setText(null);
        editTextPassword.setText(null);
        editTextname.setText(null);
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
