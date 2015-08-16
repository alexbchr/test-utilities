package com.alexbchr.testutilities.testng.refactoring;

import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;

import java.util.Map;

public class ConvertFromJUnitRefactoringContribution extends RefactoringContribution {
  public static final String REFACTORING_ID = "com.alexbchr.testutilities.testng.refactoring.convertfromjunit3";

  @Override
  public RefactoringDescriptor createDescriptor(String id, String project,
      String description, String comment, Map arguments, int flags)
      throws IllegalArgumentException
  {
    return new ConvertFromJUnitRefactoringDescriptor(REFACTORING_ID, project,
        description, comment, flags);
  }

}
