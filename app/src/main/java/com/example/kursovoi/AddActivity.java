package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private TextView tvDate;
    private ImageButton btnBack;
    private RadioGroup rgFeel1;
    private RadioGroup rgFeel2;
    private ChipGroup chipGroupWorries;
    private ChipGroup chipGroupTracts;
    private ChipGroup chipGroupMoods;
    private ChipGroup chipGroupFeels;
    private ChipGroup chipGroupAlarms;
    private EditText editTextNote;
    private Chip chipSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btnBack = findViewById(R.id.btnBack);
        rgFeel1 = findViewById(R.id.rgFeel1);
        rgFeel2 = findViewById(R.id.rgFeel2);
        chipGroupWorries = findViewById(R.id.chipGroupWorries);
        chipGroupTracts = findViewById(R.id.chipGroupTracts);
        chipGroupMoods = findViewById(R.id.chipGroupMoods);
        chipGroupFeels = findViewById(R.id.chipGroupFeels);
        chipGroupAlarms = findViewById(R.id.chipGroupAlarms);
        editTextNote = findViewById(R.id.editTextNote);
        chipSave = findViewById(R.id.chipSave);
        setDate();
        setTodayInfo();
        rgFeel1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton rb = findViewById(id);
                if(rb!=null)
                    if(rb.isChecked() && rgFeel2.getCheckedRadioButtonId()!=-1)
                        rgFeel2.clearCheck();
            }
        });
        rgFeel2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton rb = findViewById(id);
                if(rb!=null)
                    if(rgFeel1.getCheckedRadioButtonId()!=-1 && rb.isChecked())
                        rgFeel1.clearCheck();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        chipSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateString = getDate();//Получение строки даты
                if(rgFeel1.getCheckedRadioButtonId()!=-1 || rgFeel2.getCheckedRadioButtonId()!=-1){//выбрано самочувствие
                    int rbInt=-1;
                    if(rgFeel1.getCheckedRadioButtonId()==-1){//если есть выбранный RadioButton в первой группе
                        rbInt = rgFeel2.getCheckedRadioButtonId();//получить id
                    }
                    else{//если есть во второй группе
                        rbInt = rgFeel1.getCheckedRadioButtonId();//получить id
                    }
                    RadioButton rb = findViewById(rbInt);//получение RadioButton по id
                    String health = rb.getText().toString();//Получение значения о самочуствии
                    String note = editTextNote.getText().toString();//получение заметок
                    SQLiteDatabase db = getBaseContext().openOrCreateDatabase("diary.db", MODE_PRIVATE,null);
                    db.execSQL("DELETE FROM Diary_Entries WHERE date = '"+dateString+"'");//удаление всех записей за текущую дату
                    db.execSQL("DELETE FROM Diary_Entries_Worries WHERE date = '"+dateString+"'");
                    db.execSQL("DELETE FROM Diary_Entries_Tracts WHERE date = '"+dateString+"'");
                    db.execSQL("DELETE FROM Diary_Entries_Moods WHERE date = '"+dateString+"'");
                    db.execSQL("DELETE FROM Diary_Entries_Feels WHERE date = '"+dateString+"'");
                    db.execSQL("DELETE FROM Diary_Entries_Alarms WHERE date = '"+dateString+"'");
                    db.execSQL("INSERT OR IGNORE INTO Diary_Entries VALUES ('"+ dateString +"', '"+health+"', '"+note+"')");//Добавление инфо о самочуствии и заметках
                    List<Integer> idsChipWorries = chipGroupWorries.getCheckedChipIds();//Получение списка id выбранных Chip
                    for (Integer id:idsChipWorries){//Проход по всем id выбранных Chip
                        Chip chip = chipGroupWorries.findViewById(id);//получение Chip
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Worries VALUES ('"+dateString+"','"+chip.getText().toString()+"')");//Добавление инфо о беспокойствах
                    }
                    List<Integer> idsChipTracts = chipGroupTracts.getCheckedChipIds();//Получение списка id выбранных Chip
                    for (Integer id:idsChipTracts){//Проход по всем id выбранных Chip
                        Chip chip = chipGroupTracts.findViewById(id);//получение Chip
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Tracts VALUES ('"+dateString+"','"+chip.getText().toString()+"')");//Добавление инфо о симптомах ЖКТ
                    }
                    List<Integer> idsChipMoods = chipGroupMoods.getCheckedChipIds();//Получение списка id выбранных Chip
                    for (Integer id:idsChipMoods){//Проход по всем id выбранных Chip
                        Chip chip = chipGroupMoods.findViewById(id);//получение Chip
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Moods VALUES ('"+dateString+"','"+chip.getText().toString()+"')");//Добавление инфо о настроении
                    }
                    List<Integer> idsChipFeels = chipGroupFeels.getCheckedChipIds();//Получение списка id выбранных Chip
                    for (Integer id:idsChipFeels){//Проход по всем id выбранных Chip
                        Chip chip = chipGroupFeels.findViewById(id);//получение Chip
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Feels VALUES ('"+dateString+"','"+chip.getText().toString()+"')");//Добавление инфо о ощущении
                    }
                    List<Integer> idsChipAlarms = chipGroupAlarms.getCheckedChipIds();//Получение списка id выбранных Chip
                    for (Integer id:idsChipAlarms){//Проход по всем id выбранных Chip
                        Chip chip = chipGroupAlarms.findViewById(id);//получение Chip
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Alarms VALUES ('"+dateString+"','"+chip.getText().toString()+"')");//Добавление инфо о тревоге
                    }
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);//переход на главную страницу
                    startActivity(intent);
                }else{//если не указано самочувствие
                    Toast.makeText(getBaseContext(),"Укажите ваше самочувствие",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void setDate(){//Установка текущей даты
        tvDate = findViewById(R.id.tvDate);
        LocalDate date = LocalDate.now();//получение текущей даты
        String dateString = date.getDayOfMonth() + " ";//формирование строки даты
        switch (date.getMonthValue()){
            case 1: dateString += "января"; break;
            case 2: dateString += "февраля"; break;
            case 3: dateString += "марта"; break;
            case 4: dateString += "апреля"; break;
            case 5: dateString += "мая"; break;
            case 6: dateString += "июня"; break;
            case 7: dateString += "июля"; break;
            case 8: dateString += "августа"; break;
            case 9: dateString += "сентября"; break;
            case 10: dateString += "октября"; break;
            case 11: dateString += "ноября"; break;
            case 12: dateString += "декабря"; break;
        }
        tvDate.setText(dateString);//установление строки даты в TextView
    }
    private String getDate(){//получение текущей даты в формате
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now().format(formatter);
    }
    private void setTodayInfo(){//установка информации из дневника за текущий день
        String date = getDate();//Получение даты в виде строки
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("diary.db",MODE_PRIVATE, null);//Поиск данных в бд по сегодняшней дате
        Cursor query = db.rawQuery("SELECT health,note FROM Diary_Entries WHERE date ='"+date+"'",null);
        Cursor queryWorry = db.rawQuery("SELECT worry FROM Diary_Entries_Worries WHERE date = '"+date+"'",null);
        Cursor queryTract = db.rawQuery("SELECT tract FROM Diary_Entries_Tracts WHERE date = '"+date+"'",null);
        Cursor queryMood = db.rawQuery("SELECT mood FROM Diary_Entries_Moods WHERE date = '"+date+"'",null);
        Cursor queryFeel = db.rawQuery("SELECT feel FROM Diary_Entries_Feels WHERE date = '"+date+"'",null);
        Cursor queryAlarms = db.rawQuery("SELECT alarm FROM Diary_Entries_Alarms WHERE date = '"+date+"'",null);
        if(query.getCount()>0) {                //Если есть записи о здоровье и заметки
            query.moveToFirst();
            String health = query.getString(0);//Получаем значение
            for (int id = 0; id < rgFeel1.getChildCount(); id++) {//перебираем RadioButton в RadioGroup
                RadioButton rb = (RadioButton) rgFeel1.getChildAt(id);//Создаем экземпляр RadioButton
                if (rb.getText().toString().contains(health)) {//Если названия совпадают
                    rb.setChecked(true);//Поставим как выбрано
                    break;
                }
            }
            for (int id = 0; id < rgFeel2.getChildCount(); id++) {//перебираем RadioButton в RadioGroup
                RadioButton rb = (RadioButton) rgFeel2.getChildAt(id);//Создаем экземпляр RadioButton
                if (rb.getText().toString().contains(health)) {//Если названия совпадают
                    rb.setChecked(true);//Поставим как выбрано
                    break;
                }
            }
            editTextNote.setText(query.getString(1));//Вставка заметок в EditText
        }
        if (queryWorry.getCount()>0) {//Если есть записи о беспокойствах
            queryWorry.moveToFirst();//Переходим к первой строке
            ArrayList<String> worries = new ArrayList<>();//Создаем массив для списка беспокойств
            do {
                worries.add(queryWorry.getString(0));//Добавляем в массив беспокойство
            } while (queryWorry.moveToNext());//Пока есть записи
            for (int id = 0; id < chipGroupWorries.getChildCount(); id++) {//Перебор всех Chip в ChipGroup
                Chip chip = (Chip) chipGroupWorries.getChildAt(id);//Получение экземпляра Chip
                if (worries.contains(chip.getText().toString())) {//Если названия совпадают
                    chip.setChecked(true);//Поставим как выбрано
                }
            }
        }
        if (queryTract.getCount()>0) {//Если есть записи о ЖКТ
            queryTract.moveToFirst();//Переходим к первой строке
            ArrayList<String> tracts = new ArrayList<>();//Создаем массив для списка симптомов ЖКТ
            do {
                tracts.add(queryTract.getString(0));//Добавляем в массив симптом ЖКТ
            } while (queryTract.moveToNext());//Пока есть записи
            for (int id = 0; id < chipGroupTracts.getChildCount(); id++) {//Перебор всех Chip в ChipGroup
                Chip chip = (Chip) chipGroupTracts.getChildAt(id);//Получение экземпляра Chip
                if (tracts.contains(chip.getText().toString())) {//Если названия совпадают
                    chip.setChecked(true);//Поставим как выбрано
                }
            }
        }
        if (queryMood.getCount()>0) {//Если есть записи о настроении
            queryMood.moveToFirst();//Переходим к первой строке
            ArrayList<String> moods = new ArrayList<>();//Создаем массив для списка настроения
            do {
                moods.add(queryMood.getString(0));//Добавляем в массив настроение
            } while (queryMood.moveToNext());//Пока есть записи
            for (int id = 0; id < chipGroupMoods.getChildCount(); id++) {//Перебор всех Chip в ChipGroup
                Chip chip = (Chip) chipGroupMoods.getChildAt(id);//Получение экземпляра Chip
                if (moods.contains(chip.getText().toString())) {//Если названия совпадают
                    chip.setChecked(true);//Поставим как выбрано
                }
            }
        }
        if (queryFeel.getCount()>0) {//Если есть записи об ощущениях
            queryFeel.moveToFirst();//Переходим к первой строке
            ArrayList<String> feels = new ArrayList<>();//Создаем массив для списка ощущений
            do {
                feels.add(queryFeel.getString(0));//Добавляем в массив ощущений
            } while (queryFeel.moveToNext());//Пока есть записи
            for (int id = 0; id < chipGroupFeels.getChildCount(); id++) {//Перебор всех Chip в ChipGroup
                Chip chip = (Chip) chipGroupFeels.getChildAt(id);//Получение экземпляра Chip
                if (feels.contains(chip.getText().toString())) {//Если названия совпадают
                    chip.setChecked(true);//Поставим как выбрано
                }
            }
        }
        if (queryAlarms.getCount()>0) {//Если есть записи об ощущениях
            queryAlarms.moveToFirst();//Переходим к первой строке
            String alarm = queryAlarms.getString(0);
            for (int id = 0; id < chipGroupAlarms.getChildCount(); id++) {//Перебор всех Chip в ChipGroup
                Chip chip = (Chip) chipGroupAlarms.getChildAt(id);//Получение экземпляра Chip
                if (alarm.contains(chip.getText().toString())) {//Если названия совпадают
                    chip.setChecked(true);//Поставим как выбрано
                    break;
                }
            }
        }
    }
}