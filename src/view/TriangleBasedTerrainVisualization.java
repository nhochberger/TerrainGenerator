package view;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.texture.TextureCoords;

public class TriangleBasedTerrainVisualization extends TerrainVisualization {

    public TriangleBasedTerrainVisualization() {
        super();
    }

    @Override
    protected void drawSurface(final GL2 gl) {
        gl.glBegin(GL2.GL_TRIANGLES);
        final TextureCoords coords = this.texture.getImageTexCoords();
        for (int z = 0; z < this.points.getZDimension() - 1; z++) {
            for (int x = 0; x < this.points.getXDimension() - 1; x++) {
                final double distance1 = Math.abs(this.points.get(x, z) - this.points.get(x + 1, z + 1));
                final double distance2 = Math.abs(this.points.get(x + 1, z) - this.points.get(x, z + 1));

                if (distance1 < distance2) {
                    gl.glTexCoord2d(coords.bottom(), coords.left());
                    gl.glVertex3d(this.scalingFactor * x, this.points.get(x, z), this.scalingFactor * z);
                    gl.glTexCoord2d(coords.bottom(), coords.right());
                    gl.glVertex3d(this.scalingFactor * (x + 1), this.points.get(x + 1, z + 1), this.scalingFactor * (z + 1));
                    gl.glTexCoord2d(coords.top(), coords.right());
                    gl.glVertex3d(this.scalingFactor * (x + 1), this.points.get(x + 1, z), this.scalingFactor * z);
                    final float[] one1 = { 1, (float) (this.points.get(x, z + 1) - this.points.get(x, z)), 0 };
                    final float[] two1 = { 1, (float) (this.points.get(x + 1, z + 1) - this.points.get(x, z)), 1 };
                    float[] normal1 = new float[3];
                    normal1 = VectorUtil.crossVec3(normal1, two1, one1);
                    gl.glNormal3f(normal1[0], normal1[1], normal1[2]);

                    gl.glTexCoord2d(coords.bottom(), coords.left());
                    gl.glVertex3d(this.scalingFactor * x, this.points.get(x, z), this.scalingFactor * z);
                    gl.glTexCoord2d(coords.bottom(), coords.right());
                    gl.glVertex3d(this.scalingFactor * x, this.points.get(x, z + 1), this.scalingFactor * (z + 1));
                    gl.glTexCoord2d(coords.top(), coords.right());
                    gl.glVertex3d(this.scalingFactor * (x + 1), this.points.get(x + 1, z + 1), this.scalingFactor * (z + 1));
                    final float[] one2 = { 0, (float) (this.points.get(x, z + 1) - this.points.get(x, z)), 1 };
                    final float[] two2 = { 1, (float) (this.points.get(x + 1, z + 1) - this.points.get(x, z)), 1 };
                    float[] normal2 = new float[3];
                    normal2 = VectorUtil.crossVec3(normal2, one2, two2);
                    gl.glNormal3f(normal2[0], normal2[1], normal2[2]);
                } else {
                    gl.glTexCoord2d(coords.top(), coords.right());
                    gl.glVertex3d(this.scalingFactor * (x + 1), this.points.get(x + 1, z + 1), this.scalingFactor * (z + 1));
                    gl.glTexCoord2d(coords.top(), coords.left());
                    gl.glVertex3d(this.scalingFactor * (x + 1), this.points.get(x + 1, z), this.scalingFactor * z);
                    gl.glTexCoord2d(coords.bottom(), coords.right());
                    gl.glVertex3d(this.scalingFactor * x, this.points.get(x, z + 1), this.scalingFactor * (z + 1));
                    final float[] one1 = { -1, (float) (this.points.get(x, z + 1) - this.points.get(x + 1, z + 1)), 0 };
                    final float[] two1 = { 0, (float) (this.points.get(x + 1, z) - this.points.get(x + 1, z + 1)), -1 };
                    float[] normal1 = new float[3];
                    normal1 = VectorUtil.crossVec3(normal1, two1, one1);
                    gl.glNormal3f(normal1[0], normal1[1], normal1[2]);

                    gl.glTexCoord2d(coords.bottom(), coords.left());
                    gl.glVertex3d(this.scalingFactor * x, this.points.get(x, z), this.scalingFactor * z);
                    gl.glTexCoord2d(coords.bottom(), coords.right());
                    gl.glVertex3d(this.scalingFactor * x, this.points.get(x, z + 1), this.scalingFactor * (z + 1));
                    gl.glTexCoord2d(coords.top(), coords.left());
                    gl.glVertex3d(this.scalingFactor * (x + 1), this.points.get(x + 1, z), this.scalingFactor * z);
                    final float[] one2 = { 0, (float) (this.points.get(x, z + 1) - this.points.get(x, z)), 1 };
                    final float[] two2 = { 1, (float) (this.points.get(x + 1, z + 1) - this.points.get(x, z)), 0 };
                    float[] normal2 = new float[3];
                    normal2 = VectorUtil.crossVec3(normal2, one2, two2);
                    gl.glNormal3f(normal2[0], normal2[1], normal2[2]);
                }
            }
        }
        gl.glEnd();
    }

    @Override
    protected void drawWater(final GL2 gl) {
        gl.glPushMatrix();
        final float[] matShininess = { 50.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, FloatBuffer.wrap(matShininess));

        final float[] matAmbient = { 0.0f, 0.0f, 0.8f, 0.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, FloatBuffer.wrap(matAmbient));

        final float[] matDiffuse = { 0.0f, 0.0f, 0.8f, 1.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, FloatBuffer.wrap(matDiffuse));

        final float[] matSpecular = { 0.7f, 0.7f, 1.0f, 1.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, FloatBuffer.wrap(matSpecular));

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3d(0d, 1d, 0d);
        gl.glVertex3d(0d, 1d, this.scalingFactor * (this.points.getXDimension() - 1));
        gl.glVertex3d(this.scalingFactor * (this.points.getZDimension() - 1), 1d, this.scalingFactor * (this.points.getXDimension() - 1d));
        gl.glVertex3d(this.scalingFactor * (this.points.getZDimension() - 1), 1d, 0d);
        gl.glEnd();
        gl.glPopMatrix();
    }
}
