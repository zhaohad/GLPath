package zhaohad.glpath.c12_1.test;

import android.content.Context;
import android.opengl.GLES30;

import zhaohad.glpath.c12_1.R;
import zhaohad.glpath.c12_1.Utils;

public class TestProgram {
    private int mProgramId;

    private int mu_Matrix;
    public int ma_Position;

    public TestProgram(Context context) {
        mProgramId = Utils.buildProgram(context, R.raw.test_vertex, R.raw.test_fragment);
        mu_Matrix = GLES30.glGetUniformLocation(mProgramId, "u_Matrix");
        ma_Position = GLES30.glGetAttribLocation(mProgramId, "a_Position");
    }

    public void use() {
        GLES30.glUseProgram(mProgramId);
    }

    public void setUniform(float[] mat) {
        use();
        GLES30.glUniformMatrix4fv(mu_Matrix, 1, false, mat, 0);
    }
}
