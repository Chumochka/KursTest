package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ViewActivity extends AppCompatActivity {
    private TextView tvDate;
    private TextView tvHealth;
    private TextView tvWorry;
    private TextView tvTract;
    private TextView tvMood;
    private TextView tvFeel;
    private TextView tvAlarm;
    private TextView tvNote;
    private ImageButton btnDateBack;
    private ImageButton btnDateNext;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        tvDate = findViewById(R.id.tvDate);
        tvHealth = findViewById(R.id.tvHealth);
        tvWorry = findViewById(R.id.tvWorry);
        tvTract = findViewById(R.id.tvTract);
        tvMood = findViewById(R.id.tvMood);
        tvFeel = findViewById(R.id.tvFeel);
        tvAlarm = findViewById(R.id.tvAlarm);
        tvNote = findViewById(R.id.tvNote);
        btnDateBack = findViewById(R.id.btnDateBack);
        btnDateNext = findViewById(R.id.btnDateNext);
        btnBack = findViewById(R.id.btnBack);
        final LocalDate[] date = {LocalDate.now()};
        loadActivity(date[0]);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnDateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date[0] = date[0].minusDays(1);
                loadActivity(date[0]);
            }
        });
        btnDateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date[0] = date[0].plusDays(1);
                loadActivity(date[0]);
            }
        });
    }
    private void loadActivity(LocalDate date){
        setDate(date);
        if(LocalDate.now().equals(date))
            btnDateNext.setVisibility(View.GONE);
        else
            btnDateNext.setVisibility(View.VISIBLE);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(formatter);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("diary.db",MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries (date TEXT PRIMARY KEY, health Text, note Text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Worries (date TEXT, worry TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Tracts (date TEXT, tract TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Moods (date TEXT, mood TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Feels (date TEXT, feel TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Alarms (date TEXT, alarm TEXT)");
        Cursor query = db.rawQuery("SELECT health,note FROM Diary_Entries WHERE date ='"+dateString+"'",null);
        Cursor queryWorry = db.rawQuery("SELECT worry FROM Diary_Entries_Worries WHERE date = '"+dateString+"'",null);
        Cursor queryTract = db.rawQuery("SELECT tract FROM Diary_Entries_Tracts WHERE date = '"+dateString+"'",null);
        Cursor queryMood = db.rawQuery("SELECT mood FROM Diary_Entries_Moods WHERE date = '"+dateString+"'",null);
        Cursor queryFeel = db.rawQuery("SELECT feel FROM Diary_Entries_Feels WHERE date = '"+dateString+"'",null);
        Cursor queryAlarms = db.rawQuery("SELECT alarm FROM Diary_Entries_Alarms WHERE date = '"+dateString+"'",null);

        String health = "";
        String note = "";
        if(query.getCount()>0){
            query.moveToPosition(0);
            health = query.getString(0);
            note = query.getString(1);
        }
        if (health.isEmpty())
            tvHealth.setText("Нет записи");
        else
            tvHealth.setText(health);
        if(note.isEmpty())
            tvNote.setText("Пусто");
        else
            tvNote.setText(note);

        String worry= "";
        for(int i =0; i<queryWorry.getCount();i++){
            queryWorry.moveToPosition(i);
            if (queryWorry.getString(0)!=null)
                if(worry.isEmpty())
                    worry += queryWorry.getString(0);
                else
                    worry +="\n"+queryWorry.getString(0);
        }
        if(worry.isEmpty())
            worry = "Ничего";
        tvWorry.setText(worry);

        String tract = "";
        for(int i=0;i<queryTract.getCount();i++){
            queryTract.moveToPosition(i);
            if(tract.isEmpty())
                tract = queryTract.getString(0);
            else
                tract += "\n" + queryTract.getString(0);
        }
        if (tract.isEmpty())
            tract = "Ничего";
        tvTract.setText(tract);

        String mood = "";
        for(int i=0;i<queryMood.getCount();i++){
            queryMood.moveToPosition(i);
            if(mood.isEmpty())
                mood = queryMood.getString(0);
            else
                mood += "\n" + queryMood.getString(0);
        }
        if (mood.isEmpty())
            mood = "Ничего";
        tvMood.setText(mood);

        String feel = "";
        for(int i=0;i<queryFeel.getCount();i++){
            queryFeel.moveToPosition(i);
            if(feel.isEmpty())
                feel = queryFeel.getString(0);
            else
                feel += "\n" + queryFeel.getString(0);
        }
        if (feel.isEmpty())
            feel = "Ничего";
        tvFeel.setText(feel);

        String alarm = "";
        for (int i =0; i<queryAlarms.getCount();i++){
            queryAlarms.moveToPosition(i);
            if(alarm.isEmpty())
                alarm = queryAlarms.getString(0);
            else
                alarm += "\n" + queryAlarms.getString(0);
        }
        if (alarm.isEmpty())
            alarm = "Ничего";
        tvAlarm.setText(alarm);
    }
    private void setDate(LocalDate date){
        tvDate = findViewById(R.id.tvDate);
        String dateString = date.getDayOfMonth() + " ";
        switch (date.getMonthValue()){
            case 1: dateString += "января "; break;
            case 2: dateString += "февраля "; break;
            case 3: dateString += "марта "; break;
            case 4: dateString += "апреля "; break;
            case 5: dateString += "мая "; break;
            case 6: dateString += "июня "; break;
            case 7: dateString += "июля "; break;
            case 8: dateString += "августа "; break;
            case 9: dateString += "сентября "; break;
            case 10: dateString += "октября "; break;
            case 11: dateString += "ноября "; break;
            case 12: dateString += "декабря "; break;
        }
        dateString+=date.getYear();
        tvDate.setText(dateString);
    }
}