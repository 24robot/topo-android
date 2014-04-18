package com.robots2.topo;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.robots2.topo.contentprovider.TaskContentProvider;

public class UpdateTaskActivity extends ListActivity {

	private Uri taskUri;
	private EditText taskDescriptionEditText;
	private int mId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_task);
		
		taskDescriptionEditText = (EditText) findViewById(R.id.updateTaskEditText);

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
			
			taskDescriptionEditText.append(cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_DESCRIPTION)));
		}
		
		cursor.close();
	}
	
	public void onSaveTaskClick(View view) {
		setResult(RESULT_OK);
		finish();
	}
	
	public void onDeleteTaskClick(View view) {
		getContentResolver().delete(taskUri, null, null);
		finish();
	}
	
	
}
