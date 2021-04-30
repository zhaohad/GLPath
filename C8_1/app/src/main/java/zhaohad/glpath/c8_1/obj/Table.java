package zhaohad.glpath.c8_1.obj;

import android.content.Context;
import android.opengl.GLES30;

import zhaohad.glpath.c8_1.R;
import zhaohad.glpath.c8_1.gl.ShaderProgram;
import zhaohad.glpath.c8_1.gl.VertexBuffer;
import zhaohad.glpath.c8_1.util.ShaderUtils;

public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * ShaderUtils.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y, S, T
            // Triangle Fan
             0f,    0f, 0.5f, 0.5f,
            -0.5f, -0.8f,   0f, 0.9f,
             0.5f, -0.8f,   1f, 0.9f,
             0.5f,  0.8f,   1f, 0.1f,
            -0.5f,  0.8f,   0f, 0.1f,
            -0.5f, -0.8f,   0f, 0.9f
    };

    private VertexBuffer mVertexBuffer;
    public TableProgram mTableProgram;

    public Table(Context context) {
        mVertexBuffer = new VertexBuffer(VERTEX_DATA);
        mTableProgram = new TableProgram(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
    }

    private void bindData() {
        mVertexBuffer.setVertexAttrPointer(0, mTableProgram.maPosition, POSITION_COMPONENT_COUNT, STRIDE);
        mVertexBuffer.setVertexAttrPointer(POSITION_COMPONENT_COUNT, mTableProgram.maTextureCoordinates, TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        mTableProgram.useProgram();
        bindData();
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 6);
    }

    public class TableProgram extends ShaderProgram {
        private int maPosition;
        private int maTextureCoordinates;
        private int muTextureUnit;
        private int mTextureId;

        public TableProgram(Context context, int vertexResId, int fragResId) {
            super(context, vertexResId, fragResId);
            muTextureUnit = GLES30.glGetUniformLocation(mProgramId, "u_TextureUnit");
            maPosition = GLES30.glGetAttribLocation(mProgramId, "a_Position");
            maTextureCoordinates = GLES30.glGetAttribLocation(mProgramId, "a_TextureCoordinates");

            mTextureId = ShaderUtils.loadTexture(context, R.drawable.air_hockey_surface);
            // 激活文理单元
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);
            GLES30.glUniform1i(muTextureUnit, 0);
        }
    }
}
