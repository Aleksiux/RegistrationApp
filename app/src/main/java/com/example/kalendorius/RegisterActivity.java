package com.example.kalendorius;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kalendorius.databaseStructure.DBStructure;
import com.example.kalendorius.dateFormat.DateInputMask;
import com.example.kalendorius.getersSeters.Employees;
import com.example.kalendorius.getersSeters.Events;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {


    DBOpenHelper mdbOpenHelper;
    Context context;
    EditText mTextEmail;
    EditText mTextName;
    EditText mTextSurname;
    EditText mTextBirthday;
    EditText mTextDoctor;
    EditText mTextPassword;
    EditText mTextcnfPassword;
    ImageButton mButtonReg;
    TextView mTextViewLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        editText = (EditText) alertDialog.findViewById(R.id.label_field);


        mTextEmail = (EditText) findViewById(R.id.etEmail);
        mTextName = (EditText) findViewById(R.id.Name);
        mTextSurname = (EditText) findViewById(R.id.Surname);
        mTextBirthday = (EditText) findViewById(R.id.BirthDay);
        mTextPassword = (EditText) findViewById(R.id.etPassword);
        mTextcnfPassword = (EditText) findViewById(R.id.confirm_etPassword);
        mButtonReg = (ImageButton) findViewById(R.id.btnReg);
        mTextViewLogin = (TextView) findViewById(R.id.textview_login);
        mTextDoctor = (EditText) findViewById(R.id.doctor);



        ImageView bgFooter = (ImageView) findViewById(R.id.bgFooter);
        bgFooter.setAlpha(127);//TODO Sugalvot kaip graziai paadaryt


        new DateInputMask(mTextBirthday);


        mdbOpenHelper = new DBOpenHelper(this);


        mTextViewLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(LoginIntent);
            }
        });

        mButtonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mTextEmail.getText().toString();
                String name = mTextName.getText().toString();
                String surname = mTextSurname.getText().toString();
                String birthday = mTextBirthday.getText().toString();
                String doctor = mTextDoctor.getText().toString();
                String pass = mTextPassword.getText().toString();
                String cnf_pass = mTextcnfPassword.getText().toString();


                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "El. paštas yra neteisingas", Toast.LENGTH_SHORT).show();
                } else {
                    if (name.length() < 3) {
                        Toast.makeText(RegisterActivity.this, "Vardas yra per trumpas", Toast.LENGTH_SHORT).show();
                    } else {
                        if (surname.length() < 3) {
                            Toast.makeText(RegisterActivity.this, "Pavardė yra per trumpa", Toast.LENGTH_SHORT).show();
                        } else {
                            if (pass.isEmpty()) {
                                Toast.makeText(RegisterActivity.this, "Slaptažodžio laukas negali būti tušias", Toast.LENGTH_SHORT).show();
                            } else {
                                if ((pass.length() < 8)) {
                                    Toast.makeText(RegisterActivity.this, "Slaptažodis negali būti trumpesnis nei 8 simboliai", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (!pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$")) {
                                        Toast.makeText(RegisterActivity.this, "Slaptažodis neatitinka reikalavimų. Reikalingas bent 1 simbolis ir 1 skaičius", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (pass.equals(cnf_pass)) {
                                            long val = mdbOpenHelper.SaveEmployee(email, name, surname, birthday, doctor, pass);
                                            if (val > 0) {
                                                Toast.makeText(RegisterActivity.this, "Sėkmingai užsiregistravote", Toast.LENGTH_SHORT).show();
                                                Intent moveToLogin = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(moveToLogin);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Registracija nesėkminga", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Slaptažodis nesutampa", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
//        AutoCompleteTextView textView = (AutoCompleteTextView)
//                findViewById(R.id.countries);
//        textView.setAdapter(adapter);
//    }
    }
}
