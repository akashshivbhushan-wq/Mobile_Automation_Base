package mazenative.automation;

public class Duplicates_In_Array {

	public static void main(String[] args) {
/*int a[]= {10,20,10,30,40,10,20,10};
int numdup=20;
int count=0;

for(int value:a) {
	if(value==numdup) {
		count++;
	}
}
System.out.println(count);*/
		
//Search any value		
		
int a[]= {10,20,10,30,40,10,20,10};		
		
int searchnum=6;
boolean status=false;		
		
for(int i=0;i<a.length;i++) {
	if( a[i]==searchnum) {
		status=true;
		
		System.out.println("Search number found");
		break;
	                 
}
}
if(status==false) {
	System.out.println("Search number not found");
}
}
}