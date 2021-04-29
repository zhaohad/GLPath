package zhaohad.glpath.c7_1;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import zhaohad.glpath.c7_1.obj.Mallet;
import zhaohad.glpath.c7_1.obj.Table;
import zhaohad.glpath.c7_1.util.MatrixHelper;

public class GLRender implements GLSurfaceView.Renderer {

    private Context mContext;
    private float[] mProjectionMat = new float[4 * 4];
    private float[] mModelMat = new float[4 * 4];

    private Table mTable;
    private Mallet mMallet;

    public GLRender(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mTable = new Table(mContext);
        mMallet = new Mallet(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(mProjectionMat, 45, (float) width / (float) height, 1, 10);

        Matrix.setIdentityM(mModelMat, 0);
        Matrix.translateM(mModelMat, 0, 0, 0, -2.5f);
        Matrix.rotateM(mModelMat, 0, -60, 1, 0, 0);

        float[] tmpMat = new float[4 * 4];
        Matrix.multiplyMM(tmpMat, 0, mProjectionMat, 0, mModelMat, 0);
        System.arraycopy(tmpMat, 0, mProjectionMat, 0, tmpMat.length);

        mTable.mTableProgram.setUMatrix(mProjectionMat);
        mMallet.mMalletProgram.setUMatrix(mProjectionMat);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        mTable.draw();
        mMallet.draw();
    }
}
