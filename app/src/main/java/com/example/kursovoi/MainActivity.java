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

    private TextView tvLastDate;
    private Chip chipAdd;
    private Chip chipWatch;
    private Chip chipAnalyze;
    private Chip chipAppointments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chipAdd = findViewById(R.id.chipAdd);
        chipWatch = findViewById(R.id.chipWatch);
        chipAnalyze = findViewById(R.id.chipAnalyze);
        chipAppointments = findViewById(R.id.chipAppointment);
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("id")==-1 || extras.getString("name").isEmpty())
            chipAppointments.setVisibility(View.GONE);
        getLastDate();
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
                Intent intent = new Intent(MainActivity.this, AnalyzeActivity.class);
                startActivity(intent);
            }
        });
        chipAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListAppointmentsActivity.class);
                intent.putExtra("id",extras.getInt("id"));
                intent.putExtra("name", extras.getString("name"));
                startActivity(intent);
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

        Cursor query = db.rawQuery("SELECT date FROM Diary_Entries",null);//получение списка дат

        tvLastDate = findViewById(R.id.tvLastDate);
        if(query.getCount()>0){//если есть данные
            query.moveToLast();//перейти к последнему
            String dateString = query.getString(0);//получение даты
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString,formatter);//переход из строки в дату
            dateString = date.getDayOfMonth() + " ";//формирование строки для вывода
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