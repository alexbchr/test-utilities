package com.alexbchr.testutilities.testng.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import com.alexbchr.testutilities.TestUtilitiesPlugin;

public class Images {
  private static ImageRegistry m_imageRegistry;

  private final static String IMG_TESTNG = "testng";

  public static final String IMG_SUITE = "suite";
  public static final String IMG_SUITE_OK = "suiteOk";
  public static final String IMG_SUITE_SKIP = "suiteSkip";
  public static final String IMG_SUITE_FAIL = "suiteFail";
  public static final String IMG_SUITE_RUN = "suiteRun";
  public static final String IMG_TEST_HIERARCHY = "testHierarchy";
  public static final String IMG_TEST = "test";
  public static final String IMG_TEST_OK = "testOk";
  public static final String IMG_TEST_SKIP = "testSkip";
  public static final String IMG_TEST_FAIL = "testFail";
  public static final String IMG_TEST_RUN = "testRun";

  public static final String IMG_COMPARE_DISABLED = "compareDisabled";
  public static final String IMG_COMPARE_HOVER = "compareHover";
  public static final String IMG_COMPARE = "compare";

  public static final String IMG_STOP = "stop";

  static {
    Display display = Display.getCurrent();
    if(display == null) {
      display = Display.getDefault();
    }
    m_imageRegistry = new ImageRegistry(display);

    //
    // Tree display
    //
    m_imageRegistry.put(IMG_TESTNG, TestUtilitiesPlugin.getImageDescriptor("main16/testng.gif"));
    m_imageRegistry.put(IMG_SUITE, TestUtilitiesPlugin.getImageDescriptor("obj16/suite.gif"));
    m_imageRegistry.put(IMG_SUITE_OK, TestUtilitiesPlugin.getImageDescriptor("obj16/suiteok.gif"));
    m_imageRegistry.put(IMG_SUITE_SKIP, TestUtilitiesPlugin.getImageDescriptor("obj16/suiteskip.gif"));
    m_imageRegistry.put(IMG_SUITE_FAIL, TestUtilitiesPlugin.getImageDescriptor("obj16/suitefail.gif"));
    m_imageRegistry.put(IMG_SUITE_RUN, TestUtilitiesPlugin.getImageDescriptor("obj16/suiterun.gif"));
    m_imageRegistry.put(IMG_TEST_HIERARCHY, TestUtilitiesPlugin.getImageDescriptor("obj16/testhier.gif"));
    m_imageRegistry.put(IMG_TEST, TestUtilitiesPlugin.getImageDescriptor("obj16/test.gif"));
    m_imageRegistry.put(IMG_TEST_OK, TestUtilitiesPlugin.getImageDescriptor("obj16/testok.gif"));
    m_imageRegistry.put(IMG_TEST_SKIP, TestUtilitiesPlugin.getImageDescriptor("obj16/testskip.gif"));
    m_imageRegistry.put(IMG_TEST_FAIL, TestUtilitiesPlugin.getImageDescriptor("obj16/testfail.gif"));
    m_imageRegistry.put(IMG_TEST_RUN, TestUtilitiesPlugin.getImageDescriptor("obj16/testrun.gif"));

    m_imageRegistry.put(IMG_COMPARE_DISABLED,TestUtilitiesPlugin.getImageDescriptor("dlcl16/compare.gif"));
    m_imageRegistry.put(IMG_COMPARE_HOVER, TestUtilitiesPlugin.getImageDescriptor("elcl16/compare.gif"));
    m_imageRegistry.put(IMG_COMPARE, TestUtilitiesPlugin.getImageDescriptor("elcl16/compare.gif"));

    m_imageRegistry.put(IMG_STOP, TestUtilitiesPlugin.getImageDescriptor("obj16/terminatedlaunch_obj.png"));
  }

  public static Image getTestNGImage() {
    return m_imageRegistry.get(IMG_TESTNG);
  }

  public static Image getImage(String key) {
    return m_imageRegistry.get(key);
  }

  public static ImageDescriptor getImageDescriptor(String key) {
    return m_imageRegistry.getDescriptor(key);
  }
}
