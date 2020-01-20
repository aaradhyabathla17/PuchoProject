package com.puchoInc.diya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.File;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.puchoInc.diya.TextSpeakData.SpeakData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import Resources.speakData;
import services.RecordingService;

public class SpeakActivity extends AppCompatActivity {
    private ViewPager mMyViewPager;
    Intent intent;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private TabLayout mTabLayout;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    public static final int RECORD_AUDIO = 0;
    public static final int SAVE_AUDIO = 1;
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4,
            AUDIO_RECORDER_FILE_EXT_3GP};


    //new implementation
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";

    private int position;

    //Recording controls
    private Button mRecordButton = null;
    private boolean mStartRecording = true;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);
        mTabLayout = findViewById(R.id.tab_speak);
        mMyViewPager = findViewById(R.id.viewpager);
        mRecordButton = (Button) findViewById(R.id.btn_play);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.3gp";
//
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(SpeakActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(SpeakActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
                            RECORD_AUDIO);

                } else {
                    onRecord(mStartRecording);
                    mStartRecording = !mStartRecording;
                }
            }
        });

        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            } else {
                //User denied Permission.
                Toast.makeText(SpeakActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode==SAVE_AUDIO)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
                stopService(intent);
                //allow the screen to turn off again once recording is finished
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                //User denied Permission.
                Toast.makeText(SpeakActivity.this, "Storage Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void init() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        SpeakData[] speakData = Resources.speakData.getSpeakDatas();
        for (SpeakData speakData1 : speakData) {
            SpeakViewPagerItemFragment fragment = SpeakViewPagerItemFragment.getInstance(speakData1);
            fragments.add(fragment);
        }
        SpeakActivityPager pagerAdapter = new SpeakActivityPager(getSupportFragmentManager(), fragments);
        mMyViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mMyViewPager, true);
    }

    // Recording Start/Stop
    //TODO: recording pause
    private void onRecord(boolean start) {

         intent = new Intent(this, RecordingService.class);

        if (start) {
            // start recording
            mRecordButton.setBackgroundResource(R.drawable.stop_audio_button);
            //mPauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.toast_recording_start, Toast.LENGTH_SHORT).show();
            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir();
            }

            //start RecordingService
            startService(intent);
            //keep screen on while recording
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        } else {
            //stop recording
           mRecordButton.setBackgroundResource(R.drawable.circular_play_btn);
            if (ActivityCompat.checkSelfPermission(SpeakActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SpeakActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        SAVE_AUDIO);

            } else {
            stopService(intent);
            //allow the screen to turn off again once recording is finished
         getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
}
    }


}
