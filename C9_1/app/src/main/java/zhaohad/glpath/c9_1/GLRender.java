package zhaohad.glpath.c9_1;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import zhaohad.glpath.c9_1.gl.ColorProgram;
import zhaohad.glpath.c9_1.obj.Mallet;
import zhaohad.glpath.c9_1.obj.Table;
import zhaohad.glpath.c9_1.obj.TouchP;
import zhaohad.glpath.c9_1.util.MatrixHelper;

public class GLRender implements GLSurfaceView.Renderer {

    private Context mContext;
    private float[] mProjectionMat = new float[4 * 4];
    private float[] mModelMat = new float[4 * 4];

    private Table mTable;
    private Mallet mMallet;
    private TouchP mTouchP;

    private ColorProgram mProgram;
    private ColorProgram mProgramTouchP;

    private float mW;
    private float mH;

    public GLRender(Context context) {
        mContext = context;
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

        MatrixHelper.perspectiveM(mProjectionMat, 45, (float) width / (float) height, 1, 10);

        Matrix.setIdentityM(mModelMat, 0);
        Matrix.translateM(mModelMat, 0, 0, 0, -3f);
        Matrix.rotateM(mModelMat, 0, -60, 1, 0, 0);

        float[] tmpMat = new float[4 * 4];
        Matrix.multiplyMM(tmpMat, 0, mProjectionMat, 0, mModelMat, 0);
        System.arraycopy(tmpMat, 0, mProjectionMat, 0, tmpMat.length);

        mTable.mTableProgram.setUMatrix(mProjectionMat);
        mMallet.mProgram.setUMatrix(mProjectionMat);
        Matrix.setIdentityM(tmpMat, 0);
        // mTouchP.mProgram.setUMatrix(tmpMat);
        mTouchP.mProgram.setUMatrix(mProjectionMat);
        Matrix.invertM(mTouchP.mInvertM, 0, mProjectionMat, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        mTable.draw();
        mMallet.draw();
        mTouchP.draw();
    }

    public void onTouch(float x, float y) {
        float nX = x / mW * 2 - 1;
        float nY = -(y / mH * 2 - 1);
        mTouchP.setPos(nX, nY);
    }
}
