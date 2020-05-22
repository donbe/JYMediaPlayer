package com.example.myapplication;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class RecorderHelper {

    public int sampleRate = 16000; // 采样率
    public int numChannels= 1; // 通道数
    public int bitsPreSample = AudioFormat.ENCODING_PCM_16BIT; // 采样精度

    public String recordFile ; //录音文件

    private Listener mListener; // 监听器
    private  AudioRecord mAudioRecord;

    private int mBufferSize; // 缓存大小

    // 构造器

    public RecorderHelper() throws Exception {

        // 创建录音文件
        createRecordFile();

    }

    private void createRecordFile() throws Exception {
        File file = new File("jyrecord.pcm");
        boolean result = file.createNewFile();
        if (!result){
            throw new Exception("创建录音文件失败");
        }
        recordFile = file.getAbsolutePath();
    }

    private boolean initAudioRecorder() {

        mBufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                numChannels,
                bitsPreSample);

        mAudioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                numChannels,
                bitsPreSample,
                mBufferSize
        );

        int state = mAudioRecord.getState();
        if (state != AudioRecord.STATE_INITIALIZED) {
            return false;
        }

        return true;
    }


    public interface Listener {
        void onRecord(String file);

        void onRecording(ArrayList<Short> data, int totalMillisecond);

        void onRecording( byte[] pacmByte);
//        void onTimeChanged(int second, int deci_second);
    }



    public void setListener(Listener listener) {
        mListener = listener;
    }


    public void start() {

    }

    /**
    * 开始录音
    *
    * @param time 开始录制的时间
    *
    * */
    public void startAtTime(float time) {

    }

    public void stop() {

    }



}
