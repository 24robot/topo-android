package com.robots2.topo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class TaskAdapter extends ArrayAdapter<Task>{

	Context mContext;
	
	int mLayoutResourceId;
	
	public TaskAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
		
		mContext = context;
		mLayoutResourceId = layoutResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		final Task currentTask = getItem(position);
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}
		
		row.setTag(currentTask);
		
		final CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkTaskItem);
		checkBox.setText(currentTask.getText());
		checkBox.setChecked(false);
		checkBox.setEnabled(true);
		
		checkBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (checkBox.isChecked()) {
					checkBox.setEnabled(false);
					if (mContext instanceof TaskActivity) {
						TaskActivity activity = (TaskActivity) mContext;
						activity.checkTask(currentTask);
					}
				}
				
			}
		});
		
		return row;
	}
}
