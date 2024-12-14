import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;

public class HomePage extends JFrame {
    private JLabel profilePictureLabel;
    private String profilePicturePath = "Image\\BG2048.png"; 
    private String userName = "User Name"; 

    public HomePage() {
        setTitle("Game Center");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(34, 34, 34));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10)); 
        mainPanel.setBackground(new Color(34, 34, 34)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        JLabel title = new JLabel("Choose Game", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE); 
        mainPanel.add(title);

        JButton memoryGameButton = createGameButton("Memory Game", e -> launchMemoryGame());
        JButton game2Button = createGameButton("Simon Says", e -> launchGame2());
        JButton game3Button = createGameButton("Game 2048", e -> launchGame3());

        mainPanel.add(memoryGameButton);
        mainPanel.add(game2Button);
        mainPanel.add(game3Button);

        add(mainPanel, BorderLayout.CENTER);

        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());
        profilePanel.setBackground(new Color(34, 34, 34));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 

        ImageIcon profilePicture = new ImageIcon(profilePicturePath);
        Image scaledImage = profilePicture.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        profilePictureLabel = new JLabel(new ImageIcon(scaledImage));
        
        profilePictureLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showProfile();
            }
        });

        profilePictureLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 204), 3));
        profilePictureLabel.setOpaque(true);
        profilePictureLabel.setBackground(new Color(0, 122, 204)); 

        profilePanel.add(profilePictureLabel, BorderLayout.EAST);
        add(profilePanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private JButton createGameButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 122, 204));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2)); 
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 204, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 122, 204)); 
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
            SimonSaysGame game2 = new SimonSaysGame(1); 
            game2.setVisible(true);
        });
    }

    private void launchGame3() {
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

    private void showProfile() {
        JDialog dialog = new JDialog(this, "User Profile", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);
    
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
    
        ImageIcon profilePicture = new ImageIcon(profilePicturePath);
        JLabel profilePicLabel = new JLabel(new ImageIcon(profilePicture.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        contentPanel.add(profilePicLabel);
    
        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userNameLabel.setForeground(Color.BLACK); 
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        contentPanel.add(userNameLabel);
    
        dialog.add(contentPanel, BorderLayout.CENTER);
    
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBackground(Color.WHITE);
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
    
        JLabel leaderboardLabel = new JLabel("Leaderboard");
        leaderboardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        leaderboardLabel.setForeground(Color.BLACK); 
        leaderboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        leaderboardPanel.add(leaderboardLabel);
    
        JPanel gameOptionsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton memoryLeaderboardButton = createGameButton("Memory Game", e -> showLeaderboard("Memory Game"));
        JButton simonSaysLeaderboardButton = createGameButton("Simon Says", e -> showLeaderboard("Simon Says"));
        JButton game2048LeaderboardButton = createGameButton("Game 2048", e -> showLeaderboard("Game 2048"));
    
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
        String dbUrl;
        String dbUser = "root"; 
        String dbPassword = ""; 
        String query;
        StringBuilder leaderboard = new StringBuilder("<html>");
        
        switch (gameName) {
            case "Memory Game":
                dbUrl = "jdbc:mysql://localhost:3306/gamecenter";
                query = "SELECT level, time_taken FROM game_times ORDER BY time_taken ASC";
                
                try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                     PreparedStatement stmt = conn.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {
    
                    leaderboard.append("<h2>Memory Game Leaderboard</h2>");
                    leaderboard.append("<table border='1'><tr><th>Level</th><th>Time Taken (seconds)</th></tr>");
    
                    while (rs.next()) {
                        int level = rs.getInt("level");
                        int timeTaken = rs.getInt("time_taken");
                        leaderboard.append("<tr><td>").append(level).append("</td><td>").append(timeTaken).append("</td></tr>");
                    }
                    leaderboard.append("</table>");
                } catch (SQLException e) {
                    showErrorDialog(e);
                    return;
                }
                break;
    
                case "Simon Says":
                dbUrl = "jdbc:mysql://localhost:3306/gamecenter";
                query = "SELECT user_id, max_level FROM user_progress ORDER BY max_level DESC";
            
                try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                     PreparedStatement stmt = conn.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {
            
                    leaderboard.append("<h2>Simon Says Leaderboard</h2>");
                    leaderboard.append("<table border='1'><tr><th>User ID</th><th>Max Level</th></tr>");
            
                    while (rs.next()) {
                        int userId = rs.getInt("user_id");
                        int maxLevel = rs.getInt("max_level");
                        leaderboard.append("<tr><td>").append(userId).append("</td><td>").append(maxLevel).append("</td></tr>");
                    }
                    leaderboard.append("</table>");
                } catch (SQLException e) {
                    showErrorDialog(e);
                    return;
                }
                break;
    
            case "Game 2048":
                dbUrl = "jdbc:mysql://localhost:3306/gamecenter";
                query = "SELECT id, score FROM leaderboard ORDER BY score DESC LIMIT 10"; 
    
                try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                     PreparedStatement stmt = conn.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {
    
                    leaderboard.append("<h2>2048 Game Leaderboard</h2>");
                    leaderboard.append("<table border='1'><tr><th>ID attemps</th><th>Score</th></tr>");
    
                    while (rs.next()) {
                        int userId = rs.getInt("id");
                        int score = rs.getInt("score");
                        leaderboard.append("<tr><td>").append(userId).append("</td><td>").append(score).append("</td></tr>");
                    }
                    leaderboard.append("</table>");
                } catch (SQLException e) {
                    showErrorDialog(e);
                    return;
                }
                break;
    
            default:
                JOptionPane.showMessageDialog(this, "Game not recognized.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }
    
        showLeaderboardDialog(leaderboard.toString());
    }
    
    private void showLeaderboardDialog(String leaderboardHtml) {
        JLabel leaderboardLabel = new JLabel(leaderboardHtml);
        leaderboardLabel.setVerticalAlignment(SwingConstants.TOP);
    
        JScrollPane scrollPane = new JScrollPane(leaderboardLabel);
        scrollPane.setPreferredSize(new Dimension(400, 300)); 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
        JOptionPane.showMessageDialog(this, scrollPane, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showErrorDialog(SQLException e) {
        JOptionPane.showMessageDialog(this, "Error accessing the database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        new HomePage();
    }
}