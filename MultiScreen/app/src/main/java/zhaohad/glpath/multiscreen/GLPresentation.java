package zhaohad.glpath.multiscreen;

import android.app.Presentation;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class GLPresentation extends Presentation {
    private GLSurfaceView mGLViewLeft;
    private GLRender mGLRenderLeft;
    private GLSurfaceView mGLViewRight;
    private GLRender mGLRenderRight;

    public GLPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }

    public GLPresentation(Context outerContext, Display display, int theme) {
        super(outerContext, display, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_glpresetation);

        mGLViewLeft = findViewById(R.id.glViewLeft);
        mGLViewRight = findViewById(R.id.glViewRight);
        mGLViewLeft.setEGLContextClientVersion(3);
        mGLViewRight.setEGLContextClientVersion(3);

        mGLRenderLeft = new GLRender(getContext(), true);
        mGLRenderRight = new GLRender(getContext(), false);

        mGLViewLeft.setRenderer(mGLRenderLeft);
        mGLViewRight.setRenderer(mGLRenderRight);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGLViewLeft.onResume();
        mGLViewRight.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGLViewLeft.onPause();
        mGLViewRight.onPause();
    }
}
