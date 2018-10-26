import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
/**
 * A class that uses backtracking to solve a NxN labyrinth and shows the path taken by the
 * algorithm
 * @author Eduardo Moya (edmobe)
 */
public class Backtracking {
	private int[][] labyrinth;
	private int[][] solution;
	private int[][] displayMatrix;
	private int obstacles;
	private int N;
	private boolean solutionExists;
	private int iterationSpeed;
	/**
	 * Constructor for a NxN labyrinth solution using backtracking
	 * @param N width and height of the labyrinth
	 */
	public Backtracking(int N, int initialObstacles, int iterationSpeed) {
		if (N < 4)
			N = 4;
		this.labyrinth = new int[N][N];
		this.solution = new int[N][N];
		resetMatrix(solution);
		this.displayMatrix = new int[N][N];
		if (initialObstacles < 2)
			this.obstacles = 0;
		else
			this.obstacles = initialObstacles - 2;
		this.N = N;
		this.solutionExists = true;
		this.iterationSpeed = iterationSpeed;
	}
	/**
	 * Resets every value of matrix to 0 (no path)
	 * @return Zero matrix
	 */
	public void resetMatrix(int[][] matrix) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				matrix[i][j] = 0;
			}
		}
	}
	/**
	 * Method used to get the column of an specific position
	 * @param num position in the matrix (matrix[position])
	 * @return column in an matrix (matrix[row][column])
	 */
	public int getColumn(int num) {
    	return num % N;
	}
	/**
	 * Method used to get the row of an specific position
	 * @param num position in the matrix (matrix[position])
	 * @return row in an matrix (matrix[row][column])
	 */
    public int getRow(int num) {
    	return num / N;
    }
    /**
     * Method used to get all positions for a NxN matrix
     * @param n size of the matrix
     * @return array with all positions for a NxN matrix
     */
    public int[] getAllPositions(int n) {
    	int[] positions = new int[n*n];
    	for (int i = 0; i < n*n; i++) {
    		positions[i] = i;
    	}
    	return positions;
    }
    /**
     * Shuffles an array
     * @param array
     */
    public void shuffleArray(int[] array) {
    	Random random = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--)
        {
          int index = random.nextInt(i + 1);
          // Simple swap
          int a = array[index];
          array[index] = array[i];
          array[i] = a;
        }
    }
    /**
     * This method gets the obstacles for a NxN matrix and gives an array with random positions
     * for the obstacles
     * @param n size of the matrix
     * @param obstacles amount of obstacles
     * @return random positions array
     */
    public int[] getRandomPositions(int n, int obstacles) {
    	int[] allPositions = getAllPositions(n);
    	allPositions = Arrays.copyOfRange(allPositions, 1, allPositions.length - 1);
    	shuffleArray(allPositions);
    	return Arrays.copyOfRange(allPositions, 0, obstacles);
    }
    /**
     * Creates a labyrinth with randomly positioned obstacles
     * @param obstaclesNum sets a value for the obstacles
     * @return labyrinth
     */
    public int[][] createLabyrinth(int n, int obstaclesNum) {
    	this.obstacles = obstaclesNum;
    	labyrinth = new int[N][N];
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    			labyrinth[i][j] = 1;
    		}
    	}
    	int[] positions = getRandomPositions(n, obstaclesNum);
    	while (obstaclesNum > 0) {
    		int randomPos = positions[obstaclesNum - 1];
    		labyrinth[getRow(randomPos)][getColumn(randomPos)] = 0;
    		obstaclesNum--;
    	}
    	displayMatrix = labyrinth;
    	System.out.println("======== NEW LABYRINTH ==========");
    	printFor(displayMatrix, iterationSpeed * 5);
    	return labyrinth;
    }
    /**
     * This method tries to solve the labyrinth using backtracking
     * @return labyrinth
     */
	public boolean solveLabyrinth(int[][] labyrinth) {
		if (findPath(labyrinth, 0, 0, labyrinth.length, "down")) {
			System.out.println("======== SOLUTION: ==========");
			printFor(solution, iterationSpeed * 5);
			return true;
		} else{
			System.out.println("NO PATH FOUND");
			solutionExists = false;
			return solutionExists;
		}
	}
	/**
	 * Backtracking recursive method for labyrinth solving
	 * @param labyrinth
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param direction the direction taken by the algorithm the last iteration
	 * @return {@code true} if a path was found or {@code false} if a path was not found
	 */
	private boolean findPath(int[][] labyrinth, int x, int y, int N, String direction) {
		// check if labyrinth[x][y] is feasible to move
		if (x == N - 1 && y == N - 1){ //we have reached
			solution[x][y] = 3;
			updateDisplay();
			printFor(displayMatrix, iterationSpeed);
			return true;
		}
		if(isSafeToGo(x, y) && solution[x][y] != 3) {
			// move to labyrinth[x][y]
			solution[x][y] = 3;
			updateDisplay();
			printFor(displayMatrix, iterationSpeed);
			// now player has four options, either go right OR go down or left or go up
			if(direction!="up" && findPath(labyrinth, x+1, y, N, "down")){ //go down
				return true;
			}
			//else go down
			if(direction!="left" && findPath(labyrinth, x, y+1, N, "right")){ //go right
				return true;
			}
			if(direction!="down" && findPath(labyrinth, x-1, y, N, "up")){ //go up
				return true;
			}
			if(direction!="right" &&  findPath(labyrinth, x, y-1, N, "left")){ //go left
				return true;
			}
			//if none of the options work out BACKTRACK undo the move
			solution[x][y] = 2;
			return false;
		}
		return false;
	}
	/**
	 * This function will check if player can move to this cell
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return {@code true} if it is safe to go or {@code false} if it is not safe to go
	 */
	private boolean isSafeToGo(int x, int y) {
		// check if x and y are in limits and cell is not blocked
		if (x >= 0 && y >= 0 && x < N  && y < N && labyrinth[x][y] != 0) {
			return true;
		}
		return false;
	}
	/**
	 * Prints a NxN matrix with a time delay
	 * @param matrix NxN matrix to be printed
	 * @param speed time delay in milliseconds
	 */
	public void printFor(int [][] matrix, int speed){
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(" " + matrix[i][j] + 
               		 			 " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("\n Obstacles = " + obstacles + "\n");
		try {
			TimeUnit.MILLISECONDS.sleep(speed);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Updates the matrix that displays the labyrinth and the solution
	 */
	private void updateDisplay() {
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    			if(solution[i][j] == 2 || solution[i][j] == 3)
    				displayMatrix[i][j] = solution[i][j];
    			else
    				displayMatrix[i][j] = labyrinth[i][j];
    		}
    	}
    }
	/**
	 * Visualization of backtracking pathfinding. Every time a labyrinth is solved
	 * the amount of obstacles is increased by 2
	 */
	public void run() {
		while (solutionExists) {
			createLabyrinth(N, obstacles + 2);
			solveLabyrinth(labyrinth);
			resetMatrix(solution);
		}
		solutionExists = true;
		obstacles = 2;
	}
	/**
	 * Main method for testing
	 * @param args
	 */
	public static void main(String[] args) {
		Backtracking b = new Backtracking(8, 2, 500); // (size, obstacleIncrease, timePerIteration)
		b.run();
	}	
}