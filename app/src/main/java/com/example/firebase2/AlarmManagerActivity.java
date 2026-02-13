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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.GregorianCalendar;

public class AlarmManagerActivity extends AppCompatActivity {
    private AlarmReceiver alarmReciver;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    Button button;
    DatePicker datePicker;
    TimePicker timePicker;
    EditText eTpurpose;

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
        button=findViewById(R.id.Button);
        datePicker=findViewById(R.id.datePicker);
        timePicker=findViewById(R.id.timePicker);
        eTpurpose=findViewById(R.id.eTpurpose);
        AlarmManagerActivity x=this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(x,AlarmReceiver.class);
                intent.putExtra("title", eTpurpose.getText().toString());
                alarmIntent=PendingIntent.getBroadcast(x,12,intent,PendingIntent.FLAG_IMMUTABLE);
                alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

                long triggerAtMillis = new GregorianCalendar(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getHour(),
                        timePicker.getMinute()
                ).getTimeInMillis();

                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis,alarmIntent);
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