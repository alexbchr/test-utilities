package com.alexbchr.testutilities.priorj.plugin.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.alexbchr.testutilities.priorj.plugin.ui.PriorJUILog;
import com.alexbchr.testutilities.priorj.plugin.ui.views.PrioritizationView;

public class OpenPrioritizationViewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//Get the active window
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		if (window == null)
		 return null;
		 
		//Get the active page
		IWorkbenchPage page = window.getActivePage();

		if (page == null)
			return null;
			
		//Open and active the Prioritization View

		try {
			page.showView(PrioritizationView.ID);
		}
		catch (PartInitException e){
			PriorJUILog.logError("Failed to open the Prioritization view", e);
		}

		return null;

	}

}
