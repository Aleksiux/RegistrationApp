package com.example.kalendorius;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.annotation.Nullable;

import com.example.kalendorius.databaseStructure.ClientStructure;
import com.example.kalendorius.databaseStructure.DBStructure;
import com.example.kalendorius.databaseStructure.DBemployee;
import com.example.kalendorius.getersSeters.Employees;

import java.util.ArrayList;
import java.util.List;


public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "EVENT_DB";
    public static final int DB_VERSION = 1;

    public SQLiteDatabase database;

    private static final String CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + DBemployee.EMPLOYEE_TABLE_NAME
            + " ( " + DBemployee.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBemployee.EMAIL + " TEXT, "
            + DBemployee.NAME + " TEXT, "
            + DBemployee.SURNAME + " TEXT, "
            + DBemployee.DOCTOR + " TEXT, "
            + DBemployee.PASSWORD + " TEXT, "
            + DBemployee.BIRTHDAY + " TEXT " + " );";


    private static final String CREATE_EVENTS_TABLE = "CREATE TABLE " + DBStructure.EVENT_TABLE_NAME
            + " ( " + DBStructure.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBStructure.EVENT + " TEXT, "
            + DBStructure.TIME + " TEXT, "
            + DBStructure.DATE + " TEXT, "
            + DBStructure.MONTH + " TEXT, "
            + DBStructure.YEAR + " TEXT, "
            + DBStructure.CLIENTID + " TEXT NOT NULL " +
           /* + DBStructure.DOCTORID + " TEXT NOT NULL " + */" );";


    private static final String CREATE_CLIENTS_TABLE = "CREATE TABLE " + ClientStructure.CLIENT_TABLE_NAME
            + " ( " + ClientStructure.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ClientStructure.NAME + " TEXT, "
            + ClientStructure.SURNAME + " TEXT, "
            + ClientStructure.PHONE + " TEXT " + " );";

    private static final String DROP_EVENT_TABLE = "DROP TABLE IF EXISTS " + DBStructure.EVENT_TABLE_NAME;
    private static final String DROP_EMPLOYEES_TABLE = "DROP TABLE IF EXISTS " + DBemployee.EMPLOYEE_TABLE_NAME;
    private static final String DROP_CLIENTS_TABLE = "DROP TABLE IF EXISTS " + ClientStructure.CLIENT_TABLE_NAME;


    public DBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        database = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_EMPLOYEES_TABLE);
        db.execSQL(CREATE_CLIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENT_TABLE);
        db.execSQL(DROP_EMPLOYEES_TABLE);
        db.execSQL(DROP_CLIENTS_TABLE);
        onCreate(db);
    }

    public void SaveEvent(String event, String time, String date, String month, String year, long clientId/*, long doctorId*/) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENT, event);
        contentValues.put(DBStructure.TIME, time);
        contentValues.put(DBStructure.DATE, date);
        contentValues.put(DBStructure.MONTH, month);
        contentValues.put(DBStructure.YEAR, year);
        contentValues.put(DBStructure.CLIENTID, clientId);
    //    contentValues.put(DBStructure.DOCTORID, doctorId);
        database.insert(DBStructure.EVENT_TABLE_NAME, null, contentValues);

    }

    public long SaveClient(String name, String surname, String phone) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ClientStructure.NAME, name);
        contentValues.put(ClientStructure.SURNAME, surname);
        contentValues.put(ClientStructure.PHONE, phone);

        long client = database.insert(ClientStructure.CLIENT_TABLE_NAME, null, contentValues);

        return client;
    }


    public long SaveEmployee(String email, String name, String surname, String birthday, String doctor, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBemployee.EMAIL, email);
        contentValues.put(DBemployee.NAME, name);
        contentValues.put(DBemployee.SURNAME, surname);
        contentValues.put(DBemployee.BIRTHDAY, birthday);
        contentValues.put(DBemployee.DOCTOR, doctor);
        contentValues.put(DBemployee.PASSWORD, password);
        long reg = database.insert(DBemployee.EMPLOYEE_TABLE_NAME, null, contentValues);
        return reg;
    }


    public void deleteEvent(String event,String date,String time, String clientid){
        String selection = DBStructure.EVENT+"=? and "+DBStructure.DATE+"=? and "+DBStructure.TIME+"=? and " + DBStructure.CLIENTID +"=?";
        String[] selectionArg = {event,date,time, clientid};

        database.delete(DBStructure.EVENT_TABLE_NAME,selection,selectionArg);
    }


    public void deleteClient(String name, String surname, String phone){
        String selection = ClientStructure.NAME+"=? and "+ClientStructure.SURNAME+"=? and "+ClientStructure.PHONE+"=?";
        String[] selectionArg = {name, surname, phone};
        database.delete(ClientStructure.CLIENT_TABLE_NAME,selection,selectionArg);
    }

    public boolean areCredentialsCorrect(String email, String password) {
        String[] colums = {DBemployee.EMAIL, DBemployee.PASSWORD};
        SQLiteDatabase database = getReadableDatabase();
        String selection = DBemployee.EMAIL + "=? AND " + DBemployee.PASSWORD + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = database.query(DBemployee.EMPLOYEE_TABLE_NAME, colums, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        database.close();

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Cursor ReadEvents(String date) {
        String[] Projections = {DBStructure.EVENT, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR, DBStructure.CLIENTID};
        String Selection = DBStructure.DATE + "=? ";
        String[] SelectionArgs = {date};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null);

    }

    public Cursor ReadEventsperMonth(String month, String year) {
        String[] Projections = {DBStructure.EVENT, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR, DBStructure.CLIENTID};
        String Selection = DBStructure.MONTH + "=? and " + DBStructure.YEAR + "=?";
        String[] SelectionArgs = {month, year};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null);

    }


    public List<Employees> search(String keyword) {
        List<Employees> employees = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from " + DBemployee.EMPLOYEE_TABLE_NAME + " WHERE " + DBemployee.DOCTOR + " LIKE ?", new String[]{"%" + keyword + "%"});
            if (cursor.moveToFirst()) {
                employees = new ArrayList<Employees>();
            }
            do {
                Employees employee = new Employees();
                employee.setID(cursor.getInt(0));
                employee.setNAME(cursor.getString(1));
                employee.setSURNAME(cursor.getString(2));
                employees.add(employee);
            } while (cursor.moveToNext());
        } catch (Exception e) {
            employees = null;
            e.printStackTrace();
        }
        return employees;
    }

    public Cursor readEmployee(){
        String[] Projections = {DBemployee.ID, DBemployee.EMAIL, DBemployee.NAME, DBemployee.SURNAME, DBemployee.BIRTHDAY, DBemployee.DOCTOR, DBemployee.PASSWORD};
        return database.query(DBemployee.EMPLOYEE_TABLE_NAME, Projections, null, null, null, null, null);
    }


    public Cursor readClient() {

        String[] Projections = {ClientStructure.ID, ClientStructure.NAME, ClientStructure.SURNAME, ClientStructure.PHONE};
        return database.query(ClientStructure.CLIENT_TABLE_NAME, Projections, null, null, null, null, null);
    }

    public void SaveClientAndEvent(String event, String time, String date, String month, String year, String name, String surname, String phone/*, String email, String Name, String Surname, String birthday, String doctor ,String password*/) {
        long clientId = SaveClient(name, surname, phone);
    //   long doctorId = SaveEmployee(email, Name, Surname, birthday, doctor, password);


        SaveEvent(event, time, date, month, year, clientId/*, doctorId*/);

    }
}