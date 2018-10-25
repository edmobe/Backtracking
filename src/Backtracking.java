import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Backtracking {

	private int[][] solution;
	private int obstacles = 2;
	private int N;
	private boolean solutionExists = true;
	private int[] allPositions = getAllPositions();

	//initialize the solution matrix in constructor.
	public Backtracking(int N) {
		this.N = N;
		resetSolution();
	}
	void resetSolution() {
		solution = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				solution[i][j] = 0;
			}
		}
	}
	int getColumn(int num) {
    	return num % N;
	}
    int getRow(int num) {
    	return num / N;
    }
    int[] getAllPositions() {
    	int[] positions = new int[64];

    	for (int i = 0; i < 64; i++) {
    		positions[i] = i;
    	}
    	return positions;
    }
    int[] getRandomPos(int amount) {
    	int[] positions = new int[amount];
    	int available = amount;
    	while (available > 0) {
    		int randomNum = ThreadLocalRandom.current().nextInt(1, N*N - 2 - available);
    		positions[amount - available] = allPositions[randomNum];
    		available--;
    	}
    	return positions;
    }
    int[][] createLabyrinth(int obstacles) {
    	int[][] labyrinth = new int[N][N] ;
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    			labyrinth[i][j] = 1;
    		}
    	}
    	int[] positions = getRandomPos(obstacles);
    	while (obstacles > 0) {
    		int randomPos = positions[obstacles - 1];
    		labyrinth[getRow(randomPos)][getColumn(randomPos)] = 0;
    		obstacles--;
    	}
    	return labyrinth;
    }
	public void solveLabyrinth(int[][] labyrinth) {
		if (findPath(labyrinth, 0, 0, N, "down")) {
			printAll(labyrinth, solution, N);
		} else{
			System.out.println("NO PATH FOUND");
			solutionExists = false;
		}
		
	}
	public boolean findPath(int[][] labyrinth, int x, int y, int N, String direction) {
		// check if labyrinth[x][y] is feasible to move
		if (x == N-1 && y == N-1){ //we have reached
			printAll(labyrinth, solution, N);
			solution[x][y] = 3;
			return true;
		}
		if(isSafeToGo(labyrinth, x, y, N) && solution[x][y] != 3) {	
			printAll(labyrinth, solution, N);
			// move to labyrinth[x][y]
			solution[x][y] = 3;			
			// now player has four options, either go right OR go down or left or go up
			if(direction!="up" && findPath(labyrinth, x+1, y, N, "down")){ //go down
				return true;
			}
			//else go down
			if(direction!="left" && findPath(labyrinth, x, y+1, N,"right")){ //go right
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
	// this function will check if player can move to this cell
	public boolean isSafeToGo(int[][] labyrinth, int x, int y, int N) {
		// check if x and y are in limits and cell is not blocked
		if (x >= 0 && y >= 0 && x < N  && y < N && labyrinth[x][y] != 0) {
			return true;
		}
		return false;
	}
	public void printSolution(int [][] solution, int N){
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(" " + solution[i][j] + 
               		 			 " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	void printAll(int[][] labyrinth, int[][] solution, int N) {
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    			if(solution[i][j] == 2 || solution[i][j] == 3)
    				System.out.print(" " + solution[i][j] + 
                            		 " ");
    			else
    				System.out.print(" " + labyrinth[i][j] + 
                        		  	 " ");
    		}
    		System.out.println();
    	}
    	System.out.println("\n Obstacles = " + obstacles + "\n");
    	try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	void run() {
		while (solutionExists) {
			int[][] newLabyrinth = createLabyrinth(obstacles);
			solveLabyrinth(newLabyrinth);			
			obstacles += 2;
			resetSolution();
		}
		solutionExists = true;
		obstacles = 2;
	}
	public static void main(String[] args) {
		int N = 8;
		Backtracking b = new Backtracking(N);
		b.run();
	}
}