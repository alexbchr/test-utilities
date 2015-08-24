package com.alexbchr.testutilities.priorj.plugin.ui.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alexbchr.testutilities.priorj.plugin.controllers.PriorJServices;
import com.alexbchr.testutilities.priorj.plugin.models.PrioritizationItem;
import com.alexbchr.testutilities.priorj.plugin.ui.PriorJUILog;

/**
 * Manager the Prioritization Resources.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class PrioritizationManager {

	private static PrioritizationManager manager;
	
	private static List<PrioritizationItem> items = new ArrayList<PrioritizationItem>();
	
	private List<PrioritizationManagerListener> listeners = new ArrayList<PrioritizationManagerListener>();
	
	private PrioritizationManager(){
		
	}
	
	public static PrioritizationManager getManager(){
		if (manager == null){
			manager = new PrioritizationManager();
			loadPrioritizations();
		}
		return manager;
	}
	
	public void addPrioritizationManagerListener(PrioritizationManagerListener listener) {
		if (!listeners.contains(listener)) 
			listeners.add(listener);
		}
	
	public void removePrioritizationManagerListener(PrioritizationManagerListener listener) {
		listeners.remove(listener);
	}
	
	private void firePrioritizationsChanged(PrioritizationItem[] itemsAdded, PrioritizationItem[] itemsRemoved) {
		PrioritizationManagerEvent event = new PrioritizationManagerEvent( this, itemsAdded, itemsRemoved);
		for (Iterator<PrioritizationManagerListener> iter = listeners.iterator();iter.hasNext();) 
			iter.next().prioritizationChanged(event);
	}
	
	public PrioritizationItem[] getPrioritizations(){
		if (items == null)
			loadPrioritizations();
		return items.toArray(new PrioritizationItem[items.size()]);
	}
	
	public List<PrioritizationItem> getPrioritizationItems(){
		if (items == null)
			loadPrioritizations();

		return items;
	}
		
	@SuppressWarnings("unchecked")
	private static void loadPrioritizations(){
		PriorJServices services = PriorJServices.getInstance();
		
		String basepath = services.readLocalBase();
		
		if (!basepath.equals("default")){
			try {
				services.setLocalBasePath(basepath);
				if (services.existPrioritizationsXml()){
					items = (List<PrioritizationItem>) services.getPrioritizations();
				}
				else{
					if (items == null)
						items = new ArrayList<PrioritizationItem>();
					save();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PriorJUILog.logError(e);
			}
		}
		else {
			if (items==null)
				items = new ArrayList<PrioritizationItem>();
//			List<Integer> types = new ArrayList<Integer>();
//			types.add(TechniqueType.AMC);
//			items.add(new PrioritizationItem("Example", "Version Example", "C:/test",types,true, 50));
		}
	}
	
	public static void save(){
		PriorJServices services = PriorJServices.getInstance();
	    if (items != null)
	    	services.savePrioritizations(items);
	}
		
	public void addPrioritization(PrioritizationItem item){
		if (!items.contains(item))
			items.add(item);
		save();
		//firePrioritizationsChanged(items.toArray(new PrioritizationItem[items.size()]), removed);
	}
	
	public void removePrioritizations(PrioritizationItem item){
		items.remove(item);
	}
	
	public void removeAllPrioritizations(){
		if(!items.isEmpty())
			items.clear();
	}
		
	public void load() {
		// TODO Auto-generated method stub
		loadPrioritizations();
	}
	
	
}
