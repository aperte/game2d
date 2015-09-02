package game2d;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ominous yet charismatic tuna salad on 06.07.2015.
 */
public final class SoundController {
    // TODO: Add sfxvolume, bgvolume, voicevolume
    public static double volume;
    // TODO: Need better volume structure (messy down there)
    public static boolean muted;
    
    private static boolean enabled;
    private static double volumeIncrementValue = 0.05;
    private static final String SFX_PATH = "sfx/%s";
    private static HashMap<String, PlayableSound> soundMap = new HashMap<>();
    private static ArrayList<SoundPlayer> playing = new ArrayList<>();

    public static void init() {
        Settings config = new Settings("config.properties");
        enabled = Boolean.parseBoolean(config.get("sound_enabled", "true"));
        if (enabled) {
            // TODO: Get volume from config?
            volume = 0.05;
            // TODO: Get sounds from config?
            try {
                newSound("bgm", "Cold_Silence.mp3");
                newSoundEffect("beep", "beep.wav");
                newSoundEffect("slash", "slash.wav");
                newSoundEffect("stab", "stab.wav");
                newSoundEffect("AAA", "AAA.wav");
                newSoundEffect("AAA2", "AAA2.wav");
                newSoundEffect("AAA3", "AAA3.wav");
            } catch (Exception e) {
                // TODO: Fix error handling ..
                System.out.println("Error when reading sound files: turned off sound.");
                enabled = false;
                return;
            }
        } else {
            return;
        }
        System.out.println("Sound initiated and enabled!"); // TODO: Change to LOGMSG.
    }

    private static void newSoundEffect(String id, String uri) throws IOException {
        // TODO: Error handling
        putSound(id, new SoundEffect(getStringURI(String.format(SFX_PATH, uri))));
    }

    private static void newSound(String id, String uri) throws IOException {
        // TODO: Error handling
        putSound(id, new SoundPlayer(getStringURI(String.format(SFX_PATH, uri))));
    }

    private static String getStringURI(String uri) throws IOException {
        File tmp = new File(uri);
        return tmp.toURI().toString();
    }

    private static PlayableSound getSound(String id) {
        // TODO: Error handling
        return soundMap.get(id);
    }

    private static PlayableSound putSound(String id, PlayableSound sound) {
        // TODO: Error handling
        return soundMap.put(id, sound);
    }

    public static void playSound(String id) {
        if (enabled) {
            PlayableSound sound = getSound(id);
            sound.play();
            if (sound instanceof SoundPlayer) {
                playing.add((SoundPlayer) sound);
            }
        }
    }

    static public void mutePlaying() {
        if (muted) {
            for (SoundPlayer sp: playing) {
                sp.mediaPlayer.setVolume(volume);
                muted = false;
            }
        } else {
            for (SoundPlayer sp: playing) {
                sp.mediaPlayer.setVolume(0);
                muted = true;
            }
        }
    }

    static public void setVolume(Double vol) {
        volume = vol;
        for (SoundPlayer sp: playing) {
            sp.mediaPlayer.setVolume(vol);
            muted = false; // automatically unmute if incremented/decremented
        }
    }

    static public void incrementVolume() {
        if (volume < 1.0) {
            setVolume(volume + volumeIncrementValue);
        }
    }

    static public void decrementVolume() {
        if (volume > 0.0) {
            setVolume(volume - volumeIncrementValue);
        }
    }
}

/** Class for playing AudioClips, ment for short sounds with no control - fire and forget. */
@SuppressWarnings("restriction")
class SoundEffect extends PlayableSound {
	
    private AudioClip clip;
    
    SoundEffect(String uri) {
        this.clip = new AudioClip(uri);
    }

    void play() {
        // TODO: Add a short cooldown so we don't play several soundeffects too fast(?)
        if (SoundController.muted) return;
        clip.setVolume(SoundController.volume);
        new Thread(() -> clip.play()).start();
    }

}

/** Class for playing Media sound, ment for longer sounds with control possible. */
@SuppressWarnings("restriction")
class SoundPlayer extends PlayableSound {
	
	Media media;
    MediaPlayer mediaPlayer;
    Thread th;

    SoundPlayer(String uri) throws MediaException {
        this.media = new Media(uri);
    }

    void play() {
        th = new Thread(() -> {
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(SoundController.volume);
            mediaPlayer.setOnReady(() -> mediaPlayer.play());
        });
        th.start();
    }
}

/* Abstract class for gathering the playable sounds under one polymorphism */
abstract class PlayableSound {
    abstract void play();
}
