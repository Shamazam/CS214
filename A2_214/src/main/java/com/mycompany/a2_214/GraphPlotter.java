package com.mycompany.a2_214;

import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;
import java.awt.*;
import java.util.List;

public class GraphPlotter {
    // Declare global variables for the dataset, series, and the Timer
    private static XYSeries backtrackingSeries;
    private static XYSeries geneticSeries;
    private static Timer timer; // Declare the Timer as a class-level variable

    public static void createGraph(List<Integer> backtrackingFunctionCalls, List<Integer> backtrackingNonAttackingQueens,
                                   List<Integer> geneticFunctionCalls, List<Integer> geneticNonAttackingQueens) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        // Initialize the series for Backtracking and Genetic Algorithm
        backtrackingSeries = new XYSeries("Backtracking");
        geneticSeries = new XYSeries("Genetic Algorithm");

        dataset.addSeries(backtrackingSeries); // Add series to dataset
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
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // Optional: Set the font sizes and styles for readability
        chart.getTitle().setFont(new Font("Serif", Font.BOLD, 18));
        plot.getDomainAxis().setLabelFont(new Font("Serif", Font.BOLD, 14));
        plot.getRangeAxis().setLabelFont(new Font("Serif", Font.BOLD, 14));

        // Set a renderer to show points and lines clearly
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, true);
        plot.setRenderer(renderer);

        // Create the panel and frame
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("N Queens Algorithm Race");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start a Timer to plot points one by one
        timer = new Timer(100, e -> updateGraph(backtrackingFunctionCalls, backtrackingNonAttackingQueens,
                geneticFunctionCalls, geneticNonAttackingQueens));  // Use class-level Timer
        timer.setRepeats(true); // Ensure the timer repeats
        timer.start();
    }

    private static int currentIndex = 0; // Track current point index

    private static void updateGraph(List<Integer> backtrackingFunctionCalls, List<Integer> backtrackingNonAttackingQueens,
                                    List<Integer> geneticFunctionCalls, List<Integer> geneticNonAttackingQueens) {
        boolean backtrackingFinished = currentIndex >= backtrackingFunctionCalls.size();
        boolean geneticFinished = currentIndex >= geneticFunctionCalls.size();

        // Add the next point for both algorithms if they haven't finished
        if (!backtrackingFinished) {
            backtrackingSeries.add(backtrackingFunctionCalls.get(currentIndex), backtrackingNonAttackingQueens.get(currentIndex));
        }

        if (!geneticFinished) {
            geneticSeries.add(geneticFunctionCalls.get(currentIndex), geneticNonAttackingQueens.get(currentIndex));
        }

        // Increment the index only if one of them still has points
        if (!backtrackingFinished || !geneticFinished) {
            currentIndex++; // Increment the index to move to the next point
        } else {
            // Stop the timer when both series have finished plotting
            timer.stop(); // Stop the Timer directly
        }
    }
}


