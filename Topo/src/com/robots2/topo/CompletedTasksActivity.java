package com.robots2.topo;

import com.robots2.topo.contentprovider.TaskContentProvider;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CompletedTasksActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private SimpleCursorAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_completed_tasks);
		
		getLoaderManager().initLoader(0, null, this);
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
		String selectionOfTasksWithNoParents = "( " + TaskTable.COLUMN_COMPLETE + " = 1 )";
		
		CursorLoader cursorLoader =  new CursorLoader(this, TaskContentProvider.CONTENT_URI, projection, 
						selectionOfTasksWithNoParents, null, null);
		
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
}
