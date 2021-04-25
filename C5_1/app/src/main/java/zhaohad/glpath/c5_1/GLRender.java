package zhaohad.glpath.c5_1;

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

import zhaohad.glpath.c5_1.util.FileUtils;
import zhaohad.glpath.c5_1.util.ShaderUtils;

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
    private float[] mOrthoMat = new float[4 * 4];

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

        float ratio = width > height ? (float) width / (float) height : (float) height / (float) width;
        if (width > height) {
            Matrix.orthoM(mOrthoMat, 0, -ratio, ratio, -1, 1, -1, 1);
        } else {
            Matrix.orthoM(mOrthoMat, 0, -1, 1, -ratio, ratio, -1, 1);
        }
        GLES30.glUniformMatrix4fv(muMatrix, 1, false, mOrthoMat, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        GLES30.glDrawArrays(GLES20.GL_LINES, 6, 2);

        GLES30.glDrawArrays(GLES20.GL_POINTS, 8, 2);
    }
}
