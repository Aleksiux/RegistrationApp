package com.example.kalendorius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.example.kalendorius.getersSeters.Employees;

import java.util.ArrayList;
import java.util.List;

public class CustomCalendarActivity extends AppCompatActivity {

    private AutoCompleteTextView mAutoCompleteDoctorView;
    CustomCalendar customCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCalendar = (CustomCalendar) findViewById(R.id.kalendoriaus_virsus);
        customCalendar.SetUpCalendar();
     //   mAutoCompleteDoctorView = findViewById(R.id.search_Doctor);
        //loadData();




    }

    private void loadData(){
        List<Employees> employees = new ArrayList<Employees>();
        EmployeeSearchAdapter employeeSearchAdapter = new EmployeeSearchAdapter(getApplicationContext(), employees);
        mAutoCompleteDoctorView.setThreshold(1);
        mAutoCompleteDoctorView.setAdapter(employeeSearchAdapter);
    }
}
