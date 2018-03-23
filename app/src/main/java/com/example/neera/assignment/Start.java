package com.example.neera.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.neera.assignment.helper.UserSessionManager;

/**
 * Created by neera on 31-01-2018.
 */

public class Start extends AppCompatActivity {

    UserSessionManager sessn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessn = new UserSessionManager(getApplicationContext());
        boolean chk;
        chk = sessn.checkLoginspl();
        if (chk == true) {
            Intent aa=new Intent(Start.this,LogIn.class);
            startActivity(aa);
        }
        else
        {
            Intent aa=new Intent(Start.this,Welcome.class);
            startActivity(aa);
            return;
        }
    }
}
