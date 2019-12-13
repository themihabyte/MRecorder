package m.novikov.io.github.themihabyte.mrecorder;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.ColorInt;

public class CurveWaveFormRenderer implements WaveFormRenderer {
    private static final int Y_FACTOR = 0xFF;
    private static final float HALF_FACTOR = 0.5f;

    @ColorInt
    private final int mBackgroundColor;
    private final Paint mForegroundPaint;
    private final Path mWaveFormPath;

    static CurveWaveFormRenderer newInstance(int backgroundColor, int foregroundColor) {
        Paint paint = new Paint();
        paint.setColor(foregroundColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        Path waveFormPath = new Path();

        return new CurveWaveFormRenderer(backgroundColor, paint, waveFormPath);
    }

    CurveWaveFormRenderer(int mBackgroundColor, Paint mForegroundPaint, Path mWaveFormPath) {
        this.mBackgroundColor = mBackgroundColor;
        this.mForegroundPaint = mForegroundPaint;
        this.mWaveFormPath = mWaveFormPath;
    }

    @Override
    public void render(Canvas canvas, byte[] waveform) {
        canvas.drawColor(mBackgroundColor);
        float width = canvas.getWidth();
        float height = canvas.getHeight();

        mWaveFormPath.reset();

        if (waveform != null) {
            renderWaveForm(waveform, width, height);
        } else {
            renderBlank(width, height);
        }
        canvas.drawPath(mWaveFormPath, mForegroundPaint);
    }

    private void renderWaveForm(byte[] waveform, float width, float height) {
        float xIncrement = width / (float) waveform.length;
        float yIncrement = height / Y_FACTOR;
        int halfHeight = (int) (height * HALF_FACTOR);

        mWaveFormPath.moveTo(0, halfHeight);
        for (int i = 1; i < waveform.length; i++) {
            float yPosition = waveform[i] > 0 ? height - (yIncrement * waveform[i]) :
                    -(yIncrement * waveform[i]);
//            if (yPosition > 0) {
//                yPosition = height - (yIncrement * waveform[i]);
//            } else {
//                yPosition = -(yIncrement * waveform[i]);
//            }
            mWaveFormPath.lineTo(xIncrement * i, yPosition);
        }
        mWaveFormPath.lineTo(width, halfHeight);
    }

    private void renderBlank(float width, float height) {
        int y = (int) (height * HALF_FACTOR);
        mWaveFormPath.moveTo(0, y);
        mWaveFormPath.lineTo(width, y);
    }
}
