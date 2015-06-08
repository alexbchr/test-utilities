package com.alexbchr.testutilities.priorj.core;

import java.io.File;
import java.util.ArrayList;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *  Getting failures from JUnit Report and save to PriorJ Reports.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class JUnitReportFailures {
	
	public static String createFailuresScript(List<String> failures){
		StringBuilder script = new StringBuilder();
		script.append("var failuresList = new Array();\n\n");
		script.append("$(function(){\n");
		script.append("\tloadFailuresList();\n");
		script.append("});\n\n");
		script.append("function loadFailuresList(){\n");
		for (String failure : failures){
			script.append("\tfailuresList.push('" + failure + "');\n");
		}
		script.append("}\n\n");
		script.append("function getFailures(){\n");
		script.append("\treturn failuresList;\n");
		script.append("}\n");
		return script.toString();
	}
	
	/**
	 * Searching JUnit Report saved by User.
	 * 
	 * @param localbase
	 * @return
	 */
	public static String searchJUnitReportXMLFile(String localbase){
		File file = new File(localbase);
		File [] files = file.listFiles();
		for (File f: files){
			if (f.isFile() && f.getName().endsWith(".xml") && !f.getName().contains("coveragePriorJ") ){
				return f.getPath();
			}
		}
		return "fileNotFound";
	}
	
    /**
     * Get the list of failed test cases.
     * 
     * @throws Exception 
     */
    public static List<String> openFile(String xmlFilePath) throws Exception{
    	
        try {
        	List<String> failures = new ArrayList<String>();
        	
            File file = new File(xmlFilePath);

            if (file.exists()) {
                // Create a factory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                // Use the factory to create a builder
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFilePath);
                // Get a list of all elements in the document
                NodeList list = doc.getElementsByTagName("*");

                for (int i = 0; i < list.getLength(); i++) {
                    // Get element
                    Element element = (Element) list.item(i);

                    if(element.getNodeName().equals("testcase")){
                        boolean pass = true;
                        if (element.hasChildNodes()){
                            pass = false;
                        }
                  
                        if(!pass){
                        	String suite = (String) element.getAttribute("classname");
                            String testcase = (String) element.getAttribute("name");
                            failures.add(suite+"."+testcase);
                        }
                       
                    }
                }

                return failures;

            } else {
                throw new Exception("File not found!");
            }
        } catch (Exception e) {
           throw new Exception(e.getMessage());
        }
    }
    
//    public static void main(String[] args) throws Exception {
//		List<String> failures = JUnitReportFailures.openFile("C:/Users/xpto/base/AVLOld/avlOld5/avlOld5 20140627-030335.xml");
//		
//		System.out.println(failures);
//    	String scpt = JUnitReportFailures.createFailuresScript(Arrays.asList("tests.AVLTest.testInsercao","tests.AVLTest.testInsercaoCondicoesLimite"));
//    	System.out.println(scpt);
//    	
//    	String file = searchJUnitReportXMLFile("C:/Users/xpto/base/AVLOld/avlOld5/");
//    	System.out.println(file);
//    }
}
