package HomePage;

import MemoryGame.MemoryGame; // Import MemoryGame class
import Gamekedua.SimonSaysGame; // Import SimonSaysGame class
import Game3.Game3; // Import Game3 class

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomePage extends JFrame {
    private JLabel profilePictureLabel;
    private String profilePicturePath = "/Users/raflikardiansyah/Documents/Semester 5/PBO/project/Gambar/profile.jpeg"; // Define the path to the profile picture
    private String userName = "User Name"; // Define the user's name

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
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10)); // Increased spacing
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Title
        JLabel title = new JLabel("Choose Game", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        mainPanel.add(title);

        // Buttons for games
        JButton memoryGameButton = createGameButton("Memory Game", e -> launchMemoryGame());
        JButton game2Button = createGameButton("Simon Says", e -> launchGame2());
        JButton game3Button = createGameButton("Game 2048", e -> launchGame3());

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
        button.setBackground(Color.BLUE); // Change to blue for better visibility
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

    private void launchMemoryGame() {
        SwingUtilities.invokeLater(() -> {
            MemoryGame memoryGame = new MemoryGame();
            memoryGame.setVisible(true);
        });
    }

    private void launchGame2() {
        SwingUtilities.invokeLater(() -> {
            // Assume user ID is 1; change as needed
            SimonSaysGame game2 = new SimonSaysGame(1); 
            game2.setVisible(true);
        });
    }

    private void launchGame3() {
        SwingUtilities.invokeLater(() -> {
            Game3 game3 = new Game3();
            game3.setVisible(true);
        });
    }

    private void showProfile() {
        // Create a new dialog to show profile and leaderboard options
        JDialog dialog = new JDialog(this, "User Profile", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);
    
        // Main content panel with vertical layout for profile picture and username
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);
    
        // Show profile picture
        ImageIcon profilePicture = new ImageIcon(profilePicturePath);
        JLabel profilePicLabel = new JLabel(new ImageIcon(profilePicture.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        contentPanel.add(profilePicLabel);
    
        // User name label
        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        contentPanel.add(userNameLabel);
    
        // Add content panel to dialog
        dialog.add(contentPanel, BorderLayout.CENTER);
    
        // Panel for leaderboard label and buttons
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBackground(Color.BLACK);
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
    
        // Leaderboard label
        JLabel leaderboardLabel = new JLabel("Leaderboard");
        leaderboardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        leaderboardLabel.setForeground(Color.WHITE);
        leaderboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        leaderboardPanel.add(leaderboardLabel);
    
        // Game options panel
        JPanel gameOptionsPanel = new JPanel(new GridLayout(3, 1, 5, 5)); // Vertical layout with spacing
        JButton memoryLeaderboardButton = createGameButton("Memory Game", e -> showLeaderboard("Memory Game"));
        JButton simonSaysLeaderboardButton = createGameButton("Simon Says", e -> showLeaderboard("Simon Says"));
        JButton game3LeaderboardButton = createGameButton("Game 3", e -> showLeaderboard("Game 3"));
    
        // Set button colors to black
        memoryLeaderboardButton.setForeground(Color.BLACK);
        simonSaysLeaderboardButton.setForeground(Color.BLACK);
        game3LeaderboardButton.setForeground(Color.BLACK);
    
        gameOptionsPanel.add(memoryLeaderboardButton);
        gameOptionsPanel.add(simonSaysLeaderboardButton);
        gameOptionsPanel.add(game3LeaderboardButton);
    
        // Add game options panel to leaderboard panel
        leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between label and buttons
        leaderboardPanel.add(gameOptionsPanel);
    
        // Add leaderboard panel to dialog
        dialog.add(leaderboardPanel, BorderLayout.SOUTH);
    
        dialog.setVisible(true);
    }
    

    private void showLeaderboard(String gameName) {
        // Here you would add your logic to retrieve and display leaderboard data from the database
        if (gameName.equals("Memory Game")) {
            fetchMemoryGameLeaderboard();
        } else if (gameName.equals("Simon Says")) {
            fetchSimonSaysLeaderboard();
        } else if (gameName.equals("Game 3")) {
            fetchGame3Leaderboard();
        }
    }

   // Method to fetch Memory Game leaderboard
   private void fetchMemoryGameLeaderboard() {
    String dbUrl = "jdbc:mysql://localhost:3306/memory_game"; // Update with your database URL
    String dbUser = "root"; // Update with your database username
    String dbPassword = ""; // Update with your database password

    String query = "SELECT level, time_taken FROM game_times ORDER BY timestamp ASC"; // Adjust as needed

    try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        // Create a JPanel to hold the leaderboard table
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        
        // Create a StringBuilder to build the HTML string for the table
        StringBuilder leaderboard = new StringBuilder("<html><h2>Memory Game Leaderboard</h2>");
        leaderboard.append("<table border='1'><tr><th>Level</th><th>Time Taken (seconds)</th></tr>");

        while (rs.next()) {
            int level = rs.getInt("level");
            int timeTaken = rs.getInt("time_taken");
            leaderboard.append("<tr><td>").append(level).append("</td><td>").append(timeTaken).append("</td></tr>");
        }
        leaderboard.append("</table></html>");

        // Create a JLabel to display the leaderboard
        JLabel leaderboardLabel = new JLabel(leaderboard.toString());
        leaderboardLabel.setVerticalAlignment(SwingConstants.TOP); // Align to the top
        
        // Add the label to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(leaderboardLabel);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Set preferred size for scroll pane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show scrollbar

        // Show the scrollable leaderboard in a dialog
        JOptionPane.showMessageDialog(this, scrollPane, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching leaderboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void fetchSimonSaysLeaderboard() {
    String dbUrl = "jdbc:mysql://localhost:3306/memory_game"; // Update with your database URL
    String dbUser = "root"; // Update with your database username
    String dbPassword = ""; // Update with your database password

    String query = "SELECT user_id, max_level FROM user_progress"; // Query to fetch user progress

    try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        // Create a JPanel to hold the leaderboard table
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));

        // Create a StringBuilder to build the HTML string for the table
        StringBuilder leaderboard = new StringBuilder("<html><h2>Simon Says Leaderboard</h2>");
        leaderboard.append("<table border='1'><tr><th>Play</th><th>Level</th></tr>");

        while (rs.next()) {
            int userId = rs.getInt("user_id");
            int maxLevel = rs.getInt("max_level");
            leaderboard.append("<tr><td>").append(userId).append("</td><td>").append(maxLevel).append("</td></tr>");
        }
        leaderboard.append("</table></html>");

        // Create a JLabel to display the leaderboard
        JLabel leaderboardLabel = new JLabel(leaderboard.toString());
        leaderboardLabel.setVerticalAlignment(SwingConstants.TOP); // Align to the top
        
        // Add the label to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(leaderboardLabel);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Set preferred size for scroll pane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show scrollbar

        // Show the scrollable leaderboard in a dialog
        JOptionPane.showMessageDialog(this, scrollPane, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching leaderboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void fetchGame3Leaderboard() {
        // Logic to fetch Game 3 leaderboard from database
        // Implement your database query here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage());
    }
}