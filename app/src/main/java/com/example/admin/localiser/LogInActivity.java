package com.example.admin.localiser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import database.DatabaseManagement;
import static com.example.admin.localiser.ValuesInApp.Values.*;


public class LogInActivity extends Activity {

    EditText username;
    EditText password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText3);
        login = (Button) findViewById(R.id.button5);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
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

    public void signIn(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String user = username.getText().toString();
        String pass = password.getText().toString();

        if (user.matches("") && pass.matches("")){
            dialogBadCredentials(FILL_THE_DATA);
        }
        else {
            DatabaseManagement dm = new DatabaseManagement(this);
            if(dm.getLogin(user).matches(SHA1(pass))){
                dialogAcceptedLoginAndPass(ACCEPTED_DATA);
            }
            else {
                dialogBadCredentials(BAD_CREDENTIALS);
            }
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void dialogAcceptedLoginAndPass(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setTitle(TITLE_SUCCESS)
                .setIcon(R.drawable.success1)
                .setPositiveButton(BUTTON_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void dialogBadCredentials(String msg){
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

    public void forgotPass(View view) {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(DIGEST);
        md.update(text.getBytes(CODING));
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static String generateString(Random rng, String characters, int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    public void exitApplication(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(ASK)
                .setCancelable(false)
                .setTitle(TITLE_EXIT)
                .setIcon(R.drawable.close)
                .setNegativeButton(BUTTON_CANCEL, null)
                .setPositiveButton(BUTTON_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                         finish();
                         System.exit(0);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
