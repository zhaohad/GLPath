package zhaohad.glpath.c10_1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

public class Utils {
    public static final int BYTES_PER_FLOAT = 4;

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

    public static int buildProgram(Context context, int vecResId, int fragResId) {
        String vShader = readFromRaw(context, vecResId);
        String fShader = readFromRaw(context, fragResId);

        int vShaderId = compileVShader(vShader);
        int fShaderId = compileFShader(fShader);

        int program = linkProgram(vShaderId, fShaderId);

        return program;
    }

    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            Log.w("hanwei", "Could not generate a new OpenGL texture object.");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        // Read in the resource
        final Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), resourceId, options);

        if (bitmap == null) {
            Log.w("hanwei", "Resource ID " + resourceId + " could not be decoded.");

            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }
        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        // Set filtering: a default must be set, or the texture will be
        // black.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // Load the bitmap into the bound texture.
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.

        glGenerateMipmap(GL_TEXTURE_2D);

        // Recycle the bitmap, since its data has been loaded into
        // OpenGL.
        bitmap.recycle();

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

    public static String readFromRaw(Context context, int resourceId) {
        StringBuilder body = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not open resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe) {
            throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }

        return body.toString();
    }

    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect,
                                    float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);

        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }

    public static class Point {
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float x;
        public float y;
        public float z;
    }
}
