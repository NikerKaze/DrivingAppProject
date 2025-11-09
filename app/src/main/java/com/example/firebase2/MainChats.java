package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import java.sql.Time;
import java.util.ArrayList;

public class MainChats extends AppCompatActivity {

    TextView tVtitle;
    Button btn;
    ListView lv;
    ArrayList <User> users;
    UserAdapter adapter;
    GeminiManager geminiManager;
    String TAG = "MainChats";

    ValueEventListener UserListener2,UserListener;
    DatabaseReference db2,db1=FirebaseDatabase.getInstance().getReference().child("Users");


    String name, phone, email, password, uid, type;
    User curUser=null;

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

        geminiManager = GeminiManager.getInstance();

        textPrompt(tVtitle);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fbuser = refAuth.getCurrentUser();
        uid = fbuser.getUid();


        lv=findViewById(R.id.lv);
        users=new ArrayList<>();
        adapter=new UserAdapter(this, users);
        lv.setAdapter(adapter);

        ValueEventListener UserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if(user!=null) {
                        if(!user.getType().equals(curUser.getType())) {
                            DatabaseReference dbChats = FirebaseDatabase.getInstance().getReference().child("Chats");
                            ValueEventListener ChatsListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                                        Chat chat = chatSnapshot.getValue(Chat.class);
                                        if(chat!=null && chat.checkUid(curUser.getUid(),user.getUid())) {
                                            if(!chat.getMesseges().isEmpty()) {
                                                users.add(user);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            };
                            dbChats.addValueEventListener(ChatsListener);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        db2=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        ValueEventListener UserListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                curUser=user;
                if(user.getType().equals("Student")||user.getType().equals("student")) {
                    btn.setVisibility(View.VISIBLE);
                    user=dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        db2.addValueEventListener(UserListener2);
        db1.addValueEventListener(UserListener);
    }


    public void textPrompt(View view) {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        String prompt = "Respond with only a 2-word greeting. Be creative)";
        ProgressDialog pD = new ProgressDialog(this);
        pD.setTitle("Sent Prompt");
        pD.setMessage("Waiting for response...");
        pD.setCancelable(false);
        pD.show();

        geminiManager.sendTextPrompt(prompt, new GeminiCallback() {
            @Override
            public void onSuccess(String result) {
                pD.dismiss();
                tVtitle.setText(result);
            }

            @Override
            public void onFailure(Throwable error) {
                pD.dismiss();
                tVtitle.setText("Failed prompting Gemini");
                Log.e(TAG, "textPrompt/ Error: " + error.getMessage());
            }
        });
    }


    public void TeacherList(View view) {
        Intent si = new Intent(MainChats.this,TeacherList.class);
        startActivity(si);
    }
}