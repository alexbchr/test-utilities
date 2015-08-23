package com.alexbchr.testutilities.priorj.plugin.core;

import com.java.io.JavaIO;

/**
 * Defining some rules to Instrument or check differences in files.
 * 
 * Rule 1: a file is instrumented if the file is Java File, Not is a Test or Interface and
 *  
 * Rule 2: all files  is copyable exception .classpath and .project
 * 
 * @author Samuel T. C. Santos
 *
 */
public class Rule {

	 
	 private static final String regexSuite = ".*(@RunWith.*|@SuiteClasses|Test suite).*";
	 
	 private static final String regexTest = ".*(public void test.*|@Test).*";
	 
	 private static final String regexInterface = ".*public interface.*";
	
	 
	 public static boolean isClassCode(String filePath){
		 String code = JavaIO.openTextFile(filePath);
		 code = cleanString(code);
		 return !code.matches(regexTest) && !code.matches(regexSuite) && !code.matches(regexInterface);
	 }
	 
	 
	 /**
	  * Check if this file is a Test JUnit 3 or JUnit 4.
	  * 
	  * @param filePath
	  *  the path file to check.
	  * @return
	  *  true or false
	  */
	 public static boolean isTest(String filePath){
		 String code = JavaIO.openTextFile(filePath);
		 
		 code = cleanString(code);
				 
		 System.out.println(code);
		 return code.matches(regexTest);
	 }

	private static String cleanString(String code) {
		code = code.replaceAll("\t", " ");
		 code = code.replaceAll("\r", " ");
		 code = code.replaceAll("\n", " ");
		return code;
	}
	 
	 /**
	  * Check if this file is a JUnit 4 suite.
	  * 
	  * @return
	  */
	 public static boolean isSuite(String filePath){
		 String code = JavaIO.openTextFile(filePath);
		 code = cleanString(code);
		 return code.matches(regexSuite);
	 }
	 
	 public static boolean isInterface(String filePath){
		 String code = JavaIO.openTextFile(filePath);
		 code = cleanString(code);
		 return code.matches(regexInterface);
	 }
	 		
	/**
	 * Check if is a Java file.
	 * 
	 */
	public static boolean isJavaFile(String pathFile){
		return pathFile.endsWith(".java");
	}
			
}
