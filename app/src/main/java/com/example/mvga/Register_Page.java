package com.example.mvga;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Register_Page extends AppCompatActivity {


    EditText uname, pass , cpass , mail, name;
    Button reg;
    RadioGroup radio_group_Package, gender;
    RadioButton radioButton;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        uname = (EditText) findViewById(R.id.editText_inuser);
        pass = (EditText) findViewById(R.id.editText_inpass);
        cpass = (EditText) findViewById(R.id.editText_cpass);
        mail = (EditText) findViewById(R.id.editText_inemail);
        name = (EditText) findViewById(R.id.editText_name);

        gender =  findViewById(R.id.radio_group_gender);
        radio_group_Package =  findViewById(R.id.radio_group_Package);

        reg = (Button) findViewById(R.id.button_reg);

        db = new DBHelper(this);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = uname.getText().toString();
                String password = pass.getText().toString();
                String con_pass = cpass.getText().toString();
                String email = mail.getText().toString();
                String cname = name.getText().toString();

                int selectGId = gender.getCheckedRadioButtonId();
                radioButton = findViewById(selectGId);
                String gender = radioButton.getText().toString();

                int selectPKGId = radio_group_Package.getCheckedRadioButtonId();
                radioButton = findViewById(selectPKGId);
                String pkg = radioButton.getText().toString();

                if(username.equals("") || password.equals("") || con_pass.equals("") || email.equals("") || cname.equals("") || gender.equals("") || pkg.equals("")){
                    Toast.makeText(Register_Page.this, "Fill all the data",Toast.LENGTH_SHORT ).show();
                }
                else{
                    if(password.equals(con_pass)){
                        Boolean check_u= db.checkuname(username);
                        if(check_u==false){
                            Boolean insert=db.insertcustomerdata(username,password,email,cname,gender,pkg);

                            if(insert=true){
                                Toast.makeText(Register_Page.this,"Signed-up successfully!",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Register_Page.this,"Sign-up failed!",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(Register_Page.this,"Please choose a different username.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(Register_Page.this,"Please type the same password!",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });



    }
}