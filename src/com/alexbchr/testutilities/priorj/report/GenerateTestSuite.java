package com.alexbchr.testutilities.priorj.report;

/*
 * PriorJ: JUnit Test Case Prioritization.
 * 
 * Copyright (C) 2012-2013  SPLab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.List;


/**
 * This class generate a Test Suite for JUnit 4.
 * 
 * @author Samuel T. C. Santos
 * @author Julio H. Rocha
 * 
 * @version 1.0
 * 
 */
public class GenerateTestSuite {

	static String newline = System.getProperty("line.separator");

	/**
	 * Create a Test Suite with package name.
	 * 
	 * @param packageName
	 * 
	 * @return
	 */
	public static String getHeader(String packageName) {
		String strHeader = "";
		
		if (!packageName.isEmpty())
			strHeader = "package " + packageName + ";" + newline;
		
		strHeader += "import junit.framework.TestCase;" + newline;
		strHeader += "import junit.framework.TestResult;" + newline;
		
		return strHeader;
	}

	/**
	 * Get the suite Body Top.
	 * 
	 * @param className
	 * 		The class name.
	 * @return
	 */
	public static String getSuiteBodyTop(String className) {
		String strBody = "";
		
		if (className.isEmpty())
			strBody = "public class SuitePrioritizedPriorJ {" + newline;
		else
			strBody = "public class "+ className + " {" + newline;
			
		strBody += "\tprivate int failureCount = 0;" + newline;
		strBody += "\tprivate int runCount = 0;" + newline;
		strBody += "\tprivate int errorCount = 0;" + newline;
		
		return strBody;
	}

	/**
	 * This method generate a test suite with package name, suite name, and a list of tests.
	 * 
	 * @param packageName
	 * 			The package name.
	 * @param className
	 * 			The suite name.
	 * @param tests
	 * 			The list of test cases names.
	 * @return
	 */
	public static String generate(String packageName, String className, List<String> tests) {
	
		String code = getHeader(packageName);
		code += getSuiteBodyTop(className);
		code += "\tpublic void run(){" + newline;
	
		for (String string : tests) {
			String caminho = (String) string;
			caminho = caminho.replace(".java", "");
			String[] paths = caminho.split("\\.");
			String tcName = paths[paths.length - 1];
			//String suiteName = generateNameSuite(paths);
			String instance = getPathInstance(paths);
			code += "\t\tTestCase " + generateNameSuite(paths) + " = null;" + newline;
			code += "\t\t" + generateNameSuite(paths) + " = new "+ instance + "(){" + newline;
			code += "\t\t\tpublic void runTest(){" + newline;
			code += "\t\t\t\ttry{" + newline;
			code += "\t\t\t\t\t" + tcName + "();" + newline;
			code += "\t\t\t\t} catch (Exception e) {" + newline;
			code += "\t\t\t\t\te.printStackTrace();" + newline;
			code += "\t\t\t\t}" + newline;
			code += "\t\t\t}" + newline;
			code += "\t\t};" + newline;
			code += "\t\tsetResult(\"" + tcName + "\","+ generateNameSuite(paths) + ".run());" + newline;

		}
		code += "\t}" + newline;
		code += getFinalSuite(className,tests);
		
		return code;
	}
	
//	public static String newGenerate(String packageName, String className, List<String> tests) {
//		
//		String code = getHeader(packageName);
//		code += getSuiteBodyTop(className);
//	
//		for (String string : tests) {
//			String caminho = (String) string;
//			caminho = caminho.replace(".java", "");
//			String[] paths = caminho.split("\\.");
//			String tcName = paths[paths.length - 1];
//			String suiteName = generateNameSuite(paths);
//			String instance = getPathInstance(paths);
//			
//			code += "\tpublic void " +suiteName+"(){"+ newline;
//			
//			code += "\t\tTestCase " + generateNameSuite(paths) + " = null;" + newline;
//			code += "\t\t" + generateNameSuite(paths) + " = new "+ instance + "(){" + newline;
//			code += "\t\t\tpublic void runTest(){" + newline;
//			code += "\t\t\t\ttry{" + newline;
//			code += "\t\t\t\t\t" + tcName + "();" + newline;
//			code += "\t\t\t\t} catch (Exception e) {" + newline;
//			code += "\t\t\t\t\te.printStackTrace();" + newline;
//			code += "\t\t\t\t}" + newline;
//			code += "\t\t\t}" + newline;
//			code += "\t\t};" + newline;
//			code += "\t\tsetResult(\"" + tcName + "\","+ generateNameSuite(paths) + ".run());" + newline;
//			code += "\t}" + newline;
//			
//		}
//		code += getFinalSuite(className,tests);
//		
//		return code;
//	}
	
	
	/**
	 * This method generate a suite final.
	 * 
	 * @param suiteName
	 * @return
	 */
	public static String getFinalSuite(String suiteName, List<String> tests) {
		String code = "";
		code += "\tprivate void setResult(String testCaseName,TestResult result){"+ newline;
		code += "\t\tSystem.out.println(\"TestCase: \" + testCaseName +\" \" + getStatus(result));"+ newline;
		code += "\t\trunCount += result.runCount();" + newline;
		code += "\t\tfailureCount += result.failureCount();" + newline;
		code += "\t\terrorCount += result.errorCount();" + newline;
		code += "\t}" + newline;
		code += "\tprivate String getStatus(TestResult result){" + newline;
		code += "\t\tint erroCount = result.errorCount();" + newline;
		code += "\t\tint failureCount = result.failureCount();" + newline;
		code += "\t\tif(erroCount > 0) return \"[ERROR]\";" + newline;
		code += "\t\telse if(failureCount > 0) return \"[FAILURE]\";" + newline;
		code += "\t\telse return \"[ACCEPTED]\";" + newline;
		code += "\t}" + newline;
		code += "\tpublic void printResult(){" + newline;
		code += "\t\tSystem.out.println(\"=================== Test Suite Prioritized - PriorJ =================\");"+ newline;
		code += "\t\tSystem.out.println(\"Run: \" + this.runCount);" + newline;
		code += "\t\tSystem.out.println(\"Faults: \" + this.failureCount);"+ newline;
		code += "\t\tSystem.out.println(\"Errors: \" + this.errorCount);"+ newline;
		code += "\t}" + newline;
		code += "\tpublic static void main(String[] args) {" + newline;
		
		if (suiteName.isEmpty())
			code += "\t\tPrioritizedSuite st = new PrioritizedSuite();" + newline;
		else
			code += "\t\t" + suiteName + " st = new " + suiteName + "();" + newline;
		
		//code += "\t\tst.run();" + newline;
		
		for (String test : tests){
			String [] path = test.split("\\.");
			code += "\t\tst." + generateNameSuite(path)+"();"+newline;
		}
		
		code += "\t\tst.printResult();" + newline;
		code += "\t}" + newline;
		code += "}" + newline;
		return code;

	}

	/**
	 * This method generate a Name to suite file.
	 * 
	 * @param paths
	 * @return
	 */
	public static String generateNameSuite(String[] paths) {

		String caminho = "tc";

		for (String string : paths) {
			caminho += string.substring(0, 1).toUpperCase().concat(string.substring(1));
		}
		return caminho;
	}

	/**
	 * This method get a instance from file path.
	 * 
	 * @param paths
	 * @return
	 */
	public static String getPathInstance(String[] paths) {
		String caminho = "";
		for (int i = 0; i < paths.length - 1; i++) {
			caminho += paths[i] + ".";
		}
		return caminho.isEmpty() ? "" : caminho.substring(0, caminho.length() - 1) ; 
	}
}