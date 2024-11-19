package BackTrackingAlgorithm;

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
            queenImage = ImageIO.read(new File("src/pictures/q4.png")); // Use relative path
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load queen image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        setTitle("N-Queens Solution");
        int size = 600; // Set the window size to be consistent
        setSize(size, size);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new BoardPanel());
        this.setResizable(false);
        pack(); // Ensures the frame is resized to fit the content
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
                        // Ensure the image is drawn at the correct position and resized
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
        int N = 8; // Number of queens
        int[] positions = {0, 4, 7, 5, 2, 6, 1, 3}; // Example solution
        SwingUtilities.invokeLater(() -> {
            ChessboardGUI gui = new ChessboardGUI(N, positions);
            gui.setVisible(true);
        });
    }
}
