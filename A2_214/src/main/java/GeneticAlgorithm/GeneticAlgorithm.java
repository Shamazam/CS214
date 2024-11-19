package GeneticAlgorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import com.mycompany.a2_214.RaceNQueens;

public class GeneticAlgorithm {
    public ArrayList<Chromosome> population; // Population with chromosomes
    private int objectiveFunctionCalls; // Counter for objective function calls
    private int N; // Number of Queens
    private Random random = new Random();
    public boolean foundSolution = false; // Flag to check if solution is found

    // Constructor with default parameters
    public GeneticAlgorithm(int N) {
        this(N, 100, 0.1, 1000); // Default population size, mutation probability, max steps
    }

    // Constructor with customizable parameters
    public GeneticAlgorithm(int N, int populationSize, double mutationProbability, int maxSteps) {
        this.population = new ArrayList<>();
        this.objectiveFunctionCalls = 0;
        this.N = N;
        initializePopulation(populationSize);
        this.run(mutationProbability, maxSteps);
    }

    private void run(double mutationProbability, int maxSteps) {
        for (int step = 0; step < maxSteps; step++) {
            ArrayList<Chromosome> newPopulation = new ArrayList<>();


            for (int i = 0; i < population.size() / 2; i++) {
                Chromosome xParent = selectParent();
                Chromosome yParent = selectParent();

                Chromosome[] children = reproduce(xParent, yParent);

                if (random.nextDouble() < mutationProbability) {
                    children[0].mutate();
                    children[1].mutate();
                }

                newPopulation.add(children[0]);
                newPopulation.add(children[1]);
            }
            this.population = newPopulation;
            Collections.sort(this.population, Collections.reverseOrder());

            // Count objective function calls
            incrementObjectiveFunctionCalls();

            int updateInterval = 1;
            if (N == 4) {
                updateInterval = 1; // Always update
            } else if (N >= 10) {
                updateInterval = 100;
            }

            // Track best fitness in terms of non-attacking queens
            int currentBestFitness = countNonAttackingQueens(population.get(0));

            // Only update the graph every `updateInterval` calls
            if (objectiveFunctionCalls % updateInterval == 0) {
                RaceNQueens.updateGeneticData(objectiveFunctionCalls, currentBestFitness);
            }

            // Check for solution
            if (this.population.get(0).getFitness() == (N * (N - 1)) / 2) {
                foundSolution = true; // Set flag if solution is found
                break;
            }
        }

//        // Print result after finishing
//        printResults(foundSolution); // Pass the flag to print results
    }

    public void printResults(boolean foundSolution) {
        Chromosome bestSolution = population.get(0);
        int fitness = bestSolution.getFitness() * 2 / N + 1;
        System.out.println("Best Fitness: " + fitness);
        System.out.println("Number of objective function calls: " + getObjectiveFunctionCalls());

        if (foundSolution) {
            System.out.println("Solution found!");
            //displayBoard(bestSolution);
        } else {
            System.out.println("No solution found within the given steps.");
        }
    }

    private void initializePopulation(int populationSize) {
        for (int i = 0; i < populationSize; i++) {
            this.population.add(new Chromosome(N, this));
        }
    }

    private Chromosome selectParent() {
        int tournamentSize = 5; // Example size
        Chromosome best = null;

        for (int i = 0; i < tournamentSize; i++) {
            Chromosome candidate = population.get(random.nextInt(population.size()));
            if (best == null || candidate.getFitness() > best.getFitness()) {
                best = candidate;
            }
        }
        return best;
    }

    private Chromosome[] reproduce(Chromosome x, Chromosome y) {
        int intersectionPoint = random.nextInt(N);
        int[] firstChild = new int[N];
        int[] secondChild = new int[N];

        for (int i = 0; i < intersectionPoint; i++) {
            firstChild[i] = x.getGenes()[i];
            secondChild[i] = y.getGenes()[i];
        }

        for (int i = intersectionPoint; i < firstChild.length; i++) {
            firstChild[i] = y.getGenes()[i];
            secondChild[i] = x.getGenes()[i];
        }

        return new Chromosome[]{new Chromosome(firstChild), new Chromosome(secondChild)};
    }

    public void incrementObjectiveFunctionCalls() {
        objectiveFunctionCalls++;
    }

    public int getObjectiveFunctionCalls() {
        return objectiveFunctionCalls;
    }

    public boolean getFoundSolution() {
        return foundSolution;
    }

    private void displayBoard(Chromosome solution) {
        System.out.println("\nBoard Configuration:");
        for (int i = 0; i < solution.getN(); i++) {
            for (int j = 0; j < solution.getN(); j++) {
                if (solution.getGenes()[j] == i) {
                    System.out.print(" Q "); // Queen
                } else {
                    System.out.print(" . "); // Empty space
                }
            }
            System.out.println(); // New line for the next row
        }
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
            List<Integer> objectiveFunctionCounts = new ArrayList<>();
            int successfulTrials = 0; // Counter for successful trials

            for (int i = 0; i < trials; i++) {
                GeneticAlgorithm ga = new GeneticAlgorithm(n);


                // Check if a solution was found
                if (ga.getFoundSolution()) {
                    objectiveFunctionCounts.add(ga.getObjectiveFunctionCalls());
                    successfulTrials++;
                }
            }

            // Calculate statistics
            int best = Collections.min(objectiveFunctionCounts);
            int worst = Collections.max(objectiveFunctionCounts);
            double sum = 0;

            // Using for-each to sum up the function call counts
            for (int count : objectiveFunctionCounts) {
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

    private int countNonAttackingQueens(Chromosome chromosome) {
        int[] genes = chromosome.getGenes();
        boolean[] rows = new boolean[N];
        boolean[] diags1 = new boolean[2 * N]; // For diagonal checks
        boolean[] diags2 = new boolean[2 * N];

        int count = 0;
        for (int i = 0; i < N; i++) {
            if (!rows[genes[i]] && !diags1[i + genes[i]] && !diags2[i - genes[i] + N]) {
                count++;
                rows[genes[i]] = true;
                diags1[i + genes[i]] = true;
                diags2[i - genes[i] + N] = true;
            }
        }
        return count;
    }



//    // Main method to test the implementation
//    public static void main(String[] args) {
//        runMultipleTrials();
//    }
}
