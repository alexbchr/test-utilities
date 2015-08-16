package com.alexbchr.testutilities.testng.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import com.alexbchr.testutilities.TestUtilitiesPlugin;
import com.alexbchr.testutilities.testng.TestNGPluginConstants;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  /*
   * (non-Javadoc)
   *
   * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
   * initializeDefaultPreferences()
   */
  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = TestUtilitiesPlugin.getDefault().getPreferenceStore();
    store.setDefault(TestNGPluginConstants.S_OUTDIR, "/test-output");
    store.setDefault(TestNGPluginConstants.S_EXCLUDED_STACK_TRACES,
        "org.testng.internal org.testng.TestRunner org.testng.SuiteRunner "
        + "org.testng.remote.RemoteTestNG org.testng.TestNG sun.reflect java.lang");
    // Set the default to the original behavior, where the view takes focus when
    // tests finish running
    store.setDefault(TestNGPluginConstants.S_SHOW_VIEW_WHEN_TESTS_COMPLETE,
        true);
    store.setDefault(TestNGPluginConstants.S_USEPROJECTJAR_GLOBAL, true);
  }

}
