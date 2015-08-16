package com.alexbchr.testutilities.internal.wizards;

import org.eclipse.osgi.util.NLS;

public final class WizardMessages extends NLS {
	private static final String BUNDLE_NAME= "com.alexbchr.testutilities.wizards.internal.WizardMessages";//$NON-NLS-1$

	private WizardMessages() {
		// Do not instantiate
	}
	
	public static String Wizard_title_new_testcase;
	public static String ExceptionDialog_seeErrorLogMessage;
	public static String NewJUnitWizard_op_error_title;
	public static String NewJUnitWizard_op_error_message;
	public static String NewTestCaseCreationWizard_create_progress;
	public static String NewTestCaseCreationWizard_fix_selection_junit4_description;
	public static String NewTestCaseCreationWizard_fix_selection_not_now;
	public static String NewTestCaseCreationWizard_fix_selection_open_build_path_dialog;
	public static String NewTestCaseCreationWizard_fix_selection_invoke_fix;
	public static String NewTestCaseWizardPageOne_title;
	public static String NewTestCaseWizardPageOne_description;
	public static String NewTestCaseWizardPageOne_methodStub_setUp;
	public static String NewTestCaseWizardPageOne_methodStub_tearDown;
	public static String NewTestCaseWizardPageOne_methodStub_tearDownAfterClass;
	public static String NewTestCaseWizardPageOne_methodStub_constructor;
	public static String NewTestCaseWizardPageOne_methodStub_setUpBeforeClass;
	public static String NewTestCaseWizardPageOne_method_Stub_label;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, WizardMessages.class);
	}
}
