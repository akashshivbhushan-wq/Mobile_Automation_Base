package mazenative.automation;

public class Arrays_Java {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	//Delecaring Array	
	/*int a[]=new int [10];	
		
	a[0]=100;	
	a[1]=200;
	a[2]=300;
	a[3]=400;
	a[4]=500;*/
//Approch 2	
	//	int a[]= {100,200,300,400,500};
	
	//Find lengh of an array	
	//System.out.println("Length of an array: "+	a.length);
		
	//Read single value from array	
	//System.out.println(a[2]);
	// Reading all values from array	
	/*for(int i=0;i<a.length;i++)
	{
		System.out.println(i);
	}	*/
		
	// approch for Enhanced for loop	
		
	/*for(int num:a)	
	{
		
		System.out.println(num);
	}	*/
		
// Two dimentional array		
	
	
/*int a[][]	=new int[3][2];

a [0][0]=100;
a [0][1]=200;

a [1][0]=300;	
a [1][1]=400;	
	
	
a [2][0]=500;	
a [2][1]=600;	*/
	
//Approch 2	
	
	int a[][]= {{100,200},
	             {300,400},
	             {500,600} };
	
// Find size of an array	
	//System.out.println(a.length);
	
	//System.out.println(a[0].length);
	//System.out.println(a[1][1]);
	
	//Read all values using for loop
	
	/*for(int r=0;r<a.length;r++)
	{
		for(int c=0;c<a[r].length;c++)
		{
			System.out.print(a[r][c]+" ");
			System.out.println();
		}
	}*/
	
	
	
	for(int arr[]:a)
	
	{
		for(int x:arr)
		System.out.println(x);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	}
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
		
		
		
		
		
		

	


