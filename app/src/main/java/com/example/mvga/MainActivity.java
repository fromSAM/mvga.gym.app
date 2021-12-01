package com.example.mvga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button login, register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = (Button) findViewById(R.id.button_login);
        register = (Button) findViewById(R.id.button_register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeractivity();
            }
        });







        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginactivity();
            }
        });
    }


    public void loginactivity() {
        Intent intent = new Intent(this, Login_Page.class);
        startActivity(intent);


    }


    public void registeractivity() {
        Intent intent = new Intent(this, Register_Page.class);
        startActivity(intent);


    }
}