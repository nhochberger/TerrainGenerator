package view;

import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.TextureCoords;

import model.Boulder;
import model.SurfaceMap;

public class TriangleBasedTerrainVisualization extends TerrainVisualization {

    private float[][][][] normals;
    private float[][][] vertexNormals;

    public TriangleBasedTerrainVisualization() {
        super();
        this.normals = new float[0][0][0][0];
        this.vertexNormals = new float[0][0][0];
    }

    @Override
    protected void drawSurface(final GL2 gl) {
        gl.glBegin(GL2.GL_TRIANGLES);
        final TextureCoords coords = this.texture.getImageTexCoords();
        for (int z = 1; z < this.points.getZDimension() - 2; z++) {
            for (int x = 1; x < this.points.getXDimension() - 2; x++) {

                gl.glTexCoord2d(coords.bottom(), coords.left());
                gl.glNormal3f(this.vertexNormals[x][z][0], this.vertexNormals[x][z][1], this.vertexNormals[x][z][2]);
                gl.glVertex3d(this.scalingFactor * x, this.points.get(x, z), this.scalingFactor * z);
                gl.glTexCoord2d(coords.bottom(), coords.right());
                gl.glNormal3f(this.vertexNormals[x + 1][z + 1][0], this.vertexNormals[x + 1][z + 1][1], this.vertexNormals[x + 1][z + 1][2]);
                gl.glVertex3d(this.scalingFactor * (x + 1), this.points.get(x + 1, z + 1), this.scalingFactor * (z + 1));
                gl.glTexCoord2d(coords.top(), coords.right());
                gl.glNormal3f(this.vertexNormals[x + 1][z][0], this.vertexNormals[x + 1][z][1], this.vertexNormals[x + 1][z][2]);
                gl.glVertex3d(this.scalingFactor * (x + 1), this.points.get(x + 1, z), this.scalingFactor * z);

                gl.glTexCoord2d(coords.bottom(), coords.left());
                gl.glNormal3f(this.vertexNormals[x][z][0], this.vertexNormals[x][z][1], this.vertexNormals[x][z][2]);
                gl.glVertex3d(this.scalingFactor * x, this.points.get(x, z), this.scalingFactor * z);
                gl.glTexCoord2d(coords.bottom(), coords.right());
                gl.glNormal3f(this.vertexNormals[x][z + 1][0], this.vertexNormals[x][z + 1][1], this.vertexNormals[x][z + 1][2]);
                gl.glVertex3d(this.scalingFactor * x, this.points.get(x, z + 1), this.scalingFactor * (z + 1));
                gl.glTexCoord2d(coords.top(), coords.right());
                gl.glNormal3f(this.vertexNormals[x + 1][z + 1][0], this.vertexNormals[x + 1][z + 1][1], this.vertexNormals[x + 1][z + 1][2]);
                gl.glVertex3d(this.scalingFactor * (x + 1), this.points.get(x + 1, z + 1), this.scalingFactor * (z + 1));
            }
        }
        gl.glEnd();
        final GLUT glut = new GLUT();
        final List<Boulder> boulders = this.points.getBoulders();
        for (final Boulder boulder : boulders) {
            gl.glPushMatrix();
            gl.glTranslated(boulder.getX(), boulder.getY(), boulder.getZ());
            glut.glutSolidSphere(boulder.getRadius(), 5, 5);
            gl.glPopMatrix();
        }
    }

    @Override
    public void setPoints(final SurfaceMap points) {
        super.setPoints(points);
        calculateNormals();
    }

    protected void calculateNormals() {
        this.normals = new float[this.points.getXDimension()][this.points.getZDimension()][2][3];
        this.vertexNormals = new float[this.points.getXDimension()][this.points.getZDimension()][3];
        for (int z = 0; z < this.points.getZDimension() - 1; z++) {
            for (int x = 0; x < this.points.getXDimension() - 1; x++) {
                final float[] one1 = { 1, (float) (this.points.get(x, z + 1) - this.points.get(x, z)), 0 };
                final float[] two1 = { 1, (float) (this.points.get(x + 1, z + 1) - this.points.get(x, z)), 1 };
                float[] normal1 = new float[3];
                normal1 = VectorUtil.crossVec3(normal1, two1, one1);
                final float[] one2 = { 0, (float) (this.points.get(x, z + 1) - this.points.get(x, z)), 1 };
                final float[] two2 = { 1, (float) (this.points.get(x + 1, z + 1) - this.points.get(x, z)), 1 };
                float[] normal2 = new float[3];
                normal2 = VectorUtil.crossVec3(normal2, one2, two2);
                this.normals[x][z][0] = normal1;
                this.normals[x][z][1] = normal2;
            }
        }
        for (int z = 0; z < this.points.getZDimension(); z++) {
            for (int x = 0; x < this.points.getXDimension(); x++) {
                calculateFor(x, z);
            }
        }

    }

    protected void calculateFor(final int x, final int z) {
        if (0 >= x || 0 >= z || x >= this.points.getXDimension() || z >= this.points.getZDimension()) {
            return;
        }
        this.vertexNormals[x][z][0] = (this.normals[x][z][0][0] + this.normals[x][z][1][0] + this.normals[x - 1][z - 1][0][0] + this.normals[x - 1][z - 1][1][0] + this.normals[x][z - 1][1][0]
                + this.normals[x - 1][z][0][0]) / 6f;

        this.vertexNormals[x][z][1] = (this.normals[x][z][0][1] + this.normals[x][z][1][1] + this.normals[x - 1][z - 1][0][1] + this.normals[x - 1][z - 1][1][1] + this.normals[x][z - 1][1][1]
                + this.normals[x - 1][z][0][1]) / 6f;

        this.vertexNormals[x][z][2] = (this.normals[x][z][0][2] + this.normals[x][z][1][2] + this.normals[x - 1][z - 1][0][2] + this.normals[x - 1][z - 1][1][2] + this.normals[x][z - 1][1][2]
                + this.normals[x - 1][z][0][2]) / 6f;

        final float sum = this.vertexNormals[x][z][0] + this.vertexNormals[x][z][1] + this.vertexNormals[x][z][2];
        this.vertexNormals[x][z][0] *= Math.sqrt(sum);
        this.vertexNormals[x][z][1] *= Math.sqrt(sum);
        this.vertexNormals[x][z][2] *= Math.sqrt(sum);
    }
    //
    // @Override
    // protected void drawWater(final GL2 gl) {
    // gl.glPushMatrix();
    // final float[] matShininess = { 50.0f };
    // gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, FloatBuffer.wrap(matShininess));
    //
    // final float[] matAmbient = { 0.0f, 0.0f, 0.8f, 0.0f };
    // gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, FloatBuffer.wrap(matAmbient));
    //
    // final float[] matDiffuse = { 0.0f, 0.0f, 0.8f, 1.0f };
    // gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, FloatBuffer.wrap(matDiffuse));
    //
    // final float[] matSpecular = { 0.7f, 0.7f, 1.0f, 1.0f };
    // gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, FloatBuffer.wrap(matSpecular));
    //
    // gl.glBegin(GL2.GL_QUADS);
    // gl.glVertex3d(0d, 1d, 0d);
    // gl.glVertex3d(0d, 1d, this.scalingFactor * (this.points.getXDimension() - 1));
    // gl.glVertex3d(this.scalingFactor * (this.points.getZDimension() - 1), 1d, this.scalingFactor * (this.points.getXDimension() - 1d));
    // gl.glVertex3d(this.scalingFactor * (this.points.getZDimension() - 1), 1d, 0d);
    // gl.glEnd();
    // gl.glPopMatrix();
    // }
}
