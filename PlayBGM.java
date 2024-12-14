import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class PlayBGM extends Thread {
    private String filePath;
    private boolean running = true;

    public PlayBGM(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            // Load and play the audio file (example using JavaFX MediaPlayer or another library)
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            while (running) {
                Thread.sleep(100); // Allow the thread to check for `running` flag
            }

            clip.stop();
            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBGM() {
        running = false;
    }
}

