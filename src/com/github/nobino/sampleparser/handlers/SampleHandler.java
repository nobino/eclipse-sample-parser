package com.github.nobino.sampleparser.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		ITreeSelection tree;
		
		if (selection instanceof ITreeSelection) {
			tree = (ITreeSelection) selection;
		} else {
			System.out.println("selection does not implement ITreeSelection.");
			return null;
		}
		
		List<ICompilationUnit> compilationUnits = getCompilationUnits(tree);
		
		if(compilationUnits.isEmpty()) {
			System.out.println("tree has no CompilationUnit.");
			return null;
		}

		ICompilationUnit compilationUnit = compilationUnits.get(0);
		
		List<IType> types = getTypes(compilationUnit); 

		for(IType type : types) {
			System.out.println(type.toString());
		}
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"Sampleparser",
				selection.toString());
		return null;
	}
	
	private List<ICompilationUnit> getCompilationUnits(ITreeSelection tree) {
		List<ICompilationUnit> results = new ArrayList<>();
		
		@SuppressWarnings("rawtypes")
		Iterator iterator = tree.iterator();
		while(iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof ICompilationUnit) {
				results.add((ICompilationUnit) next);
			}
		}
		
		return results;
	}
	
	private List<IType> getTypes(ICompilationUnit compilationUnit) {
		try {
			return Arrays.asList(compilationUnit.getTypes());
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
