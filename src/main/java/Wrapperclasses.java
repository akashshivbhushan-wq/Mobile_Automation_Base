
public class Wrapperclasses {

	public static void main(String[] args) {
		
	/*String s1="10";	
	String  s2="20";	
	System.out.println(s1+s2);
	 System.out.println(Integer.parseInt(s1)+Integer.parseInt(s2));*/
		
	/*String s1="10.5";	
	String s2="20.9";
	System.out.println(Double.parseDouble(s1)+Double.parseDouble(s2));*/

	//String s="True";
	//System.out.println(Boolean.parseBoolean(s));
	
	
	
	//Primitive to string   
		int a=10;
		double d=10.5;
		char c='A';
		boolean bool=true;
		
	    String s=String.valueOf(a);
	    System.out.println(s);
	
	
	
	s=String.valueOf(d);
	System.out.println(s);
	
	
	
	s=String.valueOf(c);
	System.out.println(s);
	
	s=String.valueOf(bool);
	System.out.println(s);
	}

}
