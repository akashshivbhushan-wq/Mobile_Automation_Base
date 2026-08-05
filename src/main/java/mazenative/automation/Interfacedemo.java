package mazenative.automation;

interface Shape
{
	int lenght=10;
	int width=20;


void circle (); //Abstract method

default void square() {

System.out.println("This is square method");
}


static void rectangle() {
	System.out.println("This is rectangle method");
}

}

public class Interfacedemo implements Shape {
	
	public void circle () {
		System.out.println("This is circle abstact method..");
	}
	
	void newmethod() {
		System.out.println("This is new method");
	}
	
	public static void main(String[] args) {
		//Interfacedemo ifobj=new Interfacedemo();
		//ifobj.circle();
       // ifobj.square();
       // ifobj.newmethod();
       //Shape. rectangle();
       
    Shape sh=new Interfacedemo ();
       sh.circle();
       sh.square();
       Shape.rectangle();
      ((Interfacedemo) sh).newmethod();
       
       
       
       
       
       
       
       
	}

}
