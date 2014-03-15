package com.kylejanssen.BlockReversal;

import java.util.*;

public class BlockReversalSolver {

    private BlockReversal blockReversal;
    private Hashtable<BlockReversalState, BlockReversalState> openHash;
    private PriorityQueue<BlockReversalState> openQueue;
    private int iterations;

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

        // Set up components for the A* search. That is, the game object, the Hashtable to hold opened states,
        // and a PriorityQueue to store the fringe states.
        blockReversal = first == 0 ? new BlockReversal(n) : new BlockReversal(permutation);
        openHash = new Hashtable<BlockReversalState, BlockReversalState>();
        openQueue = new PriorityQueue<BlockReversalState>(blockReversal.numAdjacencies() * 1000,
                new Comparator<BlockReversalState>() {
                    public int compare(BlockReversalState first, BlockReversalState second)
                    {
                        if (first.getF() < second.getF()) return -1;
                        if (first.getF() > second.getF()) return 1;
                        if (first.getH() < second.getH()) return -1;
                        if (first.getH() > second.getH()) return 1;
                        return 0;
                    }
                }
        );
    }

    public BlockReversalState aStarSolution () {

        iterations = 0;
        BlockReversalState initialState = blockReversal.getState();
        openHash.put(initialState, initialState);
        openQueue.add(initialState);
        System.out.println("\nInput:    " + initialState);

        int count = 0;
        while (!openQueue.isEmpty()) {
            iterations++;
            blockReversal.setState(openQueue.poll());

            if (blockReversal.done())
                return openHash.get(blockReversal.getState());

            BlockReversalState[] states = blockReversal.getAdjacentStates();
            for (int i = 0; i < states.length; i++) {
                if (!openHash.contains(states[i])) {
                    openHash.put(states[i], states[i]);
                    openQueue.add(states[i]);
                }
            }

            //System.out.println("Step  " + iterations + ":   " + openHash.get(blockReversal.getState()) + "\n");
        }

        return null;
    }

    public void printSolution (BlockReversalState state) {

        ArrayList<BlockReversalState> winningPath = new ArrayList<BlockReversalState>();

        for (BlockReversalState curr = state; curr != null; curr = curr.getParent() == null ? null : openHash.get(curr.getParent()))
            winningPath.add(curr);

        System.out.println("Initial State:  " + winningPath.get(winningPath.size() - 1));
        for (int i = winningPath.size() - 2; i >= 0; i--) {
            System.out.println("Move " + (winningPath.size() - i - 1) + "       :  " + winningPath.get(i));
        }

        System.out.println("Nodes expanded: " + openHash.size());
    }

    public static void main(String[] args) {

        BlockReversalSolver solver = new BlockReversalSolver();

        long start = System.nanoTime();
        BlockReversalState winningState = solver.aStarSolution();
        System.out.println(((double)(System.nanoTime() - start) / 1000000000.0) + " sec");
        System.out.println("Iterations: " + solver.iterations);

        if (winningState != null) {
            solver.printSolution(winningState);
        }
    }
}
