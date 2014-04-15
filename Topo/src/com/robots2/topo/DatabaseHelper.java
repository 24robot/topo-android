package com.robots2.topo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "taskManager";
	public static final String TABLE_TASKS = "tasks";
	
	public static final String KEY_ID = "_id";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_COLOR = "color";
	public static final String KEY_COMPLETE = "complete";
	public static final String KEY_PARENTS = "parents";
	
	private static final String KEY_TIME = "time";
	private static final String KEY_DATE = "date";
	
	public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
	            + KEY_ID + " integer primary key autoincrement, "
				+ KEY_DESCRIPTION + " TEXT, "
				+ KEY_COLOR + " TEXT, "
				+ KEY_COMPLETE + " INT, "
				+ KEY_PARENTS + " TEXT, "
	            + KEY_TIME + " TEXT, " + KEY_DATE + " TEXT " + ")";
		
	    db.execSQL(CREATE_EVENTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
	
	    onCreate(db);
	}
	
	
	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public void addTask(Task task) {
	    SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, task.get_Id());
//        values.put(KEY_TITLE, task.get_title());
//        values.put(KEY_TIME, task.get_time());
//        values.put(KEY_DATE, task.get_date());

        db.insert(TABLE_TASKS, null, values);
        db.close();
	}

    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
	
        Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID,
                				KEY_DESCRIPTION, KEY_TIME, KEY_DATE }, KEY_ID + "=?",
                				new String[] { String.valueOf(id) }, null, null, null, null);
 
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Task task = new Task();
//	    Task task = new Task(Integer.parseInt(cursor.getString(0)),
//	               			cursor.getString(1), cursor.getString(2), null, null);
	
        return task;
    }


    public List<Task> getAllTasks() {
       List<Task> taskList = new ArrayList<Task>();
       String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       if (cursor.moveToFirst()) {
           do {
               Task task = new Task();
//               task.set_Id(Integer.parseInt(cursor.getString(0)));
//               task.set_title(cursor.getString(1));
//               task.set_time(cursor.getString(2));
//               task.set_date(cursor.getString(3));

               taskList.add(task);
           } while (cursor.moveToNext());
       }

       return taskList;
    }

	   
    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateEvent(Task task) {
//        SQLiteDatabase db = this.getWritableDatabase();

//        ContentValues values = new ContentValues();
//        values.put(KEY_TITLE, event.get_title());
//        values.put(KEY_TIME, event.get_time());
//        values.put(KEY_DATE, event.get_date());

        return 0;
//        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(event.get_Id()) });
    }

    public void deleteEvent(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_TASKS, KEY_ID + " = ?",
//                new String[] { String.valueOf(event.get_Id()) });
        db.close();
    }
}
