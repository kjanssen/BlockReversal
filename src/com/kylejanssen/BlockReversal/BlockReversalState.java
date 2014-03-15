// File: BlockReversalState.java
// Authors: Kyle Janssen, Aman Gill
// Contents: An object of this class represents a single state of the BlockReversal puzzle. It keeps
// track of who it's parent state is, and what move was made to get to the current state from the parent
// state.

package com.kylejanssen.BlockReversal;

import java.util.Arrays;

public class BlockReversalState {

    private int[] nums;
    private int h, g, begin, end;
    private BlockReversalState parent;

    public BlockReversalState (int[] nums, int h, int g, BlockReversalState parent) {
        this.nums = nums.clone();
        this.h = h;
        this.g = g;
        this.parent = parent;
        begin = end = -1;
    }

    // This function records the move that created this state from the parent state
    public void setMove (int i, int j) {
        begin = i;
        end = j;
    }

    public void setParent (BlockReversalState parent) {
        this.parent = parent;
    }

    public int[] getNums() {
        return nums.clone();
    }

    public int getH() {
        return h;
    }

    public int getG() {
        return g;
    }

    public int getF() {
        return h + g;
    }

    public BlockReversalState getParent () {
        return parent;
    }

    public String toString () {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<" + (begin == 1 ? "[" : " "));
        for (int k = 0; k < nums.length; k++)
            stringBuilder.append(nums[k] + (begin == k + 2 ? "[" : (end == k + 1 ? "]" : " ")));
        stringBuilder.append(">  h(x): " + h + ", g(x): " + g + ", f(x): " + this.getF());

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockReversalState that = (BlockReversalState) o;

        if (!Arrays.equals(nums, that.nums)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nums != null ? Arrays.hashCode(nums) : 0;

        return result;
    }
}
