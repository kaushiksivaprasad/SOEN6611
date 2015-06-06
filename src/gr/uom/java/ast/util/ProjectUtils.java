package gr.uom.java.ast.util;

import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.SystemObject;
import gr.uom.java.ast.TypeObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class ProjectUtils {
	/**
	 * This variable has details about all the children given a class's name.
	 */
	public static HashMap<String, HashSet<String>> childrenMap = new HashMap<String, HashSet<String>>();
	/**
	 * This variable has details about all the immediate children a class has.
	 */
	private static HashMap<String, HashSet<String>> immediateChildrenGraph = new HashMap<String, HashSet<String>>();
	/**
	 * This variable has details about all the packages available in the project
	 * and also classes within it.
	 */
	public static HashMap<String, Set<String>> packageDetails = new HashMap<String, Set<String>>();
	/**
	 * To get the number of classes in the system.
	 */
	public static int totNumberOfClasses = 0;
	/**
	 * Get the total number of methods in the system..
	 */
	public static int totNumberOfMethods = 0;

	private static boolean ranOnce = false;

	private static Set<String> processedClasses = new HashSet<String>();

	private static Set<ClassObject> uniqueBaseClasses = new HashSet<ClassObject>();

	public static Long totHierarchies = 0l;
	
	public static Double avgNOC = 0.0;

	public static void loadProjectDetails(SystemObject obj) {
		ranOnce = false;
		reInitializeDS();
		ListIterator<ClassObject> classIterator = obj.getClassListIterator();
		while (classIterator.hasNext()) {
			ClassObject classObject = classIterator.next();
			if (!classObject.isInterface()) {
				getTotNoOfChildren(classObject, new HashSet<String>(), obj);
				loadInheritanceDetails(classObject, obj);
				extractPackageLevelDetails(classObject);
				totNumberOfClasses++;
				totNumberOfMethods += classObject.getMethodList().size();
			}
		}
		calculateNOCForSystem();
		ranOnce = true;
		
	}
	
	private static void calculateNOCForSystem(){
		Long totChildrenForSystem = 0l;
		for(Map.Entry<String, HashSet<String>> entry : immediateChildrenGraph.entrySet()){
			totChildrenForSystem += entry.getValue().size();
		}
		avgNOC = totChildrenForSystem/(double) ProjectUtils.totNumberOfClasses;
	}
	private static void reInitializeDS(){
		childrenMap = new HashMap<String, HashSet<String>>();
		immediateChildrenGraph = new HashMap<String, HashSet<String>>();
		packageDetails = new HashMap<String, Set<String>>();
		totNumberOfClasses = 0;
		totNumberOfMethods = 0;
		ranOnce = false;
		processedClasses = new HashSet<String>();
		uniqueBaseClasses = new HashSet<ClassObject>();
		totHierarchies = 0l;
		avgNOC = 0.0;
	}

	private static void getTotNoOfChildren(ClassObject classObj,
			Set<String> childClasses, SystemObject sysObj) {
		TypeObject superClass = classObj.getSuperclass();
		if (superClass != null) {
			String ancestorName = superClass.getClassType().trim();
			ClassObject superClassObj = sysObj.getClassObject(ancestorName);
			if (superClassObj != null) {
				// to avoid java inbuilt classes..
				HashSet<String> children = childrenMap.get(ancestorName);
				if (children == null) {
					children = new HashSet<String>();
				}
				children.add(classObj.getName());
				children.addAll(childClasses);
				childrenMap.put(ancestorName, children);
				childClasses.add(classObj.getName());
				getTotNoOfChildren(superClassObj, childClasses, sysObj);
			}
		} else {
			if (!uniqueBaseClasses.contains(classObj)) {
				uniqueBaseClasses.add(classObj);
				totHierarchies++;
			}
		}
	}

	private static void loadInheritanceDetails(ClassObject classObject,
			SystemObject sysObj) {
		String ancestorName = null;
		String key = classObject.getName().trim();
		if (classObject.getSuperclass() != null) {
			ancestorName = classObject.getSuperclass().toString();
		}
		HashSet<String> children = null;
		if (ancestorName != null && ancestorName.trim().length() > 0) {
			// if the current class is inherited, ancestorName will not be null
			children = immediateChildrenGraph.get(ancestorName);
			if (children == null) {
				children = new HashSet<String>();
			}
			children.add(key);
			if (sysObj.getClassObject(ancestorName) != null)
				immediateChildrenGraph.put(ancestorName.trim(), children);
		}
	}

	private static void extractPackageLevelDetails(ClassObject classObject) {
		String name = classObject.getName().trim();
		String packageName = extractPackageNameFromWholeClassNameInteranal(name);
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

	private static String extractPackageNameFromWholeClassNameInteranal(
			String wholeClassName) {
		if (wholeClassName != null && wholeClassName.length() > 0) {
			int indx = wholeClassName.lastIndexOf(".");
			if (indx != -1) {
				String packageName = wholeClassName.substring(0, indx);
				if (processedClasses.contains(packageName)) {
					// check for inner classes.
					return extractPackageNameFromWholeClassName(packageName);
				}
				return wholeClassName.substring(0, indx);
			}
			// default package case..
			else {
				return "default";
			}
		}
		return null;
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
		if (wholeClassName != null && wholeClassName.length() > 0 && ranOnce) {
			int indx = wholeClassName.lastIndexOf(".");
			if (indx != -1) {
				String packageName = wholeClassName.substring(0, indx);
				if (!packageDetails.containsKey(packageName)) {
					// check for inner classes.
					return extractPackageNameFromWholeClassName(packageName);
				}
				return wholeClassName.substring(0, indx);
			}
			// default package case..
			else {
				return "default";
			}
		}
		return null;
	}

}
