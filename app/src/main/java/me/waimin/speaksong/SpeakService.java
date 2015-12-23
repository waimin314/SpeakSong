package me.waimin.speaksong;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.Locale;

public class SpeakService extends Service {

    private String TAG = this.getClass().getSimpleName();
    private TextToSpeech t2s ;


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();


            if(action.equals("com.android.music.playstatechanged")){
                Boolean isPlaying = intent.getBooleanExtra("playing",false);
                String state;

                state = isPlaying ? "Play":  "Paused";
                Log.i(TAG, action + " / " + state);
                showInfo(state);

            }else if(action.contains("metachanged")){
                String artist = intent.getStringExtra("artist");
                String album = intent.getStringExtra("album");
                String track = intent.getStringExtra("track");
                Log.i(TAG, artist + ":" + album + ":" + track);
                showInfo(track);
            }

            //Toast.makeText(this, track, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        t2s = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t2s.setLanguage(Locale.US);
                }
            }
        });


        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");

        registerReceiver(mReceiver, iF);

        Toast.makeText(this, "Service Started : " , Toast.LENGTH_LONG).show();

        return START_STICKY;
    }

    public void showInfo(String info){
        Toast.makeText(this, info , Toast.LENGTH_LONG).show();
        //t2s.setSpeechRate(1.5f);
        t2s.speak(info,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
