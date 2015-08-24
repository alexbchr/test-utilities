package com.alexbchr.testutilities.priorj.plugin.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.alexbchr.testutilities.priorj.plugin.controllers.PriorJServices;
/**
 * Instrumentation Wizard Page.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 *
 */
public class SelectionInstrumentationWizardPage extends WizardPage {

	private Combo projectCombo;
	private Combo projectOldCombo;
	
	private Text versionText;
	
	private PriorJServices services;
	
	protected SelectionInstrumentationWizardPage() {
		// TODO Auto-generated constructor stub
		super("New Instrumentation");
		setTitle("New Instrumentation");
		setDescription("Creating a new Instrumentation in the Selected Project");
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		 services = PriorJServices.getInstance();
		 setPageComplete(false);
		 Composite container = new Composite(parent, SWT.NULL);
		
		 final Group group = new Group(container, SWT.SHADOW_ETCHED_IN);
		
		 setControl(container);
		 
		 FillLayout groupLayout = new FillLayout();
		 groupLayout.type = SWT.VERTICAL;
		 group.setLayout(groupLayout);
		 
		 container.setLayout(groupLayout);
		 		 
		// final Group	group1 = new Group(group, SWT.SHADOW_ETCHED_IN);
		 group.setText("Instrumentation");
		 group.setLayout(new GridLayout(2, false));
		 
		 //First Label
		 final Label projectLabel = new Label(group, SWT.NONE);
		 projectLabel.setText("Select a Project");
		 
		 //First Combo
		 projectCombo = new Combo(group, SWT.SCROLL_LINE);
		 projectCombo.setVisibleItemCount(5);
		 projectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		 
		 fillCombo(projectCombo);
		 
		 
		 //Second label 
		 final Label projectOldLabel = new Label(group, SWT.NONE);
		 projectOldLabel.setText("Select Old Version");
		 
		 //Second Combo
		 projectOldCombo = new Combo(group, SWT.SCROLL_LINE);
		 projectOldCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		 projectOldCombo.setVisibleItemCount(5);
		 fillCombo(projectOldCombo);
		 
		 //Text and label for version name
		 final Label versionLabel = new Label(group, SWT.NONE);
		 versionLabel.setText("Version Name");
		 
		 versionText = new Text(group, SWT.BORDER);
		 versionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				 
		 versionText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				 updatePageComplete();     
			}

		});
	}
	
	private void fillCombo(Combo combo){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject [] projects = root.getProjects();
		for (int i=0; i < projects.length; i++){
			combo.add(projects[i].getName());
		}
	}
	
	private void updatePageComplete() {
		setPageComplete(false);
		
		if(versionText.getText() == null)
			return;
		
		if (!versionText.getText().isEmpty()){
			setPageComplete(true);
		}
	}
	
	public String getProjectName(){
		return  projectCombo.getText();
	}
	
	public String getOldProjectName(){
		return projectOldCombo.getText();
	}
	
	public String getVersionName(){
		return versionText.getText();
	}

}
