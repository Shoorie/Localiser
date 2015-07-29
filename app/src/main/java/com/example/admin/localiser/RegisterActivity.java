package com.example.admin.localiser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import database.DatabaseManagement;
import model.ClientData;

import static com.example.admin.localiser.LogInActivity.*;


public class RegisterActivity extends Activity {

    EditText login;
    EditText password;
    EditText email;
    EditText telephoneNumber;
    DatabaseManagement dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dm = new DatabaseManagement(this);
        readSelectedData();

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        telephoneNumber = (EditText) findViewById(R.id.tnumber);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void backToSignIn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    public void registerAccount(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String log = login.getText().toString();
        String pass = password.getText().toString();
        String em = email.getText().toString();
        String tn = telephoneNumber.getText().toString();

        if(!log.matches("") && !em.matches("") && !pass.matches("") && !tn.matches("")) {
            Log.d("Insert: ", "Inserting ..");
            dm.addClientDataToDatabase(new ClientData(log, SHA1(pass), em, tn));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Account created")
                    .setCancelable(false)
                    .setTitle("Success")
                    .setIcon(R.drawable.success1)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getBaseContext(), LogInActivity.class);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            readSelectedData();

        }
        else {
            dialogAlert("You must fill all form to register!");
        }
    }

    public void readSelectedData(){
        List<ClientData> clientDatas = dm.selectAll();
        Log.d("Select: ", "Selecting ..");
        for (ClientData c : clientDatas) {
            String logg = "Id: " + c.getClientId() + " Login: " + c.getLogin() +
                    " Pass: " + c.getPassword() +" Email: " + c.getEmail();
            Log.d("ClientData: ", logg);
        }
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
