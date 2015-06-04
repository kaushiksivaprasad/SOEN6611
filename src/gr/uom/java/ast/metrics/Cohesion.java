package gr.uom.java.ast.metrics;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.FieldInstructionObject;
import gr.uom.java.ast.MethodObject;
import gr.uom.java.ast.SystemObject;

public class Cohesion {
	private Map<String, Double> cohesionMap;
	
	public Cohesion(SystemObject system) {
		this.cohesionMap = new LinkedHashMap<String, Double>();
		ListIterator<ClassObject> iterator = system.getClassListIterator();
		while(iterator.hasNext()) {
			ClassObject classObject = iterator.next();
			double cohesion = computeCohesion(classObject);
			cohesionMap.put(classObject.getName(), cohesion);
		}
	}

	private double computeCohesion(ClassObject classObject) {
		List<MethodObject> methods = classObject.getMethodList();
		int methodPairs = 0;
		double sum = 0;
		for(int i=0; i<methods.size()-1; i++) {
			MethodObject methodI = methods.get(i);
			Set<FieldInstructionObject> fieldsI =
					filter(methodI.getFieldInstructions(), methodI.getClassName());
			for(int j=i+1; j<methods.size(); j++) {
				MethodObject methodJ = methods.get(j);
				Set<FieldInstructionObject> fieldsJ =
						filter(methodJ.getFieldInstructions(), methodJ.getClassName());
				sum += similarity(fieldsI, fieldsJ);
				methodPairs++;
			}
		}
		if(methodPairs > 0) {
			return sum/(double)methodPairs;
		}
		else {
			return 0;
		}
	}
	
	private Set<FieldInstructionObject> filter(List<FieldInstructionObject> fields,
			String className) {
		Set<FieldInstructionObject> set = new LinkedHashSet<FieldInstructionObject>();
		for(FieldInstructionObject field : fields) {
			if(field.getOwnerClass().equals(className)) {
				set.add(field);
			}
		}
		return set;
	}
	private double similarity(
			Set<FieldInstructionObject> fieldsI, Set<FieldInstructionObject> fieldsJ) {
		int intersectionSize = intersection(fieldsI, fieldsJ).size();
		int unionSize = union(fieldsI, fieldsJ).size();
		if(unionSize > 0) {
			return (double)intersectionSize/(double)unionSize;
		}
		else {
			return 0;
		}
	}

	private Set<FieldInstructionObject> intersection(
			Set<FieldInstructionObject> fieldsI, Set<FieldInstructionObject> fieldsJ) {
		Set<FieldInstructionObject> intersection = new HashSet<FieldInstructionObject>();
		intersection.addAll(fieldsI);
		intersection.retainAll(fieldsJ);
		return intersection;
	}


	private Set<FieldInstructionObject> union(
			Set<FieldInstructionObject> fieldsI, Set<FieldInstructionObject> fieldsJ) {
		Set<FieldInstructionObject> union = new HashSet<FieldInstructionObject>();
		union.addAll(fieldsI);
		union.addAll(fieldsJ);
		return union;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String key : cohesionMap.keySet()) {
			double value = cohesionMap.get(key);
			sb.append(key).append("\t").append(value).append("\n");
		}
		return sb.toString();
	}
}
