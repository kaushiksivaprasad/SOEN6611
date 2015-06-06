package gr.uom.java.testcases.testingAIFMIF;

import gr.uom.java.testcases.testingMIFAIF.B;

public class C extends B{

	int c =0;
	
	public int testMethod4() {
		System.out.println("This method is not overidden from class B as it is "
				+ "protected in a different package");
		
		return 0;
	}
}
