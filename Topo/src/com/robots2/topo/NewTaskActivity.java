package com.robots2.topo;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.robots2.topo.contentprovider.TaskContentProvider;

public class NewTaskActivity extends ListActivity {

	private EditText taskDescriptionEditText;
	private Uri taskUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
		
		taskDescriptionEditText = (EditText) findViewById(R.id.newTaskEditText);
		taskDescriptionEditText.requestFocus();
		taskDescriptionEditText.postDelayed(new Runnable() {
           @Override
           public void run() {
               InputMethodManager keyboard = (InputMethodManager)
               getSystemService(Context.INPUT_METHOD_SERVICE);
               keyboard.showSoftInput(taskDescriptionEditText, 0);
           }
       },200);
		
		Bundle extras = getIntent().getExtras();
		
		taskUri = (savedInstanceState == null) ? null 
				: (Uri) savedInstanceState.getParcelable(TaskContentProvider.CONTENT_ITEM_TYPE);
		
		if (extras != null) {
			taskUri = extras.getParcelable(TaskContentProvider.CONTENT_ITEM_TYPE);
			fillData(taskUri);
		}
	}
	
	private void fillData(Uri uri) {
		String[] projection = { TaskTable.COLUMN_DESCRIPTION };
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		cursor.close();
	}

	public void onAddTaskClick(View view) {
		setResult(RESULT_OK);
		finish();
	}
	
	public void onCancelAddTaskClick(View view) {
		this.onBackPressed();
	}
	
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(TaskContentProvider.CONTENT_ITEM_TYPE, taskUri);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}
	
	private void saveState() {
		String taskDescription = taskDescriptionEditText.getText().toString();
		
		if (taskDescription.length() == 0) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(TaskTable.COLUMN_DESCRIPTION, taskDescription);
		values.put(TaskTable.COLUMN_COLOR, "#FFFFFF");
		values.put(TaskTable.COLUMN_COMPLETE, 0);
		values.put(TaskTable.COLUMN_ISPRIMARYCOLOR, 0);
		
		if (taskUri == null) {
			taskUri = getContentResolver().insert(TaskContentProvider.CONTENT_URI, values);
		}
		else {
			getContentResolver().update(TaskContentProvider.CONTENT_URI, values, null, null);
		}
	}
}
