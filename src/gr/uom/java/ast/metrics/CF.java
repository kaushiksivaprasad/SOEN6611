package gr.uom.java.ast.metrics;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.SystemObject;


public class CF {

	double TC,numerator;
	double totalCF;
	Set<String> doneClasses = new HashSet<String>();
	
	public CF(SystemObject system) {
		//classes = system.getClassObjects();
		
		ListIterator<ClassObject> iterator1 = system.getClassListIterator();
		ListIterator<ClassObject> iterator2 = system.getClassListIterator();
		
		while(iterator1.hasNext()) {
			TC++;
			ClassObject classObject1 = iterator1.next();
			doneClasses.add(classObject1.getName());
			iterator2 = system.getClassListIterator();
			while(iterator2.hasNext()) {
				ClassObject classObject2 = iterator2.next();
				if(!doneClasses.contains(classObject2.getName())){
					if(classObject2.equals(classObject1) || classObject2.isFriend(classObject1.getName())||
							classObject1.equals(classObject2) || classObject1.isFriend(classObject2.getName()))
					{
						numerator++;
					}
						
				}
			}
		}
		//System.out.println(TC);
		//System.out.println(numerator);
		totalCF=numerator/((TC*TC)-TC);
	}

	
	
	public String toString() {
		return totalCF+"";
	}

}