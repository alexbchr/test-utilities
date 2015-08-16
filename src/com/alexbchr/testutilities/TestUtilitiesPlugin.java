package com.alexbchr.testutilities;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TestUtilitiesPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.alexbchr.testutilities"; //$NON-NLS-1$

	// The shared instance
	private static TestUtilitiesPlugin plugin;
	
	
	/**
	 * The constructor
	 */
	public TestUtilitiesPlugin() {
		plugin = this;
	}
	
	public static Shell getActiveWorkbenchShell() {
 		IWorkbenchWindow workBenchWindow= getActiveWorkbenchWindow();
 		if (workBenchWindow == null)
 			return null;
 		return workBenchWindow.getShell();
 	}
	

 	public static IWorkbenchWindow getActiveWorkbenchWindow() {
 		if (plugin == null)
 			return null;
 		IWorkbench workBench= plugin.getWorkbench();
 		if (workBench == null)
 			return null;
 			return workBench.getActiveWorkbenchWindow();
 	}
 	
 	public IWorkbench getWorkbench() {
 	    return PlatformUI.getWorkbench();
 	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TestUtilitiesPlugin getDefault() {
		return plugin;
	}
}
