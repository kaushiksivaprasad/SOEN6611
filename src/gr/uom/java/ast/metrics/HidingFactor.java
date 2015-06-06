package gr.uom.java.ast.metrics;

import gr.uom.java.ast.Access;
import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.FieldObject;
import gr.uom.java.ast.MethodObject;
import gr.uom.java.ast.SystemObject;
import gr.uom.java.ast.util.ProjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class HidingFactor {
	public HashMap<String, Double> mhfValueForClass = new HashMap<String, Double>();
	public Double systemMHFValue = 0.0;
	public HashMap<String, Double> ahfValueForClass = new HashMap<String, Double>();
	public Double systemAHFValue = 0.0;
	public Long totAttributes = 0l;
	public HidingFactor(SystemObject obj) {
		ListIterator<ClassObject> classIterator = obj.getClassListIterator();
		while (classIterator.hasNext()) {
			ClassObject classObject = classIterator.next();
			if(!classObject.isInterface()){
//				System.out.println(classObject.getName());
				Double mhfValue = computeMHFForClass(classObject);
				Double ahfValue = computeAHFForClass(classObject);
//				System.out.println("Final Method Hiding value :"+mhfValue);
//				System.out.println("Final Attribute Hiding value :"+ahfValue);
				ahfValueForClass.put(classObject.getName(), ahfValue);
				mhfValueForClass.put(classObject.getName(),mhfValue);
				systemMHFValue += mhfValue;
				systemAHFValue += ahfValue;
			}
		}
		systemMHFValue = systemMHFValue/(ProjectUtils.totNumberOfMethods * ProjectUtils.totNumberOfClasses);
		systemAHFValue = systemAHFValue/(ProjectUtils.totNumberOfClasses * totAttributes);
//		System.out.println("Number of methods : "+ ProjectUtils.totNumberOfMethods);
//		System.out.println("Number of Attributes : "+ totAttributes);
//		System.out.println("System MHF : "+systemMHFValue);
//		System.out.println("System AHF : "+systemAHFValue);
	}

	public double computeMHFForClass(ClassObject classObject) {
		List<MethodObject> methods = classObject.getMethodList();
		Long totVisibility = 0l;
		for (MethodObject method : methods) {
			totVisibility += getVisibilityValue(method, classObject.getName());
		}
//		System.out.println("Method visibility : "+totVisibility);
		return totVisibility / (double) (ProjectUtils.totNumberOfClasses - 1);
	}
	
	public double computeAHFForClass(ClassObject classObject){
		ListIterator<FieldObject> fields = classObject.getFieldIterator();
		Long totVisibility = 0l;
		while (fields.hasNext()) {
			totVisibility += getVisibilityValue(fields.next(), classObject.getName());
			totAttributes++;
		}
//		System.out.println("Atribute Visibility : "+totVisibility);
		return totVisibility / (double) (ProjectUtils.totNumberOfClasses - 1);
	}

	public int getVisibilityValue(Object obj, String className) {
		Access access = null;
		if (obj instanceof MethodObject) {
			access = ((MethodObject) obj).getAccess();
		} else {
			access = ((FieldObject) obj).getAccess();
		}
		if (access != null) {
			if (access.equals(Access.PUBLIC)) {
				return ProjectUtils.totNumberOfClasses - 1;
			} else if (access.equals(Access.PROTECTED)) {
				Set<String> children = ProjectUtils.childrenMap
						.get(className);
				int totNoOfChildren = 0;
				if (children != null) {
					totNoOfChildren = children.size();
				}
				return totNoOfChildren;
			} else if (access.equals(Access.NONE)) {
				return ProjectUtils.packageDetails.get(ProjectUtils
						.extractPackageNameFromWholeClassName(className)).size() - 1;
			}
		}
		return 0;
	}
}
