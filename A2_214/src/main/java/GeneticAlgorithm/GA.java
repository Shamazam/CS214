package GeneticAlgorithm;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class GA {
    public void runGA() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the number of queens (N): ");
        int N = scanner.nextInt(); // Read N from user input
        
        GeneticAlgorithm ga = new GeneticAlgorithm(N);

          
         ga.printResults(ga.getFoundSolution());
          
        if(ga.getFoundSolution()){
        ga.printResults(true);
            // Get the best solution from the genetic algorithm
            int[] bestSolution = ga.population.get(0).getGenes();

            // Create the GUI to display the solution
            SwingUtilities.invokeLater(() -> {
                ChessboardGUI gui = new ChessboardGUI(N, bestSolution);
                gui.setVisible(true);
            });
        }      
    }
    
}
