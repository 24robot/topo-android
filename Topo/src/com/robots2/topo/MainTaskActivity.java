package com.robots2.topo;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainTaskActivity extends ListActivity implements OnItemClickListener{

	private TaskContentProvider dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_main);
		
		getListView().setOnItemClickListener(this);
		
		dataSource = new TaskContentProvider(this);
		dataSource.open();
		
		List<Task> tasks = dataSource.getAllTasks();
		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this, R.layout.row_list_task, R.id.task_description, tasks);
		setListAdapter(adapter);
		
		Spinner redPrimaryTaskSpinner = (Spinner) findViewById(R.id.red_primary_task_spinner);
		Spinner greenPrimaryTaskSpinner = (Spinner) findViewById(R.id.green_primary_task_spinner);
		Spinner bluePrimaryTaskSpinner = (Spinner) findViewById(R.id.blue_primary_task_spinner);
		
		redPrimaryTaskSpinner.setAdapter(adapter);
		greenPrimaryTaskSpinner.setAdapter(adapter);
		bluePrimaryTaskSpinner.setAdapter(adapter);
		
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
				dataSource.close();
				Intent addNewTaskIntent = new Intent(this, NewTaskActivity.class);
				addNewTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(addNewTaskIntent);
				break;
				
			case (R.id.action_completed_tasks):
				dataSource.close();
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
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
//		@SuppressWarnings("unchecked")
//		ArrayAdapter<Task> adapter = (ArrayAdapter<Task>) getListAdapter();
//		Task task = null;

		/*
		if (getListAdapter().getCount() > 0) {
			ListView lv = getListView();
			int position = lv.getPositionForView(view);
			
			task = (Task) getListAdapter().getItem(position);
		}*/
		
		dataSource.close();
		Intent updateTaskIntent = new Intent(this, UpdateTaskActivity.class);
		updateTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(updateTaskIntent);

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
