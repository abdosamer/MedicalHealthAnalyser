package com.example.medicalhealthanalyser.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medicalhealthanalyser.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        initLottieTitleAnim();

        initLoginViews();
    }

    private void initLottieTitleAnim() {

    }

    private void initLoginViews() {

        Button mLoginBt = findViewById(R.id.login_button);

        mLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open new Activity
                startActivity(new Intent(SignInActivity.this,HomeActivity.class));
            }
        });

    }
}