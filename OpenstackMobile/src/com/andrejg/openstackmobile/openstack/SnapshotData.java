package com.andrejg.openstackmobile.openstack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.andrejg.openstackmobile.CommonUtilities;;

public class SnapshotData {
	
	// basic
	private String snapshotName;
	private String snapshotId;
	private String snapshotState;		
	// detail
	// TODO
	
	public SnapshotData(String asSnapshotName,String asSnapshotId,String asSnapshotState) {
		setSnapshotName(asSnapshotName);
		setSnapshotId(asSnapshotId);
		setSnapshotState(asSnapshotState);
			
	}

	public String getSnapshotId() {
		return snapshotId;
	}

	private void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}

	public String getSnapshotName() {
		return snapshotName;
	}

	private void setSnapshotName(String snapshotName) {
		this.snapshotName = snapshotName;
	}

	public String getSnapshotState() {
		return snapshotState;
	}

	private void setSnapshotState(String snapshotState) {
		this.snapshotState = snapshotState;
	}

	
		
	
	
}
