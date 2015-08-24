package com.alexbchr.testutilities.priorj.plugin.ui.models;

/**
 * Interface PrioritizationManagerListener notify interested objects
 *  when occurs changes. 
 *  
 * @author Samuel T. C. Santos
 *
 */
public interface PrioritizationManagerListener {

	/**
	 * Notify changes in the event.
	 * 
	 * @param event
	 */
	public void prioritizationChanged(PrioritizationManagerEvent event);
	
}
