package com.alexbchr.testutilities.priorj.report;

import java.util.ArrayList;
/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2014  SPLab
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
 * This class generate a Test Suite for JUnit4.x
 * 
 * @author Julio Rocha
 * @author Samuel T. C. Santos
 * @version 1.0
 * 
 */
public class GenerateTestSuiteForJUnit4 {
	
	/**
	 * This method do a header to Test Suite.
	 * 
	 * @param packageName
	 * 		The package name.
	 * @return
	 * 		Package declaration and imports declarations.
	 * 
	 */
	public static String getHeader(String packageName){
		String header = "package " + packageName+";\n";
		header += "import org.junit.runner.JUnitCore;\n";
		header += "import org.junit.runner.Request;\n";
		header += "import org.junit.runner.Result;\n";
		return header;
	}

	/**
	 * This method create the suite class declaration and 
	 * field declarations.
	 * 
	 * @return
	 * 		the class declaration and fields.
	 */
	public static String getBodyHigher(String className){
		String body = "";
		
		if (className.isEmpty())
            body = "public class SuitePrioritizedPriorJ {\n";
		else
            body = "public class "+ className + " {\n";
		
		body += "\tprivate int failureCount = 0;\n";
		body += "\tprivate int runCount = 0;\n";
		body += "\tprivate int errorCount = 0;\n";
		return body;
	}
        
	/**
	 * This method generate the suite code.
	 * 
	 * @param packageName
	 * 		the package name.
	 * @param suiteClassName
	 * 		The suite class name
	 * @param tests
	 * 		the list with prioritized test cases.
	 * @return
	 * 		the suite code.
	 */
	public static String generate(String packageName, String suiteClassName, List<String> tests) {
		List<String> jaInstanciada = new ArrayList<String>();
        String code = getHeader(packageName);
        code += getBodyHigher(suiteClassName);
        
        code += "\tpublic void run(){\n";
        code += "\t\ttry{\n";
        code += "\t\t\tJUnitCore jc = new JUnitCore();\n";
        code += "\t\t\tResult result = null;\n";
       
        for (Object string : tests) {
           String caminho = (String) string;
           caminho = caminho.replace(".java", "");
           String[] paths = caminho.split("\\.");
           String tcName = paths[paths.length-1];
           String suiteName = caminho.replaceAll("\\.", "").replace(tcName, "");
           
           String instance = getPathInstance(paths);
           String requestName = generateRequestName(suiteName, tcName);
           if(!jaInstanciada.contains(suiteName)){
               code += "\t\t\tClass "+suiteName+" = Class.forName(\""+instance+"\");\n";
               jaInstanciada.add(suiteName);
           }
           code += "\t\t\tRequest "+requestName+" = Request.method("+suiteName+",\""+tcName+"\");\n";
           code += "\t\t\tresult = jc.run("+requestName+");\n";
           code += "\t\t\tsetResult(\""+tcName+"\",result);\n";         
        }
        code += "\t\t} catch (Exception e) {\n";
        code += "\t\t\te.printStackTrace();\n";
        code += "\t\t}\n";  
        code += "\t}\n";
        code += getFinalSuite(suiteClassName, tests);
        return code;
    }
	
//	public static String newGenerate(String packageName, String className, List<String> tests) {
//       String code = getHeader(packageName);
//       code += getBodyHigher(className);
//       
//        for (String testName : tests) {
//            String caminho = testName;
//            caminho = caminho.replace(".java", "");
//            String[] paths = caminho.split("\\.");
//            String tcName = paths[paths.length-1];
//
//             String suiteName = caminho.replaceAll("\\.", "").replace(tcName, "");
//             
//        	 code += "\tpublic void "+  generateNameSuite(paths) + "() {\n";
//             code += "\t\ttry{\n";
//             code += "\t\t\tJUnitCore jc = new JUnitCore();\n";
//             code += "\t\t\tResult result = null;\n";
//             
//
//             String instance = getPathInstance(paths);
//             String requestName = generateRequestName(suiteName, tcName);
//             
//             List<String> jaInstanciada = new ArrayList<String>();
//			if(!jaInstanciada .contains(suiteName)){
//             code += "\t\t\tClass "+suiteName+" = Class.forName(\""+instance+"\");\n";
//                 jaInstanciada.add(suiteName);
//             }
//             code += "\t\t\tRequest "+requestName+" = Request.method("+suiteName+",\""+tcName+"\");\n";
//             code += "\t\t\tresult = jc.run("+requestName+");\n";
//             code += "\t\t\tsetResult(\""+tcName+"\",result);\n";  
//
//             code += "\t\t} catch (Exception e) {\n";
//             code += "\t\t\te.printStackTrace();\n";
//             code += "\t\t}\n";  
//             code += "\t}\n";
//             
//        }
//        code += getFinalSuite(className, tests);
//        return code;
//	}
	
	/**
	 * This method create the main method to test suite.
	 * 	
	 * @param className
	 * @param tests
	 * @return
	 */
	private static String getFinalSuite(String className, List<String> tests){
		String code = "";
		code += "\tprivate void setResult(String testCaseName,Result result){\n";
		code += "\t\tSystem.out.println(\"TestCase: \" + testCaseName +\" \" + getStatus(result));\n";
		code += "\t\trunCount += result.getRunCount();\n";
		code += "\t\tfailureCount += result.getFailureCount();\n";
		code += "\t}\n";
		code += "\tprivate String getStatus(Result result){\n";
		code += "\t\tint failureCount = result.getFailureCount();\n";
		code += "\t\tif(failureCount > 0) return \"[FAILURE]\";\n";
		code += "\t\telse return \"[ACCEPTED]\";\n";
		code += "\t}\n";
		code += "\tpublic void printResult(){\n";
		code += "\t\tSystem.out.println(\"=================== Test Suite Prioritized - PriorJ =================\");\n";
		code += "\t\tSystem.out.println(\"Run: \" + this.runCount);\n";
		code += "\t\tSystem.out.println(\"Faults: \" + this.failureCount);\n";
		code += "\t\tSystem.out.println(\"Errors: \" + this.errorCount);\n";
		code += "\t}\n";
		code += "\tpublic static void main(String[] args) {\n";
		
		if (className.isEmpty())
            code += "\t\tPrioritizedSuite st = new PrioritizedSuite();\n";
	    else
	            code += "\t\t" + className + " st = new " + className + "();\n";
	    
//	    for (String test : tests){
//	            String [] path = test.split("\\.");
//	            code += "\t\tst." + generateNameSuite(path)+"();\n";
//	    }
		
	    code += "\t\tst.run();\n";
		
		code += "\t\tst.printResult();\n";
		code += "\t}\n";
		code += "}\n";
		
		return code;
		
	}
	/**
	 * 
	 * @param paths
	 * @return
	 */
	private static String generateNameSuite(String[] paths){
		return GenerateTestSuite.generateNameSuite(paths);
	}
	/**
	 * 
	 * @param suiteName
	 * @param tcName
	 * @return
	 */
	private static String generateRequestName(String suiteName, String tcName){
		String result = suiteName+tcName;
		return result.replaceAll("\\.", "");
	}
	/**
	 * 
	 * @param paths
	 * @return
	 */
	private static String getPathInstance(String[] paths){
		return GenerateTestSuite.getPathInstance(paths);
	}
	
}