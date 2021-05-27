package zhaohad.glpath.c11_1.less;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import zhaohad.glpath.c11_1.Utils;

public class SkyBoxObj {
    private SkyBoxProgram mProgram;
    private FloatBuffer mVertexes;
    private ByteBuffer mIndexes;

    private static final int CNT_COMPONENTS_PER_VERTEX = 3;
    private static final float CUBE_LEN = 1f;

    private static final float[] CUBE_VERTEXES = new float[] {
            -1 * CUBE_LEN,  1 * CUBE_LEN,  1 * CUBE_LEN,     // (0) Top-left near
             1 * CUBE_LEN,  1 * CUBE_LEN,  1 * CUBE_LEN,     // (1) Top-right near
            -1 * CUBE_LEN, -1 * CUBE_LEN,  1 * CUBE_LEN,     // (2) Bottom-left near
             1 * CUBE_LEN, -1 * CUBE_LEN,  1 * CUBE_LEN,     // (3) Bottom-right near
            -1 * CUBE_LEN,  1 * CUBE_LEN, -1 * CUBE_LEN,     // (4) Top-left far
             1 * CUBE_LEN,  1 * CUBE_LEN, -1 * CUBE_LEN,     // (5) Top-right far
            -1 * CUBE_LEN, -1 * CUBE_LEN, -1 * CUBE_LEN,     // (6) Bottom-left far
             1 * CUBE_LEN, -1 * CUBE_LEN, -1 * CUBE_LEN      // (7) Bottom-right far
    };

    private static final byte[] CUBE_INDEXES = new byte[] {
            // Front
            1, 3, 0,
            0, 3, 2,

            // Back
            4, 6, 5,
            5, 6, 7,

            // Left
            0, 2, 4,
            4, 2, 6,

            // Right
            5, 7, 1,
            1, 7, 3,

            // Top
            5, 1, 4,
            4, 1, 0,

            // Bottom
            6, 2, 7,
            7, 2, 3
    };

    public SkyBoxObj(SkyBoxProgram program) {
        mVertexes = ByteBuffer
                .allocateDirect(CUBE_VERTEXES.length * Utils.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(CUBE_VERTEXES);
        mProgram = program;

        mIndexes = ByteBuffer.allocateDirect(CUBE_INDEXES.length)
                .order(ByteOrder.nativeOrder())
                .put(CUBE_INDEXES);
        /*mIndexes = ByteBuffer.allocateDirect(POINT_INDEXES.length)
                .order(ByteOrder.nativeOrder())
                .put(POINT_INDEXES);*/
        mIndexes.rewind();

        /*mLines = ByteBuffer.allocateDirect(LINE_INDEXES.length)
                .order(ByteOrder.nativeOrder())
                .put(LINE_INDEXES);
        mLines.rewind();*/
    }

    public void bindData() {
        mVertexes.rewind();
        GLES30.glVertexAttribPointer(mProgram.ma_Position, CNT_COMPONENTS_PER_VERTEX, GLES30.GL_FLOAT, false, 0, mVertexes);
        GLES30.glEnableVertexAttribArray(mProgram.ma_Position);
        mVertexes.rewind();
    }

    public void draw() {
        bindData();
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 12 * 3, GLES30.GL_UNSIGNED_BYTE, mIndexes);
        // GLES30.glDrawElements(GLES30.GL_POINTS, 8, GLES30.GL_UNSIGNED_BYTE, mIndexes);
        // GLES30.glDrawElements(GLES30.GL_LINES, 12 * 2, GLES30.GL_UNSIGNED_BYTE, mLines);
    }

    private static final byte[] POINT_INDEXES = new byte[] {
            0, 1, 2, 3, 4, 5, 6, 7
    };

    private static final byte[] LINE_INDEXES = new byte[] {
            0, 1,
            1, 3,
            3, 2,
            0, 2,
            4, 5,
            5, 7,
            7, 6,
            4, 6,
            0, 4,
            1, 5,
            2, 6,
            3, 7
    };
}
