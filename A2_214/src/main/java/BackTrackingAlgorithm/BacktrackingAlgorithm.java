package BackTrackingAlgorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import com.mycompany.a2_214.RaceNQueens;


public class BacktrackingAlgorithm {
    private int N; // Size of the board
    public List<int[]> solutions = new ArrayList<>();
    private int functionCallCount; // Counter for function calls
    private int lastNonAttackingCount = -1; // Initial invalid value

    // Constructor
    public BacktrackingAlgorithm(int n) {
        this.N = n;
        this.functionCallCount = 0; // Initialize the counter
    }

    // A utility function to check if a queen can be placed
    boolean isSafe(int board[][], int row, int col) {
        for (int i = 0; i < col; i++)
            if (board[row][i] == 1)
                return false;

        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        for (int i = row, j = col; j >= 0 && i < N; i++, j--)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    boolean solveNQUtil(int board[][], int col) {
        functionCallCount++; // Increment the function call counter

        int nonAttackingCount = countNonAttackingQueens(board);

        // Determine the update interval based on N
        int updateInterval = 0;
        if (N == 4  ) {
            updateInterval = 2; // Always update, since modulus by 1 is always true
        }else if(N == 5){
            updateInterval = 15;
        }else if (N == 6 ) {
            updateInterval = 100; // Update every 10 calls
        } else if (N == 7 ){
            updateInterval = 250;
        } else if (N == 8 || N == 9 ) {
            updateInterval = 1000; // Update every 100 calls
        } else if (N == 10) {
            updateInterval = 1000; // Update every 1000 calls
        } else if (N > 10) {
            updateInterval = (int) Math.pow(10, N - 10 + 3); // For N > 10, update every 10^x calls, starting with 10^4
        }

// Only update the graph every `updateInterval` calls
        if (functionCallCount % updateInterval == 0) {
            RaceNQueens.updateBacktrackingData(functionCallCount, nonAttackingCount);
        }


        if (col >= N) {
            int[] position = new int[N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == 1) {
                        position[j] = i; // column index is j, row index is i
                    }
                }
            }
            solutions.add(position);
            return true; // Found a solution
        }

        for (int i = 0; i < N; i++) {
            if (isSafe(board, i, col)) {
                board[i][col] = 1; // Place the queen
                solveNQUtil(board, col + 1); // Recur to place rest of the queens
                board[i][col] = 0; // BACKTRACK
            }
        }
        return false;
    }




    // This function solves the N Queen problem using Backtracking
    public List<int[]> solveNQ() {
        int board[][] = new int[N][N];
        solveNQUtil(board, 0);
        return solutions; // Return all found solutions
    }

    // Method to get the number of function calls
    public int getFunctionCallCount() {
        return functionCallCount;
    }

    public static void runMultipleTrials() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the minimum size of the board (min N): ");
    int minN = scanner.nextInt();

    System.out.print("Enter the maximum size of the board (max N): ");
    int maxN = scanner.nextInt();

    System.out.print("Enter the number of trials: ");
    int trials = scanner.nextInt();

    System.out.print("Enter the filename to save results (e.g., results.csv): ");
    String filename = scanner.next();

    System.out.print("Would you like to overwrite the file? (yes/no): ");
    String choice = scanner.next();
    boolean overwrite = choice.equalsIgnoreCase("yes");

    // Write results to CSV file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, !overwrite))) {
        // Write header if overwriting
        if (overwrite) {
            writer.write("N,Best,Worst,Mean,SuccessRate");
            writer.newLine();
        }

        // Loop over each board size from minN to maxN
        for (int n = minN; n <= maxN; n++) {
            List<Integer> functionCallCounts = new ArrayList<>();
            int successfulTrials = 0; // Counter for successful trials

            for (int i = 0; i < trials; i++) {
                BacktrackingAlgorithm nQueen = new BacktrackingAlgorithm(n);
                nQueen.solveNQ();
                functionCallCounts.add(nQueen.getFunctionCallCount());

                // Check if at least one solution was found
                if (!nQueen.solutions.isEmpty()) {
                    successfulTrials++;
                }
            }

            // Calculate statistics
            int best = Collections.min(functionCallCounts);
            int worst = Collections.max(functionCallCounts);
            double sum = 0;

            // Using for-each to sum up the function call counts
            for (int count : functionCallCounts) {
                sum += count;
            }

            double mean = sum / trials;
            double successRate = (successfulTrials / (double) trials) * 100; // Success rate as a percentage

            // Format the statistics to two decimal places
            DecimalFormat df = new DecimalFormat("#.00");

            // Write results for the current board size
            writer.write(n + "," + best + "," + worst + "," + df.format(mean) + "," + df.format(successRate) + "%");
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    System.out.println("Results written to " + filename);
}

    // A utility function to count non-attacking queens on the board
    private int countNonAttackingQueens(int board[][]) {
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 1) count++;
            }
        }
        return count;
    }


    // Main method to test the implementation
    public static void main(String[] args) {
        runMultipleTrials();
    }
}
