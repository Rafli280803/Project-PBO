import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Game2048 extends JPanel {
    private static final int TILE_SIZE = 100;
    private static final int TILE_MARGIN = 16;
    private static final int PADDING = 20; // Tambahkan padding di sekitar panel
    private static final Color BG_COLOR = new Color(0xbbada0);
    private static final String BACKGROUND_IMAGE_PATH = "background.jpg"; // Set path for your background image

    private Board board;
    private boolean moved, gameOver, gameWon;
    private Image backgroundImage;

    public Game2048() {
        setFocusable(true);
        setPreferredSize(new Dimension(
                4 * (TILE_SIZE + TILE_MARGIN) + PADDING * 2,
                4 * (TILE_SIZE + TILE_MARGIN) + PADDING * 2
        ));
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING)); // Tambahkan padding di sekitar panel
        board = new Board();

        // Load background image
        try {
            backgroundImage = new ImageIcon(BACKGROUND_IMAGE_PATH).getImage();
        } catch (Exception e) {
            backgroundImage = null; // Use a solid color background if image fails to load
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver || gameWon) return; // Disable input after the game is over or won

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> moved = board.moveLeft();
                    case KeyEvent.VK_RIGHT -> moved = board.moveRight();
                    case KeyEvent.VK_UP -> moved = board.moveUp();
                    case KeyEvent.VK_DOWN -> moved = board.moveDown();
                }

                if (moved) {
                    gameOver = board.isGameOver();
                    gameWon = board.hasWon();
                    repaint();

                    if (gameOver || gameWon) {
                        showEndGamePopup();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(BG_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw tiles
        Tile[][] tiles = board.getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                drawTile(g, tiles[i][j], i, j);
            }
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Score: " + board.getScore(), PADDING, getHeight() - PADDING);
    }

    private void drawTile(Graphics g, Tile tile, int x, int y) {
        int xOffset = PADDING + x * (TILE_SIZE + TILE_MARGIN) + TILE_MARGIN;
        int yOffset = PADDING + y * (TILE_SIZE + TILE_MARGIN) + TILE_MARGIN;
        g.setColor(tile.isEmpty() ? BG_COLOR : getTileColor(tile.getValue()));
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 10, 10);

        if (!tile.isEmpty()) {
            g.setColor(tile.getValue() == 0 ? new Color(0, 0, 0, 0) : Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String value = tile.getValue() == 0 ? "" : String.valueOf(tile.getValue());
            FontMetrics fm = g.getFontMetrics();
            int textX = xOffset + (TILE_SIZE - fm.stringWidth(value)) / 2;
            int textY = yOffset + (TILE_SIZE + fm.getHeight()) / 2 - 10;
            g.drawString(value, textX, textY);
        }
    }

    private Color getTileColor(int value) {
        return switch (value) {
            case 2 -> new Color(0xeee4da);
            case 4 -> new Color(0xede0c8);
            case 8 -> new Color(0xf2b179);
            case 16 -> new Color(0xf59563);
            case 32 -> new Color(0xf67c5f);
            case 64 -> new Color(0xf65e3b);
            case 128 -> new Color(0xedcf72);
            case 256 -> new Color(0xedcc61);
            case 512 -> new Color(0xedc850);
            case 1024 -> new Color(0xedc53f);
            case 2048 -> new Color(0xedc22e);
            default -> new Color(0x3c3a32);
        };
    }

    private void showEndGamePopup() {
        String message = gameWon ? "Congratulations! You Win!" : "Game Over!";
        int choice = JOptionPane.showOptionDialog(
                this,
                message + "\nWould you like to Retry or Quit?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Retry", "Quit"},
                "Retry"
        );

        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void resetGame() {
        board = new Board(); // Reset the board
        gameOver = false;
        gameWon = false;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("2048 Game");
        Game2048 gamePanel = new Game2048();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
