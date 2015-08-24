package com.alexbchr.testutilities.priorj.plugin.ui.providers;

import java.util.Arrays;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.alexbchr.testutilities.priorj.plugin.ui.models.PrioritizationManager;
import com.alexbchr.testutilities.priorj.plugin.ui.models.PrioritizationManagerEvent;
import com.alexbchr.testutilities.priorj.plugin.ui.models.PrioritizationManagerListener;

/*
 * The content provider class is responsible for
 * providing objects to the view. It can wrap
 * existing objects in adapters or simply return
 * objects as-is. These objects may be sensitive
 * to the current input of the view, or ignore
 * it and always show the same content 
 * (like Task List, for example).
 */

public class ViewContentProvider 
              implements IStructuredContentProvider ,
                         PrioritizationManagerListener {
	
	private TableViewer viewer;
	PrioritizationManager manager = PrioritizationManager.getManager();
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		 this.viewer = (TableViewer) viewer;
		 if (manager != null)  
			 manager.removePrioritizationManagerListener(this);
		 manager = (PrioritizationManager) newInput;
		 if (manager != null) 
			 manager.addPrioritizationManagerListener(this);
	}
	public void dispose() {
	
	}
	public Object[] getElements(Object parent) {
		return manager.getPrioritizations();
	}
	
	public void prioritizationChanged(PrioritizationManagerEvent event){
		viewer.getTable().setRedraw(false);
		try {
			viewer.remove(event.getItemsRemoved());
			viewer.add(event.getItemsAdded());
		}
		finally {
			viewer.getTable().setRedraw(true);
		}
	}
	
}