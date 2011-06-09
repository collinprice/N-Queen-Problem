package nqueen;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**********************************************************************
 * 
 * Collin Price
 * cp06vz @ brocku.ca
 * 
 * AStarNode
 * Oct. 18, 2010
 * 
 **********************************************************************/

public class AStarNode implements Comparable<AStarNode>, Collection<AStarNode>{
	
	private AStarNode parent;
	private int[] board;
	private int[] worst_queen;
	private int h;
	
	public AStarNode() {
		parent = null;
	} // base constructor
	
	public AStarNode(int[] board) {
		this.board = new int[board.length];
		System.arraycopy(board, 0, this.board, 0, board.length);
		this.h = generateH();
	} // root constructor
	
	public AStarNode(AStarNode parent, int[] board) {
		this.parent = parent;
		this.board = new int[board.length];
		System.arraycopy(board, 0, this.board, 0, board.length);
		this.h = generateH();
	} // child constructor
	
	public AStarNode getParent() {
		return parent;
	} // getParent
	
	public int getH() {
		return h;
	} // getH
	
	public int getF() {
		return h;
	} // getH
	
	public AStarNode[] getChildren() {
		
		int[] bad_queens = getWorstQueen();
		// (n^2 - n) * number of bad_queens
		AStarNode[] total_children = new AStarNode[((board.length*board.length)-board.length)*bad_queens.length];
//		System.out.println("number of children = " + total_children.length);
		int counter = 0;
		int[] temp_board = new int[board.length];
		System.arraycopy(board, 0, temp_board, 0, board.length);
		
		for (int queen : bad_queens) {
//			System.out.println("bad_queen = " + queen + ", index = " + index);
			for (int i = 0; i < (board.length*board.length);i++) {
				System.arraycopy(board, 0, temp_board, 0, board.length);
				if (boardContains(board, i) || i == queen) {
//					System.out.println("board already contains item = " + i);
					continue;
				} else {
//					System.out.println("temp_board[queen] = i, " + temp_board[queen] + " = " + i);
					temp_board[queen] = i;
					Arrays.sort(temp_board);
					/*System.out.print("temp_board = ");
					for (int j : temp_board) {
						System.out.print(j + " ");
					}
					System.out.println();*/
					total_children[counter++] = new AStarNode(this, temp_board);
//					System.out.println("child = " + total_children[counter - 1].printBoard());
				}
			}
		}
		//System.out.println("Created " + (counter) + " children");
		
		return total_children;
	} // getChildren
	
	private int[] getWorstQueen() {
		int queen = -1;
		int num_queens = 0;
		
		// detects which queen is worst
		for (int i = 0; i < worst_queen.length; i++) {
			if (worst_queen[i] >= queen) {
				queen = worst_queen[i];
			}
		}
		
		// counts number of bad queens
		for (int i = 0; i < worst_queen.length; i++) {
			if (worst_queen[i] == queen) {
				num_queens++;
			}
		}
		
		int[] bad_queens = new int[num_queens];
		int temp = 0;
		
		for (int i = 0; i < worst_queen.length; i++) {
			if (worst_queen[i] == queen) {
				bad_queens[temp++] = i;
			}
		}
		
		return bad_queens;
	} // getWorstQueen
	
	private int generateH() {
		int n = board.length;
		int attack_counter = 0;
		LinkedList<Integer> list = new LinkedList<Integer>();
		int[] hit_count = new int[board.length];
		
			// search horizonal
			for (int column = 0; column < n*n; column += n) {
				for (int row = 0; row < n; row++) {
					int queen = column+row;
					if (boardContains(board, queen)) {
						list.add(queen);
					}
				}
				if (list.size() > 1) {
					while (!list.isEmpty()) {
						int removed = list.remove();
						int temp = Arrays.binarySearch(board, removed);
						hit_count[temp]++;
//						System.out.println("removed " + removed);
//						System.out.println("added it to " + temp);
					}
					attack_counter++;
//					System.out.println("horizonal hit");
				}
				list.clear();
			}
			
			// search vertical
			for (int row = 0; row < n; row++) {
				for (int column = 0; column < n*n; column += n) {
					int queen = column+row;
					if (boardContains(board, queen)) {
						list.add(queen);
					}
				}
				if (list.size() > 1) {
					while (!list.isEmpty()) {
						int removed = list.remove();
						int temp = Arrays.binarySearch(board, removed);
						hit_count[temp]++;
//						System.out.println("removed " + removed);
//						System.out.println("added it to " + temp);
					}
					attack_counter++;
//					System.out.println("vertical hit");
				}
				list.clear();
			}
			
			
			// search down diagonal
			for (int vertical = 1; vertical < (n+1); vertical++) {
				for (int horizontal = 0; horizontal < vertical; horizontal++) {
					int queen = ((n-vertical)*n) + (horizontal)*(n+1);
					if (boardContains(board, queen)) {
						list.add(queen);
					}
				}
				if (list.size() > 1) {
					while (!list.isEmpty()) {
						int removed = list.remove();
						int temp = Arrays.binarySearch(board, removed);
						hit_count[temp]++;
//						System.out.println("removed " + removed);
//						System.out.println("added it to " + temp);
					}
					attack_counter++;
//					System.out.println("down diagonal hit1");
				}
				list.clear();
			}
			
			for (int horizontal = 1; horizontal < n; horizontal++) {
				for (int vertical = 0; vertical < (n-horizontal); vertical++) {
					int queen = horizontal + (vertical)*(n+1);
					if (boardContains(board, queen)) {
						list.add(queen);
					}
				}
				if (list.size() > 1) {
					while (!list.isEmpty()) {
						int removed = list.remove();
						int temp = Arrays.binarySearch(board, removed);
						hit_count[temp]++;
//						System.out.println("removed " + removed);
//						System.out.println("added it to " + temp);
					}
					attack_counter++;
//					System.out.println("down diagonal hit2");
				}
				list.clear();
			}
			
			
			// search up diagonal
			for (int horizontal = 0; horizontal < n; horizontal++) {
				for (int vertical = 0; vertical < (horizontal+1); vertical++) {
					int queen = horizontal + (vertical)*(n-1);
					if (boardContains(board, queen)) {
						list.add(queen);
					}
				}
				if (list.size() > 1) {
					while (!list.isEmpty()) {
						int removed = list.remove();
						int temp = Arrays.binarySearch(board, removed);
						hit_count[temp]++;
//						System.out.println("removed " + removed);
//						System.out.println("added it to " + temp);
					}
					attack_counter++;
//					System.out.println("up diagonal hit1");
				}
				list.clear();
			}
			
			for (int horizontal = 2; horizontal < (n+1); horizontal++) {
				for (int vertical = 0; vertical < ((n+1)-horizontal); vertical++) {
					int queen = ((horizontal*n) - 1) + (vertical)*(n-1);
					if (boardContains(board, queen)) {
						list.add(queen);
					}
				}
				if (list.size() > 1) {
					while (!list.isEmpty()) {
						int removed = list.remove();
						int temp = Arrays.binarySearch(board, removed);
						hit_count[temp]++;
//						System.out.println("removed " + removed);
//						System.out.println("added it to " + temp);
					}
					attack_counter++;
//					System.out.println("up diagonal hit2");
				}
				list.clear();
			}
			
//		for (int i : hit_count) {
//			System.out.println("hits = " + i);
//		}
			
		worst_queen = hit_count;
		int h_value = n;
		
		for (int i : worst_queen) {
			if (i != 0) h_value--;
		}
		return h_value;
	} // generateH
	
	
	private boolean boardContains(int[] list, int item) {
		for (int i = 0; i < list.length; i++) {
//			System.out.println("item = " + item);
//			System.out.println("i = " + i);
			if (list[i] == item) return true;
		}
		
		return false;
	} // contains
	
	@Override
	public int compareTo(AStarNode o) {
		if (this.getF() < o.getF()) return -1;
		if (this.getF() > o.getF()) return 1;
		return 0;
	}

	public boolean isGoal() {
		for (int i : worst_queen) {
			if (i != 0) return false;
		}
		
		return true;
	} // isGoal

	public int[] getBoard() {
		return board;
	}
	
	public String printBoard() {
		String stringBoard = "{";
		for (int i = 0; i < board.length; i++) {
			if (i == board.length - 1) {
				stringBoard += board[i] + "}";
			} else {
				stringBoard += board[i] + ", ";
			}
			
		}
		return stringBoard;
	} // printBoard

	@Override
	public boolean add(AStarNode e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends AStarNode> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof AStarNode) {
			if (Arrays.equals(this.getBoard(), ((AStarNode) o).getBoard())) return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<AStarNode> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}


} // AStarNode
