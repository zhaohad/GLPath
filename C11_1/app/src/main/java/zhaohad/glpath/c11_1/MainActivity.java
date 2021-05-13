package zhaohad.glpath.c11_1;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {
    private GLSurfaceView mGLView;
    private GLRender mGLRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getBaseContext();


        mGLView = new GLSurfaceView(context);
        mGLView.setEGLContextClientVersion(3);
        mGLRender = new GLRender(mGLView, context);

        setContentView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mGLView.onPause();
    }
}