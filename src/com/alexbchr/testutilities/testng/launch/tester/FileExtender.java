package com.alexbchr.testutilities.testng.launch.tester;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import com.alexbchr.testutilities.TestUtilitiesPlugin;
import com.alexbchr.testutilities.testng.util.SuiteFileValidator;


/**
 * Property tester contributing the org.testng.eclipse.isSuite property to
 * file matching the criteria: *.xml, instanceof IFile and being a suite.
 * 
 * @author <a href='mailto:the_mindstorm@evolva.ro'>Alexandru Popescu</a>
 */
public class FileExtender {
//  private static final String PROPERTY_IS_Test= "isSuite"; //$NON-NLS-1$

  public boolean test(Object receiver, String property, Object[] args, Object expectedValue,
      boolean xmlOnly) {
    if(!(receiver instanceof IFile)) {
      return false;
    }
    try {
      return SuiteFileValidator.isSuiteDefinition((IFile) receiver, xmlOnly);
    }
    catch(CoreException ce) {
      TestUtilitiesPlugin.log(ce);
    }

    return false;
  }
}
