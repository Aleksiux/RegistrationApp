package com.example.kalendorius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText mTextEmail;
    EditText mTextPassword;
    ImageButton mButtonLogin;
    TextView mTextViewRegister;
    DBOpenHelper dbOpenHelper;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        dbOpenHelper = new DBOpenHelper(this);
        mTextEmail = (EditText)findViewById(R.id.etEmail);
        mTextPassword = (EditText)findViewById(R.id.etPassword);
        mButtonLogin = (ImageButton)findViewById(R.id.btnLogin);
        mTextViewRegister = (TextView)findViewById(R.id.etRegister);
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mTextEmail.getText().toString().trim();
                String pass = mTextPassword.getText().toString().trim();

                if(dbOpenHelper.areCredentialsCorrect(email, pass))
                {
                   Intent customCalendar = new Intent(MainActivity.this, CustomCalendarActivity.class);
                    startActivity(customCalendar);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Blogai įvestas prisijungimo \nel. paštas arba slaptažodis",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showProgressingView() {

        if (!isProgressShowing) {
            View view=findViewById(R.id.progressBar1);
            view.bringToFront();
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }








}