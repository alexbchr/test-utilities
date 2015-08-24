package com.alexbchr.testutilities.priorj.plugin.ui.models;

import java.util.EventObject;

import com.alexbchr.testutilities.priorj.plugin.models.PrioritizationItem;

/**
 * Manager Event for Changes in the Source.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 *
 */
public class PrioritizationManagerEvent extends EventObject {
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4497673438874788856L;

	private final PrioritizationItem [] added;
	private final PrioritizationItem [] removed;
	
	public PrioritizationManagerEvent(PrioritizationManager source,
			PrioritizationItem [] itemsAdded,
			PrioritizationItem [] itemsRemoved) {
		super(source);
		// TODO Auto-generated constructor stub
		added = itemsAdded;
		removed = itemsRemoved;
	}

	public PrioritizationItem [] getItemsAdded(){
		return added;
	}
	
	public PrioritizationItem [] getItemsRemoved(){
		return removed;
	}
	
}
