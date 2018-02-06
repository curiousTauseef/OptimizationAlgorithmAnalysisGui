import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;


/**
* This class uses the fitness function to evaluate the chromosomes with genetic algorithmic processes like crossover and mutations.
* Also, it writes iterations of improved chromosomes into a file with ending on best chromosome iteration.
* This class also extends FitnessFunction class for using fitness function, lower and upper bounds plus dimensions specified in it.
* @author Ashish Rana
* @version 1.0
* @see FitnessFunction
* @since 07-02-2018
*/
	

public class GeneticAlgorithm extends FitnessFunction{

	/**
	* Name of the algorithm being used.	
	* @since 1.0
	*/
	private final static String algorithmName = "GeniticAlgorithm"; // Algo Name
	/**
	* One point cross-over rate.	
	* @since 1.0
	*/
	private final static double crossRate = 0.7;	// CrossOver Rate
	/**
	* Single dimension mutation rate.	
	* @since 1.0
	*/
	private final static double mutRate = 0.1;		// Mutation Rate
	/**
	* Number of iterations in algorithm.	
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
		ArrayList<Double> chromosome ; 
		/**
		* This stores a fitness value of chromosome for an instance.
		* @since 1.0
		*/
		double fitness ; 
	}
	/**
	* Population of chromosomes with fitness value.	
	* @since 1.0
	*/
	private static ArrayList<Individual> pop ;	// Stores chromosome and fitness value 	
	/**
	* resultant file name specific to algorithm.	
	* @since 1.0
	*/
	private final static String resultFileName = "Result"+algorithmName+".csv";

	/**
	* This initialize <i>bestChromosome</i> to default values when this constructor is called. 
	* @see FitnessFunction
	* @since 1.0
	*/
	
	public GeneticAlgorithm()
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
	* @return ArrayList This is the new copied <i>double</i> chromosome which is returned
	* @exception NullPointerException This exception is throwned while accessing null list
	* @exception IndexOutOfBoundsException If the index accessed is out of bounds of list's size
	* @see Individual
	* @since 1.0
	*/
	public static ArrayList<Double> deepCopy(ArrayList<Double> copyTo,ArrayList<Double> copyFrom)
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
	* @exception IndexOutOfBoundsException If the index accessed is out of bounds of list's size 
	* @see FitnessFunction
	* @since 1.0
	*/

	public static void memorizeGlobalBest(){
		
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
	* @exception IndexOutOfBoundsException If the index accessed is out of bounds of dim's size
	* @see FitnessFunction
	* @since 1.0
	*/

	public static ArrayList<Double> initializeList(ArrayList<Double> list)
	{
		FitnessFunction fObj = new FitnessFunction(); 
		for(int i=0;i<fObj.dim;i++)
			list.add(i,0.00);
	
		return list;
	}


	/**
	* This method is used for randomly picking up two chromosome from the population and performing one point crossover for them. Which generates two offsprings with different fitness values.
	* After calculating and comparing the fitness of parent and corresponding offsprings it also replaces them with best fitness valued chromosome respectively.
	* @exception NullPointerException This exception is throwned while accessing null list
	* @exception IndexOutOfBoundsException If the index accessed is out of bounds of list's size
	* @see Individual
	* @see FitnessFunction
	* @since 1.0
	*/


	public static void crossGene(){
	
		FitnessFunction fObj = new FitnessFunction(); 
	
		for(int i=0;i<popSize;i++)	
		{
			if(Math.random()<=crossRate)
			{
				int indA = (int)(Math.random()*(popSize-1));
				int indB = (int)(Math.random()*(popSize-1));
			
				int crossIndex = (int)(Math.random()*(fObj.dim-2));
			
				Individual parA = new Individual();
				parA.chromosome = new ArrayList<Double>(fObj.dim);			
				
				parA.chromosome = initializeList(parA.chromosome);
			
				parA.chromosome = deepCopy(parA.chromosome, pop.get(indA).chromosome);
				
				Individual parB = new Individual();			
				parB.chromosome = new ArrayList<Double>(fObj.dim);
			
				parB.chromosome = initializeList(parB.chromosome);
			
				parB.chromosome = deepCopy(parB.chromosome, pop.get(indB).chromosome);
			
				Individual childA = new Individual();			
				childA.chromosome = new ArrayList<Double>(fObj.dim);	
				Individual childB = new Individual();			
				childB.chromosome = new ArrayList<Double>(fObj.dim);	
			
				for(int j=0;j<fObj.dim;j++)
				{
			
					if(j<crossIndex)
					{
						childA.chromosome.add(j,parA.chromosome.get(j));
						childB.chromosome.add(j,parB.chromosome.get(j));
					}
					else
					{
						childA.chromosome.add(j,parB.chromosome.get(j));
						childB.chromosome.add(j,parA.chromosome.get(j));
					}	
			
				}
						
				double childAFitness = fObj.fitnessFunction(childA.chromosome);
				funEval++;
			
				double childBFitness = fObj.fitnessFunction(childB.chromosome);
				funEval++;
			
			
				if(childAFitness < pop.get(indA).fitness)
				{
					pop.get(indA).fitness = childAFitness;
					pop.get(indA).chromosome = deepCopy(pop.get(indA).chromosome ,childA.chromosome);
			}
			
				if(childBFitness < pop.get(indB).fitness)
				{
					pop.get(indB).fitness = childBFitness;
					pop.get(indB).chromosome = deepCopy(pop.get(indB).chromosome , childB.chromosome);
				}
			}
		}	
	}


	/**
	* This method is used for randomly picking up one chromosome from the population and mutating one chromosome value to generate new child chromosome.
	* After calculating and comparing the fitness of parent and corresponding offspring it also replaces parent with best fitness valued chromosome.
	* @exception NullPointerException This exception is throwned while accessing null list
	* @exception IndexOutOfBoundsException If the index accessed is out of bounds of list's size
	* @see Individual
	* @see FitnessFunction
	* @since 1.0
	*/

	public static void mutateGene()
	{
		FitnessFunction fObj = new FitnessFunction(); 
	
		for(int i=0;i<popSize;i++)
		{
			if(Math.random()<=mutRate)
			{
				int ind = (int)(Math.random()*(popSize-1));
			
				Individual par = new Individual();			
				par.chromosome = new ArrayList<Double>();	
			
				par.chromosome = initializeList(par.chromosome);
			
				par.chromosome = deepCopy(par.chromosome, pop.get(ind).chromosome);
			
				int mutIndex = (int)(Math.random()*(fObj.dim-1));
			
				Individual child = new Individual();			
				child.chromosome = new ArrayList<Double>(fObj.dim);	
			
				child.chromosome = initializeList(child.chromosome);
			
				child.chromosome = deepCopy(child.chromosome, pop.get(ind).chromosome); 	
			
				child.chromosome.set(mutIndex, fObj.round(((Math.random()*(fObj.uBound - fObj.lBound))+fObj.lBound),2)); 
			
				double childFitness = fObj.fitnessFunction(child.chromosome);
				funEval++;
			
				if(childFitness < pop.get(ind).fitness)
				{
					pop.get(ind).fitness = childFitness;
					pop.get(ind).chromosome = deepCopy(pop.get(ind).chromosome,child.chromosome);
				}
			
			}
	
		}

	}

	/**
	* This the main method which evaluates the chromosomes with genetic algorithm<i>(i.e. intialize, crossover, mutations and terminate)</i> and stores the result of best chromosome in the <i>.csv</i> file.
	* @param args Unused
	* @exception NullPointerException This exception is throwned while accessing null list
	* @exception IndexOutOfBoundsException If the index accessed is out of bounds of list's size
	* @since 1.0
	*/

	public static void main(String[] args)
	{
		GeneticAlgorithm genObj = new GeneticAlgorithm(); 
		genObj.initializeAll();
	
		genObj.bestChromosome = genObj.pop.get(0).chromosome;
		genObj.bestFitness = genObj.pop.get(0).fitness;
	
	
		try{
				genObj.fileWriter = new FileWriter(genObj.resultFileName);
				genObj.fileWriter.append(fileHeader);
			
				for(int i=0;i<iterNumber;i++)
				{
					genObj.crossGene();
					genObj.mutateGene();
					genObj.memorizeGlobalBest();
				
				
				
					
					if(i%1==0)
					{
						genObj.fileWriter.append(i+","+genObj.bestFitness+","+genObj.bestChromosome+"\n");
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
