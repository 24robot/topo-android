package com.robots2.topo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

public class TaskActivity extends Activity {

	private TaskAdapter mAdapter;
	
	private EditText mTextNewTask;
	
	private ProgressBar mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
		
		mProgressBar.setVisibility(ProgressBar.GONE);
		
		try {
			mTextNewTask = (EditText) findViewById(R.id.textNewTask);
			
			mAdapter = new TaskAdapter(this, R.layout.row_list_task);
			ListView listViewTask = (ListView) findViewById(R.id.listViewTask);
			listViewTask.setAdapter(mAdapter);
		} catch (Exception e) {
			createAndShowDialog(e, "Error");
		}
		
	}

	private void createAndShowDialog(Exception exception, String title) {
		Throwable ex = exception;
		if(exception.getCause() != null){
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title);
	}

	private void createAndShowDialog(String message, String title) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		
		alertBuilder.setMessage(message);
		alertBuilder.setTitle(title);
		alertBuilder.create().show();
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

	public void checkTask(Task task) {
		task.setComplete(true);
		mAdapter.remove(task);
	}
	
	public void addItem(View v) {
		Task task = new Task(mTextNewTask.getText().toString(), false);
		mAdapter.add(task);
		
		mTextNewTask.setText("");
	}
}
