package com.robots2.topo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.robots2.topo.contentprovider.TaskContentProvider;

public class MainTaskActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private EditText taskDescriptionEditText;
	private SimpleCursorAdapter mAdapter;
	private SimpleCursorAdapter mSpinnerAdapter;
	
	private Spinner redPrimaryTaskSpinner;
	private Spinner greenPrimaryTaskSpinner;
	private Spinner bluePrimaryTaskSpinner;
	
	private TextView redPrimaryTaskIdTextView;
	private TextView greenPrimaryTaskIdTextView;
	private TextView bluePrimaryTaskIdTextView;
	
	private TextView redPrimaryTaskDescriptionTextView;
	private TextView greenPrimaryTaskDescriptionTextView;
	private TextView bluePrimaryTaskDescriptionTextView;
	
	private String mId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_main);
		
		getLoaderManager().initLoader(0, null, this);
		
		taskDescriptionEditText = (EditText) findViewById(R.id.newTaskEditText);
		taskDescriptionEditText.requestFocus();
		
		taskDescriptionEditText.setImeActionLabel("Add", KeyEvent.KEYCODE_ENTER);
	
		taskDescriptionEditText.setOnKeyListener(new View.OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		        	addTask();
		          return true;
		        }
		        return false;
		    }
		});

		redPrimaryTaskSpinner = (Spinner) findViewById(R.id.red_primary_task_spinner);
		greenPrimaryTaskSpinner = (Spinner) findViewById(R.id.green_primary_task_spinner);
		bluePrimaryTaskSpinner = (Spinner) findViewById(R.id.blue_primary_task_spinner);
		
		redPrimaryTaskIdTextView = (TextView) findViewById(R.id.red_primary_task_selected_id);
		greenPrimaryTaskIdTextView = (TextView) findViewById(R.id.green_primary_task_selected_id);
		bluePrimaryTaskIdTextView = (TextView) findViewById(R.id.blue_primary_task_selected_id);
		
		redPrimaryTaskDescriptionTextView = (TextView) findViewById(R.id.red_primary_task_selected_text);
		greenPrimaryTaskDescriptionTextView = (TextView) findViewById(R.id.green_primary_task_selected_text);
		bluePrimaryTaskDescriptionTextView = (TextView) findViewById(R.id.blue_primary_task_selected_text);
		
		fillData();
		
		getLoaderManager().initLoader(10, null, this);
	}

	private void fillData() {
		String[] mFromColumns = new String[] { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		int[] mToFields = new int[] { R.id.task_id, R.id.task_description };

		ListView mListView = (ListView) findViewById(android.R.id.list);
		mAdapter = new SimpleCursorAdapter(this, R.layout.row_list_task, null, mFromColumns, mToFields, 0);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
		       public void onItemClick(AdapterView<?> parent, View view, int position,
		               long id) {
		    	   
		    	   mId = String.valueOf(id);
		    	   
		    	   new AlertDialog.Builder(MainTaskActivity.this)
			   		.setTitle("Complete task")
			   	    .setMessage("Are you sure you want to mark this task as complete?")
			   	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			   	        public void onClick(DialogInterface dialog, int which) { 
			   	        	String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_ID + " = " + mId + ")";
			   	        	ContentValues values = new ContentValues();
			   	        	values.put(TaskTable.COLUMN_COMPLETE, 1);
			   			
			   	        	getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
			   	        	
			   	        }
			   	     })
			   	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			   	        public void onClick(DialogInterface dialog, int which) { 
			   	            // do nothing
			   	        }
			   	     })
			   	    .show();
		       }
		   });
		
		
		String[] mFromColumnForSpinner = new String[] { TaskTable.COLUMN_DESCRIPTION, TaskTable.COLUMN_ID };
		int[] mToFieldsForSpinner = new int[] { R.id.task_description_spinner, R.id.task_id_spinner };
		mSpinnerAdapter = new SimpleCursorAdapter(this, R.layout.row_spinner_task, null,
				mFromColumnForSpinner, mToFieldsForSpinner, 0);
		
		redPrimaryTaskSpinner.setAdapter(mSpinnerAdapter);
		greenPrimaryTaskSpinner.setAdapter(mSpinnerAdapter);
		bluePrimaryTaskSpinner.setAdapter(mSpinnerAdapter);
		
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		String selectionOfTasksWithNoParents = "(" + TaskTable.COLUMN_PARENTS + "= 0 "
				+ "and " + TaskTable.COLUMN_PRIMARYCOLOR + " = 0 "
				+ "and " + TaskTable.COLUMN_COMPLETE + " = 0 )";
		
		String selectionOfRedTask = "(" + TaskTable.COLUMN_PARENTS + "= 0 "
				+ "and " + TaskTable.COLUMN_PRIMARYCOLOR + " = 10 "
				+ "and " + TaskTable.COLUMN_COMPLETE + " = 0 )";
		
		String selectionOfGreenTask = "(" + TaskTable.COLUMN_PARENTS + "= 0 "
				+ "and " + TaskTable.COLUMN_PRIMARYCOLOR + " = 20 "
				+ "and " + TaskTable.COLUMN_COMPLETE + " = 0 )";
		
		String selectionOfBlueTask = "(" + TaskTable.COLUMN_PARENTS + "= 0 "
				+ "and " + TaskTable.COLUMN_PRIMARYCOLOR + " = 30 "
				+ "and " + TaskTable.COLUMN_COMPLETE + " = 0 )";
		
		CursorLoader cursorLoader = null;
		
		switch (id) {
			case 0:
				cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, 
						selectionOfTasksWithNoParents, null, null);
				break;
			case 10:
				cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, 
						selectionOfRedTask, null, null);
				break;
			case 20:
				cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, 
						selectionOfGreenTask, null, null);
				break;
			case 30:
				cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, 
						selectionOfBlueTask, null, null);
				break;
				
		}
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		switch(loader.getId()) {
			case 0:
				mAdapter.swapCursor(cursor);
				mSpinnerAdapter.swapCursor(cursor);
				break;
			case 10:
				cursor.moveToFirst();
				StringBuilder redIdString = new StringBuilder();
				StringBuilder redDescriptionString = new StringBuilder();
		        while (!cursor.isAfterLast()) {
		        	redIdString.append(cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_ID)));
		        	redDescriptionString.append(cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_DESCRIPTION)));
		            cursor.moveToNext();
		        }
		        redPrimaryTaskIdTextView.setText(redIdString);
		        redPrimaryTaskDescriptionTextView.setText(redDescriptionString);
				break;
			case 20:
				cursor.moveToFirst();
				StringBuilder greenIdString = new StringBuilder();
				StringBuilder greenDescriptionString = new StringBuilder();
		        while (!cursor.isAfterLast()) {
		        	greenIdString.append(cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_ID)));
		        	greenDescriptionString.append(cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_DESCRIPTION)));
		            cursor.moveToNext();
		        }
		        greenPrimaryTaskIdTextView.setText(greenIdString);
		        greenPrimaryTaskDescriptionTextView.setText(greenDescriptionString);
				break;
			case 30:
				cursor.moveToFirst();
				StringBuilder blueIdString = new StringBuilder();
				StringBuilder blueDescriptionString = new StringBuilder();
		        while (!cursor.isAfterLast()) {
		        	blueIdString.append(cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_ID)));
		        	blueDescriptionString.append(cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_DESCRIPTION)));
		            cursor.moveToNext();
		        }
		        bluePrimaryTaskIdTextView.setText(blueIdString);
		        bluePrimaryTaskDescriptionTextView.setText(blueDescriptionString);
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
		mSpinnerAdapter.swapCursor(null);
	}
	
	public void onAddTaskClick(View view) {
		addTask();
	}
	
	private void addTask() {
		String taskDescription = taskDescriptionEditText.getText().toString();
		if (taskDescription.length() == 0) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(TaskTable.COLUMN_DESCRIPTION, taskDescription);
		values.put(TaskTable.COLUMN_COLOR, "#000000");
		values.put(TaskTable.COLUMN_COMPLETE, 0);
		
		
		values.put(TaskTable.COLUMN_PARENTS, "0");
		values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
		
		getContentResolver().insert(TaskContentProvider.CONTENT_URI, values);
		
		taskDescriptionEditText.setText("");
		taskDescriptionEditText.setClickable(true);
		taskDescriptionEditText.setFocusable(true);
		taskDescriptionEditText.requestFocus();
	}
	
	public void onRedPrimaryClick (View v) {
		
		new AlertDialog.Builder(MainTaskActivity.this)
   		.setTitle("Complete task")
   	    .setMessage("Are you sure you want to mark this task as complete?")
   	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
   	        public void onClick(DialogInterface dialog, int which) { 
   	        	String selectionOfPrimaryTaskId = "(" + TaskTable.COLUMN_PRIMARYCOLOR + " = 10 )";
	   	 		ContentValues values = new ContentValues();
	   	 		values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
	   	 		values.put(TaskTable.COLUMN_COMPLETE, 1);
	   	 		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfPrimaryTaskId, null);
   	        }
   	     })
   	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
   	        public void onClick(DialogInterface dialog, int which) { 
   	            // do nothing
   	        }
   	     })
   	    .show();
	}
	
	public void onGreenPrimaryClick (View v) {
		
		new AlertDialog.Builder(MainTaskActivity.this)
   		.setTitle("Complete task")
   	    .setMessage("Are you sure you want to mark this task as complete?")
   	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
   	        public void onClick(DialogInterface dialog, int which) { 
   	        	String selectionOfPrimaryTaskId = "(" + TaskTable.COLUMN_PRIMARYCOLOR + " = 20 )";
	   	 		ContentValues values = new ContentValues();
	   	 		values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
	   	 		values.put(TaskTable.COLUMN_COMPLETE, 1);
	   	 		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfPrimaryTaskId, null);
   	        }
   	     })
   	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
   	        public void onClick(DialogInterface dialog, int which) { 
   	            // do nothing
   	        }
   	     })
   	    .show();
	}
	
	public void onBluePrimaryClick (View v) {
		
		new AlertDialog.Builder(MainTaskActivity.this)
   		.setTitle("Complete task")
   	    .setMessage("Are you sure you want to mark this task as complete?")
   	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
   	        public void onClick(DialogInterface dialog, int which) { 
   	        	String selectionOfPrimaryTaskId = "(" + TaskTable.COLUMN_PRIMARYCOLOR + " = 30 )";
	   	 		ContentValues values = new ContentValues();
	   	 		values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
	   	 		values.put(TaskTable.COLUMN_COMPLETE, 1);
	   	 		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfPrimaryTaskId, null);
   	        }
   	     })
   	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
   	        public void onClick(DialogInterface dialog, int which) { 
   	            // do nothing
   	        }
   	     })
   	    .show();
	}


	public void makeTaskRedPrimary(View v) {
		TextView primaryTextView = (TextView) findViewById(R.id.red_primary_task_selected_text);
		TextView primaryIdTextView = (TextView) findViewById(R.id.red_primary_task_selected_id);
		TextView idTextView = (TextView) redPrimaryTaskSpinner.findViewById(R.id.task_id_spinner);
		TextView descriptionTextView = (TextView) redPrimaryTaskSpinner.findViewById(R.id.task_description_spinner);
		
		if (idTextView != null) {
			if (!primaryIdTextView.getText().equals("")) {
				String selectionOfPrimaryTaskId = "(" + TaskTable.COLUMN_ID + " = " + primaryIdTextView.getText() + ")";
				ContentValues values = new ContentValues();
				values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
				getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfPrimaryTaskId, null);
			}
			
			String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_ID + " = " + idTextView.getText() + ")";
			
			ContentValues values = new ContentValues();
			values.put(TaskTable.COLUMN_PRIMARYCOLOR, 10);
			
			getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
			
			primaryIdTextView.setText(idTextView.getText().toString());
			primaryTextView.setText(descriptionTextView.getText().toString());
		}
	}
	
	public void makeTaskGreenPrimary(View v) {
		TextView primaryTextView = (TextView) findViewById(R.id.green_primary_task_selected_text);
		TextView primaryIdTextView = (TextView) findViewById(R.id.green_primary_task_selected_id);
		TextView idTextView = (TextView) greenPrimaryTaskSpinner.findViewById(R.id.task_id_spinner);
		TextView descriptionTextView = (TextView) greenPrimaryTaskSpinner.findViewById(R.id.task_description_spinner);
		
		if (idTextView != null) {
			if (!primaryIdTextView.getText().equals("")) {
				String selectionOfPrimaryTaskId = "(" + TaskTable.COLUMN_ID + " = " + primaryIdTextView.getText() + ")";
				ContentValues values = new ContentValues();
				values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
				getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfPrimaryTaskId, null);
			}
			
			String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_ID + " = " + idTextView.getText() + ")";
			ContentValues values = new ContentValues();
			
			values.put(TaskTable.COLUMN_PRIMARYCOLOR, 20);
			getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
			
			primaryIdTextView.setText(idTextView.getText().toString());
			primaryTextView.setText(descriptionTextView.getText().toString());
		}
	}

	public void makeTaskBluePrimary(View v) {
		TextView primaryTextView = (TextView) findViewById(R.id.blue_primary_task_selected_text);
		TextView primaryIdTextView = (TextView) findViewById(R.id.blue_primary_task_selected_id);
		TextView idTextView = (TextView) bluePrimaryTaskSpinner.findViewById(R.id.task_id_spinner);
		TextView descriptionTextView = (TextView) bluePrimaryTaskSpinner.findViewById(R.id.task_description_spinner);
		
		if (idTextView != null) {
			if (!primaryIdTextView.getText().equals("")) {
				String selectionOfPrimaryTaskId = "(" + TaskTable.COLUMN_ID + " = " + primaryIdTextView.getText() + ")";
				ContentValues values = new ContentValues();
				values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
				getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfPrimaryTaskId, null);
			}
			
			String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_ID + " = " + idTextView.getText() + ")";
			ContentValues values = new ContentValues();
			
			values.put(TaskTable.COLUMN_PRIMARYCOLOR, 30);
			getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
			
			primaryIdTextView.setText(idTextView.getText().toString());
			primaryTextView.setText(descriptionTextView.getText().toString());
		}
	}
	
	
	public void clearRed(View v) {
		TextView primaryTextView = (TextView) findViewById(R.id.red_primary_task_selected_text);
		TextView primaryIdTextView = (TextView) findViewById(R.id.red_primary_task_selected_id);
		
		String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_PRIMARYCOLOR + " = 10)";
		ContentValues values = new ContentValues();
		
		values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
		
		primaryIdTextView.setText("");
		primaryTextView.setText("");
		primaryTextView.setHint(R.string.empty);
	}
	
	public void clearGreen(View v) {
		TextView primaryTextView = (TextView) findViewById(R.id.green_primary_task_selected_text);
		TextView primaryIdTextView = (TextView) findViewById(R.id.green_primary_task_selected_id);
		
		String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_PRIMARYCOLOR + " = 20)";
		ContentValues values = new ContentValues();
		
		values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
		
		primaryIdTextView.setText("");
		primaryTextView.setText("");
		primaryTextView.setHint(R.string.empty);
	}

	public void clearBlue(View v) {
		TextView primaryTextView = (TextView) findViewById(R.id.blue_primary_task_selected_text);
		TextView primaryIdTextView = (TextView) findViewById(R.id.blue_primary_task_selected_id);
		
		String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_PRIMARYCOLOR + " = 30)";
		ContentValues values = new ContentValues();
		
		values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
		
		primaryIdTextView.setText("");
		primaryTextView.setText("");
		primaryTextView.setHint(R.string.empty);
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
			case (R.id.action_completed_tasks):
				Intent showCompletedTasksIntent = new Intent(this, CompletedTasksActivity.class);
				showCompletedTasksIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(showCompletedTasksIntent);
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
