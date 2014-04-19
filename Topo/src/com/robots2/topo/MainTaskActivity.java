package com.robots2.topo;

import java.util.ArrayList;
import java.util.List;

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

	private SimpleCursorAdapter adapter;
	private Spinner redPrimaryTaskSpinner;
	private Spinner greenPrimaryTaskSpinner;
	private Spinner bluePrimaryTaskSpinner;
	private List<String> remainingPossiblePrimaryTasks;
	
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
		String selectionOfTasksWithNoParents = TaskTable.COLUMN_PARENTS + "=" + "0";
		CursorLoader cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, null, null, null);
		
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
		switch (loader.getId()) {
			case 0:
				break;
			case 1:
				break;
			default:
				break;
		}
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
		primaryTasksList.add("None");
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
		     primaryTasksList.add(cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_DESCRIPTION)));
		     cursor.moveToNext();
		}
		cursor.close();
		
		ArrayAdapter<String> primaryTasksAdapter = new ArrayAdapter<String>(getApplicationContext(), 
				R.layout.row_spinner_task, primaryTasksList);
		
		redPrimaryTaskSpinner.setAdapter(primaryTasksAdapter);
		greenPrimaryTaskSpinner.setAdapter(primaryTasksAdapter);
		bluePrimaryTaskSpinner.setAdapter(primaryTasksAdapter);
		
		AdapterView.OnItemSelectedListener redPrimaryTaskSpinnerListener = new AdapterView.OnItemSelectedListener () {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				setRedPrimaryTextView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
	    };
	    
		AdapterView.OnItemSelectedListener greenPrimaryTaskSpinnerListener = new AdapterView.OnItemSelectedListener () {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				setGreenPrimaryTextView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
	    };
	    
	    AdapterView.OnItemSelectedListener bluePrimaryTaskSpinnerListener = new AdapterView.OnItemSelectedListener () {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				setBluePrimaryTextView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
	    };
	    
	    redPrimaryTaskSpinner.setOnItemSelectedListener(redPrimaryTaskSpinnerListener);
	    greenPrimaryTaskSpinner.setOnItemSelectedListener(greenPrimaryTaskSpinnerListener);
	    bluePrimaryTaskSpinner.setOnItemSelectedListener(bluePrimaryTaskSpinnerListener);
	}

	private void setRedPrimaryTextView() {
		String text = redPrimaryTaskSpinner.getSelectedItem().toString();
		
		TextView primaryTextView = (TextView) findViewById(R.id.red_primary_task_selected_text);
		
		primaryTextView.setText(text);
	}
	
	private void setGreenPrimaryTextView() {
		String text = greenPrimaryTaskSpinner.getSelectedItem().toString();
		
		TextView primaryTextView = (TextView) findViewById(R.id.green_primary_task_selected_text);

		primaryTextView.setText(text);

	}
	
	private void setBluePrimaryTextView() {
		String text = bluePrimaryTaskSpinner.getSelectedItem().toString();
		
		TextView primaryTextView = (TextView) findViewById(R.id.blue_primary_task_selected_text);
		
		primaryTextView.setText(text);
	}
}
