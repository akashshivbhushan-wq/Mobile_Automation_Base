package mazenative.automation;

class Test1
{
int a=10,b=20;
void m1()
{
System.out.println(a+b);
}
}


class Test2 extends Test1
{
int c=100,e=50;
void m2() {
	System.out.println(c+e);
}


}

class Test3 extends Test2
{
int e=20,f=50;
void m3() {
	System.out.println(e*f);
}


}








public class Multilevel_Inheritance {

	public static void main(String[] args) {
		//Test1 obj=new Test1();
		//obj.m1();
		
		Test3 obj=new Test3();
		
		obj.m1();
        obj.m2();
		obj.m3();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
