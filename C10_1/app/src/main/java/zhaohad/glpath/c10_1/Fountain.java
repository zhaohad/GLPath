package zhaohad.glpath.c10_1;

import android.opengl.Matrix;

import java.util.Random;

public class Fountain {
    private ParticleSystem mParticleSystem;
    private float[] mDirection = new float[] {0, 0.7f, 0, 0};
    private Random mRandom = new Random();

    public Fountain(ParticleProgram program) {
        mParticleSystem = new ParticleSystem(new Utils.Point(0, -1.5f, 0), program);
    }

    public void update() {
        for (int i = 0; i < 5; ++i) {
            float[] mat = new float[4 * 4];
            Matrix.setRotateEulerM(mat, 0,
                    (mRandom.nextFloat() - 0.5f) * 10,
                    (mRandom.nextFloat() - 0.5f) * 10,
                    (mRandom.nextFloat() - 0.5f) * 10);

            float[] direct = new float[4];
            Matrix.multiplyMV(direct, 0,
                    mat, 0,
                    mDirection, 0);

            float speed = 1 + mRandom.nextFloat();
            for (int j = 0; j < 3; ++j) {
                direct[j] = direct[j] * speed;
            }
            mParticleSystem.updateParticleSystem(direct);
        }
    }

    public void draw() {
        update();
        mParticleSystem.draw();
    }
}
