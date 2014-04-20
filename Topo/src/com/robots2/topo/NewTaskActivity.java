package com.robots2.topo;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.robots2.topo.contentprovider.TaskContentProvider;

public class NewTaskActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{

	private EditText taskDescriptionEditText;
	private Uri taskUri;
	private SimpleCursorAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
		
		getLoaderManager().initLoader(0, null, this);
		
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
		}
		
		fillData(taskUri);
	}
	
	
	private void fillData(Uri uri) {
		String[] mFromColumns = new String[] { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		int[] mToFields = new int[] { R.id.task_id_with_checkbox, R.id.task_description_with_checkbox };
		
		ListView mListView = (ListView) findViewById(android.R.id.list);
		mAdapter = new SimpleCursorAdapter(this, R.layout.row_list_task_with_checkbox, 
				null, mFromColumns, mToFields, 0);
		
		mListView.setAdapter(mAdapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		CursorLoader cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, null, null, null);
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
	
	public void onAddTaskClick(View view) {
//		ListView lv = (ListView) findViewById(android.R.id.list);
//		
//		SparseBooleanArray checked = lv.getCheckedItemPositions();
//		for (int i = 0; i < checked.size(); i++) {
//		    if(checked.valueAt(i) == true) {
//		        lv.getItemAtPosition(i);
//		        
//		    }
//		}
		
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
		values.put(TaskTable.COLUMN_COLOR, "#000000");
		values.put(TaskTable.COLUMN_COMPLETE, 0);
		
		
		values.put(TaskTable.COLUMN_PARENTS, "0");
		values.put(TaskTable.COLUMN_ISPRIMARYCOLOR, 0);
		
		if (taskUri == null) {
			taskUri = getContentResolver().insert(TaskContentProvider.CONTENT_URI, values);
		}
		else {
			getContentResolver().update(TaskContentProvider.CONTENT_URI, values, null, null);
		}
	}
}
