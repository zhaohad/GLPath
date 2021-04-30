package zhaohad.glpath.c9_1;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurfaceView;
    private GLRender mGLRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(getBaseContext());
        // setEGLContextClientVersion需要在setRenderer之前调用
        mGLSurfaceView.setEGLContextClientVersion(3);

        mGLRender = new GLRender(getBaseContext());
        mGLSurfaceView.setRenderer(mGLRender);
        mGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                mGLSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mGLRender.onTouch(x, y);
                    }
                });
                return true;
            }
        });

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}