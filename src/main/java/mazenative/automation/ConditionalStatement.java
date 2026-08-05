package mazenative.automation;

public class ConditionalStatement {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	//conditional statements if else 	
		//if statement
		int personage=18;
		if(personage>=18)
		{	
			System.out.println("eligible for vote");
		}
		else	
		{
			System.out.println("Not eligible for vote");
		}
//Example2 Even or add		
		int num=5;
		if(num%2==0)
			
		{
		System.out.println("number is even");
		}
		
		else	
		{
			System.out.println("Odd number");
		}
		
		
		
	//Example 3 check no is positive negative or zero	
		int numb=-4;
		if(numb>0)
		
		{
			System.out.println("Number is positive");
		}
		
		else if(numb<0)
		{
			System.out.println("Number is negative");

		}
		else
		{
			System.out.println("zero");
		}
			
	//Largest of 3 numbers	
		
	int x=10, y=40, z=30;	
	if(x>y &&x<z)
	{	
		System.out.println("A is the Largest value");
	}
		
	else if(y>x && y>z)	
		
	{	
		System.out.println("y is the gratest value");
		
	}
	else	
	{
		System.out.println("z is the largest value");
	}
		
		
	//multiple statement	
	if(true)
	{	
	if(true)	
	{
	System.out.println("abc");	
	}
	
	else
	{
		System.out.println("xyz");
	}
	}
	else	
	{
		System.out.println(123);
	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
