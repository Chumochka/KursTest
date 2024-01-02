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
        loadActivity(date[0]);//загрузка информации за текущую дату
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
        if(LocalDate.now().equals(date))//если выбранна текущая даты
            btnDateNext.setVisibility(View.GONE);//Убрать кнопку выбора следующей даты
        else
            btnDateNext.setVisibility(View.VISIBLE);//Вернуть кнопку выбора следующей даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(formatter);//Преобразование даты в строку
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("diary.db",MODE_PRIVATE, null);//получение данных за выбранную дату
        Cursor query = db.rawQuery("SELECT health,note FROM Diary_Entries WHERE date ='"+dateString+"'",null);
        Cursor queryWorry = db.rawQuery("SELECT worry FROM Diary_Entries_Worries WHERE date = '"+dateString+"'",null);
        Cursor queryTract = db.rawQuery("SELECT tract FROM Diary_Entries_Tracts WHERE date = '"+dateString+"'",null);
        Cursor queryMood = db.rawQuery("SELECT mood FROM Diary_Entries_Moods WHERE date = '"+dateString+"'",null);
        Cursor queryFeel = db.rawQuery("SELECT feel FROM Diary_Entries_Feels WHERE date = '"+dateString+"'",null);
        Cursor queryAlarms = db.rawQuery("SELECT alarm FROM Diary_Entries_Alarms WHERE date = '"+dateString+"'",null);

        String health = "";
        String note = "";
        if(query.getCount()>0){//если есть записи
            query.moveToFirst();
            health = query.getString(0);//заполнение инфо. о самочуствии и заметках
            note = query.getString(1);
        }
        if (health.isEmpty())//если нет записи о самочувствии
            tvHealth.setText("Нет записи");//вывод, что нет записи
        else
            tvHealth.setText(health);//заполнение самочуствия
        if(note.isEmpty())//если нет заметок
            tvNote.setText("Пусто");//выводим пусто
        else
            tvNote.setText(note);//вывод заметки

        String worry= "";
        for(int i =0; i<queryWorry.getCount();i++){//получение всех записей о беспокойствах
            queryWorry.moveToPosition(i);
            if (queryWorry.getString(0)!=null)//если есть значение
                if(worry.isEmpty())//если строка о беспокойствах пуста
                    worry += queryWorry.getString(0);
                else//если есть данные в строке
                    worry +="\n"+queryWorry.getString(0);
        }
        if(worry.isEmpty())//если нет данных
            worry = "Ничего";
        tvWorry.setText(worry);

        String tract = "";
        for(int i=0;i<queryTract.getCount();i++){//получение всех записей о симптомах ЖКТ
            queryTract.moveToPosition(i);
            if(tract.isEmpty())//если строка о симптомах пуста
                tract = queryTract.getString(0);
            else//если есть данные в строке
                tract += "\n" + queryTract.getString(0);
        }
        if (tract.isEmpty())//если нет данных
            tract = "Ничего";
        tvTract.setText(tract);

        String mood = "";
        for(int i=0;i<queryMood.getCount();i++){//получение всех записей о настроении
            queryMood.moveToPosition(i);
            if(mood.isEmpty())//если строка о настроении пуста
                mood = queryMood.getString(0);
            else//если есть данные в строке
                mood += "\n" + queryMood.getString(0);
        }
        if (mood.isEmpty())//если нет данных
            mood = "Ничего";
        tvMood.setText(mood);

        String feel = "";
        for(int i=0;i<queryFeel.getCount();i++){//получение всех записей о ощущениях
            queryFeel.moveToPosition(i);
            if(feel.isEmpty())//если строка о ощущениях пуста
                feel = queryFeel.getString(0);
            else//если есть данные в строке
                feel += "\n" + queryFeel.getString(0);
        }
        if (feel.isEmpty())//если нет данных
            feel = "Ничего";
        tvFeel.setText(feel);

        String alarm = "";
        for (int i =0; i<queryAlarms.getCount();i++){//получение всех записей о тревоге
            queryAlarms.moveToPosition(i);
            if(alarm.isEmpty())//если строка о тревоге пуста
                alarm = queryAlarms.getString(0);
            else//если есть данные в строке
                alarm += "\n" + queryAlarms.getString(0);
        }
        if (alarm.isEmpty())//если нет данных
            alarm = "Ничего";
        tvAlarm.setText(alarm);
    }
    private void setDate(LocalDate date){//получение выбранной даты в виде строки
        tvDate = findViewById(R.id.tvDate);
        String dateString = date.getDayOfMonth() + " ";//формировние строки
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
        tvDate.setText(dateString);//Установка полученной строки
    }
}