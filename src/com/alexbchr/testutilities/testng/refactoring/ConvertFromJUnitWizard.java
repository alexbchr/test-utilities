package com.alexbchr.testutilities.testng.refactoring;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardPage;

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
	public IWizardPage getPage(String name) {
		IWizardPage page = super.getPage(name);
		
		if (this.getChange() != null && m_xmlPage.getChange() == null) {
			m_xmlPage.setChange(this.getChange());
			((ConvertFromJUnitRefactoring)getRefactoring()).setManualCacheChange(this.getChange());
		}
		
		return page;
	}

  @Override
  public boolean performFinish() {
    if (m_xmlPage.generateXmlFile()) {
    	m_xmlPage.finish();
    }

    return super.performFinish();
  }
}
