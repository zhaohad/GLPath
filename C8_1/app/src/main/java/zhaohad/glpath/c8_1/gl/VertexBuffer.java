package zhaohad.glpath.c8_1.gl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import zhaohad.glpath.c8_1.util.ShaderUtils;

public class VertexBuffer {
    private FloatBuffer mBuf;

    public VertexBuffer(float[] buf) {
        mBuf = ByteBuffer
                .allocateDirect(buf.length * ShaderUtils.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(buf);
    }

    public void setVertexAttrPointer(int offset, int location, int componentCount, int stride) {
        mBuf.position(offset);
        GLES30.glVertexAttribPointer(location, componentCount, GLES30.GL_FLOAT, false, stride, mBuf);
        GLES30.glEnableVertexAttribArray(location);
        mBuf.position(0);
    }
}
