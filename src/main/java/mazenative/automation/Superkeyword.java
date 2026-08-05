package mazenative.automation;


class T1{


String colour="White";



}


class T2 extends T1{

	String colour="Black";

void Displayclour()
{
System.out.println(super.colour);
}
}





public class Superkeyword {

	public static void main(String[] args) {
		T2 t=new T2();
		t.Displayclour();

	}

}
