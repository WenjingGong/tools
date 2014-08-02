package mini_test;

public class mac_exception extends Exception{
	
	int errorcode;
	
	public mac_exception(int errorcode){
		this.errorcode = errorcode;
	}
	
	public int returncode(){
		return errorcode;
	}

}
