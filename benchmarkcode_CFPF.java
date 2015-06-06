package demoPF;

/* Java code for testing coupling factor  */

class DemoSuper
{
	void demoMethod1()
	{
		System.out.println("This is Method 1");	
	}
	void demoMethod2()
	{
		System.out.println("This is Method 2");
	}
	void demoMethod3()
	{
		System.out.println("This is Method 3");	
	}
	private void demoMethod4()
	{
		System.out.println("This is Method 4 private");	
	}
	protected void demoMethod5()
	{
		System.out.println("This is Method 5 protected");	
	}
}

class DemoSubClass1 extends DemoSuper
{
	void demoSuClass1bMethod1()
	{
		System.out.println("This is Method 1");	
	}
	void demoSuClass1bMethod2()
	{
		System.out.println("This is Method 2");	
	}
	private void demoMethod4()
	{
		System.out.println("This is Method 4 trying to override");	
	}
	public void demoMethod5()
	{
		System.out.println("This is Method 5 overridden from super class");	
	}
}

class DemoSubClass2 extends DemoSubClass1
{
	void demoMethod2()
	{
		System.out.println("This is Overridden Method");
		DemoSubClass3 dSC3=new DemoSubClass3();
		dSC3.demoMethod1();
	}
	public void demoMethod5()
	{
		System.out.println("This is Method 5 trying to overridden from demo sub class1");	
	}
	
}
class DemoSubClass3 extends DemoSuper
{
	void demoMethod2()
	{
		System.out.println("This is Overridden Method");
	}
	public void demoMethod5()
	{
		System.out.println("This is Method 5  overridden from demo super class");	
	}
	
}

public class mainClass 
{
	
	public static void mainClassMethod1()
	{
		System.out.println("This is Main Method 1");
		DemoSubClass2 dSC2=new DemoSubClass2();
		dSC2.demoMethod1();
	}
	public void mainClassMethod2()
	{
		System.out.println("This is Main Method 2");
	}
	public static void main(String args[]){
		mainClassMethod1();
	}	
}
