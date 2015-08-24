package com.alexbchr.testutilities.priorj.plugin.ui.models;

import java.util.List;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.alexbchr.testutilities.priorj.plugin.ui.PriorJUILog;

/**
 * Manager for user instrumentations.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class InstrumentationManager {
	
	private static InstrumentationManager instance;
		
	private InstrumentationManager(){
		
	}
	
	public static InstrumentationManager getInstance(){
		if (instance == null)
			instance = new InstrumentationManager();
		
		return instance;
	}
	
	/**
	 * Saving to eclipse preferences Key and value.
	 * 
	 * @param key
	 * @param value
	 */
	public void saveInPreferences(String key, String value){
		Preferences preferences = InstanceScope.INSTANCE.getNode("com.alexbchr.testutilities");
		Preferences sub1 = preferences.node("node1");
	    sub1.put(key, value);
        try {
          // forces the application to save the preferences
          preferences.flush();
        } catch (BackingStoreException e2) {
          PriorJUILog.logError(e2);
        }
	}
	/**
	 * Getting value from preferences by key.
	 * 
	 * @param key
	 * @return
	 */
	public String getFromPreferences(String key){
		Preferences preferences = InstanceScope.INSTANCE.getNode("com.alexbchr.testutilities");
	    Preferences sub1 = preferences.node("node1");
	    return sub1.get(key, "default");
	}
	
}
