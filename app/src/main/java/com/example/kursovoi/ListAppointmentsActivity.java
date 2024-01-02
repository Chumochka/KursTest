package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

public class ListAppointmentsActivity extends AppCompatActivity {

    private TextView tvUser;
    private ImageButton btnBack;
    private ScrollView scrollAppointments;
    private Chip chipAddAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointments);
        btnBack = findViewById(R.id.btnBack);
        scrollAppointments = findViewById(R.id.scrollAppointments);
        chipAddAppointment = findViewById(R.id.chipAddAppointment);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListAppointmentsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        chipAddAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListAppointmentsActivity.this, AddAppointmentActivity.class);
                startActivity(intent);
            }
        });
    }
}