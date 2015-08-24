package com.alexbchr.testutilities.priorj.plugin.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.alexbchr.testutilities.TestUtilitiesPlugin;
import com.alexbchr.testutilities.priorj.plugin.controllers.PriorJServices;
import com.alexbchr.testutilities.priorj.plugin.models.PrioritizationItem;
import com.alexbchr.testutilities.priorj.plugin.ui.PriorJUILog;
import com.alexbchr.testutilities.priorj.plugin.ui.models.InstrumentationManager;
import com.alexbchr.testutilities.priorj.plugin.ui.models.PrioritizationManager;

public class NewWizardForPrioritization extends Wizard implements INewWizard {

	private PrioritizationWizardPage prioritizationPage;
	
	public NewWizardForPrioritization() {
		// TODO Auto-generated constructor stub
		IDialogSettings instrumentationSettings = TestUtilitiesPlugin.getDefault().getDialogSettings();

		IDialogSettings wizardSettings = instrumentationSettings.getSection("prioritizationPageWizard");

		if (wizardSettings == null )
			wizardSettings = instrumentationSettings.addNewSection("prioritizationPageWizard");
			
		setDialogSettings(instrumentationSettings);
	}
	
	public void addPages(){
		setWindowTitle("New Prioritization");
		prioritizationPage = new PrioritizationWizardPage();
		addPage(prioritizationPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		final String version = prioritizationPage.getInstrumentedVersionName();
		final String location = prioritizationPage.getLocation();
		
		//Retrieve project name for this version
		InstrumentationManager manager = InstrumentationManager.getInstance();
		String project = manager.getFromPreferences(version);
		
		List<Integer> techniques = prioritizationPage.getTechniques();
		int selectionSize = prioritizationPage.getSelectionSize();
		boolean isJUnitVersion3 = prioritizationPage.isFrameworkJUnit3Selected();
		
		final PrioritizationItem item = new PrioritizationItem(project, version,location, techniques,isJUnitVersion3, selectionSize);
				
		try {
			getContainer().run(true, true, new IRunnableWithProgress(){
				
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					performOperation(item, monitor);
				}
			});
		}
		catch (InvocationTargetException e){
			PriorJUILog.logError(e);
			return false;
		}
		catch (InterruptedException e){
			//User canceled, so stop but don't close wizard.
			return false;
		}
		
		return true;
	}

	private void performOperation(PrioritizationItem item,  IProgressMonitor monitor) throws InterruptedException {
		// TODO Auto-generated method stub
		 // Task 1
		 monitor.beginTask("Creating new Prioritization",IProgressMonitor.UNKNOWN); 
		 boolean toDo = true;
		 while (toDo) {
			 
			 PrioritizationManager manager = PrioritizationManager.getManager();
			 manager.addPrioritization(item);
			 
			 toDo = false;
			 if (monitor.isCanceled()) 
				 throw new InterruptedException("Canceled by user");
			 monitor.worked(1);   
		}  
		// Task 2
		monitor.beginTask("Moving Coverage File", IProgressMonitor.UNKNOWN);
	        
	    toDo = true;
	    PriorJServices services = PriorJServices.getInstance();
	    
	    String basepath = services.readLocalBase();
		if (!basepath.equals("default")){
			try {
				services.setLocalBasePath(basepath);
				services.createCurrentProjectVersion(item.getProjectName(), item.getVersionName());
				
				for (Integer technique : item.getTechniques())
					services.addTechnique(technique);
				
				while (toDo) {
		          if (monitor.isCanceled())
		        	  throw new InterruptedException("Canceled by user");
		          
		          services.moveCoverageFileToLocalBase();
		          toDo = false;
		          monitor.worked(1);
		        }
				
				 //Task 3
				monitor.beginTask("Writing finisher in Coverage File", IProgressMonitor.UNKNOWN);
		        toDo = true;
		        while (toDo) {
		          if (monitor.isCanceled())
		        	  throw new InterruptedException("Canceled by user");
		        	  services.writeXMLFinisher();
		        	  toDo = false;
		        	  monitor.worked(1);
		         }
		        
		        //Task 4
		        monitor.beginTask("Removing Copy Project", IProgressMonitor.UNKNOWN);
		        toDo = true;
		        while (toDo) {
		          if (monitor.isCanceled())
		        	  throw new InterruptedException("Canceled by user");
		        	  services.deleteTemporaryProject();
		        	  toDo = false;
		          monitor.worked(1);
		        }
		        //Task 5
		        monitor.beginTask("Searching JUnit Report and saving Failures...", IProgressMonitor.UNKNOWN);
		        toDo = true;
		        while (toDo) {
		          if (monitor.isCanceled())
		        	  throw new InterruptedException("Canceled by user");
		         
	        	  String result = services.searchJUnitReport();
	        	  if (!result.equals("fileNotFound")){
	        		  List<String> failures = services.getFailuresFromJUnitReport(result);
	        		  String scriptCode = services.createFailureScript(failures); 
	        		  services.saveFailures(scriptCode);
	        	  }
	        	  toDo = false;
		          
		          monitor.worked(1);
		        }
		        
		        //Task 6
		        monitor.beginTask("Wait, Making Prioritization and Saving Results...", IProgressMonitor.UNKNOWN);
		        toDo = true;
		        while (toDo) {
		          if (monitor.isCanceled())
		        	  throw new InterruptedException("Canceled by user");
	         
	        	  services.setFrameworkVersion(item.isJUnit3());
	        	  services.prioritizeAll(item.getSelectionSize());
	        	  toDo = false;
	         
		          monitor.worked(1);
		        }
		        //Task 7
		        monitor.beginTask("Wait, Generating Coverage Report...", IProgressMonitor.UNKNOWN);
		        toDo = true;
		        while (toDo) {
		          if (monitor.isCanceled())
		        	  throw new InterruptedException("Canceled by user");
		          			        	  
	        	  String txtReport = services.createCoverageReport();
	        	  services.saveCoverageReport(txtReport);
		          toDo = false;
		          monitor.worked(1);
		        }
		        
		        monitor.beginTask("Wait, Generating Code Tree...", IProgressMonitor.UNKNOWN);
		        toDo = true;
		        while (toDo) {
		          if (monitor.isCanceled())
		        	  throw new InterruptedException("Canceled by user");
		            
	        	  services.createTableForTrace();
	        	  services.saveRoutes();
	        	 
//	        	  if (true){
	        	  services.openBrowser("report.html");
//	        	  }
//	        	  else{
//	        		  services.openFileInEditor("report.html");
//	        	  }
				 
		          toDo = false;
		          monitor.worked(1);
		        }
				
			}
			catch (Exception e){
				PriorJUILog.logError(e);
			}
		}
	    
	 	monitor.done();
	}

}
