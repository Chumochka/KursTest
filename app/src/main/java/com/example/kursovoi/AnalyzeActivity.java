package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AnalyzeActivity extends AppCompatActivity {
    private TextView tvDate;
    private TextView tvHealth;
    private TextView tvWorry;
    private TextView tvTract;
    private TextView tvMood;
    private TextView tvFeel;
    private TextView tvAlarm;
    private ImageButton btnDateBack;
    private ImageButton btnDateNext;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        tvDate = findViewById(R.id.tvDate);
        tvHealth = findViewById(R.id.tvHealth);
        tvWorry = findViewById(R.id.tvWorry);
        tvTract = findViewById(R.id.tvTract);
        tvMood = findViewById(R.id.tvMood);
        tvFeel = findViewById(R.id.tvFeel);
        tvAlarm = findViewById(R.id.tvAlarm);
        btnDateBack = findViewById(R.id.btnDateBack);
        btnDateNext = findViewById(R.id.btnDateNext);
        btnBack = findViewById(R.id.btnBack);
        final LocalDate[] date = {LocalDate.now()};
        loadActivity(date[0]);//загрузка информации за текущую дату
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalyzeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnDateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date[0] = date[0].minusMonths(1);
                loadActivity(date[0]);
            }
        });
        btnDateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date[0] = date[0].plusMonths(1);
                loadActivity(date[0]);
            }
        });
    }
    private void setDate(LocalDate date){//формирование строки выбранной даты
        tvDate = findViewById(R.id.tvDate);
        String dateString="";
        switch (date.getMonthValue()){
            case 1: dateString = "Январь "; break;
            case 2: dateString = "Февраль "; break;
            case 3: dateString = "Март "; break;
            case 4: dateString = "Апрель "; break;
            case 5: dateString = "Май "; break;
            case 6: dateString = "Июнь "; break;
            case 7: dateString = "Июль "; break;
            case 8: dateString = "Август "; break;
            case 9: dateString = "Сентябрь "; break;
            case 10: dateString = "Октябрь "; break;
            case 11: dateString = "Ноябрь "; break;
            case 12: dateString = "Декабрь "; break;
        }
        dateString+=date.getYear();
        tvDate.setText(dateString);
    }
    private void loadActivity(LocalDate date) {
        setDate(date);
        if (LocalDate.now().getMonth().equals(date.getMonth()))//если выбрань текущий месяц
            btnDateNext.setVisibility(View.GONE);//убрать кнопку выбора следующего месяца
        else
            btnDateNext.setVisibility(View.VISIBLE);//вернуть кнопку выбора следующего месяца
        LocalDate firstDay = date.withDayOfMonth(1);
        LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());//получение первой и последней даты месяца
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(formatter);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("diary.db", MODE_PRIVATE, null);//поиск данных за выбранный период
        Cursor query = db.rawQuery("SELECT health FROM Diary_Entries WHERE date between '"+firstDay+"' and '"+lastDay+"'",null);
        Cursor queryWorry = db.rawQuery("SELECT worry FROM Diary_Entries_Worries WHERE date between '"+firstDay+"' and '"+lastDay+"'",null);
        Cursor queryTract = db.rawQuery("SELECT tract FROM Diary_Entries_Tracts WHERE date between '"+firstDay+"' and '"+lastDay+"'",null);
        Cursor queryMood = db.rawQuery("SELECT mood FROM Diary_Entries_Moods WHERE date between '"+firstDay+"' and '"+lastDay+"'",null);
        Cursor queryFeel = db.rawQuery("SELECT feel FROM Diary_Entries_Feels WHERE date between '"+firstDay+"' and '"+lastDay+"'",null);
        Cursor queryAlarms = db.rawQuery("SELECT alarm FROM Diary_Entries_Alarms WHERE date between '"+firstDay+"' and '"+lastDay+"'",null);
        ArrayList<String> healthList = new ArrayList<>();
        ArrayList<String> worryList = new ArrayList<>();
        ArrayList<String> tractList = new ArrayList<>();
        ArrayList<String> moodList = new ArrayList<>();
        ArrayList<String> feelList = new ArrayList<>();
        ArrayList<String> alarmList = new ArrayList<>();
        while(query.moveToNext()){//загрузка данных в ArrayList
            healthList.add(query.getString(0));
        }
        while(queryWorry.moveToNext()){//загрузка данных в ArrayList
            worryList.add(queryWorry.getString(0));
        }
        while (queryTract.moveToNext()){//загрузка данных в ArrayList
            tractList.add(queryTract.getString(0));
        }
        while (queryMood.moveToNext()){//загрузка данных в ArrayList
            moodList.add(queryMood.getString(0));
        }
        while (queryFeel.moveToNext()){//загрузка данных в ArrayList
            feelList.add(queryFeel.getString(0));
        }
        while (queryAlarms.moveToNext()){//загрузка данных в ArrayList
            alarmList.add(queryAlarms.getString(0));
        }
        String health = "";
        String worry = "";
        String tract = "";
        String mood = "";
        String feel = "";
        String alarm = "";
        while(!healthList.isEmpty()) {//Если есть данные в ArrayList
            String currentElem = healthList.get(0);
            if(health.isEmpty()){//если в строке нет данных
                health = currentElem +": " + writeWithDeclension(Collections.frequency(healthList,currentElem));//самочувствие + количество повторений этой записи в ArrayList
            }else{
                health +="\n"+ currentElem +": " + writeWithDeclension(Collections.frequency(healthList,currentElem));//аналогично
            }
            while (healthList.remove(currentElem));//удаление записей добавленного значения
        }
        while(!worryList.isEmpty()) {//Если есть данные в ArrayList
            String currentElem = worryList.get(0);
            if(worry.isEmpty()){//если в строке нет данных
                worry = currentElem +": " + writeWithDeclension(Collections.frequency(worryList,currentElem));//беспокойство + количество повторений этой записи в ArrayList
            }else{
                worry +="\n"+ currentElem +": " + writeWithDeclension(Collections.frequency(worryList,currentElem));//аналогично
            }
            while (worryList.remove(currentElem));//удаление записей добавленного значения
        }
        while(!tractList.isEmpty()) {//Если есть данные в ArrayList
            String currentElem = tractList.get(0);
            if(tract.isEmpty()){//если в строке нет данных
                tract = currentElem +": " + writeWithDeclension(Collections.frequency(tractList,currentElem));//симптом ЖКТ + количество повторений этой записи в ArrayList
            }else{
                tract +="\n"+ currentElem +": " + writeWithDeclension(Collections.frequency(tractList,currentElem));//аналогично
            }
            while (tractList.remove(currentElem));//удаление записей добавленного значения
        }
        while(!moodList.isEmpty()) {//Если есть данные в ArrayList
            String currentElem = moodList.get(0);
            if(mood.isEmpty()){//если в строке нет данных
                mood = currentElem +": " + writeWithDeclension(Collections.frequency(moodList,currentElem));//настроение + количество повторений этой записи в ArrayList
            }else{
                mood +="\n"+ currentElem +": " + writeWithDeclension(Collections.frequency(moodList,currentElem));//аналогично
            }
            while (moodList.remove(currentElem));//удаление записей добавленного значения
        }
        while(!feelList.isEmpty()) {//Если есть данные в ArrayList
            String currentElem = feelList.get(0);
            if(feel.isEmpty()){//если в строке нет данных
                feel = currentElem +": " + writeWithDeclension(Collections.frequency(feelList,currentElem));//ощущение + количество повторений этой записи в ArrayList
            }else{
                feel +="\n"+ currentElem +": " + writeWithDeclension(Collections.frequency(feelList,currentElem));//аналогично
            }
            while (feelList.remove(currentElem));//удаление записей добавленного значения
        }
        while(!alarmList.isEmpty()) {//Если есть данные в ArrayList
            String currentElem = alarmList.get(0);
            if(alarm.isEmpty()){//если в строке нет данных
                alarm = currentElem +": " + writeWithDeclension(Collections.frequency(alarmList,currentElem));//тревога + количество повторений этой записи в ArrayList
            }else{
                alarm +="\n"+ currentElem +": " + writeWithDeclension(Collections.frequency(alarmList,currentElem));//аналогично
            }
            while (alarmList.remove(currentElem));//удаление записей добавленного значения
        }
        if(!health.isEmpty()){//Если строка заполнена
            tvHealth.setText(health);
        }else{//Если нет данных
            tvHealth.setText("Данные за период отсутствуют");
        }
        if(!worry.isEmpty()){//Если строка заполнена
            tvWorry.setText(worry);
        }else{//Если нет данных
            tvWorry.setText("Данные за период отсутствуют");
        }
        if(!tract.isEmpty()){//Если строка заполнена
            tvTract.setText(tract);
        }else{//Если нет данных
            tvTract.setText("Данные за период отсутствуют");
        }
        if(!mood.isEmpty()){//Если строка заполнена
            tvMood.setText(mood);
        }else{//Если нет данных
            tvMood.setText("Данные за период отсутствуют");
        }
        if(!feel.isEmpty()){//Если строка заполнена
            tvFeel.setText(feel);
        }else{//Если нет данных
            tvFeel.setText("Данные за период отсутствуют");
        }
        if(!alarm.isEmpty()){//Если строка заполнена
            tvAlarm.setText(alarm);
        }else{//Если нет данных
            tvAlarm.setText("Данные за период отсутствуют");
        }
    }
    private String writeWithDeclension(int num){//выбор склонения в зависимости от числа
        if(num%10==0 || num%10==1 || (num%10>=5 && num%10<=9) || (num%100>=10 && num%100<=20)){
            return num + " раз";
        }else{
            return num + " раза";
        }
    }
}