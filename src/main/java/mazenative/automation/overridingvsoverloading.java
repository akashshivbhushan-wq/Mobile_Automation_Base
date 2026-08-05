package mazenative.automation;

class ABC{

void m1(int a) {
	System.out.println(a);
}


void m2(int a, int b) {
	System.out.println(a+b);
}
 
}
class XYZ extends ABC {
	
	void m1(int a) {
		System.out.println(a*a);
	}
	
	void m2(int a, int b,int c) {
		System.out.println(a+b+c);
	}
	 	
	
}
public class overridingvsoverloading {

	public static void main(String[] args) {
		
XYZ Obj=new XYZ();

Obj.m1(10);
Obj.m2(10, 20,40);
	}

}
