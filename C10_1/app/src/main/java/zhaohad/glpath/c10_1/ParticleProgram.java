package zhaohad.glpath.c10_1;

import android.content.Context;
import android.opengl.GLES30;

import com.particles.android.util.TextureHelper;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glUniform1i;

public class ParticleProgram {
    public int mProgramId;

    public int mu_Matrix;
    public int mu_Time;
    public int ma_Position;
    public int ma_DirectionVector;
    public int ma_ParticleStartTime;
    public int mu_TextureUnit;

    private int mTexture;

    public ParticleProgram(Context context) {
        mProgramId = Utils.buildProgram(context, R.raw.zhaohad_vertex_shader, R.raw.zhaohad_fragment_shader);

        mu_Matrix = GLES30.glGetUniformLocation(mProgramId, "u_Matrix");
        mu_Time = GLES30.glGetUniformLocation(mProgramId, "u_Time");
        mu_TextureUnit = GLES30.glGetUniformLocation(mProgramId, "u_TextureUnit");
        ma_Position = GLES30.glGetAttribLocation(mProgramId, "a_Position");
        ma_DirectionVector = GLES30.glGetAttribLocation(mProgramId, "a_DirectionVector");
        ma_ParticleStartTime = GLES30.glGetAttribLocation(mProgramId, "a_ParticleStartTime");
        // mTexture = Utils.loadTexture(context, R.drawable.particle_texture);
        mTexture = TextureHelper.loadTexture(context, R.drawable.particle_texture);
    }

    public void use() {
        GLES30.glUseProgram(mProgramId);
    }

    public void setUniform(float[] mat, float time) {
        use();
        GLES30.glUniformMatrix4fv(mu_Matrix, 1, false, mat, 0);
        GLES30.glUniform1f(mu_Time, time);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, mTexture);
        glUniform1i(mu_TextureUnit, 0);
    }
}
