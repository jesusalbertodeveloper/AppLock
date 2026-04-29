package com.jarm.applock;

import static com.jarm.applock.PasswordHashing.verifyPassword;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UsbDebuggingAuthenticationActivity extends AppCompatActivity {

    static public boolean alreadyrunning = false;
    int failedattempts = 0;
    long startTimeMillis = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alreadyrunning = true;
        setContentView(R.layout.usb_debugging_authentication_activity);
        Button unlockbutton = findViewById(R.id.unlockbutton212);
        unlockbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passwordfield = findViewById(R.id.passwordinputfieldforauthentication212);
                boolean ok = verifyPassword(getApplicationContext(), passwordfield.getText().toString());
                if (failedattempts == 5 && startTimeMillis != 0 && System.currentTimeMillis() - startTimeMillis >= 30_000) {
                    failedattempts = 0;
                    startTimeMillis = 0;
                }
                if (failedattempts == 5 && startTimeMillis != 0 && System.currentTimeMillis() - startTimeMillis < 30_000) {
                          unlockbutton.setText("Wait 30 seconds before trying again");
                } else if (ok) {
                    failedattempts = 0;
                    startTimeMillis = 0;
AccSrvForBootstrapAndLock.getInstance().setAdbHostAdditionBlocked(false);
                    Toast.makeText(UsbDebuggingAuthenticationActivity.this, "Approval unlocked.Try connecting your new host again", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    finishAndRemoveTask();
                } else if (failedattempts < 5) {
               unlockbutton.setText("Wrong password!");
                failedattempts++;
                } else {
                    unlockbutton.setText("Wrong password!");
                    startTimeMillis = System.currentTimeMillis();
                }
            }
            });
    }
    @Override
    public void onBackPressed() {
        alreadyrunning = false;
        stopLockTask();
        finishAffinity();
        finishAndRemoveTask();
    }
    @Override
    protected void onPause() {
        super.onPause();
        alreadyrunning = false;
    stopLockTask();
            finishAffinity();
            finishAndRemoveTask();
    }
}