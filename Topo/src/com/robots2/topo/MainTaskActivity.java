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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.robots2.topo.contentprovider.TaskContentProvider;

public class MainTaskActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mAdapter;
	private Spinner redPrimaryTaskSpinner;
	private Spinner greenPrimaryTaskSpinner;
	private Spinner bluePrimaryTaskSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_main);
		
		getLoaderManager().initLoader(0, null, this);

		redPrimaryTaskSpinner = (Spinner) findViewById(R.id.red_primary_task_spinner);
		greenPrimaryTaskSpinner = (Spinner) findViewById(R.id.green_primary_task_spinner);
		bluePrimaryTaskSpinner = (Spinner) findViewById(R.id.blue_primary_task_spinner);
		
		fillData();
	}

	private void fillData() {
		String[] mFromColumns = new String[] { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		int[] mToFields = new int[] { R.id.task_id, R.id.task_description };

		ListView mListView = (ListView) findViewById(android.R.id.list);
		mAdapter = new SimpleCursorAdapter(this, R.layout.row_list_task, null, mFromColumns, mToFields, 0);

		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		String selectionOfTasksWithNoParents = "(" + TaskTable.COLUMN_PARENTS + "=" + "0)";
		CursorLoader cursorLoader = null;
		
		switch (id) {
			case 0:	
				cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, null, null, null);
				break;
				
			case 1:
				cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, 
						selectionOfTasksWithNoParents, null, null);
				break;
				
		}
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
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

	private void updateSpinners(Cursor cursor) {
		ArrayList<String> primaryTasksList = new ArrayList<String>();
		primaryTasksList.add("None");
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
		     primaryTasksList.add(cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_DESCRIPTION)));
		     cursor.moveToNext();
		}
		
		ArrayAdapter<String> primaryTasksAdapter = new ArrayAdapter<String>(getApplicationContext(), 
				R.layout.row_spinner_task, primaryTasksList);
		
		redPrimaryTaskSpinner.setAdapter(primaryTasksAdapter);
		greenPrimaryTaskSpinner.setAdapter(primaryTasksAdapter);
		bluePrimaryTaskSpinner.setAdapter(primaryTasksAdapter);
		
		AdapterView.OnItemSelectedListener redPrimaryTaskSpinnerListener = new AdapterView.OnItemSelectedListener () {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
				setRedPrimaryTextView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
	    };
	    
		AdapterView.OnItemSelectedListener greenPrimaryTaskSpinnerListener = new AdapterView.OnItemSelectedListener () {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
				setGreenPrimaryTextView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	
			}
	    };
	    
	    AdapterView.OnItemSelectedListener bluePrimaryTaskSpinnerListener = new AdapterView.OnItemSelectedListener () {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
				setBluePrimaryTextView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	
			}
	    };
	    
	    redPrimaryTaskSpinner.setOnItemSelectedListener(redPrimaryTaskSpinnerListener);
	    greenPrimaryTaskSpinner.setOnItemSelectedListener(greenPrimaryTaskSpinnerListener);
	    bluePrimaryTaskSpinner.setOnItemSelectedListener(bluePrimaryTaskSpinnerListener);
	}

	private void setRedPrimaryTextView() {
		TextView primaryTextView = (TextView) findViewById(R.id.red_primary_task_selected_text);
		primaryTextView.setText(redPrimaryTaskSpinner.getSelectedItem().toString());
	}
	
	private void setGreenPrimaryTextView() {
		TextView primaryTextView = (TextView) findViewById(R.id.green_primary_task_selected_text);
		primaryTextView.setText(greenPrimaryTaskSpinner.getSelectedItem().toString());
	}
	
	private void setBluePrimaryTextView() {
		TextView primaryTextView = (TextView) findViewById(R.id.blue_primary_task_selected_text);
		primaryTextView.setText(bluePrimaryTaskSpinner.getSelectedItem().toString());
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
}
