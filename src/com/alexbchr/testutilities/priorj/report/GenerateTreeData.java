package com.alexbchr.testutilities.priorj.report;

import java.util.List;

import coverage.Method;
import coverage.Statement;
import coverage.TestCase;
import coverage.TestSuite;
import coverage.ClassCode;

/**
 * Graph tree
 * 
 * @author Samuel T. C. Santos
 *
 */
public class GenerateTreeData {
	
	List<TestSuite> suites;
	
	public GenerateTreeData(List<TestSuite> suites){
		this.suites = suites;
	}
	
	public String getGraphData(){
		StringBuilder builder = new StringBuilder();
		
		builder.append("var treeData = (function(){\n");
		builder.append("\tvar suites = [];\n");
		
		builder.append("\tfunction init(){\n");
		
		for(TestSuite suite: suites){
			for (TestCase test : suite.getTestCases()){
				for (ClassCode classcode : test.getClassCoverage()){
					for (Method method : classcode.getMethodCoverage()){
						builder.append("\t\tsuites.push({\n");
						builder.append("\t\t\tname : '"+suite.getName()+"',\n");
						builder.append("\t\t\ttestcase : {\n");
						builder.append("\t\t\t\tname: '"+test.getName()+"',\n");
						builder.append("\t\t\t\tclasscode : {\n");
						builder.append("\t\t\t\t\tname : '"+classcode.getName()+"',\n");
						builder.append("\t\t\t\t\tmethod : {\n");
						builder.append("\t\t\t\t\t\tname : '"+method.getName()+"',\n");
						String line = "[";
						List<Statement> statements = method.getUniqueStatements(); 
						for (int i=0; i < statements.size();i++){
							line += statements.get(i);
							if ( i < statements.size()-1){
								line += ", ";
							}
						}
						line += "]";
						builder.append("\t\t\t\t\t\tlines : "+line+"\n");
						builder.append("\t\t\t\t\t}\n");
						builder.append("\t\t\t\t}\n");
						builder.append("\t\t\t}\n");
						builder.append("\t\t});\n");
						
					}
				}
			}
		}
		
		builder.append("\t}\n\n");
		builder.append("\treturn {\n");
		builder.append("\t\tgetData : function (){\n");
		builder.append("\t\t\treturn suites;\n");
		builder.append("\t\t},\n");
		builder.append("\t\tload : function (){\n");
		builder.append("\t\t\tinit();\n");
		builder.append("\t\t}\n");
		builder.append("\t};\n");
		builder.append("})();\n");
		return builder.toString();
	}
	
}
