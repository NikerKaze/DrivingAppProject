package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.firebase2.FBref.refAuth;
import static com.example.firebase2.FBref.refUsers;

public class Loginok extends AppCompatActivity {

    TextView tVnameview, tVemailview, tVuidview;
    CheckBox cBconnectview;

    String name, email, uid;
    Boolean newuser;
    User user;
    long count;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st=item.getTitle().toString();
        if(st.equals("Login")) {
            Intent si = new Intent(this,Loginok.class);
            startActivity(si);
        } else if(st.equals("Data Filter-Sort")) {
            Intent si = new Intent(this,Dbact.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginok);

        tVnameview=(TextView)findViewById(R.id.tVnameview);
        tVemailview=(TextView)findViewById(R.id.tVemailview);
        tVuidview=(TextView)findViewById(R.id.tVuidview);
        cBconnectview=(CheckBox)findViewById(R.id.cBconnectview);

        Intent gi=getIntent();
        newuser=gi.getBooleanExtra("newuser",false);
        refUsers.addListenerForSingleValueEvent(VELUpdateSNum);
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser fbuser = refAuth.getCurrentUser();
        uid = fbuser.getUid();
        Query query = refUsers
                .orderByChild("uid")
                .equalTo(uid);
        query.addListenerForSingleValueEvent(VEL);
        email = fbuser.getEmail();
        name=fbuser.getDisplayName();
        tVnameview.setText(name);
        tVemailview.setText(email);
        tVuidview.setText(uid);
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        cBconnectview.setChecked(isChecked);
    }

    com.google.firebase.database.ValueEventListener VEL = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dS) {
            if (dS.exists()) {
                long count=dS.getChildrenCount();
                for(DataSnapshot data : dS.getChildren()) {
                    user = data.getValue(User.class);
                    tVnameview.setText(user.getName());
                }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    com.google.firebase.database.ValueEventListener VELUpdateSNum = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dS) {
            if (dS.exists()) {
                count=dS.getChildrenCount();
                if (newuser) {
                    user.setSerialnum(count);
                    refUsers.child(uid).setValue(user);
                }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    public void update(View view) {
        if (!cBconnectview.isChecked()){
            refAuth.signOut();
        }
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean("stayConnect",cBconnectview.isChecked());
        editor.commit();
        finish();
    }
}