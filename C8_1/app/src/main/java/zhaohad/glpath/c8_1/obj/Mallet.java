package zhaohad.glpath.c8_1.obj;

import zhaohad.glpath.c8_1.gl.Circle;
import zhaohad.glpath.c8_1.gl.ColorProgram;
import zhaohad.glpath.c8_1.gl.Cylinder;
import zhaohad.glpath.c8_1.gl.ModelData;
import zhaohad.glpath.c8_1.gl.Point;

public class Mallet {
    public ColorProgram mProgram;

    private ModelData mData;

    public Mallet(ColorProgram program) {
        mProgram = program;
        mData = new ModelData();
        mData.appendShape(new Cylinder(new Point(0, -0.4f, 0.01f), 0.03f, 0.01f, 50));
        mData.appendShape(new Circle(new Point(0, -0.4f, 0.01f), 0.03f, 50));
        mData.appendShape(new Cylinder(new Point(0, -0.4f, 0.04f), 0.01f, 0.03f, 50));
        mData.appendShape(new Circle(new Point(0, -0.4f, 0.04f), 0.01f, 50));

        mData.appendShape(new Cylinder(new Point(0, 0.4f, 0.01f), 0.03f, 0.01f, 50));
        mData.appendShape(new Circle(new Point(0, 0.4f, 0.01f), 0.03f, 50));
        mData.appendShape(new Cylinder(new Point(0, 0.4f, 0.04f), 0.01f, 0.03f, 50));
        mData.appendShape(new Circle(new Point(0, 0.4f, 0.04f), 0.01f, 50));

        mData.buildBuffer();
        bindData();
    }

    private void bindData() {
        mData.bindData(mProgram.maPosition);
    }

    public void draw() {
        mProgram.useProgram();
        bindData();
        mData.draw(mProgram);
    }
}
