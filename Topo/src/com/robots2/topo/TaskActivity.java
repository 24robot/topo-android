package com.robots2.topo;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class TaskActivity extends ListActivity {

	private TaskDataSource dataSource;
	
	private ProgressBar mProgressBar;
	
	private Spinner mColorSpinner;
	
	private String redPrimaryTask;
	private String greenPrimaryTask;
	private String bluePrimaryTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
		mProgressBar.setVisibility(ProgressBar.GONE);
		
		mColorSpinner = (Spinner) findViewById(R.id.colorSpinner);
		
		List<String> colors = new ArrayList<String>();
		
		if (redPrimaryTask == null) {
			colors.add("Red Primary Task");
		}
		
		if (greenPrimaryTask == null) {
			colors.add("Green Primary Task");
		}
		
		if (bluePrimaryTask == null) {
			colors.add("Blue Primary Task");
		}
		
		if (!colors.isEmpty()) {
			ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, colors);
			mColorSpinner.setAdapter(adapter);
			
		}
		
		dataSource = new TaskDataSource(this);
		dataSource.open();
		
		List<Task> tasks = dataSource.getAllTasks();
		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this, R.layout.row_list_task, R.id.task_description, tasks);
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
				
				e.setText("");
				
				break;
			
			case R.id.task_description:
				if (getListAdapter().getCount() > 0) {
					ListView lv = getListView();
					int position = lv.getPositionForView(view);
					
					task = (Task) getListAdapter().getItem(position);
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
