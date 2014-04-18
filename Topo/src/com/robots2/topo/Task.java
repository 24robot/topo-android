package com.robots2.topo;

import java.util.List;

public class Task {

	private long mId;
	private String mDescription;
	private String mColor;
	private boolean mComplete;
	private List<String> mParentIds;	
	private List<String> mChildrenIds;
	private boolean mIsPrimary;
	
	public Task() {
		
	}

	public long getId() {
		return mId;
	}
	
	public final void setId(long id) {
		mId = id;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public final void setDescription(String description) {
		mDescription = description;
	}
	
	public String getColor() {
		return mColor;
	}
	
	public final void setColor(String colorHex) {
		if (colorHex.equalsIgnoreCase("Red")) {
			colorHex = "FF0000";
		}
		else if (colorHex.equalsIgnoreCase("Green")) {
			colorHex = "00FF00";
		}
		else if (colorHex.equalsIgnoreCase("Blue")) {
			colorHex = "0000FF";
		}
		else {
			colorHex = "FFFFFF";
		}
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
	
	public boolean isPrimary() {
		return mIsPrimary;
	}
	
	public void setPrimary(boolean primary) {
		mIsPrimary = primary;
	}
	
	public String toString() {
		return getDescription();
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Task && ((Task) o).mId == mId;
	}
}
