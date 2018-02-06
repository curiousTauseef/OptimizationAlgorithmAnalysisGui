import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

/**
* This class uses the fitness function to evaluate the chromosomes with differential evolution's algorithmic equation. Algorithmic equation uses the current best chromosome's and current chromosome's dimensions to create newer chromosome offspring.
* Also, it writes iterations of improved chromosomes into a file with ending on best chromosome iteration.
* This class also extends FitnessFunction class for using fitness function, lower and upper bounds plus dimensions specified in it.
* @author Ashish Rana
* @version 1.0
* @see FitnessFunction
* @since 07-02-2018
*/


public class DeAlgorithm extends FitnessFunction
{
	/**
	* Name of the algorithm being used.	
	* @since 1.0
	*/
	private final static String algorithmName = "DeAlgorithm"; // Algo Name
	/**
	* Cross-over rate for the population.	
	* @since 1.0
	*/
	private final static double crossRate = 0.9;	// Cross-Over Rate.
	/**
	* Inertia factor in the population cross-over manipulation.	
	* @since 1.0
	*/
	private final static double inertia = 0.5;	// Interia Associated
	/**
	* Number of iterations in algorithms.	
	* @since 1.0
	*/
	private final static int iterNumber = 2000;	// Iteration Number
	/**
	* Size of population in algorithm.	
	* @since 1.0
	*/
	private final static int popSize = 100;		// Number of chromosomes
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
	* resultant file name specific to algorithm.	
	* @since 1.0
	*/
	private final static String resultFileName = "Result"+algorithmName+".csv";
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
	* This class is used for storing chromosome along with fitness value.
	* It provides structure for storing such values in tied up manner.
	* @since 1.0
	*/
	public static class Individual{
		/**
		* This stores an ArrayList of chromsome's dimension decimal values for an instance.
		* @since 1.0
		*/	
		ArrayList<Double> chromosome; 
		/**
		* This stores a fitness value of chromosome for an instance.
		* @since 1.0
		*/
		double fitness;
		 
	}
	/**
	* Population of chromosomes with fitness value.	
	* @since 1.0
	*/
	private static ArrayList<Individual> pop ;	// Stores chromosome and fitness value 	
	/**
	* This initialize <i>bestChromosome</i> to default values when this constructor is called. 
	* @see FitnessFunction
	* @since 1.0
	*/
	public DeAlgorithm()
	{
		FitnessFunction fObj = new FitnessFunction(); 
		bestChromosome = new ArrayList<>(fObj.dim);
		bestChromosome = initializeList(bestChromosome);
	
	}

	/**
	* This method initialize all population of chromosomes with their dimensions and fitness function values.
	* Also it binds them together into objects of Individual class.
	* @see Individual
	* @see FitnessFunction
	* @since 1.0
	*/
		public static void initializeAll()
	{
		pop = new ArrayList<Individual>(popSize);	// initialized to 100 capacity
	
		FitnessFunction fObj = new FitnessFunction(); 
	
		for(int i=0;i<popSize;i++)
		{
			ArrayList<Double> chromosome = new ArrayList<>(fObj.dim);
		
			for(int j=0;j<fObj.dim;j++)
			{
			// Adding chromosome's value with rounded value		
				chromosome.add(fObj.round(((Math.random() * (fObj.uBound - fObj.lBound)) + fObj.lBound),2));
			}
		
			Individual inst = new Individual();
		
			inst.fitness = fObj.fitnessFunction(chromosome);
			funEval++;
			inst.chromosome = chromosome;
		
			pop.add(inst);
		}
	}

	/**
	* This method creates a deepcopy i.e. a seperate copy of chromosome that will not be side effected by originals manipulation. A seperate copy of the object is created with same values as <i>copyFrom</i> parameter.   
	* @param copyTo This the chromosome in which the values copied into.
	* @param copyFrom This is the chromosome from which the values copied from iteratively
	* @return ArrayList This is the new copied <i>double</i> value containing chromosome which is returned
	* @exception NullPointerException This exception is throwned while accessing null list
	* @exception IndexOutOfBoundsException If the index accessed is out of bounds of list's size
	* @see Individual
	* @since 1.0
	*/
	public ArrayList<Double> deepCopy(ArrayList<Double> copyTo,ArrayList<Double> copyFrom)
	{	
	
		FitnessFunction fObj = new FitnessFunction(); 
	
		for(int i=0;i<copyFrom.size();i++)
		{
			copyTo.set(i,copyFrom.get(i));
				
		}
	
		return copyTo;
	}

	/**
	* This method is used for remembering best fitness and corresponding chromosome to it.
	* Best fitness and chromosome are calculated and stored for global usage throughout the program.
	* @exception NullPointerException This exception is throwned while accessing null list
	* @see FitnessFunction
	* @since 1.0
	*/

	public void memorizeGlobalBest(){
		
		FitnessFunction fObj = new FitnessFunction(); 
				
		for(Individual p : pop)
		{
			if(p.fitness < bestFitness)
			{
				bestFitness = p.fitness;
				bestChromosome = deepCopy(bestChromosome, p.chromosome);
				System.out.println("New BestChromosome Found  "+bestChromosome);	
			}
			
		}
	}

	/**
	* This method is used for stuffing default values into arraylist which can later be manipulated.
	* @param list This is a list passed with size zero
	* @return ArrayList This is a list with size of <i>dim</i> in FitnessFunction class
	* @exception NullPointerException This exception is throwned while accessing null list
	* @see FitnessFunction
	* @since 1.0
	*/
	
	public ArrayList<Double> initializeList(ArrayList<Double> list)
	{
		FitnessFunction fObj = new FitnessFunction(); 
		for(int i=0;i<fObj.dim;i++)
			list.add(i,0.00);
	
		return list;
	}

	/**
	* This method is used for randomly picking up one chromosome from the population and performing differential evolution manipulation with current best chromosome. Which generates one offsprings with different fitness values.
	* After calculating and comparing the fitness of parent and corresponding offspring it also replaces them with best fitness valued chromosome respectively.
	* @see Individual
	* @see FitnessFunction
	* @since 1.0
	*/
	  
	public void deOperation()
	{
		FitnessFunction fObj = new FitnessFunction(); 
		
		for(int i=0;i<popSize;i++)
		{
			int indA = (int)(Math.random()*(popSize-1));
			int indB = (int)(Math.random()*(popSize-1));
			
			ArrayList<Double> newChild = new ArrayList<>();
			for(int j=0;j<fObj.dim;j++)
			{
				if(Math.random()<=crossRate)
				{
					double constValue = pop.get(i).chromosome.get(j) + inertia*( pop.get(indB).chromosome.get(j) - pop.get(indA).chromosome.get(j) );
					
					if(constValue<fObj.lBound)
						constValue = fObj.round(((Math.random() * (fObj.uBound - fObj.lBound)) + fObj.lBound),2);
					
					if(constValue>fObj.lBound)
						constValue = fObj.round(((Math.random() * (fObj.uBound - fObj.lBound)) + fObj.lBound),2);	
					
					newChild.add(fObj.round(constValue,2));
				
				}
				else
				{
					newChild.add(pop.get(i).chromosome.get(j));
				}
			}
			
			double newChildFitness = fObj.fitnessFunction(newChild);
			
			funEval++;
			
			if(newChildFitness < pop.get(i).fitness)
			{
				pop.get(i).fitness = newChildFitness;
				pop.get(i).chromosome = deepCopy(pop.get(i).chromosome, newChild);
			}		
		}	
	
	
	}
	

	/**
	* This the main method which evaluates the chromosomes with differential evolution algorithm and stores the result of best chromosome in the <i>.csv</i> file.
	* @param args Unused
	* @exception NullPointerException This exception is throwned while accessing <code>null</code> list/FileWriterObject.
	* @since 1.0
	*/
	
	
	public static void main(String[] args)
	{
		DeAlgorithm deObj = new DeAlgorithm(); 
		deObj.initializeAll();
	
		deObj.bestChromosome = deObj.pop.get(0).chromosome;
		deObj.bestFitness = deObj.pop.get(0).fitness;
	
	
		try{
				deObj.fileWriter = new FileWriter(deObj.resultFileName);
				deObj.fileWriter.append(fileHeader);
			
				for(int i=0;i<iterNumber;i++)
				{
					deObj.deOperation();
					deObj.memorizeGlobalBest();
					
					if(i%1==0)
					{
						deObj.fileWriter.append(i+","+deObj.bestFitness+","+deObj.bestChromosome+"\n");
					}
					
					if(funEval>maxFunEval)
						break;
					
					funEval++;
				
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
