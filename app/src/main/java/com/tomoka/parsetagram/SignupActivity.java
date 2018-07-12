package com.tomoka.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public EditText susername_et;
    public EditText spassword_et;
    public EditText semail_et;
    public EditText shandle_et;
    public Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        susername_et = findViewById(R.id.susername_et);
        spassword_et = findViewById(R.id.spassword_et);
        semail_et = findViewById(R.id.semail_et);
        shandle_et = findViewById(R.id.shandle_et);
        submit_btn = findViewById(R.id.submit_btn);
    }

    public void onSubmit(View v) {
        final String username = susername_et.getText().toString();
        final String password = spassword_et.getText().toString();
        final String email = semail_et.getText().toString();
        final String handle = shandle_et.getText().toString();

        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("handle", handle);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("Signup", "Success");
                    final Intent intent = new Intent(SignupActivity.this, fragmentholder.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("Signup", "failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
