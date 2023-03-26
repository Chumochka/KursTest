package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Chip chipAdd;
    private Chip chipWatch;
    private Chip chipAnalyze;
    private TextView tvLastDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chipAdd = findViewById(R.id.chipAdd);
        chipWatch = findViewById(R.id.chipWatch);
        chipAnalyze = findViewById(R.id.chipAnalyze);
        chipAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        chipWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                startActivity(intent);
            }
        });
        chipAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void getLastDate(){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("diary.db",MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries (date TEXT PRIMARY KEY, health Text, note Text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Worries (date TEXT, worry TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Tracts (date TEXT, tract TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Moods (date TEXT, mood TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Feels (date TEXT, feel TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Alarms (date TEXT, alarm TEXT)");

        Cursor query = db.rawQuery("SELECT date FROM Diary_Entries",null);

        tvLastDate = findViewById(R.id.tvLastDate);
        if(query!=null){
            query.moveToPosition(query.getCount()-1);
            String dateString = query.getString(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            LocalDate date = LocalDate.parse(dateString,formatter);
            dateString = date.getDayOfMonth() + " ";
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
            dateString += date.getYear() + ".";
            tvLastDate.setText(dateString);
        }
    }
}