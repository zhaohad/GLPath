package zhaohad.glpath.c8_1.gl;

import android.util.Log;

import java.util.ArrayList;

public class ModelData {
    ArrayList<Shape> mShapes;
    private VertexBuffer mBuffer;

    public ModelData() {
        mShapes = new ArrayList<>();
    }

    public void appendShape(Shape c) {
        mShapes.add(c);
    }

    public void buildBuffer() {
        int len = 0;
        for (Shape s : mShapes) {
            len += s.mPoints.length;
        }

        float[] buf = new float[len];
        len = 0;
        for (Shape s : mShapes) {
            System.arraycopy(s.mPoints, 0, buf, len, s.mPoints.length);
            len += s.mPoints.length;
        }
        mBuffer = new VertexBuffer(buf);
    }

    public void bindData(int location) {
        mBuffer.setVertexAttrPointer(0, location, 3, 0);
    }

    public void draw(ColorProgram program) {
        int offset = 0;
        float[] colors = {0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1};
        int i = 0;
        for (Shape s : mShapes) {
            program.setColor(colors[i], colors[i + 1], colors[i + 2]);
            s.draw(offset);
            offset += s.mCntPoints;
            i = (i + 3) % 18;
        }
    }
}
