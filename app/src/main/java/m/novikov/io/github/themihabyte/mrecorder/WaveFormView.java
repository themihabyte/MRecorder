package m.novikov.io.github.themihabyte.mrecorder;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Arrays;

public class WaveFormView extends View {

    private byte[] waveForm;

    private WaveFormRenderer renderer;

    public WaveFormView(Context context) {
        super(context);
    }

    public WaveFormView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveFormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaveFormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setRenderer(WaveFormRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (renderer != null) {
            renderer.render(canvas, waveForm);
        }
    }

    public void setWaveForm(byte[] waveForm) {
        this.waveForm = Arrays.copyOf(waveForm, waveForm.length);
        invalidate();
//        this.waveForm = waveForm;
    }
}
