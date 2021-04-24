package zhaohad.glpath.c2_1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import util.FileUtils;
import util.ShaderUtils;

public class GLRender implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final int POSITION_COMPONENT_COUNT = 2;

    private float[] mVertices;
    private FloatBuffer mVerticesBuf;

    private Context mContext;
    private int mProgramId;
    private int muColor;
    private int maPosition;

    public GLRender(Context context) {
        mContext = context;
        mVertices = new float[] {
                // Triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,

                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f, 0.25f
        };

        mVerticesBuf = ByteBuffer
                .allocateDirect(mVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVerticesBuf.put(mVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vShader = FileUtils.readFromRaw(mContext, R.raw.simple_vertex_shader);
        String fShader = FileUtils.readFromRaw(mContext, R.raw.simple_fragment_shader);

        // 调用glCreateShader之前必须先调用mGLSurfaceView.setEGLContextClientVersion(2);
        // 否则glCreateShader会出现 Fatal signal 11 (SIGSEGV)的native crash
        int vShaderId = ShaderUtils.compileVShader(vShader);
        int fShaderId = ShaderUtils.compileFShader(fShader);

        mProgramId = ShaderUtils.linkProgram(vShaderId, fShaderId);

        ShaderUtils.validateProgram(mProgramId);

        GLES30.glUseProgram(mProgramId);

        muColor = GLES30.glGetUniformLocation(mProgramId, U_COLOR);
        maPosition = GLES30.glGetAttribLocation(mProgramId, A_POSITION);

        mVerticesBuf.position(0);
        GLES30.glVertexAttribPointer(maPosition, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, mVerticesBuf);
        GLES30.glEnableVertexAttribArray(maPosition);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glUniform4f(muColor, 1, 1, 1, 1);
        GLES30.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        GLES30.glUniform4f(muColor, 1, 0, 0, 1);
        GLES30.glDrawArrays(GLES20.GL_LINES, 6, 2);

        GLES30.glUniform4f(muColor, 0, 0, 1, 1);
        GLES30.glDrawArrays(GLES20.GL_POINTS, 8, 2);
    }
}
