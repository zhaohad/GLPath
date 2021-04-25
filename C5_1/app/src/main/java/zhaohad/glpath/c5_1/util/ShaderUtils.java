package zhaohad.glpath.c5_1.util;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

public class ShaderUtils {
    public static int compileVShader(String code) {
        return compileShader(GLES30.GL_VERTEX_SHADER, code);
    }

    public static int compileFShader(String code) {
        return compileShader(GLES30.GL_FRAGMENT_SHADER, code);
    }

    private static int compileShader(int type, String code) {
        int sId = GLES30.glCreateShader(type);

        if (sId == 0) {
            Log.e("hanwei", "shader create failed");
            return 0;
        }

        GLES30.glShaderSource(sId, code);
        GLES30.glCompileShader(sId);

        int[] compileRes = new int[1];
        GLES30.glGetShaderiv(sId, GLES20.GL_COMPILE_STATUS, compileRes, 0);
        Log.e("hanwei", "compiling: " + GLES30.glGetShaderInfoLog(sId));

        if (compileRes[0] == 0) {
            GLES30.glDeleteShader(sId);
            Log.e("hanwei", "shaer compile failed");
            return 0;
        }

        return sId;
    }

    public static int linkProgram(int vShaderId, int fShaderId) {
        int pId = GLES30.glCreateProgram();
        if (pId == 0) {
            Log.e("hanwei", "Program create failed");
        }

        GLES30.glAttachShader(pId, vShaderId);
        GLES30.glAttachShader(pId, fShaderId);

        GLES30.glLinkProgram(pId);

        int[] linkRes = new int[1];
        GLES30.glGetProgramiv(pId, GLES20.GL_LINK_STATUS, linkRes, 0);
        Log.e("hanwei", "Linking: " + GLES30.glGetProgramInfoLog(pId));

        if (linkRes[0] == 0) {
            GLES30.glDeleteProgram(pId);
            Log.e("hanwei", "Program link failed");
            return 0;
        }

        return pId;
    }

    public static boolean validateProgram(int pId) {
        GLES30.glValidateProgram(pId);
        int[] vStatus = new int[1];
        GLES30.glGetProgramiv(pId, GLES20.GL_VALIDATE_STATUS, vStatus, 0);
        Log.e("hanwei", "validateProgram status: " + vStatus[0]);

        return vStatus[0] != 0;
    }
}
