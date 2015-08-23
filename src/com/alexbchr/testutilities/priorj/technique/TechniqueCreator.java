package com.alexbchr.testutilities.priorj.technique;

/**
 * Applied the Method Factory Pattern.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class TechniqueCreator {

	public static final int TOTAL_METHOD_COVERAGE  = 0;
	public static final int TOTAL_STATEMENT_COVERAGE = 1;
	public static final int ADDITIONAL_METHOD_COVERAGE = 2;
	public static final int ADDITIONAL_STATEMENT_COVERAGE = 3;
	public static final int RANDOM = 4;
	public static final int CHANGED_BLOCKS = 5;
	
	/**
	 * Create a instance of a technique type.
	 * 
	 * @param typeOfTechnique
	 * @return
	 */
	public Technique create(int typeOfTechnique){
		if (typeOfTechnique == TechniqueCreator.TOTAL_METHOD_COVERAGE){
			return new TechniqueTotalMethod();
		}
		else if (typeOfTechnique == TechniqueCreator.TOTAL_STATEMENT_COVERAGE){
			return new TechniqueTotalStatement();
		}
		else if (typeOfTechnique == TechniqueCreator.ADDITIONAL_METHOD_COVERAGE){
			return new TechniqueAdditionalMethod();
		}
		else if (typeOfTechnique == TechniqueCreator.ADDITIONAL_STATEMENT_COVERAGE){
			return new TechniqueAdditionalStatement();
		}
		else if (typeOfTechnique == TechniqueCreator.RANDOM){
			return new TechniqueRandom();
		}
		else if (typeOfTechnique == TechniqueCreator.CHANGED_BLOCKS){
			return new TechniqueEchelonTotal();
		}
		else{
			throw new IllegalArgumentException("Type of Technique Invalid.");
		}
	
	}
	/**
	 * Getting an acronyms to technique identifier.
	 * 
	 * @param typeOfTechnique
	 * @return
	 */
	public static String acronyms(int typeOfTechnique){
		if (typeOfTechnique == TechniqueCreator.TOTAL_METHOD_COVERAGE){
			return "TMC";
		}
		else if (typeOfTechnique == TechniqueCreator.TOTAL_STATEMENT_COVERAGE){
			return "TSC";
		}
		else if (typeOfTechnique == TechniqueCreator.ADDITIONAL_METHOD_COVERAGE){
			return "AMC";
		}
		else if (typeOfTechnique == TechniqueCreator.ADDITIONAL_STATEMENT_COVERAGE){
			return "ASC";
		}
		else if (typeOfTechnique == TechniqueCreator.CHANGED_BLOCKS){
			return "CB";
		}
		else if (typeOfTechnique == TechniqueCreator.RANDOM){
			return "RND";
		}
		else{
			throw new IllegalArgumentException("Type of Technique Invalid.");
		}
	}
	
}
