package kpk;
class Knapsack {

    static int knapSack(int W, int[] wt, int[] val, int n)
    {
        // Base Case
        if (n == 0 || W == 0)
            return 0;

        // If weight of the nth item is more
        // than Knapsack capacity W, then
        // this item cannot be included in the optimal solution
        if (wt[n - 1] > W)
            return knapSack(W, wt, val, n - 1);

            // Return the maximum of two cases:
            // (1) nth item included
            // (2) not included
        else
            return Math.max(val[n - 1] + knapSack(W - wt[n - 1], wt, val, n - 1),
                    knapSack(W, wt, val, n - 1));
    }

    // Driver program to test above function
    public static void main(String args[])
    {
        int val[] = new int[] {7,4,9,18,9,15,4,2,6,13,18,12,12,16,19,19,10,16,14,3,14,4,15,7,5,10};
        int wt[] = new int[] {3,1,6,10,1,4, 9,1,7, 2, 6, 1, 6, 2, 2, 4, 8, 1, 7,3, 6,2, 9,5,3,3};
        int W = 40;
        int n = val.length;
        System.out.println(knapSack(W, wt, val, n));
    }
}
