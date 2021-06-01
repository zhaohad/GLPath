package zhaohad.glpath.c12_1.less;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLES30;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import zhaohad.glpath.c12_1.R;
import zhaohad.glpath.c12_1.Utils;

public class HeightMap {
    private static final int CNT_POSITION_COMPONENT = 3;

    private HeightProgram mProgram;
    private Context mContext;

    private int mVertexId;
    private int mIndexesId;
    private int mCntElements;

    public HeightMap(Context context, HeightProgram program) {
        mProgram = program;
        mContext = context;

        loadHeight();
    }

    private void loadHeight() {
        Bitmap hBmp = ((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.heightmap)).getBitmap();

        if (hBmp != null) {
            int h = hBmp.getHeight();
            int w = hBmp.getWidth();
            FloatBuffer vertexes = ByteBuffer.allocateDirect(h * w * CNT_POSITION_COMPONENT * Utils.BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            int[] pixels = new int[h * w];
            hBmp.getPixels(pixels, 0, w, 0, 0, w, h);
            vertexes.rewind();
            Log.e("hanwei", "w = " + w + " h = " + h);
            for (int i = 0; i < w; ++i) {
                for (int j = 0; j < w; ++j) {
                    float x = i / (w - 1f) - 0.5f;
                    float y = Color.red(pixels[(i * h) + j]) / 255f;
                    float z = j / (h - 1f) - 0.5f;

                    vertexes.put(x);
                    vertexes.put(y);
                    vertexes.put(z);
                    // Log.e("hanwei", "i = " + i + " j = " + j + "      " + x + " " + y + " " + z);
                }
            }

            /*FloatBuffer vertexes = ByteBuffer.allocateDirect(3 * CNT_POSITION_COMPONENT * Utils.BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            vertexes.put(0, -0.5f);
            vertexes.put(1, 0.5137255f);
            vertexes.put(2, -5f);

            vertexes.put(3, 0.5f);
            vertexes.put(4, 0.5f);
            vertexes.put(5, -5f);

            vertexes.put(6, -0.5f);
            vertexes.put(7, -0.5f);
            vertexes.put(8, -5f);*/
            mVertexId = Utils.genVertexBuffer(vertexes);

            mCntElements = (h - 1) * (w - 1) * 2 * 3;
            ShortBuffer indexes = ByteBuffer.allocateDirect(mCntElements * Utils.BYTES_PER_SHORT)
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer();
            indexes.rewind();
            for (int i = 0; i < w - 1; ++i) {
                for (int j = 0; j < h - 1; ++j) {
                    short tl = (short) (i * w + j);
                    short tr = (short) (i * w + j + 1);
                    short bl = (short) ((i + 1) * w + j);
                    short br = (short) ((i + 1) * w + j + 1);

                    indexes.put(tl);
                    indexes.put(bl);
                    indexes.put(tr);

                    indexes.put(tr);
                    indexes.put(bl);
                    indexes.put(br);
                }
            }

            /*ShortBuffer indexes = ByteBuffer.allocateDirect(3 * Utils.BYTES_PER_SHORT)
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer();*/
            indexes.put(0, (short) 0);
            indexes.put(1, (short) 1);
            indexes.put(2, (short) 2);
            mIndexesId = Utils.genIndexesBuffer(indexes);

            hBmp.recycle();
        }
    }

    private void bindData() {
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexId);
        GLES30.glVertexAttribPointer(mProgram.ma_Position, CNT_POSITION_COMPONENT, GLES30.GL_FLOAT, false, 0, 0);
        GLES30.glEnableVertexAttribArray(mProgram.ma_Position);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }

    public void draw() {
        bindData();

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mIndexesId);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, mCntElements, GLES30.GL_UNSIGNED_SHORT, 0);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
