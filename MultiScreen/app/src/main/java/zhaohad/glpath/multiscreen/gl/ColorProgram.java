package zhaohad.glpath.multiscreen.gl;

import android.content.Context;
import android.opengl.GLES30;

import zhaohad.glpath.multiscreen.R;

public class ColorProgram extends ShaderProgram {
    public int maPosition;
    private int muColor;
    public ColorProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        maPosition = GLES30.glGetAttribLocation(mProgramId, "a_Position");
        muColor = GLES30.glGetUniformLocation(mProgramId, "u_Color");

        setColor(1, 0, 0);
    }

    public void setColor(float r, float g, float b) {
        useProgram();
        GLES30.glUniform4f(muColor, r, g, b, 1);
    }
}