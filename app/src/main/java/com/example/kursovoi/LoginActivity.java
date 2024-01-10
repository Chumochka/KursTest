package com.example.kursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private EditText etLogin;
    private EditText etPassword;
    private TextView tvEnter;
    private Chip chipAuthorization;
    private int id;
    private String surname;
    private String name;
    private String patronymic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        tvEnter = findViewById(R.id.tvEnter);
        chipAuthorization = findViewById(R.id.chipAuthorization);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("diary.db",MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Login_Details (login TEXT, password TEXT)");
        Cursor query = db.rawQuery("SELECT login, password FROM Login_Details",null);//получение списка дат
        if(query.moveToFirst()){//если есть данные
             Autho(query.getString(0),query.getString(1));
        }
        tvEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("id", -1);
                intent.putExtra("name", "");
                startActivity(intent);
            }
        });
        chipAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etLogin.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Введите свой логин и пароль.", Toast.LENGTH_LONG).show();
                    return;
                }
                Autho("","");
            }
        });
    }
    private void Autho(String login, String password){
        JSONObject obj;
        String strLogin;
        String strPassword;
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("diary.db",MODE_PRIVATE, null);
        if (!login.isEmpty() && !password.isEmpty()){
            strLogin = login;
            strPassword = password;
        }else {
            strLogin = etLogin.getText().toString();
            strPassword = etPassword.getText().toString();
        }
        try {
            String response = new fetchData().execute("/patientautho?login="+strLogin+"&password="+strPassword, "GET", "").get();
            if(response.equals("null")){
                Toast.makeText(getApplicationContext(),"Некорректные данные для входа",Toast.LENGTH_LONG).show();
                db.execSQL("DELETE FROM Login_Details");
                return;
            }
            obj = new JSONObject(response);
            id = obj.getInt("id_patient");
            surname = obj.getString("surname");
            name = obj.getString("name");
            patronymic = obj.getString("patronymic");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fullName = surname+" "+name.charAt(0)+".";
        if(!patronymic.isEmpty())
            fullName += patronymic.charAt(0)+".";
        if(login.isEmpty() && password.isEmpty())
            db.execSQL("INSERT OR IGNORE INTO Login_Details VALUES ('"+etLogin.getText().toString()+"','"+etPassword.getText().toString()+"')");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name", fullName);
        startActivity(intent);
    }
}