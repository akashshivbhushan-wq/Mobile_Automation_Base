package mazenative.automation;


class Parent{
	
	void display(int a) {
		System.out.println(a);
	}
	
}

class child1 extends Parent{
void show(int b) {
	System.out.println(b);
}
}

class child2 extends Parent{
	void print(int c) {
		System.out.println(c);
	}
}



public class Hierarchical_Inheritance {

	public static void main(String[] args) {
		child1 c1=new child1();
		c1.display(200);
		c1.show(300);
		
		child2 c2=new child2();
c2.display(500) ;
c2.print(300);
	}

}
