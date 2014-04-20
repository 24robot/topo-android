package com.robots2.topo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.robots2.topo.contentprovider.TaskContentProvider;

public class UpdateTaskActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private Uri taskUri;
	private EditText taskDescriptionEditText;
	private CheckBox completeCheckbox;
	private String mId;
	
	private SimpleCursorAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_task);
		
		completeCheckbox = (CheckBox) findViewById(R.id.complete_checkbox);
		taskDescriptionEditText = (EditText) findViewById(R.id.update_task_edit_text);

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
			mId = getIntent().getStringExtra("_id");
			fillData(taskUri);
		}
		
		getLoaderManager().initLoader(0, null, this);
		
	}
	
	private void fillData(Uri uri) {
		String[] mFromColumns = new String[] { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		int[] mToFields = new int[] { R.id.task_id_with_checkbox, R.id.task_description_with_checkbox };
		
		ListView mListView = (ListView) findViewById(android.R.id.list);
		mAdapter = new SimpleCursorAdapter(this, R.layout.row_list_task_with_checkbox, 
				null, mFromColumns, mToFields, 0);
		
		mListView.setAdapter(mAdapter);
		
		String[] projection = { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			taskDescriptionEditText.append(cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_DESCRIPTION)));
		}
	}
	
	public void onSaveTaskClick(View view) {
		String taskDescription = taskDescriptionEditText.getText().toString();
		String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_ID + " = " + mId + ")";
		
		if (taskDescription.length() == 0) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(TaskTable.COLUMN_DESCRIPTION, taskDescription);
		
		if (completeCheckbox.isChecked()) {
			values.put(TaskTable.COLUMN_COMPLETE, 1);
		}
		
		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
		
		setResult(RESULT_OK);
		finish();
	}
	
	public void onDeleteTaskClick(View view) {
		new AlertDialog.Builder(this)
		.setTitle("Delete task")
	    .setMessage("Are you sure you want to delete this task?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	getContentResolver().delete(taskUri, null, null);
	        	finish();
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
	
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		String selectionOfTasksWithDifferentId = "(" + TaskTable.COLUMN_ID + " != " + mId 
				+ " and " + TaskTable.COLUMN_COMPLETE + " = " + "0 )";
		CursorLoader cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, 
				selectionOfTasksWithDifferentId, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);	
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		mAdapter.swapCursor(null);
	}
	
	
}
