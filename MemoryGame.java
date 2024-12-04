package MemoryGame;  // Pastikan nama package sesuai dengan folder

import HomePage.HomePage; // Import halaman home

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame extends JFrame {
    private final int GRID_SIZE = 4; // 4x4 grid
    private JButton[][] buttons = new JButton[GRID_SIZE][GRID_SIZE];
    private String[][] images = new String[GRID_SIZE][GRID_SIZE];
    private ArrayList<String> imageList = new ArrayList<>();
    private JButton firstSelected = null;
    private JButton secondSelected = null;
    private Timer timer;
    
    public MemoryGame() {
        setTitle("Memory Card Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        setSize(600, 600);

        // Load images from database
        loadImagesFromDatabase();

        // Initialize grid
        initializeGrid();

        setVisible(true);
    }

    private void loadImagesFromDatabase() {
        String dbUrl = "jdbc:mysql://localhost:3306/memory_game"; // URL database MySQL
        String dbUser = "root"; // Username MySQL
        String dbPassword = ""; // Password MySQL
        String query = "SELECT image_path FROM cards LIMIT 8"; // Query untuk mengambil 8 gambar

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Load images into list
            while (rs.next()) {
                String imagePath = rs.getString("image_path");
                imageList.add(imagePath);
                imageList.add(imagePath); // Duplicate for pairs
            }

            // Shuffle the image list
            Collections.shuffle(imageList);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load images from database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeGrid() {
        int index = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton button = new JButton();
                button.setFocusPainted(false);
                buttons[i][j] = button;

                // Assign images from the shuffled list
                images[i][j] = imageList.get(index);
                button.putClientProperty("image", images[i][j]);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SoundPlayer.playSound("/Users/raflikardiansyah/Documents/Semester 5/PBO/project/Sound/click.wav"); // Mainkan suara klik
                        onButtonClick(button);
                    }
                });
                add(button);

                index++;
            }
        }
    }

    private void onButtonClick(JButton button) {
        if (firstSelected != null && secondSelected != null) {
            return; // Tunggu timer selesai
        }

        String imagePath = (String) button.getClientProperty("image");
        File file = new File(imagePath);
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg)); // Tampilkan gambar
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
            // Jika kartu cocok
            firstSelected.setEnabled(false);
            secondSelected.setEnabled(false);
            
            // Mainkan suara kemenangan jika semua kartu ditemukan
            SoundPlayer.playSound("/Users/raflikardiansyah/Documents/Semester 5/PBO/project/Sound/win.wav");
            
            // Cek jika semua kartu sudah ditemukan
            checkGameCompletion();
            
            resetSelection();
        } else {
            // Mainkan suara kekalahan jika kartu tidak cocok
            SoundPlayer.playSound("/Users/raflikardiansyah/Documents/Semester 5/PBO/project/Sound/fail.wav");

            // Jika kartu tidak cocok, sembunyikan setelah 1 detik
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    firstSelected.setIcon(null);
                    secondSelected.setIcon(null);
                    resetSelection();
                    timer.stop();
                }
            });
            timer.start();
        }
    }

    private void resetSelection() {
        firstSelected = null;
        secondSelected = null;
    }

    private void checkGameCompletion() {
        if (allCardsMatched()) {
            // Mainkan suara tepuk tangan saat selesai
            SoundPlayer.playSound("/Users/raflikardiansyah/Documents/Semester 5/PBO/project/Sound/game.wav");

            // Tampilkan dialog pilihan kepada user
        int choice = JOptionPane.showOptionDialog(
            this,
            "Selamat, Anda telah menyelesaikan permainan!\nApa yang ingin Anda lakukan selanjutnya?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Main Ulang", "Kembali ke Home"}, // Pilihan
            "Main Ulang"
    );

    if (choice == JOptionPane.YES_OPTION) {
        // Main ulang
        dispose();
        new MemoryGame();
    } else {
        // Kembali ke halaman home
        dispose();
        new HomePage();
    }
        }

        }
    

    private boolean allCardsMatched() {
        // Logika untuk mengecek apakah semua kartu sudah dicocokkan
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (buttons[i][j].isEnabled()) {
                    return false; // Jika ada kartu yang belum dicocokkan
                }
            }
        }
        return true; // Semua kartu sudah dicocokkan
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemoryGame());
    }
}
