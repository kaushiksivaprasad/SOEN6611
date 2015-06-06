package gr.uom.java.jdeodorant.refactoring.views;

import gr.uom.java.ast.ASTReader;
import gr.uom.java.ast.CompilationUnitCache;
import gr.uom.java.ast.SystemObject;
import gr.uom.java.ast.metrics.AIF;
import gr.uom.java.ast.metrics.CF;
import gr.uom.java.ast.metrics.HidingFactor;
import gr.uom.java.ast.metrics.MIF;
import gr.uom.java.ast.metrics.PF;
import gr.uom.java.ast.util.ProjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

public class MetricsAction implements IObjectActionDelegate {

	private IWorkbenchPart part;
	private ISelection selection;

	private IJavaProject selectedProject;
	private IPackageFragmentRoot selectedPackageFragmentRoot;
	private IPackageFragment selectedPackageFragment;
	private ICompilationUnit selectedCompilationUnit;
	private IType selectedType;
	private IMethod selectedMethod;
	private static PrintWriter writer;

	public void run(IAction arg0) {

		try {
			writer = new PrintWriter(new FileOutputStream(new File(
					"e://logMetric.txt"), true));
			JavaCore.addElementChangedListener(new ElementChangedListener());
			CompilationUnitCache.getInstance().clearCache();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				Object element = structuredSelection.getFirstElement();
				if (element instanceof IJavaProject) {
					selectedProject = (IJavaProject) element;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedType = null;
					selectedMethod = null;
				} else if (element instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot) element;
					selectedProject = packageFragmentRoot.getJavaProject();
					selectedPackageFragmentRoot = packageFragmentRoot;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedType = null;
					selectedMethod = null;
				} else if (element instanceof IPackageFragment) {
					IPackageFragment packageFragment = (IPackageFragment) element;
					selectedProject = packageFragment.getJavaProject();
					selectedPackageFragment = packageFragment;
					selectedPackageFragmentRoot = null;
					selectedCompilationUnit = null;
					selectedType = null;
					selectedMethod = null;
				} else if (element instanceof ICompilationUnit) {
					ICompilationUnit compilationUnit = (ICompilationUnit) element;
					selectedProject = compilationUnit.getJavaProject();
					selectedCompilationUnit = compilationUnit;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedType = null;
					selectedMethod = null;
				} else if (element instanceof IType) {
					IType type = (IType) element;
					selectedProject = type.getJavaProject();
					selectedType = type;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedMethod = null;
				} else if (element instanceof IMethod) {
					IMethod method = (IMethod) element;
					selectedProject = method.getJavaProject();
					selectedMethod = method;
					selectedPackageFragmentRoot = null;
					selectedPackageFragment = null;
					selectedCompilationUnit = null;
					selectedType = null;
				}
				IWorkbench wb = PlatformUI.getWorkbench();
				IProgressService ps = wb.getProgressService();
				ps.busyCursorWhile(new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						if (ASTReader.getSystemObject() != null
								&& selectedProject.equals(ASTReader
										.getExaminedProject())) {
							new ASTReader(selectedProject, ASTReader
									.getSystemObject(), monitor);
						} else {
							new ASTReader(selectedProject, monitor);
						}
						SystemObject system = ASTReader.getSystemObject();
						ProjectUtils.loadProjectDetails(system);
						HidingFactor h = new HidingFactor(system);

						PF pf = new PF(system);

						CF cf = new CF(system);

						MIF mif = new MIF(system);

						AIF aif = new AIF(system);

						StringBuilder builder = new StringBuilder();
						builder.append("Metrics :\n");
						builder.append(h.systemMHFValue + "\n");
						builder.append("AHF:\t");
						builder.append(h.systemAHFValue + "\n");
						builder.append("Mif:\t");
						builder.append(mif + "\n");
						builder.append("AIF:\t");
						builder.append(aif + "\n");
						builder.append("PF:\t");
						builder.append(pf + "\n");
						builder.append("CF:\t");
						builder.append(cf + "\n");
						builder.append("No Of Classes:\t"
								+ ProjectUtils.totNumberOfClasses + "\n");
						builder.append("Avg. No of Methods:\t"
								+ (ProjectUtils.totNumberOfMethods / (float) ProjectUtils.totNumberOfClasses)
								+ "\n");
						builder.append("System NOH:\t"+ProjectUtils.totHierarchies+"\n");
						builder.append("Avg. NOC:\t"+ProjectUtils.avgNOC+"\n");
						builder.append("*****END Of METRIC*****");
						
						printLog(builder.toString());
						if (selectedPackageFragmentRoot != null) {
							// package fragment root selected
						} else if (selectedPackageFragment != null) {
							// package fragment selected
						} else if (selectedCompilationUnit != null) {
							// compilation unit selected
						} else if (selectedType != null) {
							// type selected
						} else if (selectedMethod != null) {
							// method selected
						} else {
							// java project selected
						}
					}
				});
			}
			writer.close();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.part = targetPart;
	}

	public static void printLog(String lineToWrite) {
		System.out.println(lineToWrite);
		writer.append(lineToWrite);
	}
}
