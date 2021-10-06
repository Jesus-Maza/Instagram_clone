package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogInActivity extends AppCompatActivity {

    public static final String TAG = "LogInActivity";
    private EditText EtUsername;
    private EditText Etpassword;
    private Button btnLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if(ParseUser.getCurrentUser() != null ){
            goMainActivity();
        }

        EtUsername =  findViewById(R.id.username);
        Etpassword = findViewById(R.id.etpassword);
        btnLogIn = findViewById(R.id.button);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onCLick Log in Button.");
                String username = EtUsername.getText().toString();
                String password = Etpassword.getText().toString();
                loginUser(username, password);

            }
        });
    }

    private void loginUser(String username, String password){
        Log.i(TAG, "Attempting to sign in " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Toast.makeText(LogInActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"Error while trying to log in",e  );
                    return;
                }
                goMainActivity();
                Toast.makeText(LogInActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}