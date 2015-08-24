package com.alexbchr.testutilities.priorj.plugin.ui.wizards;

import java.util.ArrayList;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.alexbchr.testutilities.priorj.plugin.controllers.PriorJServices;
import com.alexbchr.testutilities.priorj.plugin.ui.PriorJUILog;
import com.alexbchr.testutilities.priorj.plugin.ui.models.TechniqueType;


/**
 * Wizard for Instrumentation.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class PrioritizationWizardPage extends WizardPage {
	
	//My Groups
	Group group; 
	Group groupCheckboxes;
	Group groupSettings;
	Group groupSelection;

	//My combos
	private Combo projectCombo;
	private Combo projectOldCombo;
	private Text locationText;
	
	//My Labels
	private Label projectOldLabel;
	private Label projectLabel;
	private Label suiteSizeLabel;
	
	//My check boxes
	private Button btnCbTMC;
	private Button btnCbTSC;
	private Button btnCbAMC;
	private Button btnCbASC;
	private Button btnCbRND;
	private Button btnCbCB;
	private Button btnJUnit3;
	private Button btnJUnit4;
	private Button btnSelectDir;

	//Spinner and default size for suite selection
	private Spinner spinner;
	private int suiteSize = 10;
	
	//Control selected techniques
	List<String> added;
	
	//flags
	private boolean defaultJUnitVersion3 = true;
	
	protected PrioritizationWizardPage() {
		super("New Prioritization");
		// TODO Auto-generated constructor stub
		setTitle("Creating a New Prioritization");
		// TODO Auto-generated constructor stub
		setDescription("Creating a new Prioritization using an Instrumented Project");
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		 //services = new PriorJServices();
		 PriorJServices services = PriorJServices.getInstance();
		    
	     String basepath = services.readLocalBase();
		
		 added = new ArrayList<String>();
		 
		 setPageComplete(false);
		 Composite container = new Composite(parent, SWT.NULL);
		
		 //First Group
		 group = new Group(container, SWT.SHADOW_ETCHED_IN);
		
		 setControl(container);
		 
		 FillLayout groupLayout = new FillLayout();
		 groupLayout.type = SWT.VERTICAL;
		 group.setLayout(groupLayout);
		 
		 container.setLayout(groupLayout);
		 		 
		// final Group	group1 = new Group(group, SWT.SHADOW_ETCHED_IN);
		 group.setText("Instrumentation");
		 group.setLayout(new GridLayout(2, false));
		 
		 //First Label
		 projectLabel = new Label(group, SWT.NONE);
		 projectLabel.setText("Instrumented Project");
		 
		 //First Combo
		 projectCombo = new Combo(group, SWT.SCROLL_LINE);
		 projectCombo.setVisibleItemCount(5);
		 projectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		 
		 fillCombo(projectCombo);
		 //Second label 
		 projectOldLabel = new Label(group, SWT.NONE);
		 projectOldLabel.setText("Old Version");
		 
		 //Second Combo
		 projectOldCombo = new Combo(group, SWT.SCROLL_LINE);
		 projectOldCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		 projectOldCombo.setVisibleItemCount(5);
		 fillCombo(projectOldCombo);
		 projectOldCombo.setEnabled(false);
		 projectOldLabel.setEnabled(false);
		 
		 //Text and label for version name
		 createButtonChooseDir();
		 
		 locationText = new Text(group, SWT.BORDER);
		 locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		 if (!basepath.equals("default")){
			try {
				services.setLocalBasePath(basepath);
				locationText.setText(basepath);
				btnSelectDir.setEnabled(false);
			}
			catch(Exception e){
				PriorJUILog.logError(e);
			}
		 }
		 
		 projectCombo.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				 updatePageComplete();     
			}

		});
		 		 
		 //Second Group
		 groupCheckboxes = new Group(container, SWT.SHADOW_ETCHED_IN);
		 groupCheckboxes.setText("Techniques");
		 groupCheckboxes.setLayout(new GridLayout(2, false));
		 
		 addCheckButtons(); 
	
		 //Third Group
		 groupSettings = new Group(container, SWT.SHADOW_ETCHED_IN);
		 groupSettings.setText("Suite Code");
		 groupSettings.setLayout(new GridLayout(2, false));
		 
		 addJUnitButtons();
		 
		 //Fourth Group
		 groupSelection = new Group(container, SWT.SHADOW_ETCHED_IN);
		 groupSelection.setText("Apply Selection");
		 groupSelection.setLayout(new GridLayout(2, false));
		 addSpinerIntoGroup();	 
	}
	
	private void addJUnitButtons() {
		// TODO Auto-generated method stub
		createButtonJUnit3();
		createButtonJUnit4();
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
		
		if(projectCombo.getText() == null)
			return;
		
		if (!projectCombo.getText().isEmpty()){
			setPageComplete(true);
		}
	}
	
	private void addCheckButtons() {
		createButtonAMC();		
		createButtonASC();
		createButtonTMC();		
		createButtonTSC();
		createButtonRND();
		createButtonCB();
	}
	
	private void createButtonCB() {
		btnCbCB = new Button(groupCheckboxes, SWT.CHECK);
		btnCbCB.setText("Change Blocks (CB)");
		
		btnCbCB.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (e.getSource() == btnCbCB ){
					if (btnCbCB.getSelection() == true){
						added.add("CB");
					}
					else{
						added.remove("CB");
					}
				}
			}
		});
	}

	private void createButtonRND() {
		btnCbRND = new Button(groupCheckboxes, SWT.CHECK);
		btnCbRND.setText("Random (RND)");

		added.add("RND");
		btnCbRND.setSelection(true);

		btnCbRND.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (e.getSource() == btnCbRND ){
					if (btnCbRND.getSelection() == true){
						added.add("RND");
					}
					else{
						added.remove("RND");
					}
				}
			}
		});
	}
	
	
	private void createButtonTSC() {
		btnCbTSC = new Button(groupCheckboxes, SWT.CHECK);
		btnCbTSC.setText("Total Statement Coverage (TSC)");

		added.add("TSC");
		btnCbTSC.setSelection(true);
		
		btnCbTSC.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){

				if (e.getSource() == btnCbTSC ){
					if (btnCbTSC.getSelection() == true){
						added.add("TSC");
					}
					else{
						added.remove("TSC");
					}
				}
			}
		});
	}

	private void createButtonTMC() {
		btnCbTMC = new Button(groupCheckboxes, SWT.CHECK);
		btnCbTMC.setText("Total Method Coverage (TMC)");

		added.add("TMC");
		btnCbTMC.setSelection(true);

		btnCbTMC.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (e.getSource() == btnCbTMC ){
					if (btnCbTMC.getSelection() == true){
						added.add("TMC");
					}
					else{
						added.remove("TMC");
					}
				}
			}
		});
	}

	private void createButtonASC() {
		btnCbASC = new Button(groupCheckboxes, SWT.CHECK);
		btnCbASC.setText("Additional Statement Coverage (ASC)");

		added.add("ASC");
		btnCbASC.setSelection(true);
		btnCbASC.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){

				if (e.getSource() == btnCbASC ){
					if(btnCbASC.getSelection() == true){
						added.add("ASC");
					}
					else{
						added.remove("ASC");
					}
				}
			}
		});
	}

	private void createButtonAMC() {
		btnCbAMC = new Button(groupCheckboxes, SWT.CHECK);
		btnCbAMC.setText("Additional Method Coverage (AMC)");

		added.add("AMC");
		btnCbAMC.setSelection(true);

		btnCbAMC.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (e.getSource() == btnCbAMC ){
					if(btnCbAMC.getSelection() == true){
						added.add("AMC");
					}
					else{
						added.remove("AMC");
					}
				}
			}
		});
	}
	
	private void createButtonJUnit3() {
		btnJUnit3 = new Button(groupSettings, SWT.CHECK);
		btnJUnit3.setText("Suite for JUnit 3.x");
		btnJUnit3.setSelection(true);
		btnJUnit3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (e.getSource() == btnJUnit3 ){
					if (btnJUnit3.getSelection() == true){
						defaultJUnitVersion3 = true;
						btnJUnit4.setSelection(false);
					}
					else{
						defaultJUnitVersion3 = false;
					}
				}
			}
		});
	}

	private void createButtonJUnit4() {
		btnJUnit4 = new Button(groupSettings, SWT.CHECK);
		btnJUnit4.setText("Suite for JUnit 4.x");
		btnJUnit4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (e.getSource() == btnJUnit4 ){
					if (btnJUnit4.getSelection() == true){
						defaultJUnitVersion3 = false;
						btnJUnit3.setSelection(false);
					}
					else{
						defaultJUnitVersion3 = true;
					}
				}
			}
		});
	}
	
	private void addSpinerIntoGroup(){
		suiteSizeLabel = new Label(groupSelection, SWT.NONE);
		suiteSizeLabel.setText("Select the size of Suite");
		spinner = new Spinner(groupSelection, SWT.NONE);
		spinner.setMinimum(10);
		spinner.setIncrement(10);
		spinner.setMaximum(100);

		spinner.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent e) {
	    	  if (e.getSource() == spinner ){
	    		  suiteSize = spinner.getSelection();
	    	  }
	      }
	    });
	}
	
	private void createButtonChooseDir(){
		 btnSelectDir = new Button(group, SWT.PUSH);
		 Image image = PlatformUI.getWorkbench().getSharedImages()
				                  .getImage(ISharedImages.IMG_OBJ_FOLDER);
	     btnSelectDir.setImage(image);
	     btnSelectDir.setText("Browser...");
	    
		 btnSelectDir.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  if (event.getSource() == btnSelectDir ){
			        DirectoryDialog directoryDialog = new DirectoryDialog(group.getShell());
			        directoryDialog.setMessage("Please select a directory to save generated artifacts!");
			        String dir = directoryDialog.open();
			        if(dir != null) {
			         try {
						locationText.setText(dir);
					    } catch (Exception e) {
						e.printStackTrace();
					  }
		           }
		        }
		      }
		    }); 
	 }

	
	//Getters 
	public String getInstrumentedVersionName(){
		return  projectCombo.getText();
	}
	
	public String getInstrumentedOldVersionName(){
		return projectOldCombo.getText();
	}
	
	public String getLocation(){
		return locationText.getText();
	}
	
	public List<Integer> getTechniques(){
		List<Integer> techniques = new ArrayList<Integer>();
		for (String technique : added){
			if (technique.equals("CB"))
				if (!techniques.contains(TechniqueType.CB))
					techniques.add(TechniqueType.CB);
			if (technique.equals("AMC"));
				if (!techniques.contains(TechniqueType.AMC))
					techniques.add(TechniqueType.AMC);
			if (technique.equals("ASC"));
				if (!techniques.contains(TechniqueType.ASC))
					techniques.add(TechniqueType.ASC);
			if (technique.equals("TMC"));
				if (!techniques.contains(TechniqueType.TMC))
					techniques.add(TechniqueType.TMC);
			if (technique.equals("TSC"));
				if (!techniques.contains(TechniqueType.TSC))
					techniques.add(TechniqueType.TSC);
					
			if (technique.equals("RND"));
				if (!techniques.contains(TechniqueType.RND))
					techniques.add(TechniqueType.RND);
		}
		
		return techniques;
	}
	
	public boolean isFrameworkJUnit3Selected(){
		return defaultJUnitVersion3;
	}
	
	public int getSelectionSize(){
		return suiteSize;
	}

}
