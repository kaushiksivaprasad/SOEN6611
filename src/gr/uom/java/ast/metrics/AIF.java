package gr.uom.java.ast.metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import gr.uom.java.ast.Access;
import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.FieldObject;
import gr.uom.java.ast.MethodObject;
import gr.uom.java.ast.SystemObject;
import gr.uom.java.ast.TypeObject;
import gr.uom.java.ast.util.ProjectUtils;

public class AIF {

	HashMap<String, LinkedList<String>> inheritanceTree;
	private static int totalNumberOfAttributesInherited = 0;
	private static int totalNumberOfMethodsDeclared = 0;
	private static int totalDefinedAttributeCount = 0;
	
	public AIF(SystemObject system) {
		ListIterator<ClassObject> iterator = system.getClassListIterator();
		while (iterator.hasNext()) {
			ClassObject classObject = iterator.next();		
			
			// method call to get inherited methods of the class
			Set<FieldObject> inheritedAttributes = getInheritedAttibutes(system, classObject);
			
			ListIterator<FieldObject> presentClassFields = classObject.getFieldIterator();
			while (presentClassFields.hasNext()) {				
				FieldObject presentClassField = presentClassFields.next();
				
				if (inheritedAttributes.contains(presentClassField)) {
					inheritedAttributes.remove(presentClassField);
				}
				totalDefinedAttributeCount++;
			}
			System.out.println("Inherited methods for class "+classObject.getName() +":" +inheritedAttributes);
			
			// Count total number of methods
			totalNumberOfAttributesInherited = totalNumberOfAttributesInherited + inheritedAttributes.size();
			
//			// Count of methods declared in the present class
//			totalNumberOfMethodsDeclared = totalNumberOfMethodsDeclared + classObject.getFieldIterator().size();
		}
		
		System.out.println("Total number of inherited attributes :"+totalNumberOfAttributesInherited);
		
		int inheritedPlusDeclared = totalNumberOfAttributesInherited + totalDefinedAttributeCount;
		
		double finalAttributeInheritanceFactor = (double) totalNumberOfAttributesInherited / inheritedPlusDeclared;
		
		System.out.println("Final value of MIF :"+ finalAttributeInheritanceFactor);
	}

	private Set<FieldObject> getInheritedAttibutes(SystemObject system,
			ClassObject classObject) {
		
		Set<FieldObject> inheritedFields = new HashSet<FieldObject>();
		Set<String> classesInPackage = ProjectUtils.packageDetails
				.get(ProjectUtils
						.extractPackageNameFromWholeClassName(classObject
								.getName()));
		TypeObject superClass = classObject.getSuperclass();
		while (superClass != null && system.getClassObject(superClass
				.getClassType())!=null) {

			ClassObject superClassObject = system.getClassObject(superClass
					.getClassType());
			 ListIterator<FieldObject> superClassFields = superClassObject
					.getFieldIterator();
//			for ( FieldObject field : superClassFields) {
			while(superClassFields.hasNext()) {
				FieldObject field = superClassFields.next();
				
			 if (field.getAccess().equals(Access.PUBLIC)
						|| field.getAccess().equals(Access.PROTECTED)
						&& !field.isStatic()
						|| (field.getAccess().equals(Access.NONE) && classesInPackage
								.contains(superClassObject.getName()))) {
					inheritedFields.add(field);
				}
			}
			superClass = superClassObject.getSuperclass();
		}
		
		return inheritedFields;
	}
}
