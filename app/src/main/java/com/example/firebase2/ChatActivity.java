package com.example.firebase2;

import static com.example.firebase2.FBref.refAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.UUID;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    TextView tv;
    ListView lv;
    EditText et;
    Button btn,exitbtn;
    ArrayList<Messege> messeges=new ArrayList<>();
    private MessegeAdapter adapter;

    Chat currentChat;

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
        }
        if(st.equals("Alarms")) {
            Intent si = new Intent(this,AlarmManagerActivity.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        tv=findViewById(R.id.chatUserName);
        lv=findViewById(R.id.chatListView);
        et=findViewById(R.id.messageInput);
        btn=findViewById(R.id.sendButton);
        exitbtn=findViewById(R.id.exitbtn);

        User user2=(User)getIntent().getSerializableExtra("User");
        FirebaseUser fbuser=refAuth.getCurrentUser();
        String uid=fbuser.getUid();
        currentChat=new Chat(uid,user2.getUid(),UUID.randomUUID().toString());

        adapter=new MessegeAdapter(this, messeges);
        lv.setAdapter(adapter);
        DatabaseReference dbChats = FirebaseDatabase.getInstance().getReference().child("Chats");
        ValueEventListener ChatsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    Chat chat = chatSnapshot.getValue(Chat.class);
                    if(chat!=null && chat.checkUid(uid,user2.getUid())) {
                        messeges.clear();
                        currentChat=chat;
                        for(Messege messege : chat.getMesseges()) {
                            messeges.add(messege);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbChats.addValueEventListener(ChatsListener);

        tv.setText("Chat with "+user2.getName());

        btn.setOnClickListener(v -> {
            currentChat.addMessege(new Messege(et.getText().toString(),uid,user2.getUid()));
            dbChats.child(currentChat.getChatId()).setValue(currentChat);
            et.setText("");
        });

        exitbtn.setOnClickListener(view -> {
            finish();
        });
    }
}