package com.alexbchr.testutilities.priorj.plugin.ui.wizards;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.alexbchr.testutilities.priorj.plugin.controllers.PriorJServices;

/**
 * Local base Path Wizard Page configuration.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 *
 */
public class StorageWizardPage extends WizardPage {

	private Group group;
	private Text locationText;
	private Button btnSelectDir;
	
	protected StorageWizardPage() {
		super("Configure Local Storage Path");
		// TODO Auto-generated constructor stub
		setTitle("Setting a new Path to Local Storage");
		
		setDescription("The Plugin will save prioritization artifacts in this place!");
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		setPageComplete(false);
		Composite container = new Composite(parent, SWT.NULL);
		
		//First Group
		group = new Group(container, SWT.SHADOW_ETCHED_IN);
		 
		setControl(container);
		
		 FillLayout groupLayout = new FillLayout();
		 groupLayout.type = SWT.VERTICAL;
		 group.setLayout(groupLayout);
		 
		 container.setLayout(groupLayout);
		 
		 group.setText("Local Path to Save Artifacts");
		 group.setLayout(new GridLayout(2, false));
		
		
		//Text and label for version name
		createButtonChooseDir();
		 
		locationText = new Text(group, SWT.BORDER);
		locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		checkStorageValues();
		
		locationText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				 updatePageComplete();     
			}

		});
	}
	
	private void checkStorageValues() {
		// TODO Auto-generated method stub
	    PriorJServices services = PriorJServices.getInstance();
		String basepath = services.readLocalBase();
		if (!basepath.equals("default")){
			locationText.setText(basepath);
		}
	}

	private void createButtonChooseDir(){
		 btnSelectDir = new Button(group, SWT.PUSH);
		 Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
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
	
	private void updatePageComplete() {
		setPageComplete(false);
		
		if(locationText.getText() == null)
			return;
		
		if (!locationText.getText().isEmpty()){
			setPageComplete(true);
		}
	}
	
	//Getter and Setters
	public void setCurrentLocation(String location){
		locationText.setText(location);
	}
	
	public String getCurrentLocation(){
		return locationText.getText();
	}
	
}
