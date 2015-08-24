package com.alexbchr.testutilities.priorj.plugin.ui.models;

import java.util.Calendar;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Interface to represent Prioritization Viewer Data
 * 
 * @author Samuel T. C. Santos
 *
 */
public interface IPrioritizationItem extends IAdaptable {
	/**
	 * Get the Project Name.
	 * 
	 * @return
	 *  the project name.
	 */
	public String getProjectName();
	
	public void setProjectName(String newName);
	
	public String getVersionName();
	
	public void setVersionName(String versionName);
	
	public void setTime(Calendar time);
	
	public Calendar getTime();
	
	public List<Integer> getTypes();
	
	public void setTechniqueTypes(List<Integer> types);
	
	public void setLocation(String location);
	
	public String getLocation();
	
}
