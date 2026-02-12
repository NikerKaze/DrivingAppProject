package com.example.firebase2;

import static com.example.firebase2.FBref.refAuth;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AlarmManagerActivity extends AppCompatActivity {
    private AlarmReceiver alarmReciver;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    EditText editText;
    Button button;


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
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
        alarmReciver=new AlarmReceiver();
        editText=findViewById(R.id.editText);
        button=findViewById(R.id.Button);

        Intent intent=new Intent(this,AlarmReceiver.class);
        alarmIntent=PendingIntent.getBroadcast(this,12,intent,PendingIntent.FLAG_IMMUTABLE);
        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time=editText.getText().toString();
                int TimeInSec=Integer.parseInt(time);
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+TimeInSec*1000,alarmIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void update(View view) {
        Intent si=new Intent(this,MainChats.class);
        startActivity(si);
    }
}