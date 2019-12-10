package m.novikov.io.github.themihabyte.mrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private String filename = null;

    private Button recordButton = null;
    private boolean mStartRecording = true;
    private MediaRecorder mediaRecorder = null;

    private Button playButton = null;
    private MediaPlayer mediaPlayer = null;

    private Button openFileButton = null;

    // Request permission for recording audio
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        recordButton = findViewById(R.id.recordButton); // Find recording button in layout

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

        openFileButton = findViewById(R.id.openFileButton); // Finding openFileButton in layout
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                // Pop request for audio-recording permission
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }

        if (!permissionToRecordAccepted) finish(); // Finish app if permission isn`t given
    }

    private void onRecord(boolean start) {
        if (start) {
            EditText editText = findViewById(R.id.editTextFileName); // Getting name of recording from editText
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
                Log.e(LOG_TAG, "prepare() failed");
            }

            mediaRecorder.start();
        } else {
            // If app needs to stop recording
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
