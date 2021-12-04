package com.example.mvga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login_Page extends AppCompatActivity {

    private Button login_button;
    EditText username , password ;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        login_button = (Button) findViewById(R.id. button_loginB);
        username = (EditText) findViewById(R.id. editText_userin);
        password = (EditText) findViewById(R.id.editText_loginpass);

        db = new DBHelper( this);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userdata = username.getText().toString();
                String passdata = password.getText().toString();

                if(userdata.equals("") || passdata.equals("")) {
                    Toast.makeText(Login_Page.this, "Enter Valid Username / Passowrd", Toast.LENGTH_SHORT).show();
                }
                else {

                    Boolean checkUP = db.checkunamepass(userdata, passdata);
                    if (checkUP == true) {
                        Toast.makeText(Login_Page.this, "Logged in!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Login_Page.this, "wrong username or password!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        }








}