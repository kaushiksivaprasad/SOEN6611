package gr.uom.java.ast.util;

import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.MethodObject;
import gr.uom.java.ast.SystemObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ProjectUtils {
	/**
	 * This variable has details about all the children given a class's name.
	 */
	public static HashMap<String, LinkedList<String>> inheritanceTree = new HashMap<String, LinkedList<String>>();
	/**
	 * This variable has details about all the packages available in the project
	 * and also classes within it.
	 */
	public static HashMap<String, Set<String>> packageDetails = new HashMap<String, Set<String>>();
	/**
	 * This variable has details about all the methods of a class`s name.
	 */
	public static HashMap<String, List<MethodObject>> methodsOfClass = new HashMap<String, List<MethodObject>>();
	/**
	 * To get the number of classes in the system.
	 */
	public static int totNumberOfClasses = 0;
	
	/**
	 * Get the total number of methods in the system..
	 */
	public static int totNumberOfMethods = 0;
	
	private static Set<String> processedClasses = new HashSet<String>();

	public static void loadProjectDetails(SystemObject obj) {
		ListIterator<ClassObject> classIterator = obj.getClassListIterator();
		while (classIterator.hasNext()) {
			ClassObject classObject = classIterator.next();
			loadInheritanceDetails(classObject);
			extractPackageLevelDetails(classObject);
			totNumberOfClasses++;
		}
	}

	private static void loadInheritanceDetails(ClassObject classObject) {
		String ancestorName = null;
		String key = classObject.getName().trim();
		if (classObject.getSuperclass() != null) {
			ancestorName = classObject.getSuperclass().toString();
		}
		LinkedList<String> children = null;
		if (ancestorName != null && ancestorName.trim().length() > 0) {
			// if the current class is inherited, ancestorName will not be null
			children = inheritanceTree.get(ancestorName);
			if (children == null) {
				children = new LinkedList<String>();
			}
			children.add(key);
			key = ancestorName.trim();
		}
		inheritanceTree.put(key, children);
		methodsOfClass.put(key, classObject.getMethodList());
		totNumberOfMethods += classObject.getMethodList().size();
	}

	private static void extractPackageLevelDetails(ClassObject classObject) {
		String name = classObject.getName().trim();
		String packageName = extractPackageNameFromWholeClassName(name);
		if (packageName != null) {
			Set<String> classesSet = packageDetails.get(packageName);
			if (classesSet == null) {
				classesSet = new HashSet<String>();
			}
			classesSet.add(name);
			processedClasses.add(name);
			packageDetails.put(packageName, classesSet);
		}
	}

	/**
	 * This method extracts packageName out of the whole className and returns
	 * it.
	 * 
	 * @param wholeClassName
	 * @return
	 */
	public static String extractPackageNameFromWholeClassName(
			String wholeClassName) {
		if (wholeClassName != null && wholeClassName.length() > 0) {
			int indx = wholeClassName.lastIndexOf(".");
			if (indx != -1) {
				String packageName = wholeClassName.substring(0, indx);
				if(processedClasses.contains(packageName)){ 
					//check for inner classes.
					return extractPackageNameFromWholeClassName(packageName);
				}
				return wholeClassName.substring(0, indx);
			}
			//default package case..
			else{
				return "default";
			}
		}
		return null;
	}
	
}
