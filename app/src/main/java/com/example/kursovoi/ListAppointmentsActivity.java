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
        tvUser = findViewById(R.id.tvUser);
        btnBack = findViewById(R.id.btnBack);
        scrollAppointments = findViewById(R.id.scrollAppointments);
        chipAddAppointment = findViewById(R.id.chipAddAppointment);
        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");
        String name = extras.getString("name");
        tvUser.setText(name);
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
                startActivity(intent);
            }
        });
    }
}