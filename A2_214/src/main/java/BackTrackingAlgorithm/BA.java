/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package BackTrackingAlgorithm;

import java.util.List;
import java.util.Scanner;
import javax.swing.SwingUtilities;

/**
 *
 * @author c3rea
 */
public class BA {

    public void runBA() {
        int n;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of queens you'd like to place");
        n = scanner.nextInt();     
        BacktrackingAlgorithm queen = new BacktrackingAlgorithm(n);
        List<int[]> solutions = queen.solveNQ();
        System.out.println("Number of solutions: " + solutions.size());
        System.out.println("Number of function calls: " + queen.getFunctionCallCount());
        // For demonstration, show the first solution in the GUI
        if (!solutions.isEmpty()) {
            ChessboardGUI gui = new ChessboardGUI(n, solutions.get(0));
            gui.setVisible(true);
        } else {
            System.out.println("No solutions exist.");
        }
    }
}
