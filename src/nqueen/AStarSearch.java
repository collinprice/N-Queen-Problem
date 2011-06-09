package nqueen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**********************************************************************
 * 
 * Collin Price
 * cp06vz @ brocku.ca
 * 
 * AStarSearch
 * Oct. 18, 2010
 * 
 **********************************************************************/

public class AStarSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 0;
		int seed = -1;
		
		if (args.length == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
					
			try {
				while (n < 1) {
					System.out.print("N? ");
					try {
						n = Integer.parseInt(in.readLine());
						if (n > 1 && n < 4) {
							System.out.println("N should be equal to 1 or greater than 3.");
							n = 0;
						}
					} catch (NumberFormatException ne) {
						System.out.println("Not a number. Try again.");
						n = -1;
					}
				}
			} catch (IOException e) {
				System.out.println("Error reading value of N.");
				System.exit(1);
			}
			
			try {
				while (seed == -1) {
					System.out.print("Seed? ");
					try {
						seed = Integer.parseInt(in.readLine());
					} catch (NumberFormatException ne) {
						System.out.println("Not a number. Try again.");
						seed = -1;
					}
				}
			} catch (IOException e) {
				System.out.println("Error reading seed value.");
				System.exit(1);
			}
			
		} else { // values given from command line
			if (args.length == 2) {
				try {
					n = Integer.parseInt(args[0]);
					seed = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("Input values must be integers: int int");
					System.exit(1);
				}
			} else {
				System.out.println("Expecting two values: int int");
				System.exit(1);
			}
		}

		System.out.println("N = " + n);
		System.out.println("Seed = " + seed);
		System.out.println();
		
		Random rand = new Random(seed);
		int[] all_numbers = new int[n*n];
		
		for (int i = 0; i < n*n; i++) {
			all_numbers[i] = i;
		}
		
		shuffleNumbers(all_numbers, rand);
		
		int[] board = new int[n];
		//int[] unused = new int[n];
		System.arraycopy(all_numbers, 0, board, 0, board.length);
		Arrays.sort(board);
		
//		for (int i = 0; i < n; i++) {
//			System.out.println(board[i]);
//		}
		System.out.println("Randomly generated board.");
		printBoard(board);
		AStarNode root = new AStarNode(board);
		//System.out.println("Number of safe queens = " + root.getF());
		
		
		System.out.println("Starting A* search...");
		aStar(root);
	} // main

	private static void aStar(AStarNode board) {
		long time1 = System.currentTimeMillis();

		Vector<AStarNode> openList = new Vector<AStarNode>();
		Vector<AStarNode> closedList = new Vector<AStarNode>();
		openList.add(board);
		boolean goalFound = false;
		AStarNode best = null;
		int boardConfigurations = 0;
		
		while(!openList.isEmpty()) {
			int best_value = Integer.MIN_VALUE;
			int best_h = -1;
			for (int i = 0; i < openList.size(); i++) {
				if (openList.get(i).getF() > best_value) {
					best_value = openList.get(i).getF();
					best_h = i;
				}
			}
			best = openList.remove(best_h);
			//System.out.println("Best node = " + best.printBoard() + ", h = " + best.getF());
			closedList.add(best);
			
			boardConfigurations++;
			if (best.isGoal()) {
				goalFound = true;
				break;
			} else {
				AStarNode[] childrenOfBest = best.getChildren();
				if (childrenOfBest == null) {
					System.out.println("no children");
					System.out.println(best.isGoal());
					System.exit(1);
				}
				for (AStarNode node : childrenOfBest) {
					if (node == null) {
						//System.out.println("node == null");
						//System.out.println(childrenOfBest.length);
						continue;
					}/* else {
						node.printBoard();
					}*/
					if (closedList.contains(node) || openList.contains(node)) {
						/*System.out.println("Closed List:");
						for (AStarNode n : closedList) {
							n.printBoard();
						}
						System.out.println("-----------------");
						System.out.println("node in closed list.");*/
						continue;
					}
					/*if (!openList.contains(node)) {
						//System.out.println("node not in open list");
						openList.add(node);
					}*/
					if (node.getF() >= best.getF()/* && !closedList.contains(node) && !openList.contains(childrenOfBest)*/) {
						openList.add(node);
						//System.out.println("Added to openList = " + node.printBoard() + ", h_value = "+ node.getF());
					}
				}
			}
		}
		
		if (goalFound) {
			long time2 = System.currentTimeMillis();
			System.out.println("Goal Board Found");
			printBoard(best.getBoard());
			System.out.println(boardConfigurations + " board configurations were tested.");
			System.out.println((openList.size()+closedList.size()) + " children were created.");
			System.out.println("Searching took approximately " + (time2-time1) + " milliseconds.");
			
			
		} else {
			System.out.println("Goal Board Not Found! Ugh try another seed ;)");
		}
		
		
	} // aStar

	/*
	 * Shuffle algorithm taken from a Wikipedia article on shuffling numbers.
	 * http://en.wikipedia.org/wiki/Fisher-Yates_shuffle_algorithm
	 */
	private static void shuffleNumbers(int[] allNumbers, Random rand) {
		for (int i = allNumbers.length; i > 1; i--) {
			int j = rand.nextInt(i);
			int tmp = allNumbers[j];
			allNumbers[j] = allNumbers[i-1];
			allNumbers[i-1] = tmp;
		}
	} // shuffleNumbers
	
	private static void printBoard(int[] board) {
		
		int[] completeBoard = new int[board.length*board.length];
		
		for (int i = 0; i < board.length; i++) {
			completeBoard[board[i]] = 1;
		}
		
		System.out.print("+");
		for (int i = 0; i < (board.length*2-1); i++) {
			System.out.print("-");
		}
		System.out.println("+");
		
		for (int i = 0; i < completeBoard.length; i++) {
			System.out.print("|");
			if (i != 0 && i % board.length == 0) {
				System.out.println();
				System.out.print("|");
			}
			if (completeBoard[i] == 1) {
				System.out.print("Q");
			} else {
				System.out.print(" ");
			}
		}
		
		System.out.println("|");
		System.out.print("+");
		for (int i = 0; i < (board.length*2-1); i++) {
			System.out.print("-");
		}
		System.out.println("+");
		
	} // printBoard
	
	
	
} // AStarSearch
