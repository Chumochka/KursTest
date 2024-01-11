package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListAppointmentsActivity extends AppCompatActivity {

    private TextView tvUser;
    private ImageButton btnBack;
    private LinearLayout containerAppointments;
    private Chip chipAddAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointments);
        tvUser = findViewById(R.id.tvUser);
        btnBack = findViewById(R.id.btnBack);
        containerAppointments = findViewById(R.id.containerAppointments);
        chipAddAppointment = findViewById(R.id.chipAddAppointment);
        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");
        String name = extras.getString("name");
        tvUser.setText(name);
        try {
            String response = new fetchData().execute("/appointmentspatient?id="+id, "GET", "").get();
            if(response.equals("null")){
                return;
            }
            JSONArray array = new JSONArray(response);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dateParams.gravity = Gravity.CENTER_HORIZONTAL;
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                String doctor_surname = obj.getString("surname");
                String doctor_name = obj.getString("name");
                String doctor_patronymic = obj.getString("patronymic");
                String dateStr = obj.getString("date");
                String cabinet = obj.getString("cabinet");
                String specialization = obj.getString("specialization");
                LinearLayout layout = new LinearLayout(this);
                params.setMargins(0,convertPixelInDp(10),0,0);
                layout.setLayoutParams(params);
                layout.setPadding(convertPixelInDp(10),convertPixelInDp(8),convertPixelInDp(10),convertPixelInDp(16));
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setBackgroundResource(R.drawable.analyze_block_bg);
                containerAppointments.addView(layout);
                TextView dateView = new TextView(this);
                dateView.setText("Запись на "+formatDate(dateStr));
                dateView.setLayoutParams(dateParams);
                dateView.setTextSize(20);
                dateView.setTextColor(getResources().getColor(R.color.textColor));
                layout.addView(dateView);
                TextView descriptionView = new TextView(this);
                String doctor = "Врач: "+doctor_surname+" "+doctor_name.charAt(0)+".";
                if(!doctor_patronymic.isEmpty())
                    doctor += doctor_patronymic.charAt(0)+".";
                descriptionView.setText(doctor+"\nСпециальность: "+specialization+"\nКабинет "+cabinet);
                params.setMargins(0,convertPixelInDp(16),0,0);
                descriptionView.setLayoutParams(params);
                descriptionView.setTextSize(19);
                descriptionView.setTextColor(getResources().getColor(R.color.textColor));
                layout.addView(descriptionView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        chipAddAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListAppointmentsActivity.this, AddAppointmentActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }
    private String formatDate(String dateString){
        LocalDateTime dateTime = LocalDateTime.parse(dateString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM H:mm");
        return dateTime.format(formatter);
    }
    private int convertPixelInDp(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}