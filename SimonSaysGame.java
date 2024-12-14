import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimonSaysGame extends JFrame {
    private JButton[] buttons;
    private int[] sequence;
    private ArrayList<Integer> playerInput;
    private int currentInputIndex;
    private JLabel statusLabel;
    private JButton startButton;
    private ExecutorService executorService;
    private int level;
    private boolean gameStarted;
    private int userId; // ID pengguna

    public SimonSaysGame(int userId) { // Menerima ID pengguna sebagai parameter
        this.userId = userId; // Set ID pengguna
        setTitle("Simon Says");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buttons = new JButton[9];
        sequence = new int[50];
        playerInput = new ArrayList<>();
        currentInputIndex = 0;
        level = 1;
        gameStarted = false;

        createButtonPanel();
        createStatusLabel();
        createStartButton();

        executorService = Executors.newSingleThreadExecutor();
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        buttonPanel.setBackground(Color.BLACK);
        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();
            button.setBackground(getButtonColor(i));
            button.setOpaque(true);
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            button.addActionListener(new ButtonClickListener(i));
            button.setEnabled(false);
            buttons[i] = button;
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void createStatusLabel() {
        statusLabel = new JLabel("Tekan Start untuk mulai!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void createStartButton() {
        startButton = new JButton("Start");
        startButton.addActionListener(e -> startGame());
        add(startButton, BorderLayout.NORTH);
    }

    public void startGame() {
        gameStarted = true;
        startButton.setEnabled(false);
        level = 1;
        currentInputIndex = 0;
        playerInput.clear();
        resetButtons();
        generateSequence();
        playSequence();
    }

    public void endGame(boolean won) {
        gameStarted = false;
    
        // Simpan level terakhir ke database
        saveMaxLevelToDatabase(level - 1);
    
        String message = won ? "Anda Menang! " : "Game Over! ";
        message += "Pilih opsi di bawah ini.";
    
        int choice = JOptionPane.showOptionDialog(this,
                message,
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Play Again", "Back to Home"},
                null);
    
        if (choice == JOptionPane.YES_OPTION) {
            startGame(); // Mulai ulang permainan
        } else {
            // Kembali ke HomePage
            SwingUtilities.invokeLater(() -> {
                dispose(); // Tutup game saat kembali ke Home
            });
        }
    }
    
    public void saveMaxLevelToDatabase(int lastLevel) {
        String dbUrl = "jdbc:mysql://localhost:3306/memory_game"; // URL database MySQL
        String dbUser = "root"; // Username MySQL
        String dbPassword = ""; // Password MySQL
    
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Koneksi berhasil!");
    
            // Cek level maksimum yang sudah disimpan
            String checkQuery = "SELECT max_level FROM user_progress WHERE user_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, userId);
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next()) {
                    int savedLevel = rs.getInt("max_level");
                    System.out.println("Level yang disimpan: " + savedLevel);
    
                    // Jika level baru lebih tinggi, update level maksimum
                    if (lastLevel > savedLevel) {
                        String updateQuery = "UPDATE user_progress SET max_level = ? WHERE user_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, lastLevel);
                            updateStmt.setInt(2, userId);
                            updateStmt.executeUpdate();
                            System.out.println("Level diperbarui menjadi: " + lastLevel);
                        }
                    } else {
                        System.out.println("Level baru tidak lebih tinggi, tidak ada perubahan.");
                    }
                } else {
                    // Jika pengguna belum ada, insert level maksimum
                    String insertQuery = "INSERT INTO user_progress (user_id, max_level) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, lastLevel);
                        insertStmt.executeUpdate();
                        System.out.println("Level disimpan untuk pengguna baru: " + lastLevel);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat menyimpan level: " + e.getMessage());
        }
    }
    

    public void generateSequence() {
        for (int i = 0; i < level; i++) {
            sequence[i] = new Random().nextInt(9);
        }
    }

    public void playSequence() {
        executorService.submit(() -> {
            disableButtons();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            for (int i = 0; i < level; i++) {
                int index = sequence[i];
                flashButton(index);
            }
            enableButtons();
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Giliran Anda! Tekan tombol sesuai urutan.");
                currentInputIndex = 0;
            });
        });
    }

    private void flashButton(int index) {
        Color flashColor = Color.YELLOW;
        SwingUtilities.invokeLater(() -> {
            buttons[index].setBackground(flashColor);
            buttons[index].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        SwingUtilities.invokeLater(() -> {
            buttons[index].setBackground(getButtonColor(index));
            buttons[index].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void disableButtons() {
        SwingUtilities.invokeLater(() -> {
            for (JButton button : buttons) {
                button.setEnabled(false);
            }
        });
    }

    private void enableButtons() {
        SwingUtilities.invokeLater(() -> {
            for (JButton button : buttons) {
                button.setEnabled(true);
            }
        });
    }

    private void resetButtons() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackground(getButtonColor(i));
            buttons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }
    }

    private Color getButtonColor(int index) {
        switch (index) {
            case 0: return new Color(128, 0, 128); // Ungu
            case 1: return new Color(0, 255, 0); // Hijau terang
            case 2: return new Color(255, 0, 0); // Merah terang
            case 3: return new Color(255, 165, 0); // Oranye terang
            case 4: return new Color(0, 255, 255); // Cyan terang
            case 5: return new Color(255, 0, 255); // Magenta terang
            case 6: return new Color(255, 255, 0); // Kuning terang
            case 7: return new Color(255, 105, 180); // Pink terang
            case 8: return new Color(211, 211, 211); // Abu-abu terang
            default: return Color.GRAY;
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int index;

        public ButtonClickListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameStarted) return;

            animateButtonClick(index);

            if (index == sequence[currentInputIndex]) {
                currentInputIndex++;
                if (currentInputIndex == level) {
                    level++;
                    if (level > 9) {
                        endGame(true);
                    } else {
                        playerInput.clear();
                        generateSequence();
                        playSequence();
                        statusLabel.setText("Benar! Lanjutkan ke level " + level + ".");
                    }
                }
            } else {
                endGame(false);
            }
        }

        private void animateButtonClick(int index) {
            Color clickColor = Color.GREEN;
            SwingUtilities.invokeLater(() -> {
                buttons[index].setBackground(clickColor);
                buttons[index].setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
            });
            new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        buttons[index].setBackground(getButtonColor(index));
                        buttons[index].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                    });
                    ((Timer) e.getSource()).stop();
                }
            }).start();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        executorService.shutdown();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Misalkan user ID adalah 1, Anda bisa mengubahnya sesuai dengan pengguna yang masuk
            SimonSaysGame game = new SimonSaysGame(1); 
            game.setVisible(true);
        });
    }
}