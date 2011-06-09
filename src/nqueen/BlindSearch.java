package nqueen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**********************************************************************
 * 
 * Collin Price
 * cp06vz @ brocku.ca
 * 
 * BlindSearch
 * Oct. 18, 2010
 * 
 **********************************************************************/

public class BlindSearch {

	private static int num_results = 0;
	private static int configs_tested = 0;
	
	public static void main(String[] args) {
		int n = 0;
		
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
			
		} else { // values given from command line
			if (args.length == 1) {
				try {
					n = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					System.out.println("Input values must be integers: int");
					System.exit(1);
				}
			} else {
				System.out.println("Expecting one value: int");
				System.exit(1);
			}
		}

		System.out.println("N = " + n);
		System.out.println();
		
		
		int[] board = new int[n];
		boolean[] unused = new boolean[n];
		
		for (int i = 0; i < n; i++) {
			unused[i] = true;
		}
		long time1 = System.currentTimeMillis();
		blindSearch(unused, board, 0, n-1);
		long time2 = System.currentTimeMillis();
		System.out.println("Number of results = " + num_results);
		System.out.println(configs_tested + " board configurations were tested.");
		System.out.println("Searching took approximately " + (time2-time1) + " milliseconds.");
	} // main
	
	public static void blindSearch(boolean[] unused, int[] board, int column, int n) {
		if (column > n) {
			if (num_results < 1) {
				System.out.println("First board found.");
				printBoard(board);
				System.out.println("Please wait for final results.");
			}
			
			num_results++;
		} else {
			for (int row = 0; row <= n; row++) {
				if (unused[row] == true) {
					boolean safe = true;
					for (int i = 0; i < column; i++) {
						if (row == board[i]+column-i || row == board[i]-column+i) {
							safe = false;
							configs_tested++;
							break;
						}
					}
					if (safe) {
						unused[row] = false;
						board[column] = row;
						blindSearch(unused, board, column+1, n);
						unused[row] = true;
					}
				}
			}
		}
	} // blindSearch
	
	public static void printBoard(int[] board) {
		System.out.print("+");
		for (int i = 0; i < (board.length*2-1); i++) {
			System.out.print("-");
		}
		System.out.println("+");
		
		for (int i = 0; i < board.length; i++) {
			System.out.print("|");
			for (int j = 0; j < board.length; j++) {
				if (board[j] == i) {
					System.out.print("Q");
				} else {
					System.out.print(" ");
				}
				System.out.print("|");
			}
			System.out.println();
		}
		
		System.out.print("+");
		for (int i = 0; i < (board.length*2-1); i++) {
			System.out.print("-");
		}
		System.out.println("+");
	} // printBoard
	
} // BlindSearch
