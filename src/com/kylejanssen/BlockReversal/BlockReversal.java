package com.kylejanssen.BlockReversal;


import java.util.Random;

public class BlockReversal {

    private int[] nums;
    private int turnCount;
    private int depth;
    private int lastFrom;
    private int lastTo;
    private BlockReversalState parent;


    // This constructor creates a new BlockReversal game of length 'size'.
    public BlockReversal (int size) {

        if (size == 0)
            nums = new int[] {1, 3, 6, 4, 2, 5};
        else {
            nums = new int[size];
            initialize();
        }

        turnCount = 0;
        depth = 0;
        lastFrom = -1;
        lastTo = -1;
    }

    // This constructor creates a new BlockReversal game with the passed state.
    public BlockReversal (int[] state) {

        nums = state;
        turnCount = 0;
        depth = 0;
        lastFrom = -1;
        lastTo = -1;
    }

    // This function initializes the game state by randomizing the list of numbers.
    private void initialize() {

        Random random = new Random();

        for (int i = 0; i < nums.length; i++)
            nums[i] = i + 1;

        for (int i = 0; i < nums.length; i++) {
            int index = random.nextInt(nums.length - i) + i;
            int temp = nums[i];
            nums[i] = nums[index];
            nums[index] = temp;
        }
    }

    public int getTurnCount () {
        return turnCount;
    }

    public BlockReversalState getState () {
        return new BlockReversalState(nums, h(), depth, parent);
    }

    public void setState (BlockReversalState state) {
        nums = state.getNums();
        depth = state.getG();
    }

    public String toString () {

        String out = "\n  ";

        for (int i = 0; i < nums.length; i++)
            out += (i + 1) + " ";

        out += "\n< ";

        for (int i = 0; i < nums.length; i++)
            out += nums[i] + " ";

        out += ">  h(x): " + h() + ", g(x): " + depth + ", f(x): " + (h() + depth);

        return out;
    }

    // This function represents one turn being taken by the player
    public void move (int from, int to) {
        flip(from, to);
        turnCount++;
    }

    // This function reverses the order of the numbers between 'from' and 'to'.
    // Note: this function takes input [1, n], not [0, n - 1]
    private void flip (int from, int to) {

        if (from == lastFrom && to == lastTo)
            depth--;
        else
            depth++;

        lastFrom = from;
        lastTo = to;

        int flipSize = to - from + 1;
        int temp[] = new int[flipSize];

        for (int i = 0; i < flipSize; i++)
            temp[i] = nums[i + from - 1];

        for (int i = 0; i < flipSize; i++)
            nums[i + from - 1] = temp[flipSize - i - 1];
    }

    // This function returns true if the goal state has been reached.
    public Boolean done () {

        for (int i = 0; i < nums.length; i++)
            if (nums[i] != i + 1)
                return false;

        return true;
    }

    // This function returns the number of breakpoints in the game state. That is,
    // the number of points in the list where the values are not numerically adjacent.
    public int numBreakpoints () {

        int count = 0;

        for (int i = 0; i < nums.length -1; i++)
            if (nums[i] != nums[i + 1] + 1 && nums[i] != nums[i + 1] - 1)
                count++;

        return count;
    }

    // This function returns the heuristic value h(x) = 1/2 * the number of breakpoints.
    public int h() {
        return numBreakpoints() / 2;
    }

    // This function returns a String printout of all of the game states reachable from the current state,
    // along with their heuristic values useful for A*.
    public String getMoves () {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < nums.length; i++)
            for (int j = i + 1; j < nums.length + 1; j++) {
                flip(i, j);

                stringBuilder.append("\n" + i + ", " + j + ": < ");
                for (int k = 0; k < nums.length; k++)
                    stringBuilder.append(nums[k] + " ");
                stringBuilder.append(">  h(x): " + h() + ", g(x): " + depth + ", f(x): " + (h() + depth));

                flip(i, j);
            }

        lastFrom = lastTo = 0;

        return stringBuilder.toString();
    }

    // This function returns the number of states reachable from the current state
    public int numAdjacencies () {
        int n = 0;

        for (int i = 0; i < nums.length; i++)
            n += i;

        return n;
    }

    // This frunction returns an array of states reachable from the current state
    public BlockReversalState[] getAdjacentStates () {
        BlockReversalState[] states = new BlockReversalState[numAdjacencies()];
        BlockReversalState thisParent = getState();
        int count = 0;

        for (int i = 1; i < nums.length; i++)
            for (int j = i + 1; j < nums.length + 1; j++) {
                flip(i, j);

                states[count] = getState();
                states[count].setMove(i, j);
                states[count++].setParent(thisParent);

                flip(i, j);
            }

        lastFrom = lastTo = 0;

        return states;
    }
}
