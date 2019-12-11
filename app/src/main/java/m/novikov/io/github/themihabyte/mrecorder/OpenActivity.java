package m.novikov.io.github.themihabyte.mrecorder;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list);

        recordsView = findViewById(R.id.list);
//        records = new ArrayList<>();
        mRecordFiles = getExternalCacheDir().listFiles();

        RecordAdapter recordAdapter = new RecordAdapter(this, mRecordFiles);

recordsView.setAdapter(recordAdapter);

    }

}
