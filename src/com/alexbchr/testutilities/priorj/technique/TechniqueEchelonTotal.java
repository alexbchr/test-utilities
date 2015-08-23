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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import coverage.TestCase;

public class TechniqueEchelonTotal implements Technique {
	
	private List<String> blockAffected;
    
	public TechniqueEchelonTotal(){
	}
	
    public List<String> getBlockAffected() {
		return blockAffected;
	}

	public void setBlockAffected(List<String> blockAffected) {
		this.blockAffected = blockAffected;
	}
	
	public boolean containsBlock(String value){
		return blockAffected.contains(value);
	}
	
	public double getPercentage(double value){
		int sizeBlock = getBlockAffected().size();
		if(sizeBlock == 0) return 0;
		return value/sizeBlock;
	}

    public double getWeight(List objectList) {
		int count = 0;
        Iterator<String> itObjects = objectList.iterator();
        while(itObjects.hasNext()){
        	String obj = itObjects.next();
        	if(containsBlock(obj)){
        		count++;
        	}
        }
        return getPercentage(count);
    }

	@Override
	public List<String> prioritize(List<TestCase> tests)throws EmptySetOfTestCaseException {
	  	List<TestCase> copyList = new ArrayList<TestCase>(tests);
	    
        List<String> suiteList = new ArrayList<String>();
        
        ArrayList<TestCaseComparable> weightList = new ArrayList<TestCaseComparable>();
        
        ArrayList<TestCase> notWeighted = new ArrayList<TestCase>();
        
        for (TestCase testCase : copyList) {
        	double value = getWeight(testCase.getStatementsCoverage());
        	if (value != 0.0)
        		weightList.add(new TestCaseComparable(value, testCase));
        	else 
        		notWeighted.add(testCase);
		}
        
        Collections.sort(weightList);
        
        for (int i=weightList.size()-1; i>=0; i--) {
			TestCaseComparable obj = weightList.get(i);
        	suiteList.add(obj.getTestCase().getSignature());
		}
        
        Collections.shuffle(notWeighted);
        for (TestCase test: notWeighted)
        	suiteList.add(test.getSignature());
        

        return suiteList;
    }

    public Integer biggerWeight(List<TestCase> tests){
          
        int indexBigger = 0;
        double bigger=0;
        
        int index=0;
        for (TestCase test: tests){
            double weight = getWeight(test.getStatementsCoverage());
            
            if (weight > bigger){
                indexBigger = index;
                bigger = weight;
            }
            index++;
        }

        Map<Integer, TestCase> sameWeight = new HashMap<Integer, TestCase>();

        for (int i = 0 ; i < tests.size(); i++){
            TestCase testCurrent  =  tests.get(i);
            double weight = getWeight(testCurrent.getStatementsCoverage());
            
            if (weight==bigger)
                sameWeight.put(i, testCurrent);
        }

        int indexBigInt =0;
        
        Set listKeys = sameWeight.keySet();
      
        if (sameWeight.size()>1){
            Double indexBig = Math.random() * sameWeight.size(); 

            indexBigInt = indexBig.intValue();
            
            int counter=0;
            for (Object valueKey : listKeys){
                if (counter == indexBigInt){
                    indexBigInt = (Integer )valueKey;
                    break;
                }
                counter++;
            }
           return indexBigInt;
        }
        else{
             Iterator it = listKeys.iterator();
             Integer element = (Integer) it.next();
             
             return  element;
        }

	}
    
}
