package com.jarm.applock;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccServDisableError extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acc_serv_disable_error_screen);
        Button button922 = findViewById(R.id.button922);
        button922.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
finishAffinity();
            finishAndRemoveTask();
            }
        });
    }
}