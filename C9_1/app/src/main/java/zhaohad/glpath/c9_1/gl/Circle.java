package zhaohad.glpath.c9_1.gl;

import android.opengl.GLES30;

public class Circle extends Shape {
    public Circle(Point c, float r, int n) {
        mCntPoints = n + 2;
        mPoints = new float[3 * mCntPoints];

        int cnt = 0;
        mPoints[cnt++] = c.x;
        mPoints[cnt++] = c.y;
        mPoints[cnt++] = c.z;

        for (int i = 0; i < n + 1; ++i) {
            double rad = Math.PI * 2 / n * i;
            mPoints[cnt++] = c.x + (float) Math.cos(rad) * r;
            mPoints[cnt++] = c.y + (float) Math.sin(rad) * r;
            mPoints[cnt++] = c.z;
        }
    }

    @Override
    public void draw(int offset) {
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, offset, mCntPoints);
    }
}
