import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;


/**
* This class uses the fitness function to evaluate the particles with particle swarm optimization algorithmic process.
* Also, it writes iterations of improved chromosomes into a file with ending on best chromosome iteration.
* This class also extends FitnessFunction class for using fitness function, lower and upper bounds plus dimensions specified in it.
* @author Ashish Rana
* @version 1.0
* @see FitnessFunction
* @since 07-02-2018
*/

public class PsoAlgorithm extends FitnessFunction
{
	/**
	* Name of the algorithm being used.	
	* @since 1.0
	*/
	private final static String algorithmName = "PsoAlgorithm"; // Algo Name
	/**
	* PSO's first constant in evaluating equation.	
	* @since 1.0
	*/
	private final static double constA = 1.5;	// Acceleration Const.
	/**
	* PSO's second constant in evaluating equation.	
	* @since 1.0
	*/
	private final static double constB = 1.5;	// Acceleration Const.
	/**
	* PSO's Inertia's weight factor in evaluating equation.	
	* @since 1.0
	*/
	private final static double iWeight = 0.8;	// Interia Weight
	/**
	* PSO's velocity lower bound of particle in evaluating equation.	
	* @since 1.0
	*/
	private final static double velLowerBound = -1;	// Velocity Lower Bound
	/**
	* PSO's velocity upper bound of particle in evaluating equation.	
	* @since 1.0
	*/
	private final static double velUpperBound = 1;		// Velocity Upper Bound
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
	* best-fitness particle in the population.	
	* @since 1.0
	*/
	private static ArrayList<Double> bestParticle;
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
	* This class is used for storing chromosome along with fitness value. Also it stores all the velocity dimensions of the given chromosome.
	* Extra Information about the swarm is also stored. With current best fitness value and best particle's dimensions. 
	* It provides structure for storing such values in tied up manner.
	* @since 1.0
	*/
	public static class Individual{
		ArrayList<Double> particle; 
		ArrayList<Double> velocity; 
		ArrayList<Double> pBest; 
		double fitness;
		double pBestCollective;
		 
	}

	/**
	* Population of particles with fitness value of its own, velocity vector of its own dimensions, current best fitness value and current best particle.	
	* @since 1.0
	*/

	private static ArrayList<Individual> pop ;	// Stores chromosome and fitness value 	

	/**
	* This initialize <i>bestParticle</i> to default values when this constructor is called. 
	* @see FitnessFunction
	* @since 1.0
	*/
	
	public PsoAlgorithm()
	{
		FitnessFunction fObj = new FitnessFunction(); 
		bestParticle = new ArrayList<>(fObj.dim);
		bestParticle = initializeList(bestParticle);
	
	}

	/**
	* This method initialize all population of particles with their dimensions and fitness function values.
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
			ArrayList<Double> velocity = new ArrayList<>(fObj.dim);
			for(int j=0;j<fObj.dim;j++)
			{
			// Adding chromosome's value with rounded value		
				chromosome.add(fObj.round(((Math.random() * (fObj.uBound - fObj.lBound)) + fObj.lBound),2));
				
				velocity.add(fObj.round(((Math.random() * (velUpperBound - velLowerBound)) + velLowerBound),2));
			}
			
			Individual inst = new Individual();
		
			inst.fitness = fObj.fitnessFunction(chromosome);
			funEval++;
			inst.particle = chromosome;
			inst.velocity = velocity;
			inst.pBest = chromosome;
			inst.pBestCollective =  fObj.fitnessFunction(chromosome);
			pop.add(inst);
		}
	}

	/**
	* This method is used for remembering best fitness and corresponding chromosome to it.
	* Best fitness and chromosome are calculated and stored for global usage throughout the program.
	* @exception NullPointerException This exception is throwned while accessing null list
	* @see FitnessFunction
	* @since 1.0
	*/
	
	public static void memorizeGlobalBest(){
		
		FitnessFunction fObj = new FitnessFunction(); 
		for(Individual p : pop)
		{
			if(p.pBestCollective < bestFitness)
			{
				bestFitness = p.pBestCollective;
				bestParticle = deepCopy(bestParticle, p.pBest);
				System.out.println("New BestChromosome Found  "+bestParticle);	
			}
		}
	}

	/**
	* This method creates a deepcopy i.e. a seperate copy of chromosome that will not be side effected by originals manipulation. A seperate copy of the object is created with same values as <i>copyFrom</i> parameter.   
	* @param copyTo This the particle in which the values copied into.
	* @param copyFrom This is the particle from which the values copied from iteratively
	* @return ArrayList This is the new copied <i>double</i> value containing particle which is returned
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
	* This method is used for stuffing default values into arraylist which can later be manipulated.
	* @param list This is a list passed with size zero
	* @return ArrayList This is a list with size of <i>dim</i> in FitnessFunction class
	* @exception NullPointerException This exception is throwned while accessing null list
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
	* @see Individual
	* @see FitnessFunction
	* @since 1.0
	*/

	public static void psoOperation()
	{
		FitnessFunction fObj = new FitnessFunction(); 
		for(int i=0;i<popSize;i++)
		{
			for(int j=0;j<fObj.dim;j++)
			{
				double randA = Math.random();
				double randB = Math.random();
				
				pop.get(i).velocity.add(j,iWeight*pop.get(i).velocity.get(j) + constA*randA*(pop.get(i).pBest.get(j)-pop.get(i).particle.get(j)) + constB*randB*(bestParticle.get(j)-pop.get(i).particle.get(j)));
				
				if(pop.get(i).velocity.get(j)<velLowerBound)
					pop.get(i).velocity.add(j,Math.random() * (velUpperBound - velLowerBound) + velLowerBound);
				
				if(pop.get(i).velocity.get(j)>velUpperBound)
					pop.get(i).velocity.add(j,Math.random() * (velUpperBound - velLowerBound) + velLowerBound);
				
				pop.get(i).particle.set(j,fObj.round((pop.get(i).particle.get(j)+pop.get(i).velocity.get(j)),2));	
				
				if(pop.get(i).particle.get(j)<fObj.lBound)
					pop.get(i).particle.set(j,fObj.round(((Math.random() * (fObj.uBound - fObj.lBound)) + fObj.lBound),2));
				
				if(pop.get(i).particle.get(j)>fObj.uBound)
					pop.get(i).particle.set(j,fObj.round(((Math.random() * (fObj.uBound - fObj.lBound)) + fObj.lBound),2));
				
							
			}
			
			pop.get(i).fitness = fObj.fitnessFunction(pop.get(i).particle); 
			
			funEval++;
			
			if(pop.get(i).fitness<=pop.get(i).pBestCollective)
			{
				pop.get(i).pBestCollective = pop.get(i).fitness;
				pop.get(i).pBest = deepCopy(pop.get(i).pBest,pop.get(i).particle);  
			}
		
		}
	
	}	
	
	/**
	* This the main method which evaluates the chromosomes with pso's equations and stores the result of best chromosome in the <i>.csv</i> file.
	* @param args Unused
	* @exception NullPointerException This exception is throwned while accessing <code>null</code> list/FileWriterObject.
	* @since 1.0
	*/
	
	public static void main(String[] args)
	{
		PsoAlgorithm psoObj = new PsoAlgorithm(); 
		psoObj.initializeAll();
	
		psoObj.bestParticle = psoObj.pop.get(0).particle;
		psoObj.bestFitness = psoObj.pop.get(0).fitness;
	
	
		try{
				psoObj.fileWriter = new FileWriter(psoObj.resultFileName);
				psoObj.fileWriter.append(fileHeader);
			
				for(int i=0;i<iterNumber;i++)
				{
					psoObj.psoOperation();
					psoObj.memorizeGlobalBest();
				
				
				
					
					if(i%1==0)
					{
						psoObj.fileWriter.append(i+","+psoObj.bestFitness+","+psoObj.bestParticle+"\n");
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
