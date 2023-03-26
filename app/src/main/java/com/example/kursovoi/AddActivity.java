package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        chipSave = findViewById(R.id.chipSave);
        setDate();
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dateString = LocalDate.now().format(formatter);
                if(rgFeel1.getCheckedRadioButtonId()!=-1 || rgFeel2.getCheckedRadioButtonId()!=-1){
                    int rbInt=-1;
                    if(rgFeel1.getCheckedRadioButtonId()==-1){
                        rbInt = rgFeel2.getCheckedRadioButtonId();
                    }
                    else{
                        rbInt = rgFeel1.getCheckedRadioButtonId();
                    }
                    RadioButton rb = findViewById(rbInt);
                    String health = rb.getText().toString();
                    editTextNote = findViewById(R.id.editTextNote);
                    String note = editTextNote.getText().toString();
                    SQLiteDatabase db = getBaseContext().openOrCreateDatabase("diary.db", MODE_PRIVATE,null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries (date TEXT PRIMARY KEY, health TEXT, note TEXT)");
                    db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Worries (date TEXT, worry TEXT)");
                    db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Tracts (date TEXT, tract TEXT)");
                    db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Moods (date TEXT, mood TEXT)");
                    db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Feels (date TEXT, feel TEXT)");
                    db.execSQL("CREATE TABLE IF NOT EXISTS Diary_Entries_Alarms (date TEXT, alarm TEXT)");
                    db.execSQL("INSERT OR IGNORE INTO Diary_Entries VALUES ('"+ dateString +"', '"+health+"', '"+note+"')");
                    List<Integer> idsChipWorries = chipGroupWorries.getCheckedChipIds();
                    for (Integer id:idsChipWorries){
                        Chip chip = chipGroupWorries.findViewById(id);
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Worries VALUES ('"+dateString+"','"+chip.getText().toString()+"')");
                    }
                    List<Integer> idsChipTracts = chipGroupTracts.getCheckedChipIds();
                    for (Integer id:idsChipTracts){
                        Chip chip = chipGroupTracts.findViewById(id);
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Tracts VALUES ('"+dateString+"','"+chip.getText().toString()+"')");
                    }
                    List<Integer> idsChipMoods = chipGroupMoods.getCheckedChipIds();
                    for (Integer id:idsChipMoods){
                        Chip chip = chipGroupMoods.findViewById(id);
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Moods VALUES ('"+dateString+"','"+chip.getText().toString()+"')");
                    }
                    List<Integer> idsChipFeels = chipGroupFeels.getCheckedChipIds();
                    for (Integer id:idsChipFeels){
                        Chip chip = chipGroupFeels.findViewById(id);
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Feels VALUES ('"+dateString+"','"+chip.getText().toString()+"')");
                    }
                    List<Integer> idsChipAlarms = chipGroupAlarms.getCheckedChipIds();
                    for (Integer id:idsChipAlarms){
                        Chip chip = chipGroupAlarms.findViewById(id);
                        db.execSQL("INSERT OR IGNORE INTO Diary_Entries_Alarms VALUES ('"+dateString+"','"+chip.getText().toString()+"')");
                    }
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(),"Укажите ваше самочувствие",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void setDate(){
        tvDate = findViewById(R.id.tvDate);
        LocalDate date = LocalDate.now();
        String dateString = date.getDayOfMonth() + " ";
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
        tvDate.setText(dateString);
    }
}