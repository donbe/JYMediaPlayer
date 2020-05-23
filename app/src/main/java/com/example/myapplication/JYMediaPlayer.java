package com.example.myapplication;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class JYMediaPlayer implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener ,MediaPlayer.OnSeekCompleteListener,MediaPlayer.OnCompletionListener{

    MediaPlayer mediaPlayer = new MediaPlayer();

    private Listener mListener;

    // 定时器，用来回调播放时间和停止播放使用
    private Timer timer;

    // 播放时，跳过多少毫秒数
    private int seekTo;

    // 播放到多少毫秒
    private int stopTo;

    // 当前播放时间点
    private int currentPos;

    // 当前正在播放的context
    private Context context;

    // 当前正在播放的音乐文件路径
    private Uri uri;


    public JYMediaPlayer() {

    }


    synchronized public boolean play(Context context, Uri uri) {

        return play(context,uri,0,0);
    }

    /*
     * 播放音乐
     *
     * @param seekms 开始毫秒数
     *
     * @return 成功返回true，失败返回false
     *
     * */
    synchronized public boolean play(Context context, Uri uri, int seekms) {

        return play(context,uri,seekms,0);
    }

    /*
    * 播放音乐
    *
    * @param seekms 开始毫秒数
    * @param stopms 结束毫秒数
    *
    * @return 成功返回true，失败返回false
    *
    * */
    synchronized public boolean play(Context context, Uri uri,int seekms, int stopms) {

        // 初始化播放器
        initMediaPlayer();

        seekTo = seekms;
        stopTo = stopms;
        this.context = context;
        this.uri = uri;

        try {
            mediaPlayer.setDataSource(context, uri);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        mediaPlayer.prepareAsync();

        return true;
    }

    /*停止播放*/
    synchronized public void stop() {
        releaseMediaPlayer();
    }

    /*继续播放*/
    synchronized public void resume() {
        if (context != null && uri != null) {
            this.play(context, uri, currentPos, stopTo);
        }
    }

    /*启动定时器*/
    private void startTimer(){

        if (timer != null)return;

        JYMediaPlayer self = this;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (mediaPlayer == null)
                    return;

                try {
                    if (!mediaPlayer.isPlaying())
                        return;
                }catch (IllegalStateException e){
                    e.printStackTrace();
                    return;
                }

                currentPos = mediaPlayer.getCurrentPosition();

                if (mListener != null){
                    mListener.onPlayerPlaying(currentPos);
                }

                if (stopTo >0 && currentPos > stopTo){
                    stopTo = 0;
                    stop();
                }
            }

            // 人耳大约能分辨100毫秒左右的是延迟，所以设置50毫秒的间隔时间
        }, 0, 10);
    }

    /*
    * 停止定时器
    * */
    private void stopTimer(){

        if (timer != null){
            timer.cancel();
            timer = null;
        }

    }

    /*
    * 初始化播放器
    * */
    private void initMediaPlayer() {

        if (mediaPlayer!=null){
            stop();
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnCompletionListener(this);

        startTimer();
    }

    /*
    * 释放播放器
    * */
    private void releaseMediaPlayer() {

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            if (mListener != null){
                mListener.onPlayerStop();
            }
        }

        stopTimer();
        stopTo = 0;
        seekTo = 0;

    }

    /*获取当前播放状态*/
    public boolean isPlaying(){
        try {
            if (mediaPlayer == null)
                return false;

            return mediaPlayer.isPlaying();
        }catch (IllegalStateException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        if (mListener != null)
            mListener.onPlayerError();

        // 这里返回true，否则会回调onCompletion方法
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (seekTo > 0) {
            mediaPlayer.seekTo(seekTo);
            seekTo = 0;
        }else{
            mediaPlayer.start();
            if (mListener != null){
                mListener.onPlayerStart();
            }
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mediaPlayer.start();
        if (mListener != null){
            mListener.onPlayerStart();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mListener != null){
            mListener.onPlayerStop();
        }
    }


    public interface Listener {
        void onPlayerError();
        void onPlayerStart();
        void onPlayerStop();
        void onPlayerPlaying(int currentPosition);
    }

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }
}
