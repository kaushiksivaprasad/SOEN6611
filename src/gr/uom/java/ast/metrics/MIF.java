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

	public MIF(SystemObject system) {

		ListIterator<ClassObject> iterator = system.getClassListIterator();
		while (iterator.hasNext()) {
			ClassObject classObject = iterator.next();
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

			List<MethodObject> presentClassMethods = classObject
					.getMethodList();
			for (MethodObject presentClassMethod : presentClassMethods) {
				if (presentClassMethod.overridesMethod()) {
					inheritedMethods.remove(presentClassMethod);
				}
			}
			System.out.println(inheritedMethods);
		}

		// inheritanceTree = ProjectUtils.inheritanceTree;
		//
		// Iterator hashMapIterator = inheritanceTree.entrySet().iterator();
		//
		// while(hashMapIterator.hasNext()) {
		// Map.Entry entry = (Map.Entry)hashMapIterator.next();
		// System.out.println(entry.getKey() +" " +entry.getValue());
		//
		// ClassObject classValue = (ClassObject) entry.getKey();
		// classValue.getSuperclass();
		//
		// }

		// System.out.println(inheritanceTree);
	}
}
