import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Game2048 extends JPanel {
    private static final int TILE_SIZE = 100;
    private static final int TILE_MARGIN = 16;
    private static final int PADDING = 20;
    private static final Color BG_COLOR = new Color(0xbbada0);
    private static final String BACKGROUND_IMAGE_PATH = "Image/BG2048.png";

    private Board board;
    private boolean moved, gameOver, gameWon;
    private Image backgroundImage;
    private JLabel gifLabel; 
    private String gifPath;
    private String bgmPath;
    private JFrame frame; 

    private PlayBGM playBGM; // Tambahkan ini

public Game2048(JFrame frame) {
    this.frame = frame;
    setFocusable(true);
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(
            4 * (TILE_SIZE + TILE_MARGIN) + PADDING * 2,
            4 * (TILE_SIZE + TILE_MARGIN) + PADDING * 2 + 150
    ));
    setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
    board = new Board();

    gifPath = DatabaseHelper.getGifPath();
    bgmPath = DatabaseHelper.getBgmPath();

    if (bgmPath != null) {
        playBGM = new PlayBGM(bgmPath);
        playBGM.start();
    }

    backgroundImage = new ImageIcon(BACKGROUND_IMAGE_PATH).getImage();

    gifLabel = new JLabel();
    gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
    if (gifPath != null) {
        gifLabel.setIcon(new ImageIcon(gifPath));
    }
    gifLabel.setPreferredSize(new Dimension(getWidth(), 150));
    gifLabel.setBorder(BorderFactory.createLineBorder(new Color(0x444444), 5, true));
    add(gifLabel, BorderLayout.NORTH);

    addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (gameOver || gameWon) return;

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
                    saveScoreToDatabase(board.getScore());
                }
            }
        }
    });
}

private void stopBackSound() {
    if (playBGM != null) {
        playBGM.stopBGM();
        playBGM = null;
    }
}


private void showEndGamePopup() {
    String message = "Game Over! Would you like to Retry or Back to Home?";
    int choice = JOptionPane.showOptionDialog(
        this,
        message,
        "Game Over",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,
        new String[]{"Retry", "Home"},
        "Retry"
    );

    stopBackSound(); // Tambahkan ini untuk menghentikan back sound

    if (choice == JOptionPane.YES_OPTION) {
        resetGame();
    } else if (choice == JOptionPane.NO_OPTION) {
        frame.dispose();
    }
}


    private void resetGame() {
        board = new Board();
        gameOver = false;
        gameWon = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 150, getWidth(), getHeight() - 150, this);
        } else {
            g.setColor(BG_COLOR);
            g.fillRect(0, 150, getWidth(), getHeight() - 150);
        }

        Tile[][] tiles = board.getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                drawTile(g, tiles[i][j], i, j);
            }
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Score: " + board.getScore(), PADDING, getHeight() - PADDING);
    }

    private void drawTile(Graphics g, Tile tile, int x, int y) {
        int xOffset = PADDING + x * (TILE_SIZE + TILE_MARGIN) + TILE_MARGIN;
        int yOffset = PADDING + y * (TILE_SIZE + TILE_MARGIN) + TILE_MARGIN + 150;
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

    private void saveScoreToDatabase(int score) {
        String dbUrl = "jdbc:mysql://localhost:3306/gamecenter"; // URL of MySQL database
        String dbUser = "root"; // MySQL username
        String dbPassword = ""; // MySQL password
    
        // Use try-with-resources to automatically close resources
        String query = "INSERT INTO leaderboard (score) VALUES (?)";
    
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            // Set the score to be saved
            pstmt.setInt(1, score);
            
            // Execute the insert
            int rowsAffected = pstmt.executeUpdate();
    
            // Optionally, print the result for confirmation
            if (rowsAffected > 0) {
                System.out.println("Score saved successfully!");
            } else {
                System.out.println("Failed to save score.");
            }
        } catch (SQLException e) {
            // Handle the exception with proper error message
            System.err.println("Error while saving score to database: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2048 Game");
            Game2048 gamePanel = new Game2048(frame); // Pass the frame to the constructor
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}