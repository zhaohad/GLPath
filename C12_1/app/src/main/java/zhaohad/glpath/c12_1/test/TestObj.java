package zhaohad.glpath.c12_1.test;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import zhaohad.glpath.c12_1.Utils;

public class TestObj {
    private static final int CNT_POS_COMPONENT = 3;
    private float[] mVertices;
    private FloatBuffer mVerticesBuf;

    private TestProgram mProgram;

    public TestObj(TestProgram program) {
        mProgram = program;

        mVertices = new float[] {
                0, 0, -5f
        };

        mVerticesBuf = ByteBuffer
                .allocateDirect(mVertices.length * Utils.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVerticesBuf.put(mVertices);
    }

    private void bindData() {
        mVerticesBuf.rewind();
        GLES30.glVertexAttribPointer(mProgram.ma_Position, CNT_POS_COMPONENT, GLES30.GL_FLOAT, false, 0, mVerticesBuf);
        GLES30.glEnableVertexAttribArray(mProgram.ma_Position);
    }

    public void draw() {
        bindData();
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, 1);
    }
}
