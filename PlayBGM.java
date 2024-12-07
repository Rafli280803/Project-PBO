import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class PlayBGM extends Thread {
    private String bgmPath;

    public PlayBGM(String bgmPath) {
        this.bgmPath = bgmPath;
    }

    @Override
    public void run() {
        try {
            File musicFile = new File(bgmPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000); 
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
