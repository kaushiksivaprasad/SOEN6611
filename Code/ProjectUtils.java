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
     * This variable has details about all the packages available in the project and also classes within it.
     */
    public static HashMap<String, Set<String>> packageDetails = new HashMap<String, Set<String>>();
    /**
     * This variable has details about all the methods of a class`s name.
     */
    public static HashMap<String, List<MethodObject>> methodsOfClass = new HashMap<String, List<MethodObject>>();
    
    
    public static void loadProjectDetails(SystemObject obj){
        ListIterator<ClassObject> classIterator = obj.getClassListIterator();
        while(classIterator.hasNext()){
            ClassObject classObject = classIterator.next();
            loadInheritanceDetails(classObject);
            
        }
    }
    
    public static void loadInheritanceDetails(ClassObject classObject){
        String ancestorName = null;
        String key = classObject.getName().trim();
        if(classObject.getSuperclass() != null){
            ancestorName = classObject.getSuperclass().toString();
        }
        LinkedList<String> children = null;
        if(ancestorName != null && ancestorName.trim().length() > 0){
            //if the current class is inherited, ancestorName will not be null
            children = inheritanceTree.get(ancestorName);
            if(children == null){
                children = new LinkedList<String>();
            }
            children.add(ancestorName);
            key = ancestorName.trim();
        }
        inheritanceTree.put(key, children);
        methodsOfClass.put(key, classObject.getMethodList());
    }
    public static void extractPackageLevelDetails(ClassObject classObject){
        String name = classObject.getName().trim();
        String packageName = extractPackageNameFromWholeClassName(name);
        if(packageName != null){
            Set<String> classesSet = packageDetails.get(packageName);
            if(classesSet == null){
                classesSet = new HashSet<String>();
            }
            classesSet.add(name);
            packageDetails.put(packageName, classesSet);
        }
    }
    /**
     * This method extracts packageName out of the whole className and returns it.
     * @param wholeClassName
     * @return
     */
    public static String extractPackageNameFromWholeClassName(String wholeClassName){
        if(wholeClassName != null){
            int indx = wholeClassName.lastIndexOf(".");
            if(indx < wholeClassName.length() - 1){
                return wholeClassName.substring(0,indx);
            }
        }
        return null;
    }
}
