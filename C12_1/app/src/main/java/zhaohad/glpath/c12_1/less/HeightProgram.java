package zhaohad.glpath.c12_1.less;

import android.content.Context;
import android.opengl.GLES30;

import zhaohad.glpath.c12_1.R;
import zhaohad.glpath.c12_1.Utils;

public class HeightProgram {
    public int mProgramId;

    private int mu_Matrix;
    public int ma_Position;

    public HeightProgram(Context context) {
        mProgramId = Utils.buildProgram(context, R.raw.zhaohad_height_vertex, R.raw.zhaohad_height_fragment);

        mu_Matrix = GLES30.glGetUniformLocation(mProgramId, "u_Matrix");
        ma_Position = GLES30.glGetAttribLocation(mProgramId, "a_Position");
    }

    public void setUniform(float[] mat) {
        GLES30.glUseProgram(mProgramId);
        GLES30.glUniformMatrix4fv(mu_Matrix, 1, false, mat, 0);
    }
}
