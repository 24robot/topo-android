package com.robots2.topo;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class UpdateTaskActivity extends ListActivity {

	private TaskDataSource dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_task);

		dataSource = new TaskDataSource(this);
		dataSource.open();
		
		List<Task> tasks = dataSource.getAllTasks();
		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this, R.layout.row_list_task_with_checkbox, R.id.task_description_with_checkbox, tasks);
		setListAdapter(adapter);
	}
}
