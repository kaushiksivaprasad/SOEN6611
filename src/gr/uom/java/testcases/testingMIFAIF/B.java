package gr.uom.java.testcases.testingMIFAIF;

public class B extends A{

	protected int c=0;
	
	public int testMethod2() {
		System.out.println("It won't be overidden from class A");
		return 0;		
	}
	
	public String testMethod1() {
		System.out.println("This is overidden from class Main"
				+ "through multilevel inheritance");
		
		return null;		
	}
	
	public int testMethod3() {
		System.out.println("It can be overidden in child classes");
		return 0;		
	}
	
	protected int testMethod4() {
		System.out.println("It can't be overidden in child classes "
				+ "present in different packages");
		return 0;		
	}
}
