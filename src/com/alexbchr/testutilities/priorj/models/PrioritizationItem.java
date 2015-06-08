package com.alexbchr.testutilities.priorj.models;

import java.util.Calendar;
import java.util.List;

/**
 * This class representing a Item for Prioritization.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 */
public class PrioritizationItem implements IPrioritizationItem , Comparable<IPrioritizationItem>{
	/**
	 * A Project name from workspace 
	 */
	private String projectName;
	/**
	 * Version name given by user
	 */
	private String version;
	/**
	 * Selected location to save artifacts
	 */
	private String location;
	/**
	 * Current time 
	 */
	private Calendar time;
	/**
	 * Selected techniques by user.
	 */
	private List<Integer> techniques;
	
	private boolean isJUnit3;
	
	private int selectionSize;
	
	/**
	 * Constructor
	 */
	public PrioritizationItem(String project, String version, String location, List<Integer> techniques){
		this.projectName = project;
		this.version = version;
		this.location = location;
		this.techniques = techniques;
		this.time = Calendar.getInstance();
	}
	
	public PrioritizationItem(String project, String version, String location, List<Integer> techniques,
			boolean isJUnitVersion3, int selectionSize) {
		// TODO Auto-generated constructor stub
		this(project,version, location, techniques);
		this.isJUnit3 = isJUnitVersion3;
		this.selectionSize = selectionSize;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return version;
	}

	@Override
	public void setVersionName(String versionName) {
		this.version = versionName;
	}

	@Override
	public void setTime(Calendar time) {
		// TODO Auto-generated method stub
		this.time = time;
	}

	@Override
	public Calendar getTime() {
		// TODO Auto-generated method stub
		return time;
	}

	@Override
	public List<Integer> getTechniques() {
		// TODO Auto-generated method stub
		return techniques;
	}

	@Override
	public void setTechniqueTypes(List<Integer> types) {
		// TODO Auto-generated method stub
		this.techniques = types;
	}

	@Override
	public void setLocation(String location) {
		// TODO Auto-generated method stub
		this.location = location;
	}

	@Override
	public String getLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public int compareTo(IPrioritizationItem item) {
		// TODO Auto-generated method stub
		Long compare = this.getTime().getTimeInMillis() - item.getTime().getTimeInMillis(); 
		return compare.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrioritizationItem other = (PrioritizationItem) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (techniques == null) {
			if (other.techniques != null)
				return false;
		} else if (!techniques.equals(other.techniques))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public boolean isJUnit3() {
		// TODO Auto-generated method stub
		return isJUnit3;
	}

	@Override
	public int getSelectionSize() {
		// TODO Auto-generated method stub
		return selectionSize;
	}
	
	
}
