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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private Button buttonlogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewregister;
    FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;
    String eml, pass;
    UserSessionManager session;
    FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        buttonlogin = (Button) findViewById(R.id.buttonlogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewregister = (TextView) findViewById(R.id.textViewregistr);
        buttonlogin.setOnClickListener(this);
        textViewregister.setOnClickListener(this);
        session = new UserSessionManager(getApplicationContext());
    }

    private void Signin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        eml = email;
        pass = password;
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter the Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter the Password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing User....");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                toastMessage("User Successfully verified");
                                session.createUserLoginSession(eml.toString(),
                                        pass.toString());
                                clear();
                                progressDialog.hide();
                                Intent aa = new Intent(LogIn.this, Welcome.class);
                                startActivity(aa);
                            } else {
                                progressDialog.hide();
                                toastMessage("User Not verified Please Verify First");
                            }
                        } else {
                            progressDialog.hide();
                            toastMessage("Check UserId/Password");
                        }
                    }
                });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void clear()
    {
        editTextEmail.setText(null);
        editTextPassword.setText(null);
    }

    @Override
    public void onClick(View view) {

        {
            if (view == buttonlogin) {
                Signin();
            }
            if (view == textViewregister) {
                Intent a = new Intent(this, Signup.class);
                startActivity(a);
            }

        }
    }
}
