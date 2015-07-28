package database;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.admin.localiser.LogInActivity;
import com.example.admin.localiser.MainActivity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import database.UserDatabase.User;
import model.ClientData;

import static com.example.admin.localiser.LogInActivity.SHA1;
import static database.UserDatabase.User.USER_EMAIL;
import static database.UserDatabase.User.USER_ID;
import static database.UserDatabase.User.USER_LOGIN;
import static database.UserDatabase.User.USER_PASSWORD;
import static database.UserDatabase.User.USER_TABLE_NAME;
import static database.UserDatabase.User.USER_TELEPHONE;

public class DatabaseManagement extends SQLiteOpenHelper {
    
    private static final String DB_NAME = "UserData.db";
    private static final int DB_VERSION = 1;

    public DatabaseManagement(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE_NAME+ " ("
                + USER_ID + " integer primary key autoincrement,"
                + USER_LOGIN + " text,"
                + USER_PASSWORD + " text,"
                + USER_EMAIL +" text,"
                + USER_TELEPHONE + " integer);" +
                "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addClientDataToDatabase(ClientData clientData){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_LOGIN, clientData.getLogin());
        values.put(USER_PASSWORD, clientData.getPassword());
        values.put(USER_EMAIL, clientData.getEmail());
        values.put(USER_TELEPHONE, clientData.getTelephoneNumber());
        db.insert(USER_TABLE_NAME, null, values);
        db.close();
    }

    public List<ClientData> selectAll(){
        List<ClientData> clientDatas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + USER_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ClientData clientData = new ClientData();
                clientData.setClientId(cursor.getLong(0));
                clientData.setLogin(cursor.getString(1));
                clientData.setPassword(cursor.getString(2));
                clientData.setEmail(cursor.getString(3));
                clientDatas.add(clientData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return clientDatas;
    }

    public String getLogin(String user) {
        String password="";
        String selectQuery = "SELECT " + USER_PASSWORD + " FROM "
                            + USER_TABLE_NAME + " where "
                            + USER_LOGIN + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            password = cursor.getString(0);
            Log.d("Passy: ", password + " " + user);
        }
        cursor.close();
        db.close();
        return password;
    }

    public String getEmailAsUser(String user) {
        String email="";
        String selectQuery = "SELECT " + USER_EMAIL + " FROM "
                             + USER_TABLE_NAME + " where "
                             + USER_LOGIN + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            email = cursor.getString(0);
            Log.d("Email: ", email + " " + user);
        }
        cursor.close();
        db.close();
        return email;
    }

    public String getNumberByEmail(String email) {

        String telephoneNumber = "";
        String selectQuery = "SELECT " + USER_TELEPHONE + " FROM "
                             + USER_TABLE_NAME + " where "
                             + USER_EMAIL + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email});
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            telephoneNumber = cursor.getString(0);
            Log.d("Email: ", email + " " + telephoneNumber);
        }
        cursor.close();
        db.close();
        return telephoneNumber;
    }

    public void updatePassword(String pass, String user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PASSWORD, SHA1(pass));
        db.update(USER_TABLE_NAME, values, USER_LOGIN + "= ? ", new String[] { String.valueOf(user) });
    }
}
