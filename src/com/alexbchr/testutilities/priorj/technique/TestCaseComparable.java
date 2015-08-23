package com.alexbchr.testutilities.priorj.technique;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2013  Samuel T. C. Santos
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
import coverage.TestCase;

/**
 * This class is used to sorting a set of test case by weight values. 
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 */
public class TestCaseComparable implements Comparable {

	private double value;
	private TestCase testCase;
	
	/**
	 * The constructor to TestCaseComparable.
	 * 
	 * @param value a weight for this test case.
	 * @param testCase a test case.
	 */
	public TestCaseComparable(double value, TestCase testCase){
		this.value = value;
		this.testCase = testCase;
	}
	
	/**
	 * Get the value of weight for this test case.
	 * 
	 * @return the weight.
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Return the test case.
	 * 
	 * @return a object type <code>TestCase</code>
	 */
	public TestCase getTestCase() {
		return testCase;
	}

	public int compareTo(Object object) {
		
		TestCaseComparable test = (TestCaseComparable) object;
			
		return (int) (value - test.getValue());
	}

}
