
class Hdfc {
	
	 double roi (){
	
	return 0;
	}
}
	class sbi extends Hdfc{
	
		 double roi (){
				
				return 10.5;
				}
	}
	
	class icici extends Hdfc{
	
		 double roi (){
				
				return 12.5;
				}
	
		 
		void sum(int a,int b) {
		 System.out.println(a+b);
		}
	
	}
	
	public class Overloading {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		
		
		
		sbi obj=new sbi();
	    System.out.println(	obj.roi());
	
	    icici obj1=new icici();
	    System.out.println(obj1.roi());
	    obj1.sum(20, 5);
	    
	
	
	
	
	
	
	
	
	
	
	
	
	
	}

}
