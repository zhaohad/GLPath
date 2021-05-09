package zhaohad.glpath.c10_1;

import android.opengl.GLES30;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

public class ParticleSystem {
    private final int CNT_PARTICLES = 1000;

    private final int CNT_POSITION = 3;
    private final int CNT_DIRECTION_VECTOR = 3;
    private final int CNT_PARTICLE_START_TIME = 1;

    private final int CNT_COMPONENT = CNT_POSITION + CNT_DIRECTION_VECTOR + CNT_PARTICLE_START_TIME;
    private final int STRIDE = CNT_COMPONENT * Utils.BYTES_PER_FLOAT;

    private FloatBuffer mBuffer;
    private ParticleProgram mProgram;
    private Utils.Point mPos;
    private int mCurParticle;
    private int mCntParticles;

    private Random mRandom = new Random();

    public ParticleSystem(Utils.Point pos, ParticleProgram program) {
        mPos = pos;
        mProgram = program;
        mCurParticle = 0;

        mBuffer = ByteBuffer
                .allocateDirect((CNT_COMPONENT * CNT_PARTICLES) * Utils.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
    }

    public void bindData() {
        mProgram.use();
        int offset = 0;
        mBuffer.position(offset);
        GLES30.glVertexAttribPointer(mProgram.ma_Position, CNT_POSITION, GLES30.GL_FLOAT, false, STRIDE, mBuffer);
        GLES30.glEnableVertexAttribArray(mProgram.ma_Position);
        offset += CNT_POSITION;

        mBuffer.position(offset);
        GLES30.glVertexAttribPointer(mProgram.ma_DirectionVector, CNT_DIRECTION_VECTOR, GLES30.GL_FLOAT, false, STRIDE, mBuffer);
        GLES30.glEnableVertexAttribArray(mProgram.ma_DirectionVector);
        offset += CNT_DIRECTION_VECTOR;

        mBuffer.position(offset);
        GLES30.glVertexAttribPointer(mProgram.ma_ParticleStartTime, CNT_PARTICLE_START_TIME, GLES30.GL_FLOAT, false, STRIDE, mBuffer);
        GLES30.glEnableVertexAttribArray(mProgram.ma_ParticleStartTime);
        offset += CNT_PARTICLE_START_TIME;

        mBuffer.position(0);
    }

    public void updateParticleSystem(float[] dircetionVec) {
        int cur = mCurParticle * CNT_COMPONENT;
        float startTime = (System.currentTimeMillis() % 100000) / 1000f;

        mBuffer.put(cur, mPos.x);
        mBuffer.put(cur + 1, mPos.y);
        mBuffer.put(cur + 2, mPos.z);

        mBuffer.put(cur + 3, dircetionVec[0]);
        mBuffer.put(cur + 4, dircetionVec[1]);
        mBuffer.put(cur + 5, dircetionVec[2]);

        mBuffer.put(cur + 6, startTime);

        mCurParticle++;
        if (mCurParticle >= CNT_PARTICLES) {
            mCurParticle = 0;
        }

        mCntParticles = mCurParticle > mCntParticles ? mCurParticle : mCntParticles;
    }

    public void draw() {
        mProgram.use();
        bindData();
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, mCntParticles);
    }
}
