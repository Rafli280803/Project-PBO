import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

// Define the Game interface
interface Game {
    void launch();
}

// Create an abstract class for game launching
abstract class AbstractGameLauncher implements Game {
    protected String gameName;

    public AbstractGameLauncher(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public abstract void launch();
}

// Concrete classes for specific games
class MemoryGameLauncher extends AbstractGameLauncher {
    public MemoryGameLauncher() {
        super("Memory Game");
    }

    @Override
    public void launch() {
        SwingUtilities.invokeLater(() -> {
            MemoryGame memoryGame = new MemoryGame();
            memoryGame.setVisible(true);
        });
    }
}

class SimonSaysGameLauncher extends AbstractGameLauncher {
    public SimonSaysGameLauncher() {
        super("Simon Says");
    }

    @Override
    public void launch() {
        SwingUtilities.invokeLater(() -> {
            SimonSaysGame game2 = new SimonSaysGame(1);
            game2.setVisible(true);
        });
    }
}

class Game2048Launcher extends AbstractGameLauncher {
    public Game2048Launcher() {
        super("Game 2048");
    }

    @Override
    public void launch() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2048 Game");
            Game2048 gamePanel = new Game2048(frame);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

// Main class for the home page
public class HomePage extends JFrame {
    private JLabel profilePictureLabel;
    private String profilePicturePath = "Image/profile.jpeg";
    private String userName = "Kelompok 21";

    public HomePage() {
        setTitle("Game Center");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the window on the screen

        // Set background color of the frame
        getContentPane().setBackground(Color.BLACK);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Choose Game", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        mainPanel.add(title);

        // Game Buttons
        JButton memoryGameButton = createGameButton("Memory Game", e -> new MemoryGameLauncher().launch());
        JButton game2Button = createGameButton("Simon Says", e -> new SimonSaysGameLauncher().launch());
        JButton game3Button = createGameButton("Game 2048", e -> new Game2048Launcher().launch());

        mainPanel.add(memoryGameButton);
        mainPanel.add(game2Button);
        mainPanel.add(game3Button);

        // Adding main panel to frame
        add(mainPanel, BorderLayout.CENTER);

        // Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());
        profilePanel.setBackground(Color.BLACK);

        // Profile Picture
        ImageIcon profilePicture = new ImageIcon(profilePicturePath);
        Image scaledImage = profilePicture.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        profilePictureLabel = new JLabel(new ImageIcon(scaledImage));

        profilePictureLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showProfile();
            }
        });

        profilePanel.add(profilePictureLabel, BorderLayout.EAST);
        add(profilePanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private JButton createGameButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.CYAN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLUE);
            }
        });

        return button;
    }

    private void showProfile() {
        JDialog dialog = new JDialog(this, "User Profile", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);

        // Profile Picture
        ImageIcon profilePicture = new ImageIcon(profilePicturePath);
        JLabel profilePicLabel = new JLabel(new ImageIcon(profilePicture.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(profilePicLabel);

        // User Name Label
        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(userNameLabel);

        dialog.add(contentPanel, BorderLayout.CENTER);

        // Leaderboard Panel
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBackground(Color.BLACK);
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel leaderboardLabel = new JLabel("Leaderboard");
        leaderboardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        leaderboardLabel.setForeground(Color.WHITE);
        leaderboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardPanel.add(leaderboardLabel);

        // Game Options Panel
        JPanel gameOptionsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton memoryLeaderboardButton = createGameButton("Memory Game", e -> showLeaderboard("Memory Game"));
        JButton simonSaysLeaderboardButton = createGameButton("Simon Says", e -> showLeaderboard("Simon Says"));
        JButton game2048LeaderboardButton = createGameButton("Game 2048", e -> showLeaderboard("Game 2048"));

        // Set button colors to black
        memoryLeaderboardButton.setForeground(Color.BLACK);
        simonSaysLeaderboardButton.setForeground(Color.BLACK);
        game2048LeaderboardButton.setForeground(Color.BLACK);

        gameOptionsPanel.add(memoryLeaderboardButton);
        gameOptionsPanel.add(simonSaysLeaderboardButton);
        gameOptionsPanel.add(game2048LeaderboardButton);

        leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leaderboardPanel.add(gameOptionsPanel);

        dialog.add(leaderboardPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showLeaderboard(String gameName) {
        String dbUrl = "jdbc:mysql://localhost:3306/gamecenter"; // Update with your database URL
        String dbUser = "root"; // Update with your database username
        String dbPassword = ""; // Update with your database password

        String query = "";
        StringBuilder leaderboard = new StringBuilder("<html>");

        switch (gameName) {
            case "Memory Game":
                query = "SELECT level, time_taken FROM game_times ORDER BY timestamp ASC";
                leaderboard.append("<h2>Memory Game Leaderboard</h2>");
                leaderboard.append("<table border='1'><tr><th>Level</th><th>Time Taken (seconds)</th></tr>");
                break;
            case "Simon Says":
                query = "SELECT user_id, level FROM user_levels ORDER BY level DESC"; // Update to read from user_levels
                leaderboard.append("<h2>Simon Says Leaderboard</h2>");
                leaderboard.append("<table border='1'><tr><th>User ID</th><th>Level Achieved</th></tr>");
                break;
            case "Game 2048":
                query = "SELECT id, score FROM leaderboard ORDER BY score DESC LIMIT 10"; 
                leaderboard.append("<h2>2048 Game Leaderboard</h2>");
                leaderboard.append("<table border='1'><tr><th>ID</th><th>Score</th></tr>");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Game not recognized.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                switch (gameName) {
                    case "Memory Game":
                        int level = rs.getInt("level");
                        int timeTaken = rs.getInt("time_taken");
                        leaderboard.append("<tr><td>").append(level).append("</td><td>").append(timeTaken).append("</td></tr>");
                        break;
                    case "Simon Says":
                        int userId = rs.getInt("user_id");
                        int maxLevel = rs.getInt("level");
                        leaderboard.append("<tr><td>").append(userId).append("</td><td>").append(maxLevel).append("</td></tr>");
                        break;
                    case "Game 2048":
                        int gameId = rs.getInt("id");
                        int score = rs.getInt("score");
                        leaderboard.append("<tr><td>").append(gameId).append("</td><td>").append(score).append("</td></tr>");
                        break;
                }
            }
            leaderboard.append("</table></html>");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching leaderboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JLabel leaderboardLabel = new JLabel(leaderboard.toString());
        leaderboardLabel.setVerticalAlignment(SwingConstants.TOP); 
        JScrollPane scrollPane = new JScrollPane(leaderboardLabel);
        scrollPane.setPreferredSize(new Dimension(400, 300)); 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 

        JOptionPane.showMessageDialog(this, scrollPane, gameName + " Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePage::new);
    }
}