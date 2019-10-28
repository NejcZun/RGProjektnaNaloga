package music;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

public class MusicMixer {
    String filename;
    Audio sound;

    public MusicMixer(String filename){
        this.filename= "res/music/"+filename;
    }
    public void play(){
        try {
            sound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sound.playAsMusic(1.0f, 1.0f, true);
    }
    public void stop(){
        sound.stop();
    }
}
