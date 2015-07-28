package com.example.admin.localiser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import database.DatabaseManagement;

import static com.example.admin.localiser.LogInActivity.SHA1;
import static com.example.admin.localiser.LogInActivity.generateString;


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
            dialogAlert("Please fill whole the form to get password via sms");

        else if(dm.getEmailAsUser(user).matches(mail))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure send sms on your number ? ")
                    .setCancelable(false)
                    .setTitle("Success")
                    .setIcon(R.drawable.success1)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String randomString = generateString(new Random(), dm.getLogin(user), 10);
                            sendSMS(dm.getNumberByEmail(mail), "Hi " + user + ". Your password to Localiser is: " + randomString);
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
        else if (!dm.getEmailAsUser(user).matches(mail)) dialogAlert("Username don't matches to email address!");

    }
    public void dialogAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setTitle("Failure")
                .setIcon(R.drawable.x)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
