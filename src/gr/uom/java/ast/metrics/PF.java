package gr.uom.java.ast.metrics;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.MethodObject;
import gr.uom.java.ast.SystemObject;

import java.util.List;


import gr.uom.java.ast.util.ProjectUtils;

public class PF {

	int overriddenMethods,newMethods,descendingClasses;
	double denominator;
	int newM_overrideM_count[];
	double totalPF;
	double classPF;
	Set<ClassObject> classes;

	public PF(SystemObject system) {
		classes = system.getClassObjects();
		ListIterator<ClassObject> iterator = system.getClassListIterator();
		while(iterator.hasNext()) {
			ClassObject classObject = iterator.next();
			newM_overrideM_count=getOverridenNewMethodCount(classObject);
			overriddenMethods+=newM_overrideM_count[0];
			newMethods=newM_overrideM_count[1];
			Set<String> llClass=ProjectUtils.childrenMap.get(classObject.getName());
			
			if(llClass!=null)
			{	descendingClasses=llClass.size(); 
			//System.out.println("asdsada"+descendingClasses);
			}
			else 
				descendingClasses=0;
			
			
			denominator+=(newMethods*descendingClasses);
			//System.out.println("denominator"+denominator);
		}
		if(denominator<=0)
			totalPF=0;
		else
			totalPF=overriddenMethods/(denominator);
	}

	
	public int[] getOverridenNewMethodCount(ClassObject classObject) {
		int result[]={0,0};
		List<MethodObject> methods = classObject.getMethodList();
		
		for (MethodObject methodObject : methods) {
			if (!methodObject.overridesMethod()) {
				result[0]++;
			}
			else{
				result[1]++;
			}
		}
		return result;
	}
	

	
	
	public String toString() {
		return totalPF+"";
	}

}