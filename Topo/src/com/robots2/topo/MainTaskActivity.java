package com.robots2.topo;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
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
import android.widget.TextView;

import com.robots2.topo.contentprovider.TaskContentProvider;

public class MainTaskActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mAdapter;
	private SimpleCursorAdapter mSpinnerAdapter;
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
		
		String[] mFromColumnForSpinner = new String[] { TaskTable.COLUMN_DESCRIPTION, TaskTable.COLUMN_ID };
		int[] mToFieldsForSpinner = new int[] { R.id.task_description_spinner, R.id.task_id_spinner };
		mSpinnerAdapter = new SimpleCursorAdapter(this, R.layout.row_spinner_task, null,
				mFromColumnForSpinner, mToFieldsForSpinner, 0);

		mListView.setAdapter(mAdapter);
		
		redPrimaryTaskSpinner.setAdapter(mSpinnerAdapter);
		greenPrimaryTaskSpinner.setAdapter(mSpinnerAdapter);
		bluePrimaryTaskSpinner.setAdapter(mSpinnerAdapter);
		
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { TaskTable.COLUMN_ID, TaskTable.COLUMN_DESCRIPTION };
		String selectionOfTasksWithNoParents = "(" + TaskTable.COLUMN_PARENTS + "= 0 "
				+ "and " + TaskTable.COLUMN_ISPRIMARYCOLOR + " = 0 "
				+ "and " + TaskTable.COLUMN_COMPLETE + " = 0 )";
		
		CursorLoader cursorLoader = null;
		
		switch (id) {
			case 0:
				cursorLoader = new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, 
						selectionOfTasksWithNoParents, null, null);
				break;
				
		}
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
		mSpinnerAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
		mSpinnerAdapter.swapCursor(null);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
	
//		LinearLayout lv = (LinearLayout) listView.getChildAt(position);
//		TextView tvId = (TextView) lv.getChildAt(1);
		
		Uri taskUri = Uri.parse(TaskContentProvider.CONTENT_URI + "/" + String.valueOf(id));
		
		Intent updateTaskIntent = new Intent(this, UpdateTaskActivity.class);
		updateTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		updateTaskIntent.putExtra(TaskContentProvider.CONTENT_ITEM_TYPE, taskUri);
		updateTaskIntent.putExtra("_id", String.valueOf(id));
		
		startActivity(updateTaskIntent);
	}

	public void makeRedPrimary(View v) {
		TextView primaryTextView = (TextView) findViewById(R.id.red_primary_task_selected_text);
		TextView idTextView = (TextView) redPrimaryTaskSpinner.findViewById(R.id.task_id_spinner);
		TextView descriptionTextView = (TextView) redPrimaryTaskSpinner.findViewById(R.id.task_description_spinner);
		
		String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_ID + " = " + idTextView.getText() + ")";
		ContentValues values = new ContentValues();
		
		values.put(TaskTable.COLUMN_ISPRIMARYCOLOR, 1);
		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
		
		primaryTextView.setText(descriptionTextView.getText().toString());
	}
	
	public void makeGreenPrimary(View v) {
		TextView primaryTextView = (TextView) findViewById(R.id.green_primary_task_selected_text);
		TextView idTextView = (TextView) greenPrimaryTaskSpinner.findViewById(R.id.task_id_spinner);
		TextView descriptionTextView = (TextView) greenPrimaryTaskSpinner.findViewById(R.id.task_description_spinner);
		
		String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_ID + " = " + idTextView.getText() + ")";
		ContentValues values = new ContentValues();
		
		values.put(TaskTable.COLUMN_ISPRIMARYCOLOR, 1);
		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
		
		primaryTextView.setText(descriptionTextView.getText().toString());
	}

	public void makeBluePrimary(View v) {
		TextView primaryTextView = (TextView) findViewById(R.id.blue_primary_task_selected_text);
		TextView idTextView = (TextView) bluePrimaryTaskSpinner.findViewById(R.id.task_id_spinner);
		TextView descriptionTextView = (TextView) bluePrimaryTaskSpinner.findViewById(R.id.task_description_spinner);
		
		String selectionOfTasksWithSameId = "(" + TaskTable.COLUMN_ID + " = " + idTextView.getText() + ")";
		ContentValues values = new ContentValues();
		
		values.put(TaskTable.COLUMN_ISPRIMARYCOLOR, 1);
		getContentResolver().update(TaskContentProvider.CONTENT_URI, values, selectionOfTasksWithSameId, null);
		
		primaryTextView.setText(descriptionTextView.getText().toString());
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
