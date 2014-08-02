package mini_test;

public class ofn_exception extends Exception{
	
	int errorcode;
	
	public ofn_exception(int errorcode){
		this.errorcode = errorcode;
	}
	
	public int returncode(){
		return errorcode;
	}

}
