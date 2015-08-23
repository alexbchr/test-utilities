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
import java.util.Iterator;
import java.util.List;

import coverage.TestCase;

public class TechniqueEchelonAdditional implements Technique {
    
    private List<String> blockAffected;
    private int sizeBlockAffected = 0;
    
    public TechniqueEchelonAdditional(){
           
    }
	
    public List<String> getBlockAffected() {
		return blockAffected;
    }

    public void setBlockAffected(List<String> blockAffected) {
            this.blockAffected = blockAffected;
    }

    public boolean containsBlock(String value){
            Iterator<String> itBlock = blockAffected.iterator();
            while (itBlock.hasNext()) {
                    String statment = itBlock.next();
                    if(statment.equals(value)){
                            return true;
                    }
            }
            return false;
    }

    public double getPercentage(double value){;
            if(sizeBlockAffected == 0) return 0;
            return value/sizeBlockAffected;
    }

    public double getWeight(List objectList) {
        int count = 0;
        Iterator<String> itObjects = objectList.iterator();
        while(itObjects.hasNext()){
                String obj = itObjects.next();
                if(containsBlock(obj)){
                        count++;
                        blockAffected.remove(obj);
                }
        }
        return getPercentage(count);
    }
	
    public TestCase biggerWeight(List<TestCase> tests){
        int indexBigger = 0;
        double bigger=0;
        
        int index=0;
        for (TestCase test: tests){
            double weight = getWeight(test.getStatementsCoverageDistinct());
           
            if (weight > bigger){
                indexBigger = index;
                bigger = weight;
            }
            index++;
        }
        
        List<TestCase> sameWeight = new ArrayList<TestCase>();
        
        for (TestCase test: tests){
            double weight = getWeight(test.getStatementsCoverageDistinct());
            if (weight == bigger){
                sameWeight.add(test);
            }
        }
        
        if (sameWeight.size()>1)
            Collections.shuffle(sameWeight);
        
        return  sameWeight.size() > 0 ? sameWeight.get(0): new TestCase("");
        
    }
    

	@Override
	public List<String> prioritize(List<TestCase> tests) throws EmptySetOfTestCaseException {
	    List<TestCase> copyList = new ArrayList<TestCase>(tests);
	    
        List<String> suiteList = new ArrayList<String>();
        
        TestCase test = null;
       
        while(!copyList.isEmpty() && !blockAffected.isEmpty()){
            test = biggerWeight(copyList);
            if (!test.getSignature().isEmpty()){
                copyList.remove(test);
               	suiteList.add(test.getSignature());
            }
         
        }
        
        for (TestCase tst : copyList){
        	suiteList.add(tst.getSignature());
        }
        
        return suiteList;
	}

}
