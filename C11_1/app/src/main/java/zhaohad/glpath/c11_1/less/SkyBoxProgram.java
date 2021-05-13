package zhaohad.glpath.c11_1.less;

import android.content.Context;
import android.opengl.GLES30;

import com.particles.android.util.TextureHelper;

import zhaohad.glpath.c11_1.R;
import zhaohad.glpath.c11_1.Utils;

import static android.opengl.GLES20.GL_TEXTURE0;

public class SkyBoxProgram {
    private int mProgramId;
    public int ma_Position;
    private int mu_Matrix;
    private int mu_TextureUnit;

    private final int mSkyBoxTextureId;
    private final int[] RES_CUBE_TEXTURE = new int[] {
            R.drawable.left, R.drawable.right,
            R.drawable.bottom, R.drawable.top,
            R.drawable.front, R.drawable.back
    };

    public SkyBoxProgram(Context context) {
        mProgramId = Utils.buildProgram(context, R.raw.zhaohad_vertext_shader, R.raw.zhaohad_fragment_shader);
        ma_Position = GLES30.glGetAttribLocation(mProgramId, "a_Position");
        mu_Matrix = GLES30.glGetUniformLocation(mProgramId, "u_Matrix");
        mu_TextureUnit = GLES30.glGetUniformLocation(mProgramId, "u_TextureUnit");

        mSkyBoxTextureId = Utils.loadCubeMap(context, RES_CUBE_TEXTURE);
    }

    public void setUniforms(float[] mat) {
        GLES30.glUseProgram(mProgramId);

        GLES30.glUniformMatrix4fv(mu_Matrix, 1, false, mat, 0);

        GLES30.glActiveTexture(GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, mSkyBoxTextureId);
        GLES30.glUniform1i(mu_TextureUnit, 0);
    }
}
