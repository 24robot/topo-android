package com.robots2.topo;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;

public class UpdateTaskActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_task);

	}
	
	public void onSaveTaskClick(View view) {
		setResult(RESULT_OK);
		finish();
	}
	
	public void onDeleteTaskClick(View view) {
		finish();
	}
}
