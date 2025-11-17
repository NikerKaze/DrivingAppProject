package com.example.firebase2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}