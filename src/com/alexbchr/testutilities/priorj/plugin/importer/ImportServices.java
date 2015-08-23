package com.alexbchr.testutilities.priorj.plugin.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.alexbchr.testutilities.priorj.plugin.controllers.PriorJServices;
import com.java.io.JavaIO;

/**
 * This class realize the import of Suite Java to selected package.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class ImportServices {
	
	private static ImportServices importer;
	
	private String from;
	private String to;
	private String packageDeclaration;
	private String project;
	
	private ImportServices(){
		from = "";
		to = "";
		packageDeclaration = "";
		project = "";
	}
	
	/**
	 * Getting an instance of this importer.
	 * 
	 * @return
	 */
	public static ImportServices getInstance(){
		if (importer == null)
			importer = new ImportServices();
		return importer;
	}

	public void setFrom(String from){
		this.from = from;
	}
	
	public String getFrom(){
		return from;
	}
	
	public void setTo(String to){
		this.to = to;
	}
	
	public String getTo (){
		return to;
	}
	
	public void setPackageDeclaration(String packageDeclaration){
		this.packageDeclaration = packageDeclaration;
	}
	
	public void setProject(String project){
		this.project = project;
	}
	
	public List<String> listProjects(){
		List<String> projects = new ArrayList<String>();
		
		if (!from.isEmpty()){
			File dir = new File(from);
			File [] files = dir.listFiles();
			for (File file : files){
				if (file.isDirectory()){
					projects.add(file.getName());
				}
			}
		}
		
		return projects;
	}

	public List<String> listVersionsTo(String project) {
		// TODO Auto-generated method stub
		List<String> versions = new ArrayList<String>();
		
		if (!from.isEmpty()){
			File dir = new File(from);
			File [] files = dir.listFiles();
			for (File file : files){
				if (file.isDirectory() && file.getName().equals(project)){
				
					File [] subDirs = file.listFiles();
					for (File f  : subDirs){
						if (f.isDirectory()){
							versions.add(f.getName());
						}
					}
					break;
				}
			}
		}
		
		return versions;
	}
	
	public List<String> listPackagesFromProject(String project){
		List<String> packagesList = new ArrayList<String>();
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject [] projects = root.getProjects();
		
	    for (int i=0; i < projects.length; i++){
			String proj = projects[i].getName();
			if (proj.equals(project)){
				IJavaProject javaProject = JavaCore.create(projects[i]);
				IPackageFragment[] packages;
				try {
					packages = javaProject.getPackageFragments();
					for (IPackageFragment mypackage : packages) {     
					  if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
					   packagesList.add( mypackage.getPath().toOSString());
					  }
					}	
				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	    
	    return packagesList;
	}
	
	public void performImport() throws CoreException{
		if (!to.isEmpty() && !from.isEmpty()){
			File file = new File(from);
			File [] files = file.listFiles();
			List<String> units = new ArrayList<String>();
			for (File f : files){
				if (f.getName().endsWith(".java")){
					
					String location = f.getAbsolutePath();
					String fileName = f.getName();
					try {
						units.add(f.getName());
						JavaIO.copy(location, to+File.separator+fileName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			refreshProject(project);
			changePackageDeclaration(units);
		}
	}
	
	private void changePackageDeclaration(List<String> units){
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject [] projects = root.getProjects();
		
	    for (int i=0; i < projects.length; i++){
			String proj = projects[i].getName();
			if (proj.equals(project)){
				IJavaProject javaProject = JavaCore.create(projects[i]);
				IPackageFragment[] packages;
				try {
					packages = javaProject.getPackageFragments();
					for (IPackageFragment mypackage : packages) {     
					  if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
						   for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
							   if (units.contains(unit.getElementName())){
							      unit.createPackageDeclaration(mypackage.getElementName(), null);     
							   }
							}
					  }
					}
					System.out.println(units);
				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	    
	}
	
	public  void refreshProject(String project) throws CoreException{
		PriorJServices services = PriorJServices.getInstance();
		services.refreshProject(project);
	}
}
