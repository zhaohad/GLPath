package zhaohad.glpath.c10_1;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glEnable;

public class GLRender implements GLSurfaceView.Renderer {

    private Context mContext;
    private ParticleProgram mParticleProgram;
    private float[] mProjectionMat;
    public Fountain mFountain;

    public GLRender(Context context) {
        mContext = context;
        mProjectionMat = new float[4 * 4];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mParticleProgram = new ParticleProgram(mContext);
        mFountain = new Fountain(mParticleProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);

        Utils.perspectiveM(mProjectionMat, 45, (float) width / (float) height, 1, 10);
        float[] tmp = new float[4 * 4];
        Matrix.setIdentityM(tmp, 0);
        Matrix.translateM(tmp, 0, 0, 0, -5);
        float[] tmp1 = new float[4 * 4];
        Matrix.multiplyMM(tmp1, 0, mProjectionMat, 0, tmp, 0);
        System.arraycopy(tmp1, 0, mProjectionMat, 0, tmp1.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        // 需要注意精度，直接用long型转float再进行加减法会出现错误
        float time = (System.currentTimeMillis() % 100000) / 1000f;

        mParticleProgram.setUniform(mProjectionMat, time);
        mFountain.draw();
    }
}
