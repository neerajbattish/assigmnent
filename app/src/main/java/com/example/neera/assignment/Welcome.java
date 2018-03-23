package com.example.neera.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.example.neera.assignment.helper.UserSessionManager;
import com.example.neera.assignment.model.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Welcome extends AppCompatActivity {

    private static final String Tag = "ViewDatabase";

    //firebase database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myref;
    private String userID;
    UserSessionManager session;
    UserInformation uInfo;
    private TextView txt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        session = new UserSessionManager(getApplicationContext());
        uInfo = new UserInformation();
       // txt1 = (TextView) findViewById(R.id.txtwelcm);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


//        try {
//           InputStream is = getAssets().open("ANURADHA.doc");
//
//            String str="";
//            StringBuffer buf = new StringBuffer();
//          //  InputStream is = this.getResources().openRawResource(R.drawable.);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            if (is!=null) {
//                while ((str = reader.readLine()) != null) {
//                    buf.append(str + "\n" );
//                }
//            }
//            is.close();
//
//
//
//            // We guarantee that the available method returns the total
//            // size of the asset...  of course, this does mean that a single
//            // asset can't be more than 2 gigs.
//            int size = is.available();
//
//            // Read the entire asset into a local byte buffer.
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            // Convert the buffer into a string.
//            String text = new String(buffer);
//
//            // Finally stick the string into the text view.
//            TextView tv = (TextView)findViewById(R.id.txtwelcm);
//            tv.setText(text);
//        } catch (IOException e) {
//            // Should never happen!
//            throw new RuntimeException(e);
//        }



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(Tag, "onAuthStateChanged:signed_in" + user.getUid());
                } else {
                    Log.d(Tag, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out");
                }
            }
        };
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        try {
            ///for retriving the data from firebase and display the name
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail());
                uInfo.setPassword(ds.child(userID).getValue(UserInformation.class).getPassword());
                uInfo.setUsername(ds.child(userID).getValue(UserInformation.class).getUsername());
                Log.d(Tag, "ShowData: name" + uInfo.getUsername());
                Log.d(Tag, "ShowData: email" + uInfo.getEmail());
                Log.d(Tag, "ShowData: Password" + uInfo.getPassword());
              //  txt1.setText("Welcome " + uInfo.getUsername());
            }
        } catch (Exception e) {
            toastMessage(e.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logut) {
            session.logoutUser();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
