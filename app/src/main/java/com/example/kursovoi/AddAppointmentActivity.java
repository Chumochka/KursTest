package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddAppointmentActivity extends AppCompatActivity {

    private TextView tvUser;
    private ImageButton btnBack;
    private Spinner spinnerSpecialization;
    private Spinner spinnerDoctor;
    private EditText etDateWish;
    private Chip chipAddAppointment;
    private String[][] specializationList;
    private String[][] doctorList;
    private int doctorPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        tvUser = findViewById(R.id.tvUser);
        btnBack = findViewById(R.id.btnBack);
        spinnerSpecialization = findViewById(R.id.spinnerSpecialization);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        etDateWish = findViewById(R.id.etDateWish);
        chipAddAppointment = findViewById(R.id.chipAddAppointment);
        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");
        String name = extras.getString("name");
        tvUser.setText(name);
        try {
            String response = new fetchData().execute("/specs", "GET", "").get();
            if(!response.equals("null")) {
                JSONArray array = new JSONArray(response);
                specializationList = new String[array.length()][2];
                List<String> specArray = new ArrayList<String>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    specializationList[i][0] = Integer.toString(obj.getInt("id_specialization"));
                    specializationList[i][1] = obj.getString("name");
                    specArray.add(obj.getString("name"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, specArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSpecialization.setAdapter(adapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        spinnerSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {
                try {
                    String id_spec = specializationList[j][0];
                    String response = new fetchData().execute("/doctorspecialization?id_specialization="+id_spec, "GET", "").get();
                    if(!response.equals("null")) {
                        JSONArray array = new JSONArray(response);
                        doctorList = new String[array.length()][2];
                        List<String> doctorsArray = new ArrayList<String>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            doctorList[i][0] = obj.getString("id_worker");
                            doctorList[i][1] = obj.getString("surname") + " " + obj.getString("name").charAt(0) + ".";
                            if (!obj.getString("patronymic").isEmpty()){
                                doctorList[i][1] += obj.getString("patronymic") +".";
                            }
                            doctorsArray.add(doctorList[i][1]);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddAppointmentActivity.this, android.R.layout.simple_spinner_item, doctorsArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDoctor.setAdapter(adapter);
                        spinnerDoctor.setEnabled(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerDoctor.setEnabled(false);
            }
        });
        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doctorPosition=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        chipAddAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_wish = etDateWish.getText().toString();
                if(doctorPosition>-1 && !date_wish.isEmpty()){
                    String a = doctorList[doctorPosition][0];
                    int id_doctor = Integer.parseInt(a);
                    try {
                        JSONObject json = new JSONObject("{\"id_appointment\": 0, \"id_patient\": "+id+",\"id_doctor\": "+id_doctor+", \"date_wish\": "+date_wish+", \"date\": null, \"cabinet\": null}");
                        String response = new fetchData().execute("/appointment", "POST", json.toString()).get();
                        if(response.equals("null")) {
                            Toast.makeText(getApplicationContext(), "Не удалось добавить запись",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Запись добавлена",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Выберите доктора и напишите пожелания в времени приема", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}