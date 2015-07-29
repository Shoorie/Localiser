package com.example.admin.localiser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import database.DatabaseManagement;
import static com.example.admin.localiser.LogInActivity.generateString;
import static com.example.admin.localiser.ValuesInApp.Values.*;


public class ForgotPassword extends Activity {

    EditText email;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.editText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void recover(View view) {

        final String user = username.getText().toString();
        final String mail = email.getText().toString();

        final DatabaseManagement dm = new DatabaseManagement(this);

        if(user.matches("") && mail.matches(""))
            dialogAlert(FILL_WHOLE_FORM);

        else if(dm.getEmailAsUser(user).matches(mail))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(SURE_TO_SEND_SMS)
                    .setCancelable(false)
                    .setTitle(TITLE_SUCCESS)
                    .setIcon(R.drawable.success1)
                    .setNegativeButton(BUTTON_CANCEL, null)
                    .setPositiveButton(BUTTON_OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String randomString = generateString(new Random(), dm.getLogin(user), 10);
                            sendSMS(dm.getNumberByEmail(mail), HI + user + YOUR_PASS + randomString);
                            try {
                                dm.updatePassword(randomString, user);
                            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getBaseContext(), LogInActivity.class);
                            startActivity(intent);
                            //jesli pojdzie to pomyslec nad tym, zeby zrobic nowe Activity ze zmiana hasla na nowe
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else if (!dm.getEmailAsUser(user).matches(mail)) dialogAlert(NOT_MATCHES);

    }
    public void dialogAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setTitle(TITLE_FAILURE)
                .setIcon(R.drawable.x)
                .setPositiveButton(BUTTON_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
