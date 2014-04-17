package com.robots2.topo;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskTable {

	public static final String TABLE_TASKS = "tasks";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_COMPLETE = "complete";
	public static final String COLUMN_PARENTS = "parents";
	public static final String COLUMN_CHILDREN = "children";
	public static final String COLUMN_COLOR = "color";
	
	private static final String CREATE_TASKS_TABLE = "CREATE TABLE " 
			+ TABLE_TASKS 
			+ "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "			
			+ COLUMN_COMPLETE + " INT NOT NULL, "
			+ COLUMN_PARENTS + " TEXT NOT NULL, "
			+ COLUMN_COLOR + " TEXT NOT NULL "
			+ ")";
	  
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(CREATE_TASKS_TABLE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    Log.w(TaskTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
	    onCreate(database);
	}
}
