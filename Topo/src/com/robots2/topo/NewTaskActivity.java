package com.robots2.topo;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class NewTaskActivity extends ListActivity {

	private TaskContentProvider dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);

		dataSource = new TaskContentProvider(this);
		dataSource.open();
		
		List<Task> tasks = dataSource.getAllTasks();
		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this, R.layout.row_list_task_with_checkbox, R.id.task_description_with_checkbox, tasks);
		setListAdapter(adapter);
	}
	
	public void onAddTaskClick(View view) {
		Intent intent = new Intent(this, MainTaskActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	public void onCancelAddTaskClick(View view) {
		this.onBackPressed();
	}
}
