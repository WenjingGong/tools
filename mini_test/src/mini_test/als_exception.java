package mini_test;

public class als_exception extends Exception{
	int errorcode;
	
	public als_exception(int errorcode){
		this.errorcode = errorcode;
	}
	
	public int returncode(){
		return errorcode;
	}
}
