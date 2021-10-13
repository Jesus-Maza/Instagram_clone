package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LogInActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    public static final String TAG = "LogInActivity";
    private EditText EtUsername;
    private EditText Etpassword;
    private Button btnLogIn;
    public static final int REQUEST_CODE = 20;
    private Button btnSign_Up;


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
        btnSign_Up = findViewById(R.id.btnSignUp);

        btnSign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sign_user = EtUsername.getText().toString();
                String password_user = Etpassword.getText().toString();
                Sign_Up(sign_user, password_user);
            }
        });

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

    private void Sign_Up(String sign_user, String user_password){
        ParseUser user = new ParseUser();
        user.setUsername(sign_user);
        user.setPassword(user_password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if( e == null){
                    Toast.makeText(LogInActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.e(TAG, "Error", e);
                }
                goMainActivity();
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