package com.andrejg.openstackmobile.openstack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.andrejg.openstackmobile.CommonUtilities;;

public class InstanceData {
	
	// basic
	private String instanceName;
	private String instanceState;
	private String instanceId;
	private Date created;
	// detail
	private boolean extended;
	private String instanceFlavor;
	private String instanceRAM;
	private String instanceVCPU;
	private String instanceDISK;
	
	public InstanceData(String asInstanceName,String asInstanceState, String asInstanceID, String asDateCreated) {
		extended = false;
		setInstanceName(asInstanceName);
		setInstanceState(asInstanceState);
		setInstanceId(asInstanceID);
		try {
			setCreated(CommonUtilities.parse(asDateCreated));
		} catch (ParseException e) {
			setCreated(null);
		}		
	}
	
	public InstanceData(String asInstanceName,String asInstanceState, String asInstanceID, String asDateCreated,
						String asInstanceFlavor, String asInstanceRAM, String asInstanceVCPU, String asInstanceDISK) {
		this(asInstanceName,asInstanceState,asInstanceID,asDateCreated);
		extended = false;
		
		setInstanceFlavor(asInstanceFlavor);
		setInstanceRAM(asInstanceRAM);
		setInstanceVCPU(asInstanceVCPU);
		setInstanceDISK(asInstanceDISK);
	}
	
	
	public String getInstanceName() {
		return instanceName;
	}
	private void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getInstanceState() {
		return instanceState;
	}

	private void setInstanceState(String instanceState) {
		this.instanceState = instanceState;
	}	

	public Date getCreated() {
		return created;
	}

	private void setCreated(Date created) {
		this.created = created;
	}

	public String getInstanceId() {
		return instanceId;
	}

	private void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceFlavor() {
		return instanceFlavor;
	}

	private void setInstanceFlavor(String instanceFlavor) {
		this.instanceFlavor = instanceFlavor;
	}

	public String getInstanceRAM() {
		return instanceRAM;
	}

	private void setInstanceRAM(String instanceRAM) {
		this.instanceRAM = instanceRAM;
	}

	public String getInstanceVCPU() {
		return instanceVCPU;
	}

	private void setInstanceVCPU(String instanceVCPU) {
		this.instanceVCPU = instanceVCPU;
	}

	public String getInstanceDISK() {
		return instanceDISK;
	}

	private void setInstanceDISK(String instanceDISK) {
		this.instanceDISK = instanceDISK;
	}
	
	
}
