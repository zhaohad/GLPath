package zhaohad.glpath.c8_1.gl;

import android.content.Context;
import android.opengl.GLES30;
import android.util.Log;

import zhaohad.glpath.c8_1.util.ShaderUtils;

public class ShaderProgram {
    protected int mProgramId;

    private int muMatrix;

    public ShaderProgram(Context context, int vertexResId, int fragResId) {
        mProgramId = ShaderUtils.buildProgram(context, vertexResId, fragResId);

        muMatrix = GLES30.glGetUniformLocation(mProgramId, "u_Matrix");
        Log.e("hanwei", "muMatrix = " + muMatrix + " mProgram = " + mProgramId);
    }

    public void useProgram() {
        GLES30.glUseProgram(mProgramId);
    }

    public void setUMatrix(float[] mat) {
        useProgram();
        GLES30.glUniformMatrix4fv(muMatrix, 1, false, mat, 0);
    }
}
