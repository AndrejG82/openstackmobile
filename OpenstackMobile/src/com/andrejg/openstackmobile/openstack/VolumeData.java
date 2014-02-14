package com.andrejg.openstackmobile.openstack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.andrejg.openstackmobile.CommonUtilities;;

public class VolumeData {
	
	// basic
	private String volumeName;
	private String volumeDesc;
	private String volumeId;
	private String volumeState;	
	private Date created;
	private String volumeSize;
	// detail
	// TODO
	
	public VolumeData(String asVolumeName,String asVolumeDesc, String asVolumeId,String asVolumeState, String asVolumeSize, String asDateCreated) {
		setVolumeName(asVolumeName);
		setVolumeDesc(asVolumeDesc);
		setVolumeId(asVolumeId);
		setVolumeState(asVolumeState);
		setVolumeSize(asVolumeSize);
		
		try {
			setCreated(CommonUtilities.parse(asDateCreated));
		} catch (ParseException e) {
			setCreated(null);
		}		
	}

	public String getVolumeName() {
		return volumeName;
	}

	private void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	public String getVolumeDesc() {
		return volumeDesc;
	}

	private void setVolumeDesc(String volumeDesc) {
		this.volumeDesc = volumeDesc;
	}

	public String getVolumeId() {
		return volumeId;
	}

	private void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public String getVolumeState() {
		return volumeState;
	}

	private void setVolumeState(String volumeState) {
		this.volumeState = volumeState;
	}

	public Date getCreated() {
		return created;
	}

	private void setCreated(Date created) {
		this.created = created;
	}

	public String getVolumeSize() {
		return volumeSize;
	}

	private void setVolumeSize(String volumeSize) {
		this.volumeSize = volumeSize;
	}
	
		
	
	
}
