import java.util.ArrayList;
import java.math.BigDecimal;


/**
* FitnessFunction class contains fitness function whose
* value will be optimized with optimization algorithms,
* functionality of this class involves calculating the fitness
* value of a chromosome and returning it.
* <p>
*  
* @author Ashish Rana
* @version 1.0
* @since 07-02-2018
* 
*/


// From Nature Inspired Algorithms
// Predefined Ackley's Function Coded

public class FitnessFunction{

	// Fitness Function Parameters

	/**
	* This variable represents the dimensions of the chromosome that is passed for fitness evaluation.
	* @since 1.0
	*/
	public int dim = 10; // assumption
	/**
	* This variable represents the lower bound value of each entry of dimensions of the chromosome that is passed for fitness evaluation.
	* @since 1.0
	*/
	public int lBound = -35;	// X(i) value's lower bound defined by function 
	/**
	* This variable represents the upper bound value of each entry of dimensions of the chromosome that is passed for fitness evaluation.
	* @since 1.0
	*/
	public int uBound = 35;	// X(i) value's upper bound defined by function

	
	/** 
	* This method takes takes an arraylist of chromosome as input and evaluates the
	* fitness value and returns it back with rounded off value.
	* @param x This is arraylist of chromosomes containing double values of chromosome/particle.
	* @return double This is the returned fitness value rounded off to desired accuracy.
	* @exception NullPointerException when invoked by <code>null</code> ArrayList Object.
	* @since 1.0
	*/

	public double fitnessFunction(ArrayList<Double> x){
	
		double termA = 0;
		double termB = 0;	
	
		for(int i=0;i<dim;i++)
		{
			termA = termA + x.get(i)*x.get(i);
			termB = termB + Math.cos(x.get(i));
		 
		}
		double finalTermA = 20*Math.exp(-0.02*Math.sqrt(termA/dim));
		double finalTermB = Math.exp(termB/dim);
		double result = 20+Math.exp(1)-finalTermA-finalTermB;	
		// return double rounded result
		return round(result, 2);

		}

	// method to round code for result using BigDecimal class

	/**
	* This is method that takes the fitness value and the decimal digit to be
	* rounded off as arguement and evaluates the rounded off value.
	* @param d This is decimal fitness value as input
	* @param decimalPlace This specifies the number of digits to be rounded off to.
	* @return double Rounded off double value is returned
	* @since 1.0
	*/	

	public static double round(double d, int decimalPlace){
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }	
}
