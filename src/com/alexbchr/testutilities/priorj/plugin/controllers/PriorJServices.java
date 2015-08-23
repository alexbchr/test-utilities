package com.alexbchr.testutilities.priorj.plugin.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import manager.Coverage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.PartInitException;

import com.alexbchr.testutilities.priorj.report.GenerateTreeData;
import com.alexbchr.testutilities.priorj.technique.EmptySetOfTestCaseException;
import com.alexbchr.testutilities.priorj.technique.Technique;
import com.alexbchr.testutilities.priorj.technique.TechniqueCreator;

import com.alexbchr.testutilities.priorj.plugin.core.AJDTHandler;
import com.alexbchr.testutilities.priorj.plugin.core.DifferenceVisitor;
import com.alexbchr.testutilities.priorj.plugin.core.JUnitLaunch;
import com.alexbchr.testutilities.priorj.plugin.core.JUnitReportFailures;
import com.java.io.JavaIO;

import com.alexbchr.testutilities.priorj.controller.DataManager;
import com.alexbchr.testutilities.priorj.controller.PriorJ;
import coverage.TestCase;
import coverage.TestSuite;

/**
 * PriorJ Controller is an controller to plugin tasks.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class PriorJServices {
	
	private static PriorJServices instance;
	
	/**
	 * AJDT services
	 */
	private AJDTHandler ajdtHandler;
	/**
	 * all projects into the workspace
	 */
	private IProject [] projects;
	
	/**
	 * Our API for prioritization
	 */
	PriorJ priorj;
	
	private String slash = JavaIO.SEPARATOR;
	
	/**
	 * Constructor of Controller.
	 * 
	 */
	private PriorJServices(){
		ajdtHandler = new AJDTHandler();	
		projects = ajdtHandler.getProjects();
		priorj = PriorJ.getInstance();
	}
	
	public static PriorJServices getInstance(){
		if (instance == null){
			instance = new PriorJServices();
		}
		return instance;
	}
	
	/**
	 * Get the project in the workspace.
	 * 
	 * @return
	 */
	public IProject [] getProjects(){
		return projects;
	}
	
	/**
	 * Get the techniques identifications.
	 * 
	 * @return
	 */
	public List<Integer> getTechniques(){
		return priorj.getTechniques();
	}
	
	/**
	 * Add  a technique by id.
	 *
	 * @param technique
	 */
	public void addTechnique(Integer typeOfTechnique){
		priorj.addTechnique(typeOfTechnique);
	}
	
	/**
	 * Remove a technique by id.
	 * 
	 * @param techniqueId
	 */
	public void removeTechnique(Integer typeOfTechnique){
		priorj.removeTechnique(typeOfTechnique);
	}
	
	/**
	 * Cloning a Java Project.
	 *   
	 * @param selectedProjectName
	 * @param newProjectName
	 * @throws Exception
	 */
	public void cloneProject(String selectedProjectName, String newProjectName) throws Exception{
		DataManager.createFolderVersion(selectedProjectName, newProjectName);
		ajdtHandler.copyProject(selectedProjectName, newProjectName);
	}
	
	/**
	 * This plugin works with a selected project, many tasks need of the project name.
	 * 
	 * @return
	 */
	public String getSelectedProjectName(){
		return DataManager.getProjectFolderName();
	}
	/**
	 * This method should set a name from selected project in workspace.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public void setSelectedProjectName(String selectedProjectName) throws Exception{
		DataManager.createProjectFolder(selectedProjectName);
	}
	
	/**
	 * Path to place where is saved the artifacts generated.
	 * 
	 * @param sysPath
	 * @throws Exception 
	 */
	public void setLocalBasePath(String localBasePath) throws Exception {
		DataManager.createLocalbase(localBasePath);
	}

	/**
	 * Get the place to save artifacts generated.
	 * 
	 * @return
	 */
	public String getLocalBasePath() {
		return DataManager.getLocalBasePath();
	}
	
	/**
	 * This method prioritize with many techniques simultaneously.
	 * 
	 * @param size
	 *  the size of generated suite.
	 *  
	 * @throws Exception 
	 * 
	 */
	public void prioritizeAll(int size) throws Exception {
		Coverage coverage = new Coverage();
		List<TestSuite> suites = getSuites();	
		List<TestCase> allTests = coverage.getAllTests(suites);
		priorj.prioritizeAll(size, allTests);
	}
	
	/**
	 * This method prioritize a set of tests.
	 * 
	 * @param tests
	 * @param typeOfTechnique
	 * @return
	 * @throws EmptySetOfTestCaseException
	 */
	public List<String> prioritize(List<TestCase> tests, int typeOfTechnique) throws EmptySetOfTestCaseException{
		TechniqueCreator creator = new TechniqueCreator();
		Technique technique = creator.create(typeOfTechnique);
		return technique.prioritize(tests);
	}

	/**
	 * This method create a prioritized test suite from a list of tests.
	 * 
	 * @param suiteName
	 *  the suite name
	 * @param tests
	 *  the prioritized test order
	 * @return
	 *  the suite code
	 * @throws Exception 
	 */
	public String createSuite(String suiteName, List<String> tests) throws Exception{
		return priorj.createSuite(suiteName, tests);
	}
	
	/**
	 * Create the order report.
	 * 
	 * @param listTests
	 *   a list of prioritized tests names.
	 *   
	 * @return
	 *  the order report.
	 *  
	 * @throws Exception 
	 */
	public String createOrder(int typeOfTechnique, List<String> listTests) throws Exception {
		return priorj.createOrderReport(typeOfTechnique, listTests);
	}
	
	/**
	 * Create the coverage report.
	 * 
	 * @param tests
	 * @return
	 * @throws Exception
	 */
	public String createCoverageReport() throws Exception{
		List<TestSuite> suites = getSuites();	
		return priorj.createCoverageReport(suites);
	}
	
	/**
	 * Retrieve a list of test suites.
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<TestSuite> getSuites() {
		List<List> allSuites = DataManager.openCoverageData();
		Coverage coverage = new Coverage();
		List<TestSuite> suites = coverage.getSuiteList(allSuites);
		return suites;
	}

	/**
	 * Save the coverage Report generated by plugin.
	 * 
	 * @param report
	 */
	public void saveCoverageReport(String report) {
		DataManager.save("report.js","js",report);
	}
	
	/**
	 * This method save the prioritized order.
	 * 
	 * @param fileName
	 * @param order
	 */
	public void saveOrder(String fileName, String order) {
		DataManager.save(fileName, order);
	}

	/**
	 * This method save a suite.
	 * 
	 * @param fileName
	 *  the file name.
	 * @param suiteCode
	 *  the suites code generated by plugin.
	 */
	public void saveSuite(String fileName, String suiteCode) {
		DataManager.save(fileName,suiteCode);
	}

	/**
	 * This method open a file in the editor.
	 * 
	 * @param filename
	 * @throws PartInitException
	 */
	public void openFileInEditor(String filename) throws PartInitException{
		String path = DataManager.getCurrentPath() + slash + filename;
		ajdtHandler.openFileInEditorView(path);
	}
	
	/**
	 * Given a technique id, this method return the acronyms.
	 * 
	 * @param typeOfTechnique
	 * @return
	 */
	public String getTechniqueName(int typeOfTechnique){
		return TechniqueCreator.acronyms(typeOfTechnique);
	}
	
	/**
	 * This method move the coveragePriorJ.xml to local base directory.
	 * @throws IOException  
	 * 
	 */
	public void moveCoverageFileToLocalBase() throws IOException {
		String projectName = DataManager.getProjectVersion();
		if(projectName.contains("_Old")){
			projectName = projectName.replace("_Old", "");
		}
		String origin = ajdtHandler.getFullProjectPath(projectName);
		origin += slash + "report" + slash;
		String destiny = DataManager.getCurrentPath();
	//	JavaIO.copy(origin, destiny, "coveragePriorJ.xml");
		JavaIO.copyAll(new File(origin), new File(destiny), true);
	}
	
	/**
	 * This method remove the temporary project.
	 * 
	 * @throws CoreException
	 */
	public void deleteTemporaryProject() throws CoreException{
		String versionName = DataManager.getProjectVersion();
		ajdtHandler.deleteProject(versionName);
		
		if(versionName.contains("_Old")){
			versionName = versionName.replace("_Old", "");
			ajdtHandler.deleteProject(versionName);
		}
		
	}
	
	/**
	 * Create project and version folders in the local base.
	 * 
	 * @param projectName
	 * @param versionName
	 * @throws Exception 
	 */
	public void createCurrentProjectVersion(String projectName, String versionName) throws Exception {
		DataManager.createFolderVersion(projectName, versionName);
	}
	
	/**
	 * Accessing current path to save data.
	 * 
	 * @return
	 */
	public String getCurrentPath() {
		return DataManager.getCurrentPath();
	}
	
	
	
	/**
	 * Check if a project already exists in workspace.
	 * 
	 * @param projectName
	 * @return
	 */
	public boolean existProject(String projectName) {
		return ajdtHandler.existProject(projectName);
	}

	/**
	 * Calling the JVM and do a launch project.
	 * 
	 * @param className
	 * @throws CoreException 
	 * 
	 * @throws InterruptedException 
	 */
	public synchronized void launchJUnitTests() throws CoreException, InterruptedException{
		String projectName = DataManager.getProjectVersion();
		IProject project = ajdtHandler.getProject(projectName);
		JUnitLaunch launch = new JUnitLaunch();
		launch.performLaunch(JavaCore.create(project), ILaunchManager.RUN_MODE);
	}

	/**
	 * When the aspect complete your task it not write the last </list> to 
	 * the  file, this method write a last line to close <list> tag
	 */
	public void writeXMLFinisher() {
		JavaIO.createXMLFile(getCurrentPath(), "coveragePriorJ.xml", "</list>", true);
	}

	/**
	 * Reading the coverage file to local data.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<List> getRawData() {
		return DataManager.openCoverageData();
	}
	
	/**
	 * This method reset the plug-in to start state.
	 *  
	 */
	public void resetPlugin(){
		priorj.getTechniques().clear();
	}

	/**
	 * Get the project location by project name.
	 *  
	 * @param projectName
	 * @return
	 */
	public String getProjectPath(String projectName) {
		return ajdtHandler.getFullProjectPath(projectName);
	}

	/**
	 * Checking the difference between two versions.
	 * 
	 * @param newPojectName
	 * @param oldProjectName
	 * @return
	 * @throws Exception
	 */
	public void checkDiffs(String newPojectName, String oldProjectName) throws Exception {
		DifferenceVisitor visitor = new DifferenceVisitor(oldProjectName);
		IProject currentProject = ajdtHandler.getProject(newPojectName);
		currentProject.accept(visitor, IResource.NONE);
		List<String> diffs = visitor.getDifferences();
		saveDiffs(diffs);
		priorj.setAffectedBlocks(diffs);
	}
	/**
	 * Doing a refresh in a project.
	 * 
	 * @param projectName
	 * @throws CoreException 
	 */
	public void refreshProject(String projectName) throws CoreException {
		IProject project = ajdtHandler.getProject(projectName);
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
	}
	/**
	 * Saving the difference list.
	 * 
	 * @param differencesList
	 */
	public void saveDiffs(List<String> differencesList) {
		JavaIO.saveObjectToXML(getCurrentPath(), "diffs.xml", differencesList, false);
	}

	/**
	 * Open a difference List.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> openDiffs() {
		String path = getCurrentPath()+slash+"diffs.xml";
		List<String> diffs = (List<String>) JavaIO.getObjectFromXML(path);
		return diffs;
	}
	
	public void openBrowser(String file) throws IOException {
		String path = DataManager.getCurrentPath()+JavaIO.SEPARATOR+file;
		File htmlFile = new File(path);
		Desktop.getDesktop().browse(htmlFile.toURI());
	}

	public void createTableForTrace() {
		List<TestSuite> suites  = getSuites();
		GenerateTreeData treeData = new GenerateTreeData(suites);
		String data = treeData.getGraphData();
		DataManager.save("treedata.js", "js", data);
	}

	public void saveRoutes() {
		String routes = DataManager.traceRoutesFromBase();
		JavaIO.createTextFile(DataManager.getLocalBasePath(), "routes.js", routes, false);
	}
	
	public void savePrioritizations(Object prior) {
		JavaIO.saveObjectToXML(DataManager.getLocalBasePath(), "prioritizations.xml", prior, false);
	}
	
	public Object getPrioritizations(){
		String path = DataManager.getLocalBasePath() + JavaIO.SEPARATOR + "prioritizations.xml";
		return JavaIO.getObjectFromXML(path);
	}
	
	
	public boolean existPrioritizationsXml(){
		String path = DataManager.getLocalBasePath() + JavaIO.SEPARATOR + "prioritizations.xml";
		return JavaIO.exist(path);
	}

	public void saveLocalBase(String path){
		ajdtHandler.saveInPreferences("localbasepath", path);
	}
	
	public String readLocalBase(){
		return ajdtHandler.getFromPreferences("localbasepath");
	}

	public String searchJUnitReport() {
		return JUnitReportFailures.searchJUnitReportXMLFile(getCurrentPath());
	}

	public List<String> getFailuresFromJUnitReport(String xmlFilePath) throws Exception {
		List<String> failures = JUnitReportFailures.openFile(xmlFilePath);
		return failures;
	}

	public void saveFailures(String script) {
		DataManager.save("failures.js", "js", script);
	}

	public String createFailureScript(List<String> failures) {
		String script = JUnitReportFailures.createFailuresScript(failures);
		return script;
	}

	public void setFrameworkVersion(boolean defaultJUnitVersion3) {
		priorj.setJUnitFrameworkVersion4(defaultJUnitVersion3);
	}
	
}