package com.alexbchr.testutilities.priorj.report;

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

import com.alexbchr.testutilities.priorj.technique.TechniqueCreator;

/**
 * This class generate an execution order report to a specific technique.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 * 
 */
public class GenerateExecutionOrderReport {
	
	private static String name;
	
	public static String create(int typeOfTechnique, List<String> tests){
            StringBuilder builder = new StringBuilder();
            name = TechniqueCreator.acronyms(typeOfTechnique);
            builder.append("var " +name.toLowerCase()+"Tests = new Array();\n");
            builder.append("\n$(function(){\n");
            builder.append("\tloadList"+name+"();\n");
            builder.append("});\n\n");
            builder.append("function loadList"+name+"(){\n");
            for (int i=0; i<tests.size(); i++){
            	builder.append("\t"+name.toLowerCase()+"Tests['"+tests.get(i)+"'] = "+(i+1)+";\n");
            }
            builder.append("}\n\n");
            builder.append("function getOrder"+name+"(){\n");
            builder.append("\treturn "+name.toLowerCase()+"Tests;\n");
            builder.append("}\n\n");
            builder.append("function get"+name+"Position(testName){\n");
            builder.append("\treturn "+name.toLowerCase()+"Tests[testName];\n");
            builder.append("}\n");
            return builder.toString();
	}

}
