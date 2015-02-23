package com.sa.noproxy;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper{
	public DataBase(Context context, String name,CursorFactory factory, int version)
	{
	super(context, name, factory, version);
	}
	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	String ATTENDANCE_CREATE = "create table "+"ATTENDANCE"+
			"( " +"ROLL"+" integer primary key,"+ "PRESENT"+" integer); ";
	String REG_TABLE = "create table "+"REGISTER"+
			"( " +"ROLL"+" integer primary key"+ "); ";
	@Override
	public void onCreate(SQLiteDatabase _db)
	{
		
	_db.execSQL(ATTENDANCE_CREATE);
	_db.execSQL(REG_TABLE);
	Log.d("debug", "created the table");

	}
	// Called when there is a database version mismatch meaning that the version
	// of the database on disk needs to be upgraded to the current version.
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
	{
	// Log the version upgrade.
	Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");
	Log.d("db","upgraded");
	// Upgrade the existing database to conform to the new version. Multiple
	// previous versions can be handled by comparing _oldVersion and _newVersion
	// values.
	// The simplest case is to drop the old table and create a new one.
	_db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
	// Create a new one.
	onCreate(_db);
	}
	}
