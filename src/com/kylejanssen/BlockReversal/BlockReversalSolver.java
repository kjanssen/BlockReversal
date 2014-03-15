// File: BlockReversalSolver.java
// Authors: Kyle Janssen, Aman Gill
// Content: This program takes as input an initial state for the block reversal problem, and uses A*
// to find a shortest path to the solution.

package com.kylejanssen.BlockReversal;

import java.util.*;

public class BlockReversalSolver {

    private BlockReversal blockReversal;
    private HashMap<BlockReversalState, BlockReversalState> openHash;
    private PriorityQueue<BlockReversalState> openQueue;
    private int iterations;


    // Default constructor for the Solver. It gets input from the user and instantiates the required
    // data structures.
    public BlockReversalSolver () {

        // Get input from the user
        Scanner input = new Scanner(System.in);
        int n;
        int[] permutation;

        System.out.print("\nThis is the Block Reversal Solver. Please enter an input size: ");
        n = input.nextInt();
        permutation = new int[n];

        System.out.print("\nPlease enter a space-separated permutation of the numbers 1 to " + n + " (or 0 for random): ");

        int first = input.nextInt();
        if (first != 0)
            for (int i = 0; i < n; i++)
                permutation[i] = i == 0 ? first : input.nextInt();

        // Set up components for the A* search. That is, the game object, the HashMap to hold opened states,
        // and a PriorityQueue to store the fringe states.
        blockReversal = first == 0 ? new BlockReversal(n) : new BlockReversal(permutation);
        openHash = new HashMap<BlockReversalState, BlockReversalState>();
        openQueue = new PriorityQueue<BlockReversalState>(blockReversal.numAdjacencies() * 1000,
                new Comparator<BlockReversalState>() {
                    public int compare(BlockReversalState first, BlockReversalState second)
                    {
                        // This is a custom comparator for the PriorityQueue. It uses the state's f value
                        // as the primary key, and its h value to break ties.
                        if (first.getF() < second.getF()) return -1;
                        if (first.getF() > second.getF()) return 1;
                        if (first.getH() < second.getH()) return -1;
                        if (first.getH() > second.getH()) return 1;
                        return 0;
                    }
                }
        );
    }

    // This function performs an A* search and returns the goal state object, whose lineage can
    // be traced back to the initial state.
    public BlockReversalState aStarSolution () {

        // A counter for the number of iterations A* takes.
        iterations = 0;

        // Adding the initial state to the OPEN set.
        BlockReversalState initialState = blockReversal.getState();
        openHash.put(initialState, initialState);
        openQueue.add(initialState);
        System.out.println("\nInput:    " + initialState + "\n");

        while (!openQueue.isEmpty()) {
            iterations++;

            // The current state used by A* is the current state of the blockReversal object. Here we
            // load the best state from the PriorityQueue.
            BlockReversalState next = openQueue.poll();
            //openHash.remove(next);
            blockReversal.setState(next);


            // Check to see if we have reached the goal state.
            if (blockReversal.done())
                return openHash.get(blockReversal.getState());

            // For each adjacent state to the current state, if it has not been visited, add it to the open set.
            BlockReversalState[] states = blockReversal.getAdjacentStates();
            for (int i = 0; i < states.length; i++) {
                if (openHash.get(states[i]) == null) {
                    openHash.put(states[i], states[i]);
                    openQueue.add(states[i]);
                }
            }
        }

        // If all states are expanded the the goal state has not been reached, return null.
        return null;
    }

    // This function traces the path from the given state to the initial state and prints them out in order.
    public void printSolution (BlockReversalState state) {

        ArrayList<BlockReversalState> winningPath = new ArrayList<BlockReversalState>();

        // Get a list of the state path in reverse order.
        for (BlockReversalState curr = state; curr != null; curr = curr.getParent() == null ? null : openHash.get(curr.getParent()))
            winningPath.add(curr);

        // Print the state path in correct order.
        System.out.println("Initial State:  " + winningPath.get(winningPath.size() - 1));
        for (int i = winningPath.size() - 2; i >= 0; i--) {
            System.out.println("Move " + (winningPath.size() - i - 1) + "       :  " + winningPath.get(i));
        }

        // Print the total number of nodes expanded.
        System.out.println("\nNodes expanded: " + openHash.size());
    }

    public static void main(String[] args) {

        BlockReversalSolver solver = new BlockReversalSolver();

        // Start timer and A* function.
        long start = System.nanoTime();
        BlockReversalState winningState = solver.aStarSolution();
        System.out.println("Get1");

        // Print elapsed time and A* iterations
        System.out.println(((double)(System.nanoTime() - start) / 1000000000.0) + " sec");
        System.out.println("Iterations: " + solver.iterations + "\n");

        System.out.println("Get2");

        // If solution is valid, print path.
        if (winningState != null) {
            System.out.println("Get3");
            solver.printSolution(winningState);
        }
    }
}
