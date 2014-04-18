package com.robots2.topo.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.robots2.topo.TaskDatabaseHelper;
import com.robots2.topo.TaskTable;

public class TaskContentProvider extends ContentProvider {

	private TaskDatabaseHelper database;
	
	private static final int TASKS = 10;
	private static final int TASK_ID = 20;
	
	private static final String AUTHORITY = "com.robots2.topo.contentprovider.TaskContentProvider";	
	private static final String BASE_PATH = "tasks";	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/tasks";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/task";
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		sUriMatcher.addURI(AUTHORITY, BASE_PATH, TASKS);
		sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TASK_ID);
	}
	
	@Override
	public boolean onCreate() {
		database = new TaskDatabaseHelper(getContext());
		return false;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sUriMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		
		switch(uriType) {
			case TASKS:
				rowsDeleted = sqlDB.delete(TaskTable.TABLE_TASKS, selection, selectionArgs);
				break;
				
			case TASK_ID:
				String id = uri.getLastPathSegment();
				
				if(TextUtils.isEmpty(selection)) {
					rowsDeleted = sqlDB.delete(TaskTable.TABLE_TASKS, TaskTable.COLUMN_ID + "=" + id, null);
				}
				else {
					rowsDeleted = sqlDB.delete(TaskTable.TABLE_TASKS, TaskTable.COLUMN_ID + "=" + id + " and " + selection,
							selectionArgs);
				}
				break;
				
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return rowsDeleted;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sUriMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		
		switch(uriType) {
			case TASKS:
				id = sqlDB.insert(TaskTable.TABLE_TASKS, null, values);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		checkColumns(projection);
		queryBuilder.setTables(TaskTable.TABLE_TASKS);
		
		int uriType = sUriMatcher.match(uri);
		
		switch(uriType) {
			case TASKS:
				break;
			case TASK_ID:
				queryBuilder.appendWhere(TaskTable.COLUMN_ID + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = sUriMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		
		switch(uriType) {
			case TASKS:
				rowsUpdated = sqlDB.update(TaskTable.TABLE_TASKS, values, selection, selectionArgs);
				break;
				
			case TASK_ID:
				String id = uri.getLastPathSegment();
				
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = sqlDB.update(TaskTable.TABLE_TASKS, values, TaskTable.COLUMN_ID + " = " + id, null);
				}
				else {
					rowsUpdated = sqlDB.update(TaskTable.TABLE_TASKS, values, 
							TaskTable.COLUMN_ID + " = " + id + " and " + selection, selectionArgs);
				}
				break;
				
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return 0;
	}
	
	private void checkColumns(String[] projection) {
		String[] available = { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION, TaskTable.COLUMN_COLOR,  
				TaskTable.COLUMN_COMPLETE, TaskTable.COLUMN_PARENTS, TaskTable.COLUMN_CHILDREN };
		
		if (projection != null) {
			HashSet<String> requestColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			
			if (!availableColumns.containsAll(requestColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}
