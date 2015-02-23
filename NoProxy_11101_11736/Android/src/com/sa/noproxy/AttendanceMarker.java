package com.sa.noproxy;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class AttendanceMarker
{
static String DATABASE_NAME = "check.db";
static final int DATABASE_VERSION = 1;
public static final int NAME_COLUMN = 1;
String course;
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
String currentDate = sdf.format(new Date());
// TODO: Create public field for each column in your table.
// SQL Statement to create a new database.
static final String REG_TABLE = "create table "+"REGISTER"+
"( " +"ID"+" integer primary key"+ "); ";
// Variable to hold the database instanceSTER
static final String ATTENDANCE_TABLE = "create table "+"ATTENDANCE"+
		"( " +"ROLL"+" integer primary key,"+ "PRESENT"+" text); ";
public  SQLiteDatabase db;
// Context of the application using the database.
private final Context context;
// Database open/upgrade helper
private DataBase dbHelper;
public  AttendanceMarker(Context _context,String _course)
{
course = _course;
context = _context;
DATABASE_NAME = course+".db";
dbHelper = new DataBase(context, DATABASE_NAME, null, DATABASE_VERSION);
}
public  AttendanceMarker open() throws SQLException
{
db = dbHelper.getWritableDatabase();
return this;
}
public void close()
{
db.close();
}

        public  SQLiteDatabase getDatabaseInstance()
{
return db;
}

        public void insertEntry(String table,String Roll)
{
ContentValues newValues = new ContentValues();
// Assign values for each row.
newValues.put("ROLL", Roll);

// Insert the row into your table
db.insert(table, null, newValues);
Toast.makeText(context, "Succesfully registered "+Roll+ " ", Toast.LENGTH_LONG).show();
}
public int deleteEntry(String UserName)
{
//String id=String.valueOf(ID);
String where="USERNAME=?";
int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
// Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
return numberOFEntriesDeleted;
}
public void emptyTable(String table)
{
//String id=String.valueOf(ID);
	db.execSQL("delete from "+table);

}
public String getSinlgeEntry(String table,String Roll)
{
Cursor cursor=db.query(table, null, " ROLL=?", new String[]{Roll}, null, null, null);
if(cursor.getCount()<1) // UserName Not Exist
{
cursor.close();
return "NOT EXIST";
}
return "EXIST";
}
public void  updateEntry(String RollNum,String mark)
{
// Define the updated row content.
ContentValues updatedValues = new ContentValues();
// Assign values for each row.
updatedValues.put("ROLL", RollNum);
updatedValues.put("PRESENT",mark);

String where="ROLL = ?";
db.update("ATTENDANCE",updatedValues, where, new String[]{RollNum});
//Toast.makeText(context, "update success", Toast.LENGTH_LONG).show();
}
public void markAttendance(String rollNum, String mark) {
	// TODO Auto-generated method stub
	ContentValues newValues = new ContentValues();
	// Assign values for each row.
	newValues.put("ROLL", rollNum);
	newValues.put("PRESENT", mark);

	// Insert the row into your table
	db.insert("ATTENDANCE", null, newValues);
	//Toast.makeText(context, "Succesfully registered "+rollNum+ " ", Toast.LENGTH_LONG).show();
}
public String checkAttendance(String rollNum){
	/*String sqlQuery = "SELECT * FROM ATTENDANCE WHERE ROLL = "+rollNum+";";
	Cursor cursor = db.rawQuery(sqlQuery, null);
	return cursor.getString(cursor.getColumnIndex("PRESENT"));*/
	String temp = null;
	String selectQuery = "SELECT PRESENT FROM ATTENDANCE WHERE ROLL=?";
	Cursor c = db.rawQuery(selectQuery, new String[] { rollNum });
	if (c.moveToFirst()) {
	    temp = c.getString(c.getColumnIndex("PRESENT"));
	}
	c.close();
	return temp;
}
private static String encryptPassword(String password)
{
    String sha1 = "";
    try
    {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes("UTF-8"));
        sha1 = byteToHex(crypt.digest());
    }
    catch(NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
    catch(UnsupportedEncodingException e)
    {
        e.printStackTrace();
    }
    return sha1;
}

private static String byteToHex(final byte[] hash)
{
    Formatter formatter = new Formatter();
    for (byte b : hash)
    {
        formatter.format("%02x", b);
    }
    String result = formatter.toString();
    formatter.close();
    return result;
}
public String composeJSONfromSQLite(String course) throws JSONException{
	
    String selectQuery = "SELECT  * FROM ATTENDANCE";
    Cursor cursor = db.rawQuery(selectQuery, null);
    String code = encryptPassword("noproxy");
    JSONArray array = new JSONArray();
    if (cursor.moveToFirst()) {
        do {
        	JSONObject json = new JSONObject();
        	json.put("code", code);
        	json.put("course", course);
        	json.put("roll", cursor.getString(0));
            json.put("present", cursor.getString(1));
            json.put("date",currentDate);
            array.put(json);
           
        } while (cursor.moveToNext());
    }
    Log.d("early",array.toString());
    return array.toString();
   
}
}

