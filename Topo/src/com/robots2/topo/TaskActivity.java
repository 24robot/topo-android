package com.robots2.topo;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;

public class TaskActivity extends ListActivity {

	private TaskDataSource dataSource;
	
	private ProgressBar mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
		mProgressBar.setVisibility(ProgressBar.GONE);
		
		dataSource = new TaskDataSource(this);
		dataSource.open();
		
		List<Task> tasks = dataSource.getAllTasks();
		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this, R.layout.row_list_task, R.id.checkTaskItem, tasks);
		setListAdapter(adapter);
		
	}
	
	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Task> adapter = (ArrayAdapter<Task>) getListAdapter();
		Task task = null;
		
		switch(view.getId()) {
			case R.id.buttonAddTask:
				EditText e = (EditText) findViewById(R.id.textNewTask);
				String description = e.getText().toString();
				
				if (description != null) {
					task = dataSource.addTask(description);
					adapter.add(task);
				}
				
				break;
			
			case R.id.checkTaskItem:
				if (getListAdapter().getCount() > 0) {
					task = (Task) getListAdapter().getItem(0);
			        dataSource.deleteTask(task);;
			        adapter.remove(task);
				}
				
				break;
		}
		
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}
}
