package firstproject.cuteshake;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlay {

    private static MediaPlayer mediaPlayer = null;

    public static void play(Context context, int resid){
        mediaPlayer = MediaPlayer.create(context, resid);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void pause(){
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    public static void resume(){
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    public static void chage(Context context, int resid){
        mediaPlayer = MediaPlayer.create(context, resid);
        mediaPlayer.setLooping(true);

    }
}
