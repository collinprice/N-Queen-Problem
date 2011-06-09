Blind Search

Compiling
Compile from the current directory with: javac nqueen/BlindSearch.java

Running
To run use: 

java nqueen/BlindSearch
or
java nqueen/BlindSearch n
- where n is the number of queens


A* Search

How it works:
My heuristic for A* is based on the number of non-attacked queens. The board with the most amount of non-attacked queens is the "best" board for searching. The queen that is most attacked on the "best" board is the queen that is moved to generate the children. If there is more than one queen being attacked the most, children are generated from all of these queens.

The goal state is reached if the number of non-attacked queens is equal to the total number of queens.

Compiling
Compile from the current directory with: javac nqueen/AStarSearch.java

Running
To run use: 

java nqueen/AStarSearch
or
java nqueen/AStarSearch n seed
- where n is the number of queens, and seed is the seed value for the random generator
