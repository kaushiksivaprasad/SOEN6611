package gr.uom.java.ast.metrics;

import gr.uom.java.ast.Access;
import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.MethodObject;
import gr.uom.java.ast.SystemObject;
import gr.uom.java.ast.TypeObject;
import gr.uom.java.ast.util.ProjectUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class MIF {

	HashMap<String, LinkedList<String>> inheritanceTree;
	private static int totalNumberOfMethodsInherited = 0;
	private static int totalNumberOfMethodsDeclared = 0;
	static double finalMethodInheritanceFactor;
	public MIF(SystemObject system) {

		ListIterator<ClassObject> iterator = system.getClassListIterator();
		while (iterator.hasNext()) {
			ClassObject classObject = iterator.next();		
			
			// method call to get inherited methods of the class
			Set<MethodObject> inheritedMethods = getInheritedMethods(system, classObject);

			List<MethodObject> presentClassMethods = classObject
					.getMethodList();
			for (MethodObject presentClassMethod : presentClassMethods) {
				if (presentClassMethod.overridesMethod()) {
					inheritedMethods.remove(presentClassMethod);
				}
			}
			//System.out.println("Inherited methods for class "+classObject.getName() +":" +inheritedMethods);
			
			// Count total number of methods
			totalNumberOfMethodsInherited = totalNumberOfMethodsInherited + inheritedMethods.size();
			
			// Count of methods declared in the present class
			totalNumberOfMethodsDeclared = totalNumberOfMethodsDeclared + classObject.getMethodList().size();
		}
		
		//System.out.println("Total number of inherited methods :"+totalNumberOfMethodsInherited);
		
		int inheritedPlusDeclared = totalNumberOfMethodsInherited + totalNumberOfMethodsDeclared;
		
		finalMethodInheritanceFactor = (double) totalNumberOfMethodsInherited / inheritedPlusDeclared;
		
		//System.out.println("Final value of MIF :"+ finalMethodInheritanceFactor);
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Final value of MIF :"+ finalMethodInheritanceFactor;
	}
	
	public Set<MethodObject> getInheritedMethods(SystemObject system, ClassObject classObject) {
		Set<MethodObject> inheritedMethods = new HashSet<MethodObject>();
		Set<String> classesInPackage = ProjectUtils.packageDetails
				.get(ProjectUtils
						.extractPackageNameFromWholeClassName(classObject
								.getName()));
		TypeObject superClass = classObject.getSuperclass();
		while (superClass != null && system.getClassObject(superClass
				.getClassType())!=null) {

			ClassObject superClassObject = system.getClassObject(superClass
					.getClassType());
			List<MethodObject> superClassMethods = superClassObject
					.getMethodList();
			for (MethodObject method : superClassMethods) {
				if (method.getAccess().equals(Access.PUBLIC)
						|| method.getAccess().equals(Access.PROTECTED)
						&& !method.isStatic()
						|| (method.getAccess().equals(Access.NONE) && classesInPackage
								.contains(superClassObject.getName()))) {
					inheritedMethods.add(method);
				}
			}
			superClass = superClassObject.getSuperclass();
		}
		
		return inheritedMethods;
	}
}
