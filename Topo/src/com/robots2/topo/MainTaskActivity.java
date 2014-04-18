package com.robots2.topo;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.robots2.topo.contentprovider.TaskContentProvider;

public class MainTaskActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int ACTIVITY_ADD = 0;
	private static final int ACTITIVITY_UPDATE = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
	private SimpleCursorAdapter adapter;
	private Spinner redPrimaryTaskSpinner;
	private Spinner greenPrimaryTaskSpinner;
	private Spinner bluePrimaryTaskSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_main);
		
		setListAdapter(adapter);
		
		redPrimaryTaskSpinner = (Spinner) findViewById(R.id.red_primary_task_spinner);
		greenPrimaryTaskSpinner = (Spinner) findViewById(R.id.green_primary_task_spinner);
		bluePrimaryTaskSpinner = (Spinner) findViewById(R.id.blue_primary_task_spinner);
		
		fillData();
		updateSpinners();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
			case (R.id.action_add_task):
				Intent addNewTaskIntent = new Intent(this, NewTaskActivity.class);
				addNewTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(addNewTaskIntent);
				break;
				
			case (R.id.action_completed_tasks):
				Intent showCompletedTasksIntent = new Intent(this, CompletedTasksActivity.class);
				showCompletedTasksIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(showCompletedTasksIntent);
				break;
			
			case (R.id.action_settings):
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		
		Uri taskUri = Uri.parse(TaskContentProvider.CONTENT_URI + "/" + id);
		
		Intent updateTaskIntent = new Intent(this, UpdateTaskActivity.class);
		updateTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		updateTaskIntent.putExtra(TaskContentProvider.CONTENT_ITEM_TYPE, taskUri);
		
		startActivity(updateTaskIntent);
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		CursorLoader cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, null, null, null);
		
		return cursorLoader;
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}
	
	private void fillData() {
		String[] from = new String[] { TaskTable.COLUMN_DESCRIPTION };
		int[] to = new int[] { R.id.task_description };
		
		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.row_list_task, null, from, to, 0);
		
		setListAdapter(adapter);
	}

	private void updateSpinners() {
		Uri uri = TaskContentProvider.CONTENT_URI;
		String[] projection = { TaskTable.COLUMN_DESCRIPTION };
		String selection = TaskTable.COLUMN_ISPRIMARYCOLOR + " = " + "0";
		
		Cursor cursor = getContentResolver().query(uri, projection, selection, null, null);
		
		ArrayList<String> primaryTasksList = new ArrayList<String>();
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
		     primaryTasksList.add(cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_DESCRIPTION)));
		     cursor.moveToNext();
		}
		
		redPrimaryTaskSpinner.setAdapter(adapter);
		greenPrimaryTaskSpinner.setAdapter(adapter);
		bluePrimaryTaskSpinner.setAdapter(adapter);
	}

}
