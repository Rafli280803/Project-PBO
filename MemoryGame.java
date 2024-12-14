import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame extends JFrame {
    private int rows = 4;
    private int cols = 4;
    private JButton[][] buttons;
    private String[][] images;
    private ArrayList<String> imageList = new ArrayList<>();
    private JButton firstSelected = null;
    private JButton secondSelected = null;
    private Timer timer;
    private int currentLevel = 1;
    private int elapsedTime = 0; // Waktu yang telah berjalan dalam detik
    private JLabel timerLabel;

    public MemoryGame() {
        setTitle("Memory Card Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(220, 220, 220)); // Light gray background

        // Panel untuk grid permainan
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(rows, cols));
        gamePanel.setBackground(new Color(255, 255, 255)); // White background for the game area
        add(gamePanel, BorderLayout.CENTER);

        // Timer Label di bawah grid
        timerLabel = new JLabel("Waktu: 0 detik", JLabel.CENTER);
        timerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        timerLabel.setForeground(Color.RED); // Change to a more vibrant color
        add(timerLabel, BorderLayout.SOUTH);

        loadImagesFromDatabase();
        initializeLevel();
        setVisible(true);
    }

    private void initializeLevel() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Panel untuk grid permainan
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(rows, cols));
        buttons = new JButton[rows][cols];
        images = new String[rows][cols];
        loadImagesForLevel(); // Pastikan gambar sesuai level
        initializeGrid(gamePanel); // Initialize the grid

        // Tambahkan gamePanel ke frame
        add(gamePanel, BorderLayout.CENTER);

        // Inisialisasi timer
        elapsedTime = 0;
        startTimer();

        // Timer Label di bawah grid
        timerLabel = new JLabel("Waktu: 0 detik", JLabel.CENTER);
        timerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24)); // Ukuran font ditingkatkan
        timerLabel.setForeground(Color.RED); // Warna diubah untuk visibilitas yang lebih baik
        add(timerLabel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void loadImagesFromDatabase() {
        String dbUrl = "jdbc:mysql://localhost:3306/gamecenter"; // URL database MySQL
        String dbUser = "root"; // Username MySQL
        String dbPassword = ""; // Password MySQL

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT image_path FROM cards")) {

            while (rs.next()) {
                String imagePath = rs.getString("image_path");
                imageList.add(imagePath);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load images from database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadImagesForLevel() {
        ArrayList<String> levelImages = new ArrayList<>();
        int pairsNeeded = (rows * cols) / 2;

        if (imageList.size() < pairsNeeded) {
            JOptionPane.showMessageDialog(this, "Not enough images in the database!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ambil gambar sebanyak pasangan yang dibutuhkan untuk level ini
        for (int i = 0; i < pairsNeeded; i++) {
            levelImages.add(imageList.get(i));
        }

        // Gandakan untuk pasangan dan acak
        levelImages.addAll(new ArrayList<>(levelImages));
        Collections.shuffle(levelImages);

        // Pastikan grid terisi dengan gambar yang sudah diacak
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                images[i][j] = levelImages.get(index++);
            }
        }
    }

    private void initializeGrid(JPanel gamePanel) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                button.setFocusPainted(false);
                button.setBackground(new Color(100, 150, 200)); // Button color
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Arial", Font.PLAIN, 18)); // Font for buttons
                button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // Border for buttons
                buttons[i][j] = button;

                button.putClientProperty("image", images[i][j]);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SoundPlayer.playSound("wav\\click.wav");
                        onButtonClick(button);
                    }
                });

                gamePanel.add(button);
            }
        }
    }

    private void onButtonClick(JButton button) {
        if (firstSelected != null && secondSelected != null) {
            return;
        }

        String imagePath = (String) button.getClientProperty("image");
        File file = new File(imagePath);
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
        } else {
            JOptionPane.showMessageDialog(this, "Image not found: " + imagePath, "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (firstSelected == null) {
            firstSelected = button;
        } else if (firstSelected != button) {
            secondSelected = button;
            checkMatch();
        }
    }

    private void checkMatch() {
        if (firstSelected.getClientProperty("image").equals(secondSelected.getClientProperty("image"))) {
            firstSelected.setEnabled(false);
            secondSelected.setEnabled(false);
            SoundPlayer.playSound("wav\\win.wav"); // Suara kartu cocok
            checkGameCompletion();
            resetSelection();
        } else {
            SoundPlayer.playSound("wav\\fail.wav"); // Suara kartu tidak cocok
            Timer tempTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    firstSelected.setIcon(null);
                    secondSelected.setIcon(null);
                    resetSelection();
                    ((Timer)e.getSource()).stop(); // Hentikan timer sementara
                }
            });
            tempTimer.start();
        }
    }

    private void resetSelection() {
        firstSelected = null;
        secondSelected = null;
    }

    private void checkGameCompletion() {
        if (allCardsMatched()) {
            // Hentikan timer untuk memperbarui waktu
            if (timer != null) {
                timer.stop();
            }

            // Hentikan pembaruan tampilan waktu
            timerLabel.setText("Waktu: " + elapsedTime + " detik (selesai)");

            // Simpan waktu pengguna ke database
            saveTimeToDatabase(elapsedTime);

            if (currentLevel < 3) {
                SoundPlayer.playSound("wav\\levelup.wav"); // Suara level selesai
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Selamat, Anda telah menyelesaikan Level " + currentLevel + "!\nApakah Anda ingin melanjutkan ke level berikutnya?",
                        "Level Selesai",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Next", "Back to Home"},
                        "Next"
                );

                if (choice == JOptionPane.YES_OPTION) {
                    currentLevel++;
                    cols = (currentLevel == 2) ? 5 : 6; // Update jumlah kolom untuk level 2 dan 3
                    initializeLevel();
                } else {
                    dispose();
                }
            } else {
                SoundPlayer.playSound("wav\\game.wav"); // Suara game selesai
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Selamat, Anda telah menyelesaikan semua level!\nApa yang ingin Anda lakukan selanjutnya?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Play Again", "Back to Home"},
                        "Play Again"
                );

                if (choice == JOptionPane.YES_OPTION) {
                    currentLevel = 1;
                    cols = 4; // Reset ke jumlah kolom level 1
                    initializeLevel();
                } else {
                    dispose();
                    new HomePage();
                }
            }
        }
    }

    private boolean allCardsMatched() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (buttons[i][j].isEnabled()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timerLabel.setText("Waktu: " + elapsedTime + " detik");
            }
        });
        timer.start();
    }

    private void saveTimeToDatabase(int time) {
        String dbUrl = "jdbc:mysql://localhost:3306/gamecenter"; // URL database MySQL
        String dbUser = "root"; // Username MySQL
        String dbPassword = ""; // Password MySQL

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO game_times (level, time_taken) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, currentLevel);
                pstmt.setInt(2, time);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemoryGame());
    }
}