import javax.swing.*;

public abstract class AbstractGame extends JFrame {
    protected boolean gameStarted;
    protected int level;
    protected long flashDelay;

    public abstract void startGame();
    public abstract void endGame(boolean won);
    public abstract void generateSequence();
    public abstract void playSequence();
}
