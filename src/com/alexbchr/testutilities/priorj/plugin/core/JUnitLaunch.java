package com.alexbchr.testutilities.priorj.plugin.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;

import com.alexbchr.testutilities.TestUtilitiesPlugin;

/**
 * Implementing a sample JUnit launch.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class JUnitLaunch {

	private static final String EMPTY_STRING= "";
	
	public JUnitLaunch(){
		//default
	}
	
	public void performLaunch(IJavaElement element, String mode) throws InterruptedException, CoreException {
		ILaunchConfigurationWorkingCopy temporary= createLaunchConfiguration(element);
		ILaunchConfiguration config= findExistingLaunchConfiguration(temporary, mode);
		if (config == null) {
			// no existing found: create a new one
			config= temporary.doSave();
		}
		DebugUITools.launch(config, mode);
	}
	
	protected ILaunchConfigurationWorkingCopy createLaunchConfiguration(IJavaElement element) throws CoreException {
		final String testName;
		final String mainTypeQualifiedName;
		final String containerHandleId;

		switch (element.getElementType()) {
			case IJavaElement.JAVA_PROJECT:
			case IJavaElement.PACKAGE_FRAGMENT_ROOT:
			case IJavaElement.PACKAGE_FRAGMENT: {
				String name= JavaElementLabels.getTextLabel(element, JavaElementLabels.ALL_FULLY_QUALIFIED);
				containerHandleId= element.getHandleIdentifier();
				mainTypeQualifiedName= EMPTY_STRING;
				testName= name.substring(name.lastIndexOf(IPath.SEPARATOR) + 1);
			}
			break;
			case IJavaElement.TYPE: {
				containerHandleId= EMPTY_STRING;
				mainTypeQualifiedName= ((IType) element).getFullyQualifiedName('.'); // don't replace, fix for binary inner types
				testName= element.getElementName();
			}
			break;
			case IJavaElement.METHOD: {
				IMethod method= (IMethod) element;
				containerHandleId= EMPTY_STRING;
				mainTypeQualifiedName= method.getDeclaringType().getFullyQualifiedName('.');
				testName= method.getDeclaringType().getElementName() + '.' + method.getElementName();
			}
			break;
			default:
				throw new IllegalArgumentException("Invalid element type to create a launch configuration: " + element.getClass().getName()); //$NON-NLS-1$
		}

		String testKindId= TestKindRegistry.getContainerTestKindId(element);

		ILaunchConfigurationType configType= getLaunchManager().getLaunchConfigurationType(getLaunchConfigurationTypeId());
		ILaunchConfigurationWorkingCopy wc= configType.newInstance(null, getLaunchManager().generateLaunchConfigurationName(testName));

		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, mainTypeQualifiedName);
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, element.getJavaProject().getElementName());
		wc.setAttribute(JUnitLaunchConfigurationConstants.ATTR_KEEPRUNNING, false);
		wc.setAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_CONTAINER, containerHandleId);
		wc.setAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_RUNNER_KIND, testKindId);
		JUnitMigrationDelegate.mapResources(wc);
		AssertionVMArg.setArgDefault(wc);
		if (element instanceof IMethod) {
			wc.setAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_METHOD_NAME, element.getElementName()); // only set for methods
		}
		return wc;
	}
		
	protected String getLaunchConfigurationTypeId() {
		return JUnitLaunchConfigurationConstants.ID_JUNIT_APPLICATION;
	}
	
	private ILaunchManager getLaunchManager() {
			return DebugPlugin.getDefault().getLaunchManager();
	}
	
	private List<ILaunchConfiguration> findExistingLaunchConfigurations(ILaunchConfigurationWorkingCopy temporary) throws CoreException {
		ILaunchConfigurationType configType= temporary.getType();

		ILaunchConfiguration[] configs= getLaunchManager().getLaunchConfigurations(configType);
		String[] attributeToCompare= getAttributeNamesToCompare();

		ArrayList<ILaunchConfiguration> candidateConfigs= new ArrayList<ILaunchConfiguration>(configs.length);
		for (ILaunchConfiguration config : configs) {
			if (hasSameAttributes(config, temporary, attributeToCompare)) {
				candidateConfigs.add(config);
			}
		}
		return candidateConfigs;
	}
	
	
	private static boolean hasSameAttributes(ILaunchConfiguration config1, ILaunchConfiguration config2, String[] attributeToCompare) {
		try {
			for (String element : attributeToCompare) {
				String val1= config1.getAttribute(element, EMPTY_STRING);
				String val2= config2.getAttribute(element, EMPTY_STRING);
				if (!val1.equals(val2)) {
					return false;
				}
			}
			return true;
		} catch (CoreException e) {
			// ignore access problems here, return false
		}
		return false;
	}
	
	protected String[] getAttributeNamesToCompare() {
			return new String[] {
			IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, JUnitLaunchConfigurationConstants.ATTR_TEST_CONTAINER,
				IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, JUnitLaunchConfigurationConstants.ATTR_TEST_METHOD_NAME
			};
	}
	
	
	private ILaunchConfiguration findExistingLaunchConfiguration(ILaunchConfigurationWorkingCopy temporary, String mode) throws InterruptedException, CoreException {
		List<ILaunchConfiguration> candidateConfigs= findExistingLaunchConfigurations(temporary);

		// If there are no existing configs associated with the IType, create
		// one.
		// If there is exactly one config associated with the IType, return it.
		// Otherwise, if there is more than one config associated with the
		// IType, prompt the
		// user to choose one.
		int candidateCount= candidateConfigs.size();
		if (candidateCount == 0) {
			return null;
		} else if (candidateCount == 1) {
			return candidateConfigs.get(0);
		} else {
			// Prompt the user to choose a config. A null result means the user
			// cancelled the dialog, in which case this method returns null,
			// since cancelling the dialog should also cancel launching
			// anything.
			ILaunchConfiguration config= chooseConfiguration(candidateConfigs, mode);
			if (config != null) {
				return config;
			}
		}
		return null;
	}

	private ILaunchConfiguration chooseConfiguration(List<ILaunchConfiguration> configList, String mode) throws InterruptedException {
		IDebugModelPresentation labelProvider= DebugUITools.newDebugModelPresentation();
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), labelProvider);
		dialog.setElements(configList.toArray());
		dialog.setTitle(JUnitMessages.JUnitLaunchShortcut_message_selectConfiguration);
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			dialog.setMessage(JUnitMessages.JUnitLaunchShortcut_message_selectDebugConfiguration);
		} else {
			dialog.setMessage(JUnitMessages.JUnitLaunchShortcut_message_selectRunConfiguration);
		}
		dialog.setMultipleSelection(false);
		int result= dialog.open();
		if (result == Window.OK) {
			return (ILaunchConfiguration) dialog.getFirstResult();
		}
		throw new InterruptedException(); // cancelled by user
	}

	private Shell getShell() {
		return JUnitPlugin.getActiveWorkbenchShell();
	}
	
	public static class TestKindRegistry {
		
		public static final String JUNIT3_TEST_KIND_ID= "org.eclipse.jdt.junit.loader.junit3"; 
		public static final String JUNIT4_TEST_KIND_ID= "org.eclipse.jdt.junit.loader.junit4"; 
		
		public static String getContainerTestKindId(IJavaElement element) {
					if (element != null) {
						IJavaProject project= element.getJavaProject();
						if (CoreTestSearchEngine.is50OrHigher(project) && CoreTestSearchEngine.hasTestAnnotation(project)) {
							return JUNIT4_TEST_KIND_ID;
						}
					}
					return JUNIT3_TEST_KIND_ID;
				}
	}
	
	private static class JUnitMessages{
		
		public static String JUnitLaunchShortcut_message_selectConfiguration;
		
		public static String JUnitLaunchShortcut_message_selectDebugConfiguration;
		
		public static String JUnitLaunchShortcut_message_selectRunConfiguration;
	}
	
	
	private static class CoreTestSearchEngine {
		
		public static boolean is50OrHigher(String compliance) {
			 return !isVersionLessThan(compliance, JavaCore.VERSION_1_5);
		}
	
		public static boolean is50OrHigher(IJavaProject project) {
			String source= project != null ? project.getOption(JavaCore.COMPILER_SOURCE, true) : JavaCore.getOption(JavaCore.COMPILER_SOURCE);
			return is50OrHigher(source);
		}
		
		public static boolean isVersionLessThan(String version1, String version2) {
			if (JavaCore.VERSION_CLDC_1_1.equals(version1)) {
				version1= JavaCore.VERSION_1_1 + 'a';
			}
			if (JavaCore.VERSION_CLDC_1_1.equals(version2)) {
				version2= JavaCore.VERSION_1_1 + 'a';
			}
			return version1.compareTo(version2) < 0;
		}
		
		public static boolean hasTestCaseType(IJavaProject javaProject) {
	 		try {
	 			return javaProject != null && javaProject.findType(JUnitCorePlugin.TEST_SUPERCLASS_NAME) != null;
	 		} catch (JavaModelException e) {
				// not available
			}
			return false;
		}
			
		public static boolean hasTestAnnotation(IJavaProject project) {
				try {
					return project != null && project.findType(JUnitCorePlugin.JUNIT4_ANNOTATION_NAME) != null;
				} catch (JavaModelException e) {
					// not available
				}
				return false;
		}
	}
	
	public static class JUnitCorePlugin {
		
		public static final String PLUGIN_ID= "org.eclipse.jdt.junit";
		
		public final static String JUNIT4_ANNOTATION_NAME= "org.junit.Test";
		
		public final static String TEST_SUPERCLASS_NAME= "junit.framework.TestCase";
		
		public static final String CORE_PLUGIN_ID= "org.eclipse.jdt.junit.core";
		
	}
	
	public static class JUnitPreferencesConstants{
		public static final String ENABLE_ASSERTIONS= JUnitCorePlugin.PLUGIN_ID + ".enable_assertions";
	}
	
	public static class JUnitLaunchConfigurationConstants{
		
		public static final String ID_JUNIT_APPLICATION= "org.eclipse.jdt.junit.launchconfig";
		
		public static final String ATTR_KEEPRUNNING = JUnitCorePlugin.PLUGIN_ID+ ".KEEPRUNNING_ATTR";
		
		public static final String ATTR_TEST_CONTAINER= JUnitCorePlugin.PLUGIN_ID+".CONTAINER"; 
		
		public static final String ATTR_TEST_RUNNER_KIND= JUnitCorePlugin.PLUGIN_ID+".TEST_KIND";
		
		public static final String ATTR_TEST_METHOD_NAME= JUnitCorePlugin.PLUGIN_ID+".TESTNAME";
		
	}

	public static class JUnitMigrationDelegate{
		public static void mapResources(ILaunchConfigurationWorkingCopy config) throws CoreException {
			IResource resource = getResource(config);
	 		if (resource == null) {
	 			config.setMappedResources(null);
	 		} else {
	 			config.setMappedResources(new IResource[]{resource});
	 		}
	 	}
		
		private static IResource getResource(ILaunchConfiguration config) throws CoreException {
			 		String projName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String)null);
			 		String containerHandle = config.getAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_CONTAINER, (String)null);
			 		String typeName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, (String)null);
					IJavaElement element = null;
					if (containerHandle != null && containerHandle.length() > 0) {
						element = JavaCore.create(containerHandle);
					} else if (projName != null && Path.ROOT.isValidSegment(projName)) {
						IJavaProject javaProject = getJavaModel().getJavaProject(projName);
						if (javaProject.exists()) {
							if (typeName != null && typeName.length() > 0) {
								element = javaProject.findType(typeName);
							}
							if (element == null) {
								element = javaProject;
							}
						} else {
							IProject project= javaProject.getProject();
							if (project.exists() && !project.isOpen()) {
								return project;
							}
						}
					}
					IResource resource = null;
					if (element != null) {
						resource = element.getResource();
					}
					return resource;
				}
		
		/*
		* Convenience method to get access to the java model.
		*/
		private static IJavaModel getJavaModel() {
			return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		}
	}
	
	
	public static class AssertionVMArg {
		
		private static final String SHORT_VM_ARG_TEXT = "-ea";
		
		public static void setArgDefault(ILaunchConfigurationWorkingCopy config) {
	 		String argText= getEnableAssertionsPreference() ? SHORT_VM_ARG_TEXT : ""; 
	 		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, argText);
		}	
		
		public static boolean getEnableAssertionsPreference() {
			return Platform.getPreferencesService().getBoolean(JUnitCorePlugin.CORE_PLUGIN_ID, JUnitPreferencesConstants.ENABLE_ASSERTIONS, false, null);
		}
	}

	/**
	 * A class to select elements out of a list of elements.
	 * 
	 * @since 2.0
	 */
	public static class ElementListSelectionDialog extends  AbstractElementListSelectionDialog  {

	    private Object[] fElements;

	    /**
	     * Creates a list selection dialog.
	     * @param parent   the parent widget.
	     * @param renderer the label renderer.
	     */
	    public ElementListSelectionDialog(Shell parent, ILabelProvider renderer) {
	        super(parent, renderer);
	    }

	    /**
	     * Sets the elements of the list.
	     * @param elements the elements of the list.
	     */
	    public void setElements(Object[] elements) {
	        fElements = elements;
	    }

	    /*
	     * @see SelectionStatusDialog#computeResult()
	     */
	   protected void computeResult() {
	        setResult(Arrays.asList(getSelectedElements()));
	    }

	    /*
	     * @see Dialog#createDialogArea(Composite)
	     */
	    protected Control createDialogArea(Composite parent) {
	        Composite contents = (Composite) super.createDialogArea(parent);
	        createMessageArea(contents);
	        createFilterText(contents);
	        createFilteredList(contents);
	        setListElements(fElements);
	        setSelection(getInitialElementSelections().toArray());
	        return contents;
	    }	
	}
		
	public static class JUnitPlugin {
				
		public static Shell getActiveWorkbenchShell() {
	 		return TestUtilitiesPlugin.getActiveWorkbenchShell();
	 	}
		
	 	public static IWorkbenchWindow getActiveWorkbenchWindow() {
	 		return TestUtilitiesPlugin.getActiveWorkbenchWindow();
	 	}
	 	
	 	public IWorkbench getWorkbench() {
	 	    return PlatformUI.getWorkbench();
	 	}
	}
}
