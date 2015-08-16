package com.alexbchr.testutilities.testng.ui.tree;

import org.eclipse.swt.widgets.TreeItem;
import com.alexbchr.testutilities.testng.ui.RunInfo;

/**
 * A tree node representing a test method (only its name).
 * 
 * @author Cedric Beust <cedric@beust.com>
 */
public class TestMethodTreeItem extends BaseTestMethodTreeItem {

  public TestMethodTreeItem(TreeItem parent, RunInfo runInfo) {
    super(parent, runInfo);
  }

  protected String getLabel() {
    RunInfo runInfo = getRunInfo();
    String result = runInfo.getMethodName() + runInfo.getInvocationCountDisplay();
    return result;
  }
}
