/* *****************************************************************************
 *  Name:    Rahat
 *  NetID:   aring
 *  Precept: P00
 *
 *  Description:
 *                Calculates perculation states
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] threshHoldVals; // dd

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        threshHoldVals = new double[trials];
        double b = 0.0;
        boolean[][] visited = new boolean[n + 1][n + 1];
        Percolation perc = null;
        int countCell = 0;
        int rand = 0;
        int rand2 = 0;
        for (int i = 0; i < trials; i++) {
            perc = new Percolation(n);
            while (!perc.percolates()) {
                 rand = StdRandom.uniform(1, n + 1);
                 rand2 = StdRandom.uniform(1, n + 1);
                // System.out.println(rand + " - " + rand2);
                if (!visited[rand][rand2]) {
                    visited[rand][rand2] = true;
                    perc.open(rand, rand2);
                    // System.out.println("---------AFTER EFFECT--- "+ i +"------------");
                    // perc.print();
                    // System.out.println("----------");
                }
                // else {
                //     // System.out.println("---------NO EFFECT--- "+ i +"------------") ;
                //     // perc.print();
                //     // System.out.println("----------");
                // }
            }
             System.out.println("-----------------" + i + "---------------");
            countCell = perc.numberOfOpenSites();
            b = n;
            threshHoldVals[i] = countCell / (b * b);

            countCell = 0;
            visited = new boolean[n + 1][n + 1];
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshHoldVals);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshHoldVals);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return StdStats.max(threshHoldVals);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return StdStats.min(threshHoldVals);
    }

    // test client (see below)
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException();
        }

        PercolationStats percStat = new PercolationStats(n, t);
        System.out.print("mean =                  =   ");
        System.out.print(percStat.mean());
        System.out.println("");
        System.out.print("stddev =                =   ");
        System.out.print(percStat.stddev());
        System.out.println("");
        System.out.print("95% confidence interval =  [");
        System.out.print(percStat.confidenceLo());
        System.out.print(",");
        System.out.print(percStat.confidenceHi());
        System.out.print("]");
        System.out.println("");

    }
}