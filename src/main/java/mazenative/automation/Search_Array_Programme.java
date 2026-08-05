package mazenative.automation;

public class Search_Array_Programme {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	int a[]	= {10,20,30,40,50};
	int search_value=200;
boolean status=false;

for(int i=0;i<a.length;i++) {
	if(a[i]==search_value) {
		status=true;
		System.out.println("search number found");
	}
}
	if(status==false) {
		System.out.println("number not found");
	}
	
	
	
	
	
	
	}

}
