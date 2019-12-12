package m.novikov.io.github.themihabyte.mrecorder;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OpenActivity extends AppCompatActivity {

    //    private ArrayList<File> records;
    private ListView recordsView;

    private File mPrivateRootDir;
    private File mRecordsDir;
    File[] mRecordFiles;
    String[] mRecordFilenames;
    int ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list);

        recordsView = findViewById(R.id.list);
//        records = new ArrayList<>();
        mRecordFiles = getExternalCacheDir().listFiles();

        RecordAdapter recordAdapter = new RecordAdapter(this, mRecordFiles);

        recordsView.setAdapter(recordAdapter);

        recordsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(OpenActivity.this, MainActivity.class);
//                intent.putExtra("filename", mRecordFiles[((int) id)].getName());
                ID = (int) id;
                finish();
            }
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("filename", mRecordFiles[ID].getName());
        setResult(1, intent);
        super.finish();
    }
}
