package gr.uom.java.testcases.testingMIFAIF;

public class A extends Main{

	int b=0;
	protected int c = 0;
	
	public String testMethod1() {
		System.out.println("This is overidden method from the class Main");
		return null;		
	}
	
	private int testMethod2() {
		System.out.println("This method is declared private and not be "
				+ "overidden by any child class");
		return 0;		
	}
	
}
