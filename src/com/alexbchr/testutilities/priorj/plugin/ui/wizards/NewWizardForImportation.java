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
import com.alexbchr.testutilities.priorj.plugin.importer.ImportServices;
import com.alexbchr.testutilities.priorj.plugin.ui.PriorJUILog;

public class NewWizardForImportation extends Wizard implements INewWizard {

	private ImportedWizardPage importPage;
	private ImportServices importer;
	
	public NewWizardForImportation() {
		// TODO Auto-generated constructor stub
		IDialogSettings instrumentationSettings = TestUtilitiesPlugin.getDefault().getDialogSettings();

		IDialogSettings wizardSettings = instrumentationSettings.getSection("importPageWizard");

		if (wizardSettings == null )
			wizardSettings = instrumentationSettings.addNewSection("importPageWizard");
	
		setDialogSettings(instrumentationSettings);
	}
	
	public void addPages(){
		setWindowTitle("Import");
		importPage = new ImportedWizardPage();
		
		addPage(importPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		importer = ImportServices.getInstance();
		
		importer.setFrom(importPage.getFrom());
		importer.setTo(importPage.getTo());
		importer.setPackageDeclaration(importPage.getPackage());
		importer.setProject(importPage.getProject());
		
		final String project = importPage.getProject();
		
		try {
			getContainer().run(true, true, new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					performOperation(project, monitor);
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
	
	private void performOperation(String project, IProgressMonitor monitor) throws InterruptedException {
		// TODO Auto-generated method stub
		 monitor.beginTask("Importing suites...",IProgressMonitor.UNKNOWN); 
		 boolean toDo = true;
		 while (toDo) {
			// PriorJServices services = PriorJServices.getInstance();
			 try {
				
				 importer.performImport();
				 //importer.refreshProject(project);
				
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
