package m.novikov.io.github.themihabyte.mrecorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

public class RecordAdapter extends ArrayAdapter<File> {

    public RecordAdapter(@NonNull Context context, @NonNull File[] objects) {
        super(context,0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        File currentFile = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.record_name);
        nameTextView.setText(currentFile.getName());

        String size = String.valueOf(currentFile.length()/1000f)+ "kb";

        TextView sizeTextView = listItemView.findViewById(R.id.record_size);
        sizeTextView.setText(size);

        return listItemView;
    }
}
