package com.alexbchr.testutilities.priorj.plugin.ui.wizards;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.alexbchr.testutilities.priorj.plugin.controllers.PriorJServices;
import com.alexbchr.testutilities.priorj.plugin.importer.ImportServices;
import com.alexbchr.testutilities.priorj.plugin.ui.PriorJUILog;

/**
 * 
 * Imported Page
 * 
 * @author Samuel T. C. Santos
 * @verion 1.0
 *
 */
public class ImportedWizardPage  extends WizardPage {
	
	private Group groupFrom;
	private Group groupTo;
	
	private Combo projectCombo;
	private Combo versionCombo;
	private Combo pkgCombo;
	
	private PriorJServices services;
	private ImportServices importer;
	
	protected ImportedWizardPage() {
		super("Import Suite Code");
		setTitle("Import Suite Code");
		setDescription("Importing the suite code");
	}

	@Override
	public void createControl(Composite parent) {
	 // TODO Auto-generated method stub
	 services = PriorJServices.getInstance();
	 importer = ImportServices.getInstance();
 
     String basepath = services.readLocalBase();
    	if (!basepath.equals("default")){
 	       try {
		    	importer.setFrom(basepath);
		    }
		    catch(Exception e){
			  PriorJUILog.logError(e);
		    }
	  }
 
	 setPageComplete(false);
	 Composite container = new Composite(parent, SWT.NULL);
	
	 groupFrom = new Group(container, SWT.SHADOW_ETCHED_IN);
	
	 setControl(container);
	 
	 FillLayout groupLayout = new FillLayout();
	 groupLayout.type = SWT.VERTICAL;
	 groupFrom.setLayout(groupLayout);
	 
	 container.setLayout(groupLayout);
	 		 
	// final Group	group1 = new Group(group, SWT.SHADOW_ETCHED_IN);
	 groupFrom.setText("From");
	 groupFrom.setLayout(new GridLayout(2, false));
	 
	
	//First Label
	 final Label projectLabel = new Label(groupFrom, SWT.NONE);
	 projectLabel.setText("Select a Project ");
	 
	 //First Combo
	 projectCombo = new Combo(groupFrom, SWT.SCROLL_LINE);
	 projectCombo.setVisibleItemCount(5);
	 projectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		 
	 fillProjectCombo(projectCombo);
	 
	 projectCombo.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent e) {
	       versionCombo.removeAll();
	       fillVersionCombo(versionCombo, projectCombo.getText());
	       pkgCombo.removeAll();
	       fillPackagesCombo(pkgCombo, projectCombo.getText());
	      }

	  });
	 

	//Second Label
	 final Label versionLabel = new Label(groupFrom, SWT.NONE);
	 versionLabel.setText("Select a Version ");
	 
	 //Second Combo
	 versionCombo = new Combo(groupFrom, SWT.SCROLL_LINE);
	 versionCombo.setVisibleItemCount(5);
	 versionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		 
	// fillCombo(versionCombo);
	 
	 	 
	 //Second Group
	 groupTo = new Group(container, SWT.SHADOW_ETCHED_IN);
	 
	 groupTo.setText("To");
	 groupTo.setLayout(new GridLayout(2, false));
	 
	//Third Label
	 final Label pkgLabel = new Label(groupTo, SWT.NONE);
	 pkgLabel.setText("Select a Package");
	 
	 //Third Combo
	 pkgCombo = new Combo(groupTo, SWT.SCROLL_LINE);
	 pkgCombo.setVisibleItemCount(5);
	 pkgCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		 
	 
	 pkgCombo.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				 updatePageComplete();     
			}

		});
	}
	
	private void updatePageComplete() {
		setPageComplete(false);
		
		if(pkgCombo.getText() == null)
			return;
		
		if (!pkgCombo.getText().isEmpty() &&
				!projectCombo.getText().isEmpty() && 
				!versionCombo.getText().isEmpty()){
			setPageComplete(true);
		}
		
	}

	private void fillPackagesCombo(Combo combo,String project) {
		// TODO Auto-generated method stub
		List<String> versions = importer.listPackagesFromProject(project);

		for (int i=0; i < versions.size(); i++){
			combo.add(versions.get(i));
		}
	
	}
	
	private void fillVersionCombo(Combo combo,String project) {
		// TODO Auto-generated method stub
		List<String> versions = importer.listVersionsTo(project);

		for (int i=0; i < versions.size(); i++){
			combo.add(versions.get(i));
		}
	
	}
	
	private void fillProjectCombo(Combo combo){
		List<String> projects = importer.listProjects();
		for (int i=0; i < projects.size(); i++){
			combo.add(projects.get(i));
		}
	}
	
	public String getTo(){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
		return 	root.getRawLocation().toOSString() + pkgCombo.getText();
	}

	public String getFrom(){
		return importer.getFrom()+File.separator+projectCombo.getText()+File.separator+versionCombo.getText();
	}
	
	public String getProject(){
		return projectCombo.getText();
	}
	
	public String getPackage(){
		return pkgCombo.getText();
	}
}
