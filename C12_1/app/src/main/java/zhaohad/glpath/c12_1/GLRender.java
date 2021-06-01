package zhaohad.glpath.c12_1;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import zhaohad.glpath.c12_1.less.HeightMap;
import zhaohad.glpath.c12_1.less.HeightProgram;
import zhaohad.glpath.c12_1.less_last.Fountain;
import zhaohad.glpath.c12_1.less_last.ParticleProgram;
import zhaohad.glpath.c12_1.less_last.SkyBoxObj;
import zhaohad.glpath.c12_1.less_last.SkyBoxProgram;
import zhaohad.glpath.c12_1.test.TestObj;
import zhaohad.glpath.c12_1.test.TestProgram;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class GLRender implements GLSurfaceView.Renderer {
    private Context mContext;
    private GLSurfaceView mView;

    private SkyBoxProgram mSkyBoxProgram;
    private SkyBoxObj mSkyBoxObj;
    private Fountain mFountain;
    private ParticleProgram mParticleProgram;
    private HeightProgram mHeightProgram;
    private HeightMap mHeightMap;

    private float[] mProjectionMat;
    private float[] mModelMat;
    private float[] mMat;

    private float mRotationX;
    private float mRotationY;

    private TestProgram mTestProgram;
    private TestObj mTestObj;

    public GLRender(GLSurfaceView glView, Context context) {
        mContext = context;
        mView = glView;
        mView.setRenderer(this);
        mView.setOnTouchListener(mOnTouchListener);

        mProjectionMat = new float[4 * 4];
        mModelMat = new float[4 * 4];
        mMat = new float[4 * 4];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glEnable(GL_DEPTH_TEST);

        mSkyBoxProgram = new SkyBoxProgram(mContext);
        mSkyBoxObj = new SkyBoxObj(mSkyBoxProgram);

        mParticleProgram = new ParticleProgram(mContext);
        mFountain = new Fountain(mParticleProgram);

        mHeightProgram = new HeightProgram(mContext);
        mHeightMap = new HeightMap(mContext, mHeightProgram);

        mTestProgram = new TestProgram(mContext);
        mTestObj = new TestObj(mTestProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        Utils.perspectiveM(mProjectionMat, 45, (float) width / (float) height, 1f, 100);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        drawHeightMap();

        drawSkyBox();

        drawFountain();

        /*mTestProgram.setUniform(mMat);
        mTestObj.draw();*/
    }

    private void drawHeightMap() {
        Matrix.setIdentityM(mModelMat, 0);
        Matrix.rotateM(mModelMat, 0, -mRotationY, 1, 0, 0);
        Matrix.rotateM(mModelMat, 0, -mRotationX, 0, 1, 0);
        Matrix.translateM(mModelMat, 0, 0, -1.5f, -5f);
        Matrix.scaleM(mModelMat, 0, 100, 10, 100);
        Matrix.multiplyMM(mMat, 0, mProjectionMat, 0, mModelMat, 0);

        mHeightProgram.setUniform(mMat);
        mHeightMap.draw();

    }

    private void drawSkyBox() {
        Matrix.setIdentityM(mModelMat, 0);
        Matrix.rotateM(mModelMat, 0, -mRotationY, 1, 0, 0);
        Matrix.rotateM(mModelMat, 0, -mRotationX, 0, 1, 0);
        Matrix.multiplyMM(mMat, 0, mProjectionMat, 0, mModelMat, 0);
        mSkyBoxProgram.setUniforms(mMat);
        mSkyBoxObj.draw();
    }

    private void drawFountain() {
        Matrix.setIdentityM(mModelMat, 0);
        Matrix.rotateM(mModelMat, 0, -mRotationY, 1, 0, 0);
        Matrix.rotateM(mModelMat, 0, -mRotationX, 0, 1, 0);
        Matrix.translateM(mModelMat, 0, 0, -1.5f, -5f);
        Matrix.multiplyMM(mMat, 0, mProjectionMat, 0, mModelMat, 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        GLES30.glDepthMask(false);
        float time = (System.currentTimeMillis() % 100000) / 1000f;
        mParticleProgram.setUniform(mMat, time);
        mFountain.draw();
        GLES30.glDepthMask(true);
        glDisable(GL_BLEND);
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
        Log.e("hanwei", "mRotationY = " + mRotationY + " mRotationX = " + mRotationX);
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
