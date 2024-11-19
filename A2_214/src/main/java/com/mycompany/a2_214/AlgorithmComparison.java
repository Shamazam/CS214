package com.mycompany.a2_214;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AlgorithmComparison {

    // Nested class to hold results for each algorithm
    static class AlgorithmResults {
        int n;
        String best;
        String worst;
        String mean;
        String successRate;

        public AlgorithmResults(int n, String best, String worst, String mean, String successRate) {
            this.n = n;
            this.best = best;
            this.worst = worst;
            this.mean = mean;
            this.successRate = successRate;
        }
    }

 

public static void createComparisonTable() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the filename for the Backtracking algorithm results: ");
    String backtrackingFile = scanner.next();

    System.out.print("Enter the filename for the Genetic algorithm results: ");
    String geneticFile = scanner.next();

    // Create lists to store results
    List<AlgorithmResults> backtrackingResults = readResults(backtrackingFile);
    List<AlgorithmResults> geneticResults = readResults(geneticFile);

    // ANSI escape codes for colors
    String reset = "\u001B[0m";
    String backtrackingColor = "\u001B[34m"; // Blue
    String geneticColor = "\u001B[32m"; // Green
    String headerColor = "\u001B[36m"; // Cyan

    // Print Backtracking results table
    System.out.println("\n\n" + backtrackingColor + "Backtracking Algorithm Results" + reset);
    System.out.printf("%s%-10s %-20s %-20s %-20s %-20s%s%n",
            headerColor, "N", "Best", "Worst", "Mean", "Success Rate", reset);
    System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

    for (AlgorithmResults bResult : backtrackingResults) {
        System.out.printf(backtrackingColor+ "%-10d %-20s %-20s %-20s %-20s%n",
                bResult.n,
                bResult.best, bResult.worst, bResult.mean, bResult.successRate);
    }

    System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
    System.out.println();

    // Print Genetic results table
    System.out.println(geneticColor + "Genetic Algorithm Results" + reset);
    System.out.printf("%s%-10s %-20s %-20s %-20s %-20s%s%n",
            headerColor, "N", "Best", "Worst", "Mean", "Success Rate", reset);
    System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

    for (AlgorithmResults gResult : geneticResults) {
        System.out.printf(geneticColor+ "%-10d %-20s %-20s %-20s %-20s%n",
                gResult.n,
                gResult.best, gResult.worst, gResult.mean, gResult.successRate);
    }

    System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
    System.out.println("End of Comparison Tables");
}






    private static List<AlgorithmResults> readResults(String filename) {
        List<AlgorithmResults> results = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int n = Integer.parseInt(parts[0]);
                String best = parts[1];
                String worst = parts[2];
                String mean = parts[3];
                String successRate = parts[4];

                results.add(new AlgorithmResults(n, best, worst, mean, successRate));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}
