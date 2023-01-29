package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddActivity extends AppCompatActivity {
    RadioGroup rgFeel1;
    RadioGroup rgFeel2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        rgFeel1 = findViewById(R.id.rgFeel1);
        rgFeel2 = findViewById(R.id.rgFeel2);
        rgFeel1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb!=null)
                    if(rb.isChecked() && rgFeel2.getCheckedRadioButtonId()!=-1)
                        rgFeel2.clearCheck();
            }
        });
        rgFeel2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb!=null)
                    if(rgFeel1.getCheckedRadioButtonId()!=-1 && rb.isChecked())
                        rgFeel1.clearCheck();
            }
        });
    }
}