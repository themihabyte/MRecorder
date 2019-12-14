package m.novikov.io.github.themihabyte.mrecorder;

public class RendererFactory {
    public WaveFormRenderer createCurveWaveFormRenderer(int foreground, int background){
        return CurveWaveFormRenderer.newInstance(background, foreground);
    }
}
