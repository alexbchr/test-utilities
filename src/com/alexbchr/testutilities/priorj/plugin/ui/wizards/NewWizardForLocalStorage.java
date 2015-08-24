package com.alexbchr.testutilities.priorj.plugin.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.alexbchr.testutilities.TestUtilitiesPlugin;
import com.alexbchr.testutilities.priorj.plugin.controllers.PriorJServices;
import com.alexbchr.testutilities.priorj.plugin.ui.PriorJUILog;

public class NewWizardForLocalStorage extends Wizard implements INewWizard {

	private StorageWizardPage storagePage;
	
	public NewWizardForLocalStorage() {
		// TODO Auto-generated constructor stub		
		IDialogSettings instrumentationSettings = TestUtilitiesPlugin.getDefault().getDialogSettings();

		IDialogSettings wizardSettings = instrumentationSettings.getSection("storagePageWizard");

		if (wizardSettings == null )
			wizardSettings = instrumentationSettings.addNewSection("storagePageWizard");
	
		setDialogSettings(instrumentationSettings);			
	}
	
	public void addPages(){
		setWindowTitle("Configure Local Storage Path");
		storagePage = new StorageWizardPage();
		
		addPage(storagePage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		
		final String location = storagePage.getCurrentLocation();
				
		try {
			getContainer().run(true, true, new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					performOperation(location, monitor);
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
	
	private void performOperation(String location, IProgressMonitor monitor) throws InterruptedException {
		// TODO Auto-generated method stub
		 monitor.beginTask("Saving Selected location to Preferences",IProgressMonitor.UNKNOWN); 
		 boolean toDo = true;
		 while (toDo) {
			 PriorJServices services = PriorJServices.getInstance();
			 try {
				services.setLocalBasePath(location);
				services.saveLocalBase(location); //saving to eclipse preferences
				toDo = false;
			 } catch (Exception e) {
				// TODO Auto-generated catch block
				PriorJUILog.logError(e);
			}
		     			
			 if (monitor.isCanceled()) 
				 throw new InterruptedException("Canceled by user");
			 monitor.worked(1);   
		}  
	 	monitor.done();
	}
	

}
