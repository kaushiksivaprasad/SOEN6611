package gr.uom.java.ast.metrics;

import gr.uom.java.ast.Access;
import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.FieldObject;
import gr.uom.java.ast.SystemObject;
import gr.uom.java.ast.TypeObject;
import gr.uom.java.ast.util.ProjectUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

public class AIF {

	HashMap<String, LinkedList<String>> inheritanceTree;
	private static int totalNumberOfAttributesInherited = 0;
	private static int totalNumberOfMethodsDeclared = 0;
	private static int totalDefinedAttributeCount = 0;
	static int inheritedPlusDeclared=0;
	static double finalAttributeInheritanceFactor;
	public AIF(SystemObject system) {
		ListIterator<ClassObject> iterator = system.getClassListIterator();
		FieldObject presentClassField;
		inheritedPlusDeclared = 0;
		while (iterator.hasNext()) {
			ClassObject classObject = iterator.next();		
			
			if(classObject.isInterface()){
				continue;
			}
			totalDefinedAttributeCount =0;
			// method call to get inherited methods of the class
			Set<String> inheritedAttributes = getInheritedAttibutes(system, classObject);
			
			ListIterator<FieldObject> presentClassFields = classObject.getFieldIterator();
			while (presentClassFields.hasNext()) {				
				presentClassField = presentClassFields.next();
								
				if (inheritedAttributes.contains(presentClassField.toString())) {
					
					inheritedAttributes.remove(presentClassField.toString());
					presentClassFields.remove();
				}
					totalDefinedAttributeCount++;
			}
			//System.out.println("Inherited methods for class "+classObject.getName() +":" +inheritedAttributes);
			
			// Count total number of methods
			totalNumberOfAttributesInherited = totalNumberOfAttributesInherited + inheritedAttributes.size();
			
			inheritedPlusDeclared = inheritedPlusDeclared + (inheritedAttributes.size() + totalDefinedAttributeCount);
			
//			// Count of methods declared in the present class
//			totalNumberOfMethodsDeclared = totalNumberOfMethodsDeclared + classObject.getFieldIterator().size();
		}
		
		//System.out.println("Total number of inherited attributes :"+totalNumberOfAttributesInherited);
		
//		int inheritedPlusDeclared = totalNumberOfAttributesInherited + totalDefinedAttributeCount;
		
		finalAttributeInheritanceFactor = (double) totalNumberOfAttributesInherited / inheritedPlusDeclared;
		
//		System.out.println("Total defined attribute :" + totalDefinedAttributeCount);
		//System.out.println();
		//System.out.println("Final value of AIF :"+ totalNumberOfAttributesInherited +"/"+
		//                      inheritedPlusDeclared +"="+finalAttributeInheritanceFactor);
	}
	@Override
	public String toString() {
		return finalAttributeInheritanceFactor+"";
	}

	private Set<String> getInheritedAttibutes(SystemObject system,
			ClassObject classObject) {
		
		Set<String> inheritedFields = new HashSet<String>();
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
					inheritedFields.add(field.toString());
				}
			}
			superClass = superClassObject.getSuperclass();
		}
		
		return inheritedFields;
	}
}
