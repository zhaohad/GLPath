package zhaohad.glpath.c9_1.obj;

import android.opengl.GLES30;
import android.opengl.Matrix;

import zhaohad.glpath.c9_1.gl.Circle;
import zhaohad.glpath.c9_1.gl.ColorProgram;
import zhaohad.glpath.c9_1.gl.ModelData;
import zhaohad.glpath.c9_1.gl.Point;
import zhaohad.glpath.c9_1.gl.VertexBuffer;

public class TouchP {
    public ColorProgram mProgram;
    private Point mPos;

    public float[] mInvertM;

    public TouchP(ColorProgram program) {
        mProgram = program;

        mPos = new Point(0, 0, 0);

        mInvertM = new float[4 * 4];
    }

    private void bindData() {
        // float[] pos = new float[]{mPos.x, mPos.y, mPos.z};
        // VertexBuffer buf = new VertexBuffer(pos);
        // buf.setVertexAttrPointer(0, mProgram.maPosition, 3, 0);
    }

    public void draw() {
        mProgram.useProgram();
        // bindData();
        ModelData data = new ModelData();
        data.appendShape(new Circle(mPos, 0.1f, 50));
        data.buildBuffer();
        data.bindData(mProgram.maPosition);
        data.draw(mProgram);
        // GLES30.glDrawArrays(GLES30.GL_POINTS, 0, 1);
    }

    public void setPos(float x, float y) {
        float[] point1 = new float[]{x, y, -1, 1};
        float[] point2 = new float[]{x, y, 1, 1};
        float[] p1 = new float[4];
        float[] p2 = new float[4];
        Matrix.multiplyMV(p1, 0, mInvertM, 0, point1, 0);
        Matrix.multiplyMV(p2, 0, mInvertM, 0, point2, 0);

        float[] faxiangliang = new float[] {0, 0, 1};
        Point pingmianshangyidian = new Point(0, 0, 0);
        Point zhixianshangyidian1 = new Point(p1[0] / p1[3], p1[1] / p1[3], p1[2] / p1[3]);
        Point zhixianshangyidian2 = new Point(p2[0] / p2[3], p2[1] / p2[3], p2[2] / p2[3]);
        Point jiaodian = zhixianyupingmianjiaodian(zhixianshangyidian1, zhixianshangyidian2, pingmianshangyidian, faxiangliang);
        mPos.x = jiaodian.x;
        mPos.y = jiaodian.y;
        mPos.z = jiaodian.z;
    }

    private float dotMultiply(float x1, float y1, float z1, float x2, float y2, float z2) {
        return x1 *x2 + y1 * y2 + z1 * z2;
    }

    private void danweixiangliang(float[] out, float x, float y, float z) {
        float len = Matrix.length(x, y, z);
        out[0] = x / len;
        out[1] = y / len;
        out[2] = z / len;
    }

    /**
     * 求直线与平面的交点
     * @param zhixianshangyidian1 直线上一点1
     * @param zhixianshangyidian2 直线上一点2
     * @param pingmianshangyidian 平面上一点
     * @param pingmiandanweifaxiangliang 平面法向量
     * @return
     */
    private Point zhixianyupingmianjiaodian(Point zhixianshangyidian1, Point zhixianshangyidian2, Point pingmianshangyidian, float[] pingmiandanweifaxiangliang) {
        float[] liangdianxiangliang = new float[] {
                zhixianshangyidian2.x - zhixianshangyidian1.x,
                zhixianshangyidian2.y - zhixianshangyidian1.y,
                zhixianshangyidian2.z - zhixianshangyidian1.z
        };
        danweixiangliang(liangdianxiangliang, liangdianxiangliang[0], liangdianxiangliang[1], liangdianxiangliang[2]);
        // 《3D数学基础：图形与游戏开发》269页，求直线与平面交点
        float d = dotMultiply(pingmianshangyidian.x, pingmianshangyidian.y, pingmianshangyidian.z,
                pingmiandanweifaxiangliang[0], pingmiandanweifaxiangliang[1], pingmiandanweifaxiangliang[2]);
        float p0n = dotMultiply(zhixianshangyidian1.x, zhixianshangyidian1.y, zhixianshangyidian1.z,
                pingmiandanweifaxiangliang[0], pingmiandanweifaxiangliang[1], pingmiandanweifaxiangliang[2]);
        float dn = dotMultiply(liangdianxiangliang[0], liangdianxiangliang[1], liangdianxiangliang[2],
                pingmiandanweifaxiangliang[0], pingmiandanweifaxiangliang[1], pingmiandanweifaxiangliang[2]);
        float t = (d - p0n) / dn;
        Point jiaodian = new Point(zhixianshangyidian1.x + liangdianxiangliang[0] * t, zhixianshangyidian1.y + liangdianxiangliang[1] * t, zhixianshangyidian1.z + liangdianxiangliang[2] * t);
        return jiaodian;
    }
}
