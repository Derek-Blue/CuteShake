package firstproject.cuteshake;

import android.content.Context;
import android.media.MediaPlayer;

//未使用 宣告靜態測試感覺較穩
public class MusicModel {

    MediaPlayer mediaPlayer;
    Context context;
    int resource;

    public MusicModel(MediaPlayer mediaPlayer, Context context, int resource) {
        this.mediaPlayer = mediaPlayer;
        this.context = context;
        this.resource = resource;
    }

    public void play(){
        mediaPlayer = MediaPlayer.create(context, resource);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void stop(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void pause(){
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    public void resume(){
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    public void chageMusic(int resource){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        this.resource = resource;
        mediaPlayer = MediaPlayer.create(context, resource);
        mediaPlayer.setLooping(true);

        if (!MainActivity.isPlay){
            mediaPlayer.start();
        }
    }
}
