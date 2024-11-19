package com.mycompany.a2_214;

import BackTrackingAlgorithm.BacktrackingAlgorithm;
import GeneticAlgorithm.GeneticAlgorithm;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class RaceNQueens {
    private static List<Integer> backtrackingFunctionCalls = new ArrayList<>();
    private static List<Integer> backtrackingNonAttackingQueens = new ArrayList<>();
    private static List<Integer> geneticFunctionCalls = new ArrayList<>();
    private static List<Integer> geneticNonAttackingQueens = new ArrayList<>();

    public static void updateBacktrackingData(int functionCalls, int nonAttacking) {
        backtrackingFunctionCalls.add(functionCalls);
        backtrackingNonAttackingQueens.add(nonAttacking);
    }

    public static void updateGeneticData(int functionCalls, int nonAttacking) {
        geneticFunctionCalls.add(functionCalls);
        geneticNonAttackingQueens.add(nonAttacking);
    }

    public static void race(int n) {
        // Flags to track when both algorithms finish solving
        final boolean[] backtrackingFinished = {false};
        final boolean[] geneticFinished = {false};

        // Backtracking Task
        BacktrackingTask backtrackingTask = new BacktrackingTask(n);
        Thread backtrackingThread = new Thread(() -> {
            backtrackingTask.run();
            synchronized (backtrackingFinished) {
                backtrackingFinished[0] = true;
                backtrackingFinished.notify(); // Notify waiting threads if any
            }
        });

        // Genetic Algorithm Task
        GeneticTask geneticTask = new GeneticTask(n);
        Thread geneticThread = new Thread(() -> {
            geneticTask.run();
            synchronized (geneticFinished) {
                geneticFinished[0] = true;
                geneticFinished.notify(); // Notify waiting threads if any
            }
        });

        // Start both threads
        backtrackingThread.start();
        geneticThread.start();

        // Wait until both algorithms have completed their execution
        synchronized (backtrackingFinished) {
            while (!backtrackingFinished[0]) {
                try {
                    backtrackingFinished.wait(); // Wait until backtracking is finished
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        synchronized (geneticFinished) {
            while (!geneticFinished[0]) {
                try {
                    geneticFinished.wait(); // Wait until genetic algorithm is finished
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Create the graph after both algorithms have finished
        GraphPlotter g = new GraphPlotter();
        g.createGraph(backtrackingFunctionCalls, backtrackingNonAttackingQueens, geneticFunctionCalls, geneticNonAttackingQueens);

        // Determine if both algorithms found solutions
        boolean backtrackingSolved = backtrackingNonAttackingQueens.getLast() == n; // Adjust to your method of checking solution
        boolean geneticSolved = geneticNonAttackingQueens.getLast() == n; // Adjust to your method of checking solution

        // Compare the results and determine the winner
        if (backtrackingSolved && !geneticSolved) {
            System.out.println("Backtracking algorithm wins because Genetic algorithm couldn't find a solution!");
        } else if (!backtrackingSolved && geneticSolved) {
            System.out.println("Genetic algorithm wins because Backtracking algorithm couldn't find a solution!");
        } else if (backtrackingSolved && geneticSolved) {
            // Compare based on function calls if both found solutions
            if (backtrackingFunctionCalls.get(backtrackingFunctionCalls.size() - 1) <
                    geneticFunctionCalls.get(geneticFunctionCalls.size() - 1)) {
                System.out.println("Backtracking algorithm wins with fewer function calls!");
            } else {
                System.out.println("Genetic algorithm wins with fewer function calls!");
            }
        } else {
            System.out.println("Neither algorithm could find a solution.");
        }

        //createGraph();

    }





    private static void createGraph() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries backtrackingSeries = new XYSeries("Backtracking");
        for (int i = 0; i < backtrackingFunctionCalls.size(); i++) {
            backtrackingSeries.add(backtrackingFunctionCalls.get(i), backtrackingNonAttackingQueens.get(i));
        }
        dataset.addSeries(backtrackingSeries);

        XYSeries geneticSeries = new XYSeries("Genetic Algorithm");
        for (int i = 0; i < geneticFunctionCalls.size(); i++) {
            geneticSeries.add(geneticFunctionCalls.get(i), geneticNonAttackingQueens.get(i));
        }
        dataset.addSeries(geneticSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "N Queens Algorithm Race",
                "Function Calls",
                "Non-Attacking Queens",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize the chart appearance
        chart.setBackgroundPaint(Color.white);

        // Access the plot and customize it
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // Optional: Set the font sizes and styles for readability
        chart.getTitle().setFont(new Font("Serif", Font.BOLD, 18));
        plot.getDomainAxis().setLabelFont(new Font("Serif", Font.BOLD, 14));
        plot.getRangeAxis().setLabelFont(new Font("Serif", Font.BOLD, 14));

        // Create the panel and frame
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("N Queens Algorithm Race");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the size of the board (N): ");
        int n = scanner.nextInt();

        race(n);
    }

    // Backtracking Task Class
    static class BacktrackingTask implements Runnable {
        private final int n;

        public BacktrackingTask(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            BacktrackingAlgorithm nQueen = new BacktrackingAlgorithm(n);
            nQueen.solveNQ();
            updateBacktrackingData(nQueen.getFunctionCallCount(), nQueen.solutions.size() == 0 ? 0 : n); // Record N if solution found
        }
    }

    // Genetic Algorithm Task Class
    static class GeneticTask implements Runnable {
        private final int n;

        public GeneticTask(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            GeneticAlgorithm ga = new GeneticAlgorithm(n);
            geneticFunctionCalls.add(ga.getObjectiveFunctionCalls());
            geneticNonAttackingQueens.add(ga.getFoundSolution() ? n : 0); // Store N if solution found, otherwise 0
        }
    }
}
