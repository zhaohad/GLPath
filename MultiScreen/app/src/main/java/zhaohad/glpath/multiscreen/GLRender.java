package zhaohad.glpath.multiscreen;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import zhaohad.glpath.multiscreen.gl.ColorProgram;
import zhaohad.glpath.multiscreen.obj.Mallet;
import zhaohad.glpath.multiscreen.obj.Table;
import zhaohad.glpath.multiscreen.obj.TouchP;
import zhaohad.glpath.multiscreen.util.MatrixHelper;

public class GLRender implements GLSurfaceView.Renderer {
    private boolean mIsLeft;

    private Context mContext;
    private float[] mProjectionMat = new float[4 * 4];
    private float[] mViewMat = new float[4 * 4];
    private float[] mTransMat = new float[4* 4];

    private Table mTable;
    private Mallet mMallet;
    private TouchP mTouchP;
    private float mEyeX;

    private ColorProgram mProgram;
    private ColorProgram mProgramTouchP;

    private float mW;
    private float mH;

    public GLRender(Context context, boolean left) {
        mContext = context;
        mIsLeft = left;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mProgram = new ColorProgram(mContext);
        mProgramTouchP = new ColorProgram(mContext);

        mTable = new Table(mContext);
        mMallet = new Mallet(mProgram);
        mTouchP = new TouchP(mProgramTouchP);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        mW = width;
        mH = height;
        GLES30.glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(mProjectionMat, 45, (float) width / (float) height, 1, 100);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        calculateTransMat();

        mTable.draw();
        mMallet.draw();
        mTouchP.draw();
    }

    float mA = 0;
    public void calculateTransMat() {
        float flag = 0.1f;
        float eyeX = mIsLeft ? -1 * flag : 1 * flag;
        float[] tmpMat = new float[16];
        Matrix.setIdentityM(tmpMat, 0);
        Matrix.rotateM(tmpMat, 0, mA, 0, 1, 0);
        float[] viewMat = new float[16];
        Matrix.setLookAtM(viewMat, 0, eyeX, -2, 1, mEyeX, 0, 0, 0, 0, 1);
        Matrix.multiplyMM(mViewMat, 0, tmpMat, 0, viewMat, 0);
        /*Matrix.setLookAtM(mViewMat, 0, eyeX, -2, 1, mEyeX, 0, 0, 0, 0, 1);
        Matrix.rotateM(mViewMat, 0, mA, 0, 0, 1);*/
        mA += 1;
        // mEyeX += 0.001f;
        // mEyeX += 1f;
        for (int i = 0; i < 4; ++i) {
            String s = "";
            for (int j = 0; j < 4; ++j) {
                s += mViewMat[i * 4 + j] + ", ";
            }
            Log.e("hanweimm", s);
        }

        /*Matrix.setIdentityM(mModelMat, 0);
        Matrix.translateM(mModelMat, 0, 0, 0, -3f);
        Matrix.rotateM(mModelMat, 0, -60, 1, 0, 0);*/

        Matrix.multiplyMM(mTransMat, 0, mProjectionMat, 0, mViewMat, 0);

        mTable.mTableProgram.setUMatrix(mTransMat);
        mMallet.mProgram.setUMatrix(mTransMat);
        mTouchP.mProgram.setUMatrix(mTransMat);
        Matrix.invertM(mTouchP.mInvertM, 0, mTransMat, 0);
    }

    public void onTouch(float x, float y) {
        float nX = x / mW * 2 - 1;
        float nY = -(y / mH * 2 - 1);
        mTouchP.setPos(nX, nY);
    }
}
