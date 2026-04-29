package com.jarm.applock;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PasswordHashing.isPasswordSet(getApplicationContext()) || SettingsActivity.isAuthenticationSuccessful()) {
            setContentView(R.layout.password_setup_activity);
            EditText passwordinputfieldforsetup = findViewById(R.id.passwordinputfieldforsetup);
            EditText passwordconfirmationinputfield = findViewById(R.id.passwordconfirmationinputfield);
            Button passwordchangebutton = findViewById(R.id.passwordchangebutton);
            passwordchangebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (passwordinputfieldforsetup.getText().toString().equals(passwordconfirmationinputfield.getText().toString())) {
                        PasswordHashing.setupPasswordHash(getApplicationContext(), passwordconfirmationinputfield.getText().toString());
                        Toast.makeText(PasswordSetupActivity.this, "Password set successfully!", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    }
                }
            });
        } else {
            finishAndRemoveTask();
        }
    }
}