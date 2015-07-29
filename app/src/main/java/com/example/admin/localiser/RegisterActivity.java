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
import java.security.NoSuchAlgorithmException;
import java.util.List;
import database.DatabaseManagement;
import model.ClientData;
import static com.example.admin.localiser.LogInActivity.*;
import static com.example.admin.localiser.ValuesInApp.Values.*;


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
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
            builder.setMessage(ACCOUNT_CREATED)
                    .setCancelable(false)
                    .setTitle(TITLE_SUCCESS)
                    .setIcon(R.drawable.success1)
                    .setPositiveButton(BUTTON_OK, new DialogInterface.OnClickListener() {
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
            dialogAlert(FILL_WHOLE_REGISTER);
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
