package com.alexbchr.testutilities.priorj.core;

import com.java.io.JavaIO;

/**
 * This class is a replicator to a project.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class PathGenerator {
	
	/**
	 * Project duplicated (copy)
	 */
	private String target;
	private String currentFile;
	private String currentFolder;
	private String originFile;
	
	/**
	 * Project that is being duplicated.(original)
	 * 
	 */
	private String currentProject;
	
	private final String slash = JavaIO.SEPARATOR;
	
	public PathGenerator(){
		target = "";
		currentFile = "";
		currentFolder = "";
		originFile = "";
	}
	
	/**
	 *  Target is the project that will be duplicated.
	 *  
	 * @param projectName
	 */
	public void setTarget(String projectName) {
		this.target = projectName;
	}
	/**
	 * Getting the project target.
	 * 
	 * @return
	 */
	public String getTarget(){
		return target;
	}
	
	/**
	 * Set the current file to duplicate.
	 * 
	 * @param filePath
	 */
	public void setCurrentFile(String filePath) {
		this.currentFile = filePath;		
	}

	/**
	 * Getting the file that is being duplicated.
	 * 
	 * @return
	 */
	public String getCurrentFile() {
		return currentFile;
	}
	
		
	/**
	 * Convert a file path from current project to target project.
	 * 
	 */
	public void convertCurrentFilePath() {
		originFile = currentFile; //need save origin location.
		currentFile = replaceProjecName(currentFile);
	}
	
	/**
	 * Set the current project in the visitor.
	 * 
	 * @param projectName
	 */
	public void setCurrentProject(String projectName) {
		this.currentProject = projectName;
	}
	
	/**
	 * Getting the current project that is being visited.
	 * 
	 * @return
	 */
	public String getCurrentProject(){
		return currentProject;
	}

	/**
	 * Creating a folder duplicated in the target project.
	 * 
	 */
	public void duplicateCurrentFolder() {
		JavaIO.createFolder(currentFolder);
	}
	/**
	 * Change the project name in path.
	 * 
	 * @param path
	 */
	public String replaceProjecName(String path){
		String segments [] = JavaIO.getSegments(path);

		for (int i=0; i< segments.length; i++){
			if (segments[i].equals(currentProject)){
				segments[i] = target;
				break;
			}
		}
		
		String newPath ="";
		for (int i=0; i<segments.length;i++){
			newPath+=segments[i];
			if (i<segments.length-1)
				newPath+= slash;
		}
		return newPath;
	}
	
	public String getOriginalFile() {
		return originFile;
	}
	
}