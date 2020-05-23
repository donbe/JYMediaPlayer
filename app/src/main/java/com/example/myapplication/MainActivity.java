package com.example.myapplication;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,JYMediaPlayer.Listener{

    private RecorderHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        player = new JYMediaPlayer();
        player.setmListener(this);


        buttonAction();


    }

    private void buttonAction() {

        // 开始录音
        Button startRecord = findViewById(R.id.startRecord);
        startRecord.setOnClickListener(this);


        // 停止录音
        Button stopRecord = findViewById(R.id.stopRecord);
        stopRecord.setOnClickListener(this);


        // 开始播放
        Button startPlay = findViewById(R.id.startPlay);
        startPlay.setOnClickListener(this);

        //停止播放
        Button stopPlay = findViewById(R.id.stopPlay);
        stopPlay.setOnClickListener(this);

        //停止播放
        Button startSeek = findViewById(R.id.startSeek);
        startSeek.setOnClickListener(this);

        // 播放状态
        Button isplaying= findViewById(R.id.isplaying);
        isplaying.setOnClickListener(this);

        // 继续播放
        Button resume= findViewById(R.id.resume);
        resume.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.startRecord:{
                startRecord();
            }
            break;
            case R.id.stopRecord:{
                stopRecord();
            }
            break;
            case R.id.startPlay:{
                startPlay();
            }
            break;
            case R.id.stopPlay:{
                stopPlay();
            }
            break;
            case R.id.startSeek:{
                startSeek();
            }
            break;
            case R.id.isplaying:{
                isplaying();
            }
            break;
            case R.id.resume:{
                resume();
            }
            break;
        }
    }

    /*开始录音*/
    private void startRecord(){

    }

    /*停止录音*/
    private void stopRecord(){

    }

    /*继续播放录音*/
    private void resume(){

        player.resume();
    }

    /*播放状态*/
    private void isplaying(){

        if (player.isPlaying()){
            alert("播放中");
        }else{
            alert("未播放");
        }

    }


    private JYMediaPlayer player;

    /*开始播放*/
    private void startPlay(){

//        Uri uri = Uri.parse("android.resource://com.example.myapplication/raw/iloveyou");
        Uri uri = Uri.parse("android.resource://com.example.myapplication/raw/mangzhong");
        boolean ret = player.play(this,uri,10000);
        if (!ret){
            alert("播放失败");
        }
    }


    private void startSeek(){

//        Uri uri = Uri.parse("android.resource://com.example.myapplication/raw/iloveyou");
        Uri uri = Uri.parse("android.resource://com.example.myapplication/raw/mangzhong");
        boolean ret = player.play(this,uri,30000,40000);
        if (!ret){
            alert("播放失败");
        }
    }

    /*停止播放*/
    private void stopPlay(){

        if (player!=null)
            player.stop();
    }

    private void alert(String text){
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onPlayerError() {
        Log.i("donbe","onPlayerError:");
    }

    @Override
    public void onPlayerStart() {
        Log.i("donbe","onPlayerStart:");
    }

    @Override
    public void onPlayerStop() {
        Log.i("donbe","onPlayerStop:");
    }

    @Override
    public void onPlayerPlaying(int currentPosition) {

        Log.i("donbe","currentPosition:"+currentPosition);
    }
}
