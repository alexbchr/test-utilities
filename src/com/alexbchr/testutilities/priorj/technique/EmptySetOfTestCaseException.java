package com.alexbchr.testutilities.priorj.technique;

/**
 * When the set of test cases is empty. 
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 */
public class EmptySetOfTestCaseException extends Exception {

	public EmptySetOfTestCaseException(){
		super("Empty Set of Test Cases!");
	}
}
