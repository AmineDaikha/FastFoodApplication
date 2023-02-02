package com.fastfoodapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fastfoodapp.R;


public class LoginActivity extends AppCompatActivity {

    EditText eText_Ip_Server, username;

    Button btnConnect;

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        eText_Ip_Server = findViewById(R.id.edText_IpServer);
        username = findViewById(R.id.edText_username);
        btnConnect = findViewById(R.id.btnConnect);

        mPrefs = this.getSharedPreferences("IP_S", 0);
        String IP_Server = mPrefs.getString("tag", "nnnn");

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Champs vide !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("tag", eText_Ip_Server.getText().toString()).commit();
                mEditor.putString("user", username.getText().toString()).commit();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        String myString = "nnnn";
        if (IP_Server.equals(myString)) {

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}