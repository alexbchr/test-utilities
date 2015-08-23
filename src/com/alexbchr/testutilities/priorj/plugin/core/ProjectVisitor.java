package com.alexbchr.testutilities.priorj.plugin.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;

import com.java.io.JavaIO;

import core.InstrumentClass;

/**
 * This class is a visitor to locate all files in the project.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class ProjectVisitor implements IResourceProxyVisitor {
			
	/**
	 * Creating a new visitor with a new project to set resources.
	 * 
	 * @param newProjectName
	 */
	public ProjectVisitor (){
	
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
	/**
	 * This method hold a copy of each resource founded in the visitor walk.
	 *  
	 * @param resource
	 * @throws Exception 
	 */
	private void processResource(IResource resource) throws Exception {
		
		if ( resource instanceof IFile && resource.exists()){
			IFile file = (IFile) resource;
			routeLocationFile(file);
		}
	}
	
	/**
	 * Copy all files from current project to temporary project.
	 * 
	 * @param file
	 * @throws Exception 
	 */
	private void routeLocationFile(IFile file) throws Exception {
		String filePath = file.getLocation().toOSString();
		instrumentResource(filePath);
	}
	
	/**
	 * Instrumenting the current path resource.
	 * 
	 * @throws Exception
	 */
	public void instrumentResource(String currentFile) throws Exception{
		if (!Rule.isInterface(currentFile) && Rule.isJavaFile(currentFile)){
			String fileName = JavaIO.getLastSegment(currentFile);
			String path = JavaIO.removeLastSegment(currentFile);
			if (Rule.isClassCode(currentFile))
				instrumentResource(path, fileName, false);
			else
				instrumentResource(path,fileName, true);
		}
	}
	
	   /**
	    * Instrumenting a file.
	    * 
	    * @param path
	    *  the path to the java file.
	    *  
	    * @param fileName
	    *  the java file name.
	    *  
	    * @throws Exception
	    */
	   public void instrumentResource(String path,String fileName, boolean isTest) throws Exception {
			 InstrumentClass instrument = new InstrumentClass(path, fileName, isTest);
			 instrument.instrumentationRun();   
	   }
}
