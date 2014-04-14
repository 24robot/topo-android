package com.robots2.topo;

import java.util.List;

public class Task {

	private String mText;
	
	private String mId;
	
	private boolean mComplete;
	
	private List<String> mParentIds;
	
	private String mColor;
	
	public Task() {
		
	}
	
	public Task(String text, String id) {
		this.setText(text);
		this.setId(id);
	}
	
	public Task(String text, boolean b) {
		this.setText(text);
		this.setComplete(b);
	}

	public String toString() {
		return getText();
	}
	
	public String getText() {
		return mText;
	}
	
	public final void setText(String text) {
		mText = text;
	}
	
	public String getId() {
		return mId;
	}
	
	public final void setId(String id) {
		mId = id;
	}
	
	public boolean isComplete() {
		return mComplete;
	}
	
	public void setComplete(boolean complete) {
		mComplete = complete;
	}
	
	public void addParent(String parentId) {
		mParentIds.add(parentId);
	}
	
	public void removeParent(String parentId) {
		boolean removedSuccessfully = false;
		for (String parent : mParentIds) {
			if (parentId.equals(parent)) {
				mParentIds.remove(parent);
				removedSuccessfully = true;
				break;
			}
		}
		
		if (!removedSuccessfully) {
			// Throw suitable error here
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Task && ((Task) o).mId == mId;
	}
}
