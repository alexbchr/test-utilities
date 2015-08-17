package com.alexbchr.testutilities.testng.refactoring;

import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

import com.alexbchr.testutilities.TestUtilitiesPlugin;

public class ConvertFromJUnitWizard extends RefactoringWizard {

  private TestNGXmlPage m_xmlPage;

  public ConvertFromJUnitWizard(Refactoring refactoring, int flags) {
    super(refactoring, flags);
  }

  @Override
  protected void addUserInputPages() {
    m_xmlPage = new TestNGXmlPage();
    addPage(m_xmlPage);
  }

  @Override
  public boolean performFinish() {
    if (m_xmlPage.generateXmlFile()) {
    	Change changeBase = getChange();
		if (changeBase != null && changeBase.getClass().isInstance(CompositeChange.class)) {
			CompositeChange compChange = (CompositeChange)changeBase;
			
			if (compChange.getChildren() != null && 
					compChange.getChildren().length > 0 && 
					compChange.getChildren()[0].getClass().isInstance(ConvertFromJUnitCompositeChange.class)) {
				
				ConvertFromJUnitCompositeChange change = (ConvertFromJUnitCompositeChange)compChange.getChildren()[0];
				
			}
		}
    	
    	m_xmlPage.finish();
    }

    return super.performFinish();
  }
}
