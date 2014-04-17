package com.robots2.topo;

import com.robots2.topo.contentprovider.TaskContentProvider;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewTaskActivity extends ListActivity {

	private EditText taskDescriptionEditText;
	private Uri taskUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
		
		taskDescriptionEditText = (EditText) findViewById(R.id.newTaskEditText);
		
		Bundle extras = getIntent().getExtras();
		
		taskUri = (savedInstanceState == null) ? null 
				: (Uri) savedInstanceState.getParcelable(TaskContentProvider.CONTENT_ITEM_TYPE);
		
		if (extras != null) {
			taskUri = extras.getParcelable(TaskContentProvider.CONTENT_ITEM_TYPE);
			fillData(taskUri);
		}
	}
	
	private void fillData(Uri uri) {
		// TODO Auto-generated method stub
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
		
		if (taskUri == null) {
			taskUri = getContentResolver().insert(TaskContentProvider.CONTENT_URI, values);
		}
		else {
			getContentResolver().update(TaskContentProvider.CONTENT_URI, values, null, null);
		}
	}
}
