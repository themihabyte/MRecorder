package m.novikov.io.github.themihabyte.mrecorder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private static final int REQUEST_PERMISSIONS = 200;
    private static final int CAPTURE_SIZE = 256;

    private String filename = null;

    EditText editText;

    private Visualizer visualizer;
    private WaveFormView waveFormView;

    private Button recordButton = null;
    private boolean mStartRecording = true; // To start recording
    private MediaRecorder mediaRecorder = null;

    private Button playButton = null;
    private MediaPlayer mediaPlayer = null;
    private boolean mStartPlaying = true; // To start playing

    private Button openFileButton = null;

    // Request permission for recording audio
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToModifyAudio = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i])
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            }
        }

        editText = findViewById(R.id.editTextFileName); // Getting name of recording from editText

        recordButton = findViewById(R.id.recordButton); // Find recording button in layout

        /*TODO:
         *  colors in visualizer?
         * */

        waveFormView = findViewById(R.id.waveForm);

        RendererFactory rendererFactory = new RendererFactory();
        waveFormView.setRenderer(rendererFactory.createCurveWaveFormRenderer(getResources().getColor(R.color.colorPrimaryDark, null), Color.TRANSPARENT));

        // Set Listener to recoding button as InnerClass
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                // If recording might be started
                if (mStartRecording) recordButton.setText("stop recording");
                    // If recording might be finished
                else recordButton.setText("record");

                mStartRecording = !mStartRecording;
            }
        });

        playButton = findViewById(R.id.playButton); // Finding playButton in layout
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filename == null) return;
                onPlay(mStartPlaying);
                if (mStartPlaying) playButton.setText("stop");
                else playButton.setText("play");

                mStartPlaying = !mStartPlaying;
            }
        });

        openFileButton = findViewById(R.id.openFileButton); // Finding openFileButton in layout
        openFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OpenActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onPause() {
        if (visualizer != null) {
            visualizer.setEnabled(false);
            visualizer.release();
            visualizer.setDataCaptureListener(null, 0, false, false);
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) startVisualizer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            switch (requestCode) {
                case 1:
                    String fName = data.getStringExtra("filename");
                    editText.setText(fName);
                    filename = getExternalCacheDir().getAbsolutePath() + "/" + fName;

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                // Pop request for audio-recording permission
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToModifyAudio = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;

        }

        if (!permissionToRecordAccepted && !permissionToModifyAudio)
            finish(); // Finish app if permission isn`t given
    }

    private void onRecord(boolean start) {
        if (start) {

            filename = getExternalCacheDir().getAbsolutePath() + "/" +
                    editText.getText().toString() + ".3gp"; // Setting filename

            mediaRecorder = new MediaRecorder(); // Creating MediaRecorder instance
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // Set recording from micro
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // Output format is 3gp
            mediaRecorder.setOutputFile(filename);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "mediaRecorder.prepare() failed");
            }

            mediaRecorder.start();
        } else {
            // If app needs to stop recording
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(filename);
            } catch (IOException e) {
                Log.e(LOG_TAG, "mediaPlayer.setDataSource() failed");
            }

            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "mediaPlayer.prepare() failed");
            }

            startVisualizer();
            mediaPlayer.start();


            // Set listener for ending audio
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                        playButton.setText("play");
                        mStartPlaying = true;
                    }
                }
            });
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void startVisualizer() {
        visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                if (waveFormView != null) waveFormView.setWaveForm(waveform);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                //TODO
            }
        }, Visualizer.getMaxCaptureRate(), true, false);

        visualizer.setCaptureSize(CAPTURE_SIZE);
        visualizer.setEnabled(true);
    }
}
