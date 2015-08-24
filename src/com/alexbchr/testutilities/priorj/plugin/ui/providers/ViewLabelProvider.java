package com.alexbchr.testutilities.priorj.plugin.ui.providers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.alexbchr.testutilities.priorj.plugin.models.PrioritizationItem;

/**
 * 
 * Prioritization View Content Provider.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 */
public class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
	public String getColumnText(Object obj, int index) {
				
		PrioritizationItem itemView = (PrioritizationItem) obj;
		switch (index) { 
			case 0 :            
				return dataFormat(itemView.getTime());        
			case 1 :            
				return itemView.getProjectName();        
			case 2 :
				return itemView.getVersionName();
			case 3 :            
				return Integer.toString(itemView.getTechniques().size());
			case 4 :
				 return itemView.getLocation();
			case 5 :
				 return itemView.isJUnit3() ? "JUnit3" : "JUnit4";
			case 6 :
				return Integer.toString(itemView.getSelectionSize());
			default :            
				return "unknown " + index;
		}
		
	}
	
	private String dataFormat(Calendar date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		return format.format(date.getTime());
	}
	
	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}
	
	public Image getImage(Object obj) {
		return null;
	}
}