package com.alexbchr.testutilities.priorj.controller;

import java.util.ArrayList;
import java.util.List;

import com.alexbchr.testutilities.priorj.report.GenerateCoverageReport;
import com.alexbchr.testutilities.priorj.report.GenerateExecutionOrderReport;
import com.alexbchr.testutilities.priorj.report.GenerateTestSuite;
import com.alexbchr.testutilities.priorj.report.GenerateTestSuiteForJUnit4;
import com.alexbchr.testutilities.priorj.technique.EmptySetOfTestCaseException;
import com.alexbchr.testutilities.priorj.technique.Technique;
import com.alexbchr.testutilities.priorj.technique.TechniqueCreator;
import com.alexbchr.testutilities.priorj.technique.TechniqueEchelonTotal;
import manager.Coverage;
import core.Difference;
import core.InstrumentClass;
import coverage.TestCase;
import coverage.TestSuite;

/**
 * Class PriorJ priorization of JUnit Test Cases. 
 *  
 * @author Samuel T. C. Santos
 *
 */
public class PriorJ {

	private static PriorJ instance;
	private static List<Integer> techniques;
	private static List<String> affectedBlocks;
	private static boolean junitFrameworkVersion4;
	
	public static PriorJ getInstance(){
		if (PriorJ.instance == null){
			techniques = new ArrayList<Integer>();
			affectedBlocks = new ArrayList<String>();
			PriorJ.instance = new PriorJ();
			junitFrameworkVersion4 = false;
		}
		return PriorJ.instance;
	}

	/**
	 * Adding the selected Technique by user.
	 * 
	 * @param typeOfTechnique
	 */
	public void addTechnique(int typeOfTechnique) {
		if (typeOfTechnique == TechniqueCreator.ADDITIONAL_METHOD_COVERAGE){
			techniques.add(TechniqueCreator.ADDITIONAL_METHOD_COVERAGE);
		}
		else if(typeOfTechnique == TechniqueCreator.ADDITIONAL_STATEMENT_COVERAGE){
			techniques.add(TechniqueCreator.ADDITIONAL_STATEMENT_COVERAGE);
		}
		else if (typeOfTechnique == TechniqueCreator.CHANGED_BLOCKS){
			techniques.add(TechniqueCreator.CHANGED_BLOCKS);
		}
		else if (typeOfTechnique == TechniqueCreator.RANDOM){
			techniques.add(TechniqueCreator.RANDOM);
		}
		else if (typeOfTechnique == TechniqueCreator.TOTAL_METHOD_COVERAGE){
			techniques.add(TechniqueCreator.TOTAL_METHOD_COVERAGE);
		}
		else if (typeOfTechnique == TechniqueCreator.TOTAL_STATEMENT_COVERAGE){
			techniques.add(TechniqueCreator.TOTAL_STATEMENT_COVERAGE);
		}
		else{
			throw new IllegalArgumentException("Invalid Technique Type!");
		}
	}
	
	/**
	 * Removing a selected technique.
	 * 
	 * @param typeOfTechnique
	 */
	public void removeTechnique(int typeOfTechnique){
		if (techniques.contains(typeOfTechnique)){
			int index = techniques.indexOf(typeOfTechnique);
			techniques.remove(index);
		}
	}

	/**
	 * The list of selected Techniques Types.
	 * 
	 * @return
	 */
	public List<Integer> getTechniques() {
		return techniques;
	}

	/**
	 * Getting a list with all  Test Suites.
	 * 
	 * @param allSuites
	 * @return
	 */
	public List<TestSuite> getTestSuites(@SuppressWarnings("rawtypes") List<List> allSuites) {
		Coverage coverage = new Coverage();
		return coverage.getSuiteList(allSuites);
	}

	/**
	 * Getting a list with all Test Cases.
	 * 
	 * @param suites
	 * @return
	 */
	public List<TestCase> getTestCases(List<TestSuite> suites) {
		Coverage coverage = new Coverage();
		return coverage.getAllTests(suites);
	}
	
	
	/**
	 * Getting an selection (fraction) to generate the suite.
	 * 
	 * @param suiteSize
	 * @param allTests
	 * @return
	 */
	public List<String> getSelection(int suiteSize, List<String> allTests){
		int total = allTests.size();
		int selectedSize  = total * suiteSize/100; 
		return allTests.subList(0, selectedSize);
	}

	/**
	 * 
	 * @param totalMethodCoverage
	 * @param allTests
	 * @return
	 * @throws EmptySetOfTestCaseException 
	 */
	public List<String> prioritize(int typeOfTechnique, List<TestCase> allTests) throws EmptySetOfTestCaseException {
		TechniqueCreator creator = new TechniqueCreator();
		if (typeOfTechnique == TechniqueCreator.CHANGED_BLOCKS){
			TechniqueEchelonTotal technique = new TechniqueEchelonTotal();
			technique.setBlockAffected(affectedBlocks);
			return technique.prioritize(allTests);
			
		}
		else{
			Technique technique = creator.create(typeOfTechnique);
			return technique.prioritize(allTests);
		}
	}
	/**
	 * 
	 * 
	 * @param suiteSize
	 * 
	 * @param typeOfTechnique
	 * @param allTests
	 * @return
	 * @throws Exception 
	 */
	public List<String> prioritizeAll(int suiteSize, List<TestCase> allTests) throws Exception{
		List<String> prioritizedList = new ArrayList<String>();
		for (Integer typeOfTechnique : techniques){
			//getting the suite names
			String acronyms = TechniqueCreator.acronyms(typeOfTechnique);
			//prioritize the tests.
			prioritizedList = prioritize(typeOfTechnique, allTests);
			prioritizedList = getSelection(suiteSize, prioritizedList);
			//saving the produced artifacts
			String order = createOrderReport(typeOfTechnique, prioritizedList);
			DataManager.save(acronyms+".js","js", order);
			String suite = createSuite(acronyms, prioritizedList);
			DataManager.save(acronyms+".java", suite);
		}
		return prioritizedList;
	}
	/**
	 * This method prioritize with many techniques simultaneously.
	 * 
	 * @throws Exception 
	 * 
	 */
	public void prioritizeAll(List<TestCase> allTests) throws Exception {
		for (Integer typeOfTechnique : techniques){
			//getting the suite names
			String acronyms = TechniqueCreator.acronyms(typeOfTechnique);
			//prioritize the tests.
			List<String> prioritizedList = prioritize(typeOfTechnique, allTests);
			//saving the produced artifacts
			String order = createOrderReport(typeOfTechnique, prioritizedList);
			DataManager.save(acronyms+".js","js", order);
			String suite = createSuite(acronyms, prioritizedList);
			DataManager.save(acronyms+".java", suite);
		}
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
		if (isJUnit4()){
			return GenerateTestSuiteForJUnit4.generate("tests", suiteName, tests);
		}
		else{
			return GenerateTestSuite.generate("tests", suiteName, tests);
		}
	}
	
	private boolean isJUnit4() {
		return junitFrameworkVersion4;
	}
	/**
	 * Set the JUnit Framework version as 4.x
	 * 
	 * @param option
	 * 		true if the Framework version is 4.x
	 * 		if the Framework version is 3.x set false;
	 */
	public void setJUnitFrameworkVersion4(boolean option){
		junitFrameworkVersion4 = true;
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
	public String createOrderReport(int typeOfTechnique, List<String> listTests) throws Exception {
		return GenerateExecutionOrderReport.create(typeOfTechnique,listTests);
	}

	/**
	 * Create a coverage report.
	 *  
	 * @param suites
	 * @return
	 */
	public String createCoverageReport(List<TestSuite> suites) {
		GenerateCoverageReport textReport = new GenerateCoverageReport(suites);
		return textReport.generateCoverageReport();
	}
	
	/**
	 * This method instrument a class.
	 * 
	 * @param filePath
	 *  the path to the java file.
	 * @param file
	 *  the java file name.
	 * @param isTest
	 *  true or false indicating if the file is a test file.
	 *  
	 * @throws Exception 
	 */
	public void instrument(String filePath, String file, boolean isTest) throws Exception {
		 InstrumentClass inst = new InstrumentClass(filePath, file, isTest);
	     inst.instrumentationRun();
	}
	
	/**
	 * Check differences between two versions of same code.
	 * 
	 * @param pathCodeNew
	 * @param pathCodeOld
	 * @return
	 * @throws Exception
	 */
	public List<String> checkDifference(String filePath, String oldFilePath) throws Exception {
		Difference difference = new Difference(filePath, oldFilePath);
		difference.diff();
		List<String> affected = difference.getStatementsDiff();
        return affected;
	}

	
	/**
	 * 
	 * @param blocks
	 */
	public void setAffectedBlocks(List<String> blocks) {
		this.affectedBlocks = blocks;
	}	
	
	public static void main(String[] args) {
		System.out.println("PriorJ version 1.0.6");
	}

}