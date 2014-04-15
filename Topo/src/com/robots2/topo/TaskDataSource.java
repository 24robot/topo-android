package com.robots2.topo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskDataSource {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	private String[] allColumns = { DatabaseHelper.KEY_ID, DatabaseHelper.KEY_DESCRIPTION, 
			DatabaseHelper.KEY_COLOR, DatabaseHelper.KEY_COMPLETE, DatabaseHelper.KEY_PARENTS };
	
	public TaskDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Task addTask(String taskDescription) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_DESCRIPTION, taskDescription);
		
		long insertId = database.insert(DatabaseHelper.TABLE_TASKS, null, values);
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_TASKS, allColumns, DatabaseHelper.KEY_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		
		Task newTask = cursorToTask(cursor);
		cursor.close();
		return newTask;
	}
	
	public void deleteTask(Task task) {
		long id = task.getId();
		database.delete(DatabaseHelper.TABLE_TASKS, DatabaseHelper.KEY_ID + " = " + id, null);
	}
	
	public List<Task> getAllTasks() {
		List<Task> tasks = new ArrayList<Task>();
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_TASKS, allColumns, null, null, null, null, null);		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			Task task = cursorToTask(cursor);
			tasks.add(task);
			cursor.moveToNext();
		}
		cursor.close();
		return tasks;
	}

	private Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		task.setId(cursor.getLong(0));
		task.setDescription(cursor.getString(1));
		return task;
	}
}
