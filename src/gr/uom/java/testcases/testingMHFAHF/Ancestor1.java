package gr.uom.java.testcases.testingMHFAHF;

public class Ancestor1 {
	public int i = 0;
	protected int j = 0;
	private int z = 0;
	int q = 0;
	public void publicMethod1(){
		System.out.println("This is a publicMethod1 method..");
	}
	protected void protectedMethod2(){
		System.out.println("This is a protectedMethod2 method..");
	}
	private void privateMethod3(){
		System.out.println("This is a privateMethod3 method..");
	}
	void defaultMethod(){
		System.out.println("this is a default method..");
	}
}
