import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimonSaysGame extends AbstractGame {
    private JButton[] buttons;
    private int[] sequence;
    private int currentIndex;
    private JLabel statusLabel;
    private JButton startButton;
    private ExecutorService executorService;

    public SimonSaysGame() {
        setTitle("Simon Says");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buttons = new JButton[9];
        sequence = new int[50]; 
        currentIndex = 0;
        gameStarted = false;

        createButtonPanel();
        createStatusLabel();
        createStartButton();

        executorService = Executors.newSingleThreadExecutor();
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();
            button.setBackground(getButtonColor(i));
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

    @Override
    public void startGame() {
        gameStarted = true;
        startButton.setEnabled(false);
        currentIndex = 0;
        level = 1; 
        flashDelay = 500; 
        resetButtons();
        generateSequence();
        playSequence();
    }

    @Override
    public void endGame(boolean won) {
        gameStarted = false;
        if (won) {
            statusLabel.setText("Anda Menang! Tekan Start untuk bermain lagi.");
        } else {
            statusLabel.setText("Game Over! Tekan Start untuk coba lagi.");
        }
        startButton.setEnabled(true);
        resetButtons();
    }

    @Override
    public void generateSequence() {
        Random random = new Random();
        sequence[currentIndex] = random.nextInt(9); 
    }

    @Override
    public void playSequence() {
        executorService.submit(() -> {
            disableButtons();
            for (int i = 0; i <= currentIndex; i++) {
                int index = sequence[i];
                flashButton(index);
            }
            enableButtons();
            SwingUtilities.invokeLater(() -> statusLabel.setText("Giliran Anda!"));
        });
    }

    private void flashButton(int index) {
        SwingUtilities.invokeLater(() -> buttons[index].setBackground(Color.WHITE));
        try {
            Thread.sleep(flashDelay);  
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        SwingUtilities.invokeLater(() -> buttons[index].setBackground(getButtonColor(index)));
        try {
            Thread.sleep(flashDelay); 
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
        }
    }

    private Color getButtonColor(int index) {
        switch (index) {
            case 0: return Color.RED;
            case 1: return Color.GREEN;
            case 2: return Color.BLUE;
            case 3: return Color.ORANGE;
            case 4: return Color.CYAN;
            case 5: return Color.MAGENTA;
            case 6: return Color.YELLOW;
            case 7: return Color.PINK;
            case 8: return Color.LIGHT_GRAY;
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

            if (index == sequence[currentIndex]) {
                currentIndex++;
                if (currentIndex == level) {
                    level++;
                    flashDelay -= 50;  
                    generateSequence(); 
                    playSequence();
                    statusLabel.setText("Benar! Lanjutkan ke level " + level + ".");
                }
            } else {
                endGame(false);
                statusLabel.setText("Game Over! Tekan Start untuk coba lagi.");
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        executorService.shutdown(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimonSaysGame game = new SimonSaysGame();
            game.setVisible(true);
        });
    }
}
