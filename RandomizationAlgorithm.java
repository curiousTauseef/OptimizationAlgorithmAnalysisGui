import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;


/**
* This class uses the fitness function to evaluate the chromosomes with randomization processes like random-walk.
* Also, it writes iterations of improved chromosomes into a file with ending on best chromosome iteration.
* This class also extends FitnessFunction class for using fitness function, lower and upper bounds plus dimensions specified in it.
* @author Ashish Rana
* @version 1.0
* @see FitnessFunction
* @since 07-02-2018
*/

public class RandomizationAlgorithm extends FitnessFunction
	{
	/**
	* Name of the algorithm being used.	
	* @since 1.0
	*/
	private static String algorithmName = "RandomizationAlgorithm";
	/**
	* Number of iterations in algorithm.	
	* @since 1.0
	*/
	private final static int iterNumber = 2000;	// Iteration Number 	
	/**
	* best-fitness value in the population.	
	* @since 1.0
	*/
	private static double bestFitness = 99999999; // Store Best Fitness  
	/**
	* best-fitness chromosome in the population.	
	* @since 1.0
	*/
	private static ArrayList<Double> bestChromosome;
	/**
	* File-header to be appended into result file.	
	* @since 1.0
	*/
	private static final String fileHeader = "Iteration,Fitness,Chromosome"+"\n";
	/**
	* FileWriter Object for writing into result file.
	* @see FileWriter	
	* @since 1.0
	*/
	private static FileWriter fileWriter = null;	 
	/**
	* resultant file name specific to algorithm.	
	* @since 1.0
	*/
	private final static String resultFileName = "Result"+algorithmName+".csv";
	/**
	* Maximum number of function evaluations in algorithm.	
	* @since 1.0
	*/
	private final static int maxFunEval = 90000; // Max Allowable Function Evaluation
	/**
	* Stores number of function evaluations in algorithm.	
	* @since 1.0
	*/
	private static int funEval = 0;	// Counting Function Evaluations

	/**
	* This initialize <i>bestChromosome</i> to default values when this constructor is called. 
	* @since 1.0
	*/
		
	public RandomizationAlgorithm()
	{
		bestChromosome = new ArrayList<>();
	}
	
		/**
	* This method creates a deepcopy i.e. a seperate copy of chromosome that will not be side effected by originals manipulation. A seperate copy of the object is created with same values as <i>copyFrom</i> parameter.   
	* @return ArrayList This is the newly generated <i>double</i> value containing chromosome which is returned
	* @see Individual
	* @since 1.0
	*/

	public ArrayList<Double> genChromosome()
	{
		FitnessFunction fObj = new FitnessFunction(); 
		ArrayList<Double> solChrome = new ArrayList<>();
		
		for(int j=0;j<fObj.dim;j++)
			{
			// Adding chromosome's value with rounded value		
				solChrome.add(fObj.round(((Math.random() * (fObj.uBound - fObj.lBound)) + fObj.lBound),2));
			}
			
		return solChrome;	 
	}

	/**
	* This method is used for randomly generating chromosome and comparing with the best-fitness. Storing the most fit chromosome with value until the termination criteria is reached.
	* @exception NullPointerException This exception is throwned while accessing <code>null</code> list/FileWriterObject.
	* @see Individual
	* @see FitnessFunction
	* @see FileWriter
	* @since 1.0
	*/
	
	
	public static void main(String[] args)
	{
		RandomizationAlgorithm ranObj = new RandomizationAlgorithm(); 
		FitnessFunction fObj = new FitnessFunction(); 
		
	
		try{
				ranObj.fileWriter = new FileWriter(ranObj.resultFileName);
				ranObj.fileWriter.append(fileHeader);
				
				ArrayList<Double> chromValue;
				for(int i=0;i<iterNumber;i++)
				{
					chromValue = new ArrayList<>();
					chromValue = ranObj.genChromosome();
					
					double fitness = fObj.fitnessFunction(chromValue);
					funEval++;
					if(fitness<ranObj.bestFitness)
					{
						ranObj.bestFitness = fitness;
						bestChromosome = chromValue;
					}
										
					if(i%1==0)
					{
						ranObj.fileWriter.append(i+","+ranObj.bestFitness+","+ranObj.bestChromosome+"\n");
					}
					
					if(funEval>maxFunEval)
						break;
					
				}
			
				System.out.println("CSV file created");
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				fileWriter.flush();
			  	fileWriter.close();
			}
			catch(IOException e){
			    e.printStackTrace();
			 }
		}            
	}
	

	}
