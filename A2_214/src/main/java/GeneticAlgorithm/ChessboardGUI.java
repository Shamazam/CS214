package GeneticAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ChessboardGUI extends JFrame {
    private int N; // Number of Queens
    private int[] positions; // Positions of the queens
    private BufferedImage queenImage; // Image of the queen piece

    public ChessboardGUI(int N, int[] positions) {
        this.N = N;
        this.positions = positions;

        // Load the queen image from the src/pictures directory
        try {
            queenImage = ImageIO.read(new File("src/pictures/qq.png")); // Use relative path
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1); // Exit if image loading fails
        }

        setTitle("N-Queens Solution");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new BoardPanel());
        this.setResizable(false);
        pack(); // Adjust the window size to fit the content
    }

    class BoardPanel extends JPanel {
        @Override
        public Dimension getPreferredSize() {
            // Set the preferred size based on N to ensure the grid fits properly
            int cellSize = Math.max(50, 600 / N);
            return new Dimension(N * cellSize, N * cellSize);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBoard(g);
        }

        private void drawBoard(Graphics g) {
            int cellSize = getWidth() / N; // Calculate the size of each cell
            int queenSize = (int) (cellSize * 0.8); // Set the queen's size to 80% of cell size

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    // Alternate colors for the chessboard
                    g.setColor((i + j) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);

                    // Draw the queen using the resized image
                    if (positions[j] == i) {
                        g.drawImage(queenImage,
                                j * cellSize + (cellSize - queenSize) / 2,
                                i * cellSize + (cellSize - queenSize) / 2,
                                queenSize,
                                queenSize,
                                this);
                    }
                }
            }
            drawGrid(g, cellSize);
        }

        private void drawGrid(Graphics g, int cellSize) {
            g.setColor(Color.BLACK);
            for (int i = 0; i <= N; i++) {
                g.drawLine(i * cellSize, 0, i * cellSize, getHeight());
                g.drawLine(0, i * cellSize, getWidth(), i * cellSize);
            }
        }
    }

    public static void main(String[] args) {
        // Example usage: initialize with N and a solution
        int N = 4; // Number of queens
        int[] positions = {1, 3, 0, 2}; // Example solution
        SwingUtilities.invokeLater(() -> {
            ChessboardGUI gui = new ChessboardGUI(N, positions);
            gui.setVisible(true);
        });
    }
}
