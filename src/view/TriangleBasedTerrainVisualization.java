package view;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.VectorUtil;

public class TriangleBasedTerrainVisualization extends TerrainVisualization {

    public TriangleBasedTerrainVisualization() {
        super();
    }

    @Override
    protected void drawSurface(final GL2 gl) {
        gl.glBegin(GL2.GL_TRIANGLES);
        for (int z = 0; z < points().getDimension() - 1; z++) {
            for (int x = 0; x < this.points().getDimension() - 1; x++) {
                gl.glVertex3d(x, this.points().get(x, z), z);
                gl.glVertex3d(x + 1, this.points().get(x + 1, z + 1), z + 1);
                gl.glVertex3d(x + 1, this.points().get(x + 1, z), z);
                final float[] one1 = { 1, (float) (this.points().get(x, z + 1) - this.points().get(x, z)), 0 };
                final float[] two1 = { 1, (float) (this.points().get(x + 1, z + 1) - this.points().get(x, z)), 1 };
                float[] normal1 = new float[3];
                normal1 = VectorUtil.crossVec3(normal1, two1, one1);
                gl.glNormal3f(normal1[0], normal1[1], normal1[2]);

                gl.glVertex3d(x, this.points().get(x, z), z);
                gl.glVertex3d(x, this.points().get(x, z + 1), z + 1);
                gl.glVertex3d(x + 1, this.points().get(x + 1, z + 1), z + 1);
                final float[] one2 = { 0, (float) (this.points().get(x, z + 1) - this.points().get(x, z)), 1 };
                final float[] two2 = { 1, (float) (this.points().get(x + 1, z + 1) - this.points().get(x, z)), 1 };
                float[] normal2 = new float[3];
                normal2 = VectorUtil.crossVec3(normal2, one2, two2);
                gl.glNormal3f(normal2[0], normal2[1], normal2[2]);
            }
        }
        gl.glEnd();
    }

}
