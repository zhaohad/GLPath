package zhaohad.glpath.c10_1;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mGLView;
    private GLRender mGLRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new GLSurfaceView(getBaseContext());
        mGLView.setEGLContextClientVersion(3);

        mGLRender = new GLRender(getBaseContext());
        mGLView.setRenderer(mGLRender);

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