package com.robots2.topo;

public class Task {

	private String mText;
	
	private String mId;
	
	private boolean mComplete;
	
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
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Task && ((Task) o).mId == mId;
	}
}
