package gr.uom.java.testcases.newpackage.testingMHFAHF;

import gr.uom.java.testcases.testingMHFAHF.Child;

public class Child2 extends Child{
	public int i = 0;
	protected int j = 0;
	int qr = 0;
	protected void protectedMethod2(){
		System.out.println("This is a protectedMethod2 method..");
	}
	public void publicMethod1(){
		System.out.println("This is a publicMethod1 method..");
	}
	void defaultMethod(){
		System.out.println("This is a defaultMethod method...");
	}
}
