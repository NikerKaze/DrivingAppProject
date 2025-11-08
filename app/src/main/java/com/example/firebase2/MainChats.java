package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.INVISIBLE;
import static com.example.firebase2.FBref.refAuth;
import static com.example.firebase2.FBref.refAuth;
import static com.example.firebase2.FBref.refUsers;

import java.util.ArrayList;

public class MainChats extends AppCompatActivity {

    TextView tVtitle;
    Button btn;
    ListView lv;

    String name, phone, email, password, uid, type;
    User user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st=item.getTitle().toString();
        if(st.equals("Profile")) {
            Intent si = new Intent(this,Loginok.class);
            startActivity(si);
        } else if(st.equals("Data Filter-Sort")) {
            Intent si = new Intent(this,Dbact.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chats);

        tVtitle=(TextView)findViewById(R.id.tVtitle);
        btn=(Button)findViewById(R.id.btn);
        lv=(ListView)findViewById(R.id.lv);

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fbuser = refAuth.getCurrentUser();
        uid = fbuser.getUid();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        ValueEventListener UserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getType().equals("Student") || user.getType().equals("student")) {
                    btn.setVisibility(View.VISIBLE);
                    user = dataSnapshot.getValue(User.class);
                    tVtitle.setText("Hello " + user.getName() + "!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        db.addValueEventListener(UserListener);


        ArrayList<Chat> chats = new ArrayList<>();
        DatabaseReference dbChats = FirebaseDatabase.getInstance().getReference().child("Chats");
        ValueEventListener ChatsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    Chat chat = chatSnapshot.getValue(Chat.class);
                    if (chat != null) {
                        chats.add(chat);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbChats.addValueEventListener(ChatsListener);

    }

    public void TeacherList(View view) {
        Intent si = new Intent(MainChats.this,TeacherList.class);
        startActivity(si);
    }
}