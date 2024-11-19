package GeneticAlgorithm;

import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {
    private int N; // Number of Queens
    private int[] genes; // Each position shows the vertical position of a queen in the corresponding column
    private int fitness; // Fitness score of the chromosome

    // Reference to GeneticAlgorithm to track calls
    private static GeneticAlgorithm geneticAlgorithm;

    // Constructs a randomly created chromosome
    public Chromosome(int N, GeneticAlgorithm ga) {
        this.N = N;
        this.genes = new int[N];
        geneticAlgorithm = ga;
        Random r = new Random();

        for (int i = 0; i < this.genes.length; i++) {
            this.genes[i] = r.nextInt(N);
        }
        this.calculateFitness();
    }

    // Constructor for copying a chromosome
    public Chromosome(int[] genes) {
        this.genes = new int[genes.length];
        System.arraycopy(genes, 0, this.genes, 0, genes.length);
        this.N = genes.length;
        this.calculateFitness();
    }

    // Calculates the fitness score of the chromosome
    void calculateFitness() {
        int nonThreats = 0;
        for (int i = 0; i < this.genes.length; i++) {
            for (int j = i + 1; j < this.genes.length; j++) {
                if ((this.genes[i] != this.genes[j]) && (Math.abs(i - j) != Math.abs(this.genes[i] - this.genes[j]))) {
                    nonThreats++;
                }
            }
        }
        this.fitness = nonThreats;

        // Increment objective function call
        if (geneticAlgorithm != null) {
            geneticAlgorithm.incrementObjectiveFunctionCalls();
        }
    }

    // Mutate by randomly changing the position of a queen
    void mutate() {
        Random r = new Random();
        int index = r.nextInt(N);
        int newPosition;
        do {
            newPosition = r.nextInt(N);
        } while (newPosition == this.genes[index]);
        this.genes[index] = newPosition;
        this.calculateFitness();
    }

    public int[] getGenes() {
        return this.genes;
    }

    public int getFitness() {
        return this.fitness;
    }

    public int getN() {
        return this.N;
    }

    @Override
    public int compareTo(Chromosome x) {
        return this.fitness - x.fitness;
    }
}
