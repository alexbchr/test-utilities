package com.alexbchr.testutilities.priorj.plugin.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;

import com.java.io.JavaIO;

import core.Difference;

/**
 * This visitor check the diferences between version project.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class DifferenceVisitor implements IResourceProxyVisitor  {

	private List<String> differences;
	private String projectName;
	private PathGenerator pathGenerator;
	
	/**
	 * Checking difference between Projects.
	 * 
	 * @param projectName
	 *   Old Version project.
	 *   
	 */
	public DifferenceVisitor(String projectName){
		this.projectName = projectName;
		this.differences = new ArrayList<String>();
		this.pathGenerator = new PathGenerator();
	}
	
	
	@Override
	public boolean visit(IResourceProxy proxy) throws CoreException {
		String name = proxy.getName();
		if (name != null){
			try {
				processResource(proxy.requestResource());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private void processResource(IResource resource) {
		if ( resource instanceof IFile && resource.exists()){
			IFile file = (IFile) resource;
			checkFile(file);
		}
	}

	private void checkFile(IFile file) {
		String newVersionProjectName = file.getProject().getName();
		String filePath = file.getLocation().toOSString();
		pathGenerator.setTarget(projectName);
		pathGenerator.setCurrentProject(newVersionProjectName);
		pathGenerator.convertCurrentFilePath();
		String oldFilePath = pathGenerator.getCurrentFile();
		if(JavaIO.exist(filePath) && JavaIO.exist(oldFilePath)){
			if (!Rule.isInterface(oldFilePath) && Rule.isClassCode(oldFilePath)){
				checkingDifferences(filePath, oldFilePath);
				checkingDifferences(oldFilePath,filePath);
			}
    	}
	}

	/**
	 * Checking the files.
	 * 
	 * @param filePath
	 * @param oldFilePath
	 */
	private void checkingDifferences(String filePath, String oldFilePath) {
		Difference difference = new Difference(filePath, oldFilePath);
		 difference.diff();
		 List<String> affected = difference.getStatementsDiff();
		 if (!affected.isEmpty()) {
		     differences.addAll(affected);
		 }
	}

	/**
	 * Getting differences checked by visitor.
	 * 
	 * @return
	 */
	public List<String> getDifferences() {
		return differences;
	}

}
