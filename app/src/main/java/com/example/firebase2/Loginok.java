package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.firebase2.FBref.refAuth;
import static com.example.firebase2.FBref.refUsers;

public class Loginok extends AppCompatActivity {

    TextView tVnameview, tVemailview,tVtypeview,tVphoneview;
    CheckBox cBconnectview;

    String name, email,phone, uid,type="a";
    User user;
    long count;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st=item.getTitle().toString();
        if(st.equals("Profile")) {
            Intent si = new Intent(this,Loginok.class);
            startActivity(si);
        }
        if(st.equals("Alarms")) {
            Intent si = new Intent(this,AlarmManagerActivity.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginok);

        tVnameview=(TextView)findViewById(R.id.tVnameview);
        tVemailview=(TextView)findViewById(R.id.tVemailview);
        tVtypeview=(TextView)findViewById(R.id.tVtypeview);
        tVphoneview=(TextView)findViewById(R.id.tVphoneview);
        cBconnectview=(CheckBox)findViewById(R.id.cBconnectview);

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser fbuser=refAuth.getCurrentUser();
        uid=fbuser.getUid();

        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        ValueEventListener UserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tVtypeview.setText(user.getType());
                tVnameview.setText(user.getName());
                tVemailview.setText(user.getEmail());
                tVphoneview.setText(user.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        db.addValueEventListener(UserListener);

        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        cBconnectview.setChecked(isChecked);
    }

    public void update(View view) {
        if (!cBconnectview.isChecked()){
            refAuth.signOut();
        }
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean("stayConnect",cBconnectview.isChecked());
        editor.commit();
        Intent si=new Intent(this,MainChats.class);
        startActivity(si);
    }
}