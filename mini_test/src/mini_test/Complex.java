package mini_test;


/*
 * This is a class of complex numbers. Define real part, imaginary part and their computation.
 */
public class Complex {
	
	private double real;
	private double imag;
	
	public Complex(double r, double i){
		this.real = r;
		this.imag = i;
	}
	
	public double getReal(){
		return this.real;
	}
	
	public double getImag(){
		return this.imag;
	}
	
	public void setReal(double r){
		this.real = r;
	}
	
	public void setImag(double i){
		this.imag = i;
	}
	
	public Complex add(Complex c){
		double newreal = real+c.getReal();
		double newimag = imag+c.getImag();
		return new Complex(newreal,newimag);
	}
	
	public Complex minus(Complex c){
		double newreal = real-c.getReal();
		double newimag = imag-c.getImag();
		return new Complex(newreal,newimag);
	}
	
	public Complex multiply(Complex c){
		double newreal = real*c.getReal()-imag*c.getImag();
		double newimag = real*c.getImag()+imag*c.getReal();
		return new Complex(newreal,newimag);
	}
	
	public Complex divide(Complex c){
		double abs = Math.sqrt(c.getReal()*c.getReal()+c.getImag()*c.getImag());
		double newreal = (real*c.getReal()+imag*c.getImag())/abs;
		double newimag = (imag*c.getReal()-real*c.getImag())/abs;
		return new Complex(newreal,newimag);
		
	}
	
	public String tostring(){
		String str = null;
		if (imag > 0) {
			str = Double.toString(real) + "+" + Double.toString(imag) + "i";
		}
		else if (imag==0) {
			str = Double.toString(real);
		}
		else {
			str = Double.toString(real)+"-"+Double.toString(imag)+"i";
		}
		return str;
	}
	
	public double abs(){
		double abs_value = Math.sqrt(imag*imag+real*real);
		return abs_value;
	}

}
