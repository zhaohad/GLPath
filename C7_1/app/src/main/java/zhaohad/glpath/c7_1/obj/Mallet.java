package zhaohad.glpath.c7_1.obj;

import android.content.Context;
import android.opengl.GLES30;

import zhaohad.glpath.c7_1.R;
import zhaohad.glpath.c7_1.gl.ShaderProgram;
import zhaohad.glpath.c7_1.gl.VertexBuffer;
import zhaohad.glpath.c7_1.util.ShaderUtils;

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * ShaderUtils.BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y, R, G, B
            0f, -0.4f, 0f, 0f, 1f,
            0f,  0.4f, 1f, 0f, 0f };

    private VertexBuffer mVertexBuffer;
    public MalletProgram mMalletProgram;

    public Mallet(Context context) {
        mVertexBuffer = new VertexBuffer(VERTEX_DATA);
        mMalletProgram = new MalletProgram(context);
    }

    private void bindData() {
        mVertexBuffer.setVertexAttrPointer(0, mMalletProgram.maPosition, POSITION_COMPONENT_COUNT, STRIDE);
        mVertexBuffer.setVertexAttrPointer(POSITION_COMPONENT_COUNT, mMalletProgram.maColor, COLOR_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        mMalletProgram.useProgram();
        bindData();
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, 2);
    }

    public class MalletProgram extends ShaderProgram {
        private int maPosition;
        private int maColor;
        public MalletProgram(Context context) {
            super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

            maPosition = GLES30.glGetAttribLocation(mProgramId, "a_Position");
            maColor = GLES30.glGetAttribLocation(mProgramId, "a_Color");
        }
    }
}
