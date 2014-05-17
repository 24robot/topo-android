package com.robots2.topo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.robots2.topo.contentprovider.TaskContentProvider;

public class MainTaskActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private EditText taskDescriptionEditText;
	private SimpleCursorAdapter mAdapter;
	private SimpleCursorAdapter mSpinnerAdapter;
	
	private TextView redPrimaryTaskIdTextView;
	private TextView greenPrimaryTaskIdTextView;
	private TextView bluePrimaryTaskIdTextView;
	
	private TextView redPrimaryTaskDescriptionTextView;
	private TextView greenPrimaryTaskDescriptionTextView;
	private TextView bluePrimaryTaskDescriptionTextView;
	
	private Button redChooseButton;
	private Button greenChooseButton;
	private Button blueChooseButton;
	
	private String red_color_id;
	private String blue_color_id;
	private String green_color_id;
	
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
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		        	addTask();
		          return true;
		        }
		        return false;
		    }
		});
		
		redPrimaryTaskIdTextView = (TextView) findViewById(R.id.red_primary_task_selected_id);
		greenPrimaryTaskIdTextView = (TextView) findViewById(R.id.green_primary_task_selected_id);
		bluePrimaryTaskIdTextView = (TextView) findViewById(R.id.blue_primary_task_selected_id);
		
		redChooseButton = (Button) findViewById(R.id.red_choose_button);
		greenChooseButton = (Button) findViewById(R.id.green_choose_button);
		blueChooseButton = (Button) findViewById(R.id.blue_choose_button);
		
		redPrimaryTaskDescriptionTextView = (TextView) findViewById(R.id.red_primary_task_selected_text);
		greenPrimaryTaskDescriptionTextView = (TextView) findViewById(R.id.green_primary_task_selected_text);
		bluePrimaryTaskDescriptionTextView = (TextView) findViewById(R.id.blue_primary_task_selected_text);
		
		redPrimaryTaskDescriptionTextView.addTextChangedListener(new TextWatcher() {

			   public void afterTextChanged(Editable s) {}

			   public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			   public void onTextChanged(CharSequence s, int start, int before, int count) {
			      if(s.length() != 0) {
			    	  redChooseButton.setText(getResources().getString(R.string.swap_button_text));
			      }
			      else {
			    	  redChooseButton.setText(getResources().getString(R.string.add_button_text));
			      }
			   }
		  });
		
		greenPrimaryTaskDescriptionTextView.addTextChangedListener(new TextWatcher() {

			   public void afterTextChanged(Editable s) {}

			   public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			   public void onTextChanged(CharSequence s, int start, int before, int count) {
			      if(s.length() != 0) {
			    	  greenChooseButton.setText(getResources().getString(R.string.swap_button_text));
			      }
			      else {
			    	  greenChooseButton.setText(getResources().getString(R.string.add_button_text));
			      }
			   }
		  });
		
		bluePrimaryTaskDescriptionTextView.addTextChangedListener(new TextWatcher() {

			   public void afterTextChanged(Editable s) {}

			   public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			   public void onTextChanged(CharSequence s, int start, int before, int count) {
			      if(s.length() != 0) {
			    	  blueChooseButton.setText(getResources().getString(R.string.swap_button_text));
			      }
			      else {
			    	  blueChooseButton.setText(getResources().getString(R.string.add_button_text));
			      }
			   }
		  });
		
		red_color_id = "10";
		green_color_id = "20";
		blue_color_id = "30";
		
		fillData();
		
		getLoaderManager().initLoader(Integer.parseInt(red_color_id), null, this);
		getLoaderManager().initLoader(Integer.parseInt(green_color_id), null, this);
		getLoaderManager().initLoader(Integer.parseInt(blue_color_id), null, this);
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
	
	public void completePrimaryTask (View view) {
		String color_id = red_color_id;
		
		if (view.getId() == R.id.green_complete_button) {
			color_id = green_color_id;
		}
		else if (view.getId() == R.id.blue_complete_button) {
			color_id = blue_color_id;
		}
		
		final String final_color_id = color_id;
		
		new AlertDialog.Builder(MainTaskActivity.this)
   		.setTitle("Complete task")
   	    .setMessage("Are you sure you want to mark this task as complete?")
   	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
   	        public void onClick(DialogInterface dialog, int which) { 
   	        	String selectionOfPrimaryTaskId = "(" + TaskTable.COLUMN_PRIMARYCOLOR + " = " + final_color_id + ")";
	   	 		ContentValues values = new ContentValues();
	   	 		values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
	   	 		values.put(TaskTable.COLUMN_COMPLETE, 1);
	   	 		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfPrimaryTaskId, null);
   	        }
   	     })
   	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
   	        public void onClick(DialogInterface dialog, int which) { 
   	        }
   	     })
   	    .show();
	}
	
	public void choosePrimaryTask(View view) {
		final View v = view;
		
		AlertDialog.Builder builder = new Builder(this);
	    builder.setTitle("Set Primary Task");
	    builder.setAdapter(mAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
            	Cursor cursor = (Cursor)mAdapter.getItem(position);
            	String idText = cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_ID));
            	String descriptionText = cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_DESCRIPTION));
            	setPrimaryTask(v, idText, descriptionText);
            }
        });

	    builder.show();
	    
	}
	
	private void setPrimaryTask(View view, String idText, String descriptionText) {
		TextView primaryTextView = (TextView) findViewById(R.id.red_primary_task_selected_text);
		TextView primaryIdTextView = (TextView) findViewById(R.id.red_primary_task_selected_id);
		String color_id = red_color_id;

		if (view.getId() == R.id.green_choose_button) {
			primaryTextView = (TextView) findViewById(R.id.green_primary_task_selected_text);
			primaryIdTextView = (TextView) findViewById(R.id.green_primary_task_selected_id);

			color_id = green_color_id;
		}
		else if (view.getId() == R.id.blue_choose_button) {
			primaryTextView = (TextView) findViewById(R.id.blue_primary_task_selected_text);
			primaryIdTextView = (TextView) findViewById(R.id.blue_primary_task_selected_id);
			color_id = blue_color_id;
		}
		
		if (idText != null) {
			if (!primaryIdTextView.getText().equals("")) {
				String selectionOfPrimaryTaskId = "(" + TaskTable.COLUMN_ID + " = " + primaryIdTextView.getText() + ")";
				ContentValues values = new ContentValues();
				values.put(TaskTable.COLUMN_PRIMARYCOLOR, 0);
				getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfPrimaryTaskId, null);
			}
			
			String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_ID + " = " + idText + ")";
			
			ContentValues values = new ContentValues();
			values.put(TaskTable.COLUMN_PRIMARYCOLOR, color_id);
			
			getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
			
			primaryIdTextView.setText(idText);
			primaryTextView.setText(descriptionText);
		}
	}
	
	public void clearPrimaryTask(View view) {
		TextView primaryTextView = (TextView) findViewById(R.id.red_primary_task_selected_text);
		TextView primaryIdTextView = (TextView) findViewById(R.id.red_primary_task_selected_id);
		String color_id = red_color_id;
		
		if (view.getId() == R.id.green_clear_button) {
			primaryTextView = (TextView) findViewById(R.id.green_primary_task_selected_text);
			primaryIdTextView = (TextView) findViewById(R.id.green_primary_task_selected_id);
			color_id = green_color_id;
		}
		else if (view.getId() == R.id.blue_clear_button) {
			primaryTextView = (TextView) findViewById(R.id.blue_primary_task_selected_text);
			primaryIdTextView = (TextView) findViewById(R.id.blue_primary_task_selected_id);
			color_id = blue_color_id;
		}
		
		String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_PRIMARYCOLOR + " = " + color_id  + ")";
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
				startActivity(showCompletedTasksIntent);
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
