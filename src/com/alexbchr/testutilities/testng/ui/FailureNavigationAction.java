/*
 * $Id$
 * $Date$
 */
package com.alexbchr.testutilities.testng.ui;

import com.alexbchr.testutilities.TestUtilitiesPlugin;
import com.alexbchr.testutilities.testng.util.ResourceUtil;

import org.eclipse.jface.action.Action;


/**
 * Class usage XXX
 * 
 * @version $Revision$
 */
public class FailureNavigationAction extends Action {
  private TestRunnerViewPart fPart;

  public FailureNavigationAction(TestRunnerViewPart part) {
    super(ResourceUtil.getString("ShowNextFailureAction.label"));  //$NON-NLS-1$
    setDisabledImageDescriptor(TestUtilitiesPlugin.getImageDescriptor("dlcl16/select_next.gif")); //$NON-NLS-1$
    setHoverImageDescriptor(TestUtilitiesPlugin.getImageDescriptor("elcl16/select_next.gif")); //$NON-NLS-1$
    setImageDescriptor(TestUtilitiesPlugin.getImageDescriptor("elcl16/select_next.gif")); //$NON-NLS-1$
    setToolTipText(ResourceUtil.getString("ShowNextFailureAction.tooltip")); //$NON-NLS-1$
    fPart= part;
  }
  
  public void run() {
    fPart.selectNextFailure();
  }
}
