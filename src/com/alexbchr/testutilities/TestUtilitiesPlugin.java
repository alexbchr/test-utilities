package com.alexbchr.testutilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractSet;
import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.testng.remote.RemoteTestNG;

import com.alexbchr.testutilities.testng.TestNGPluginConstants;
import com.alexbchr.testutilities.testng.ui.TestRunnerViewPart;
import com.alexbchr.testutilities.testng.ui.util.ConfigurationHelper;
import com.alexbchr.testutilities.testng.util.JDTUtil;
import com.alexbchr.testutilities.testng.util.PreferenceStoreUtil;
import com.alexbchr.testutilities.testng.util.SWTUtil;

/**
 * The activator class controls the plug-in life cycle
 */
public class TestUtilitiesPlugin extends AbstractUIPlugin implements ILaunchListener {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.alexbchr.testutilities"; //$NON-NLS-1$
	private static URL m_fgIconBaseURL;

	// The shared instance
	private static TestUtilitiesPlugin plugin;
	
	private static boolean m_isStopped = false;
	
	private AbstractSet n_trackedLaunches = new HashSet(20);

    private BundleContext m_context;
    private PreferenceStoreUtil m_preferenceUtil;
	
    public TestUtilitiesPlugin() {
        plugin = this;
        try {
          m_fgIconBaseURL = new URL(Platform.getBundle(PLUGIN_ID).getEntry("/"), "icons/testng/full/"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        catch(MalformedURLException e) {
          ;
        }
      }

      public static TestUtilitiesPlugin getDefault() {
        return plugin;
      }

      public static String getPluginId() {
        return PLUGIN_ID;
      }
      
      public static Shell getActiveWorkbenchShell() {
   		IWorkbenchWindow workBenchWindow= getActiveWorkbenchWindow();
   		if (workBenchWindow == null)
   			return null;
   		return workBenchWindow.getShell();
   	}

   	public static IWorkbenchWindow getActiveWorkbenchWindow() {
   		if (plugin == null)
   			return null;
   		IWorkbench workBench= plugin.getWorkbench();
   		if (workBench == null)
   			return null;
   			return workBench.getActiveWorkbenchWindow();
   	}

      public static PreferenceStoreUtil getPluginPreferenceStore() {
        return plugin.m_preferenceUtil; 
      }
      
      public static void log(String s) {
        log(new Status(IStatus.INFO, getPluginId(), IStatus.OK, s, null)); //$NON-NLS-1$
      }

      public static void log(Throwable e) {
        log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, "Error", e)); //$NON-NLS-1$
      }

      public static void log(IStatus status) {
        getDefault().getLog().log(status);
      }

      /**
       * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
       */
      @Override
      public void start(BundleContext context) throws Exception {
        super.start(context);

        m_context= context;
        ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        launchManager.addLaunchListener(this);
        m_isStopped = false;
        m_preferenceUtil= new PreferenceStoreUtil(getPreferenceStore());
      }

      /**
       * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
       */
      @Override
      public void stop(BundleContext context) throws Exception {
        try {
          // Dispose the font
          if (null != BOLD_FONT) {
            BOLD_FONT.dispose();
          }
          m_isStopped = true;
          ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
          launchManager.removeLaunchListener(this);
        }
        finally {
          super.stop(context);
          m_context= null;
        }
      }

      public static boolean isStopped() {
        return m_isStopped;
      }
      
      /**
       * @see org.eclipse.debug.core.ILaunchListener#launchRemoved(org.eclipse.debug.core.ILaunch)
       */
      public void launchRemoved(final ILaunch launch) {
        n_trackedLaunches.remove(launch);
        asyncExec(new Runnable() {
            public void run() {
              TestRunnerViewPart testRunnerViewPart = findTestRunnerViewPartInActivePage();
              if((testRunnerViewPart != null)
                 && testRunnerViewPart.isCreated()
                 && launch.equals(testRunnerViewPart.getLastLaunch())) {

                testRunnerViewPart.reset();
              }
            }
          });
      }

      public static void asyncExec(Runnable runnable) {
        SWTUtil.getDisplay().asyncExec(runnable);
      }

      /**
       * @see org.eclipse.debug.core.ILaunchListener#launchAdded(org.eclipse.debug.core.ILaunch)
       */
      public void launchAdded(final ILaunch launch) {
        n_trackedLaunches.add(launch);
      }

      /**
       * @see org.eclipse.debug.core.ILaunchListener#launchChanged(org.eclipse.debug.core.ILaunch)
       */
      public void launchChanged(final ILaunch launch) {
        if(!n_trackedLaunches.contains(launch)) {
          return;
        }

        int port = -1;
        String projectName = "";
        String subName = "";
        
        try {
          // test whether the launch defines the JUnit attributes
          port = ConfigurationHelper.getPort(launch);
          projectName = ConfigurationHelper.getProjectName(launch);
          subName = ConfigurationHelper.getSubName(launch);
        }
        catch(Exception ex) {
          log(ex);
          return;
        }

    //  log(new Status(IStatus.WARNING, getPluginId(), IStatus.WARNING, 
    //  "launch a project with NULL name. Ignoring", null));
        if(null == projectName) {
          return;
        }

        final int   finalPort = isDebug() ? Integer.parseInt(RemoteTestNG.DEBUG_PORT) : port;
        final IJavaProject ijp = JDTUtil.getJavaProject(projectName);
        final String name = subName;
        
        n_trackedLaunches.remove(launch);

        asyncExec(new Runnable() {
            public void run() {
              connectTestRunner(launch, ijp, name, finalPort);
            }
          });
      }

      public void connectTestRunner(ILaunch launch, 
                                    IJavaProject launchedProject,
                                    String subName,
                                    int port) {
        IViewPart viewPart = showTestRunnerViewPartInActivePage(findTestRunnerViewPartInActivePage());
        // The returned view part could be an ErrorViewPart or null if something went wrong in the init
        if (viewPart instanceof TestRunnerViewPart) {
          ((TestRunnerViewPart) viewPart).startTestRunListening(launchedProject, subName, port, launch);
        }
      }

      private IViewPart showTestRunnerViewPartInActivePage(TestRunnerViewPart testRunner) {
        IWorkbenchPart activePart = null;
        IWorkbenchPage page = null;
        try {

          // TODO: have to force the creation of view part contents
          // otherwise the UI will not be updated
          if((testRunner != null) && testRunner.isCreated()) {
            return testRunner;
          }

          page = SWTUtil.getActivePage(getWorkbench());

          if(page == null) {
            return null;
          }

          activePart = page.getActivePart();

          //  show the result view if it isn't shown yet
          return page.showView(TestRunnerViewPart.NAME);
        }
        catch(PartInitException pie) {
          log(pie);

          return null;
        }
        finally {

          //restore focus stolen by the creation of the result view
          if((page != null) && (activePart != null)) {
            page.activate(activePart);
          }
        }
      }

      private TestRunnerViewPart findTestRunnerViewPartInActivePage() {
        IWorkbenchPage page = SWTUtil.getActivePage(getWorkbench());
        if(null == page) {
          return null;
        }

        // If something went wrong, the JDT will return an ErrorViewPart, which cannot
        // be case into TestRunnerViewPart
        IViewPart view = page.findView(TestRunnerViewPart.NAME);
        if (view instanceof TestRunnerViewPart) {
          return (TestRunnerViewPart) view;
        } else {
          return null;
        }
      }

      public static URL makeIconFileURL(String name) throws MalformedURLException {
        if(m_fgIconBaseURL == null) {
          throw new MalformedURLException();
        }

        return new URL(m_fgIconBaseURL, name);
      }

      public static ImageDescriptor getImageDescriptor(String relativePath) {
        try {
          return ImageDescriptor.createFromURL(makeIconFileURL(relativePath));
        }
        catch(MalformedURLException e) {

          // should not happen
          return ImageDescriptor.getMissingImageDescriptor();
        }
      }

      private static void ppp(final Object message) {
//        System.out.println("[TestNG]:- " + message);
      }

      // Assume that all the controls that go through bold() have the same font, 
      // which is probably a safe assumption.  The BOLD font will be disposed
      // in the stop() method
      public static Font BOLD_FONT = null;
      public static Font REGULAR_FONT = null;

      /**
       * Make the passed label bold or not based on the boolean.  This method
       * is put here because it initializes BOLD_FONT, which is later disposed
       * in the stop method of this class
       */
      public static void bold(Control label, boolean bold) {
        if (null == BOLD_FONT) {
          REGULAR_FONT = label.getFont();
          FontData data = REGULAR_FONT.getFontData() [0];
          data.setStyle(data.getStyle() | SWT.BOLD);
          BOLD_FONT = new Font(label.getDisplay(), data);
        }
        
        label.setFont(bold ? BOLD_FONT : REGULAR_FONT);
      }
      
      public Bundle getBundle(String bundleName) {
        Bundle bundle= Platform.getBundle(bundleName);
        if (bundle != null)
            return bundle;
        
        // Accessing unresolved bundle
        ServiceReference serviceRef= m_context.getServiceReference(PackageAdmin.class.getName());
        PackageAdmin admin= (PackageAdmin) m_context.getService(serviceRef);
        Bundle[] bundles= admin.getBundles(bundleName, null);
        if (bundles != null && bundles.length > 0)
            return bundles[0];
        return null;
      }
      
      public static boolean isEmtpy(String string) {
        return null == string || "".equals(string.trim());
      } 
      
      /**
       * Returns a String that can be used as an identifying key for 
       * a system property set when tests fail. The value of the property
       * is the comma-separated results, if any, of calling getTestName() on the failed
       * tests. This can be used by @Factory implementations to decide 
       * which instances to construct.
       * @return
       */
      public static String getFailedTestsKey() {
    	  return TestNGPluginConstants.S_FAILED_TESTS;
      }

      public static boolean isVerbose() {
        return RemoteTestNG.isVerbose();
      }

      public static boolean isDebug() {
        return RemoteTestNG.isDebug();
      }
}
