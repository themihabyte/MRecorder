package m.novikov.io.github.themihabyte.mrecorder;

import android.graphics.Canvas;

public interface WaveFormRenderer {
    void render(Canvas canvas, byte[] waveform);
}
