package zhaohad.glpath.c11_1;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import zhaohad.glpath.c11_1.less.SkyBoxObj;
import zhaohad.glpath.c11_1.less.SkyBoxProgram;

public class GLRender implements GLSurfaceView.Renderer {
    private SkyBoxProgram mSkyBoxProgram;
    private SkyBoxObj mSkyBoxObj;

    private Context mContext;
    private GLSurfaceView mView;

    private float[] mProjectionMat;
    private float[] mModelMat;
    private float[] mMat;

    private float mRotationX;
    private float mRotationY;

    public GLRender(GLSurfaceView view, Context context) {
        mView = view;
        mView.setRenderer(this);
        mView.setOnTouchListener(mOnTouchListener);

        mContext = context;
        mProjectionMat = new float[4 * 4];
        mModelMat = new float[4 * 4];
        mMat = new float[4 * 4];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mSkyBoxProgram = new SkyBoxProgram(mContext);
        mSkyBoxObj = new SkyBoxObj(mSkyBoxProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

        Utils.perspectiveM(mProjectionMat, 45, (float) width / (float) height, 1f, 10);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        Matrix.setIdentityM(mModelMat, 0);
        // Matrix.translateM(mModelMat, 0, 0, 0, -5);
        Matrix.rotateM(mModelMat, 0, -mRotationX, 0, 1, 0);
        Matrix.rotateM(mModelMat, 0, -mRotationY, 1, 0, 0);
        Matrix.multiplyMM(mMat, 0, mProjectionMat, 0, mModelMat, 0);
        mSkyBoxProgram.setUniforms(mMat);
        mSkyBoxObj.draw();
    }

    private void handDrag(float dx, float dy) {
        float factor = 16;
        mRotationX += dx / factor;
        mRotationY += dy / factor;

        if (mRotationY < -90) {
            mRotationY = -90;
        } else if (mRotationY > 90) {
            mRotationY = 90;
        }
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        private float mPreX;
        private float mPreY;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event != null) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mPreX = event.getX();
                        mPreY = event.getY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        float x = event.getX();
                        float y = event.getY();
                        float dX = x - mPreX;
                        float dY = y - mPreY;
                        mPreX = x;
                        mPreY = y;
                        mView.queueEvent(() -> {
                            handDrag(dX, dY);
                        });
                        break;
                    }
                }
            }
            return true;
        }
    };
}
