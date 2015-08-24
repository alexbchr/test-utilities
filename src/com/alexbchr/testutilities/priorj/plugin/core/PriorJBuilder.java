package com.alexbchr.testutilities.priorj.plugin.core;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * PriorJBuild class.
 * 
 * @author  Samuel T. C. Santos
 * 
 */
public class PriorJBuilder extends IncrementalProjectBuilder {

	public static final String ID = "com.alexbchr.testutilities.priorj.plugin.PriorJBuilder";
	
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD){
			fullBuild(getProject(), monitor);
		}
		else{
			incrementalBuild(getProject(), monitor, getDelta(getProject()));
		}
		return null;
	}
	
	public void incrementalBuild(IProject project, IProgressMonitor monitor, IResourceDelta delta) throws CoreException {
		if (delta == null ){
			fullBuild(project, monitor);
		}
		else{
			// do nothing 
			//delta.accept(null);
		}
	}
	
	public void fullBuild(IProject project , IProgressMonitor monitor) throws CoreException {
		project.accept(new ProjectVisitor(), IResource.NONE);
	}

}
