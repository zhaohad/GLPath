package zhaohad.glpath.c6_1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import zhaohad.glpath.c6_1.util.MatrixHelper;
import zhaohad.glpath.c6_1.util.ShaderUtils;
import zhaohad.glpath.c6_1.util.FileUtils;

public class GLRender implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;

    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final String U_MATRIX = "u_Matrix";

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;

    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private float[] mVertices;
    private FloatBuffer mVerticesBuf;

    private Context mContext;
    private int mProgramId;
    private int maPosition;
    private int maColor;
    private int muMatrix;
    private float[] mProjectionMat = new float[4 * 4];
    private float[] mModelMat = new float[4 * 4];

    public GLRender(Context context) {
        mContext = context;
        mVertices = new float[] {
                // Triangle Fan
                 0f,    0f,   1f,   1f,   1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                 0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                 0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                 0.5f, 0f, 1f, 0f, 0f,

                // Mallets
                0f, -0.4f, 0f, 0f, 1f,
                0f,  0.4f, 1f, 0f, 0f
        };

        mVerticesBuf = ByteBuffer
                .allocateDirect(mVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVerticesBuf.put(mVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        String vShader = FileUtils.readFromRaw(mContext, R.raw.simple_vertex_shader);
        String fShader = FileUtils.readFromRaw(mContext, R.raw.simple_fragment_shader);

        // 调用glCreateShader之前必须先调用mGLSurfaceView.setEGLContextClientVersion(2);
        // 否则glCreateShader会出现 Fatal signal 11 (SIGSEGV)的native crash
        int vShaderId = ShaderUtils.compileVShader(vShader);
        int fShaderId = ShaderUtils.compileFShader(fShader);

        mProgramId = ShaderUtils.linkProgram(vShaderId, fShaderId);

        ShaderUtils.validateProgram(mProgramId);

        GLES30.glUseProgram(mProgramId);

        maPosition = GLES30.glGetAttribLocation(mProgramId, A_POSITION);
        maColor = GLES30.glGetAttribLocation(mProgramId, A_COLOR);
        muMatrix = GLES30.glGetUniformLocation(mProgramId, U_MATRIX);

        mVerticesBuf.position(0);
        GLES30.glVertexAttribPointer(maPosition, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, STRIDE, mVerticesBuf);
        GLES30.glEnableVertexAttribArray(maPosition);

        mVerticesBuf.position(POSITION_COMPONENT_COUNT);
        GLES30.glVertexAttribPointer(maColor, COLOR_COMPONENT_COUNT, GLES30.GL_FLOAT, false, STRIDE, mVerticesBuf);
        GLES30.glEnableVertexAttribArray(maColor);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(mProjectionMat, 45, (float) width / (float) height, 1, 10);

        // OpenGL向量是竖式，坐标变换是变换矩阵左乘向量，但实际的变换顺序要从右想做看（从后向前看）所以下列变换的实际顺序是：
        // 1. 绕X轴旋转-60°
        // 2. 沿屏幕z轴正向（屏幕指向屏幕外方向）移动2.5
        // 3. 做透视投影变换
        // 注意：物体坐标系（手机坐标）采用的是左手坐标系，OpenGL的坐标系是右手坐标系，所以在第2步向屏幕外移动投影变换后即向屏幕内移动
        // 也是由于左右坐标系的差异，OpenGL的正交投影和透视投影变换与z轴坐标先关的变换都是负数
        Matrix.setIdentityM(mModelMat, 0);
        Matrix.translateM(mModelMat, 0, 0, 0, -2.5f);
        Matrix.rotateM(mModelMat, 0, -60, 1, 0, 0);

        float[] tmpMat = new float[4 * 4];
        Matrix.multiplyMM(tmpMat, 0, mProjectionMat, 0, mModelMat, 0);
        System.arraycopy(tmpMat, 0, mProjectionMat, 0, tmpMat.length);

        GLES30.glUniformMatrix4fv(muMatrix, 1, false, mProjectionMat, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        GLES30.glDrawArrays(GLES20.GL_LINES, 6, 2);

        GLES30.glDrawArrays(GLES20.GL_POINTS, 8, 2);
    }
}
