package view;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

public class TerrainVisualization implements GLEventListener {

    private static final float INITIAL_ZOOM = 0.1f;
    private final GLU glu;
    private float yAngle;
    private float xAngle;
    private float xTranslation;
    private float yTranslation;
    private float zoom = INITIAL_ZOOM;
    private float[][] points;

    public TerrainVisualization() {
        super();
        this.glu = new GLU();
        this.points = new float[0][0];
    }

    @Override
    public void init(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        lighting(gl);
    }

    @Override
    public void dispose(final GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
    }

    @Override
    public void display(final GLAutoDrawable drawable) {
        update();
        render(drawable);
    }

    private void update() {
        // TODO Auto-generated method stub
    }

    private void render(final GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -15.0f);
        gl.glScaled(this.getZoom(), this.getZoom(), this.getZoom());
        gl.glTranslatef(this.xTranslation, this.yTranslation, 0f);
        gl.glRotatef(this.getxAngle(), 1.0f, 0.0f, 0.0f);
        gl.glRotatef(this.getyAngle(), 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(-this.points.length / 2, 0f, -this.points.length / 2);

        drawCoordinates(gl);

        drawTerrain(gl);
        gl.glPopMatrix();
        gl.glFlush();
    }

    private void drawTerrain(final GL2 gl) {
        gl.glPushMatrix();
        final float[] matShininess = { 0.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, FloatBuffer.wrap(matShininess));

        final float[] matAmbient = { 0.2f, 0.2f, 0.2f, 0.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, FloatBuffer.wrap(matAmbient));

        final float[] matDiffuse = { 0.87f, 0.72f, 0.53f, 1.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, FloatBuffer.wrap(matDiffuse));

        final float[] matSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, FloatBuffer.wrap(matSpecular));

        gl.glBegin(GL2.GL_QUADS);
        for (int x = 0; x < this.points.length - 1; x++) {
            for (int z = 0; z < this.points[0].length - 1; z++) {
                gl.glNormal3f(x, this.points[x][z + 1], z + 1);
                gl.glVertex3f(x, this.points[x][z + 1], z + 1);
                gl.glNormal3f(x + 1, this.points[x + 1][z + 1], z + 1);
                gl.glVertex3f(x + 1, this.points[x + 1][z + 1], z + 1);
                gl.glNormal3f(x + 1, this.points[x + 1][z], z);
                gl.glVertex3f(x + 1, this.points[x + 1][z], z);
                gl.glNormal3f(x, this.points[x][z], z);
                gl.glVertex3f(x, this.points[x][z], z);
            }
        }
        gl.glEnd();
        gl.glPopMatrix();
    }

    @Override
    public void reshape(final GLAutoDrawable drawable, final int x, final int y, final int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) {
            height = 1;
        }

        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        this.glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void lighting(final GL2 gl) {

        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glShadeModel(GL2.GL_SMOOTH);

        final float[] ambientLight = { 0.2f, 0.4f, 0.2f, 0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);

        final float[] diffuseLight = { 0.7f, 1f, 0.8f, 0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);

        final float[] specularLight = { 0.3f, 0.3f, 0.3f, 0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);

        final float[] lightPosition = { 10000.0f, 10000.0f, 10000.0f, 0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, FloatBuffer.wrap(lightPosition));

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);
    }

    private void drawCoordinates(final GL2 gl) {
        gl.glPushMatrix();
        final float[] matShininess = { 50.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, FloatBuffer.wrap(matShininess));

        final float[] matDiffuse = { 0.0f, 0.0f, 0.0f, 0.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, FloatBuffer.wrap(matDiffuse));

        final float[] matSpecular = { 0.0f, 0.0f, 0.0f, 0.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, FloatBuffer.wrap(matSpecular));

        gl.glLineWidth(1.5f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        final float[] matAmbientGreen = { 0.0f, 1.0f, 0.0f, 0.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, FloatBuffer.wrap(matAmbientGreen));
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(10f, 0f, 0f);
        gl.glEnd();
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        final float[] matAmbientRed = { 1.0f, 0.0f, 0.0f, 0.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, FloatBuffer.wrap(matAmbientRed));
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0f, 10f, 0f);
        gl.glEnd();
        final float[] matAmbientBlue = { 0.0f, 0.0f, 1.0f, 0.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, FloatBuffer.wrap(matAmbientBlue));
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0f, 0f, 10f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    public float getZoom() {
        return this.zoom;
    }

    public void setZoom(final float zoom) {
        this.zoom = zoom;
    }

    public float getxAngle() {
        return this.xAngle;
    }

    public void setxAngle(final float xAngle) {
        this.xAngle = xAngle;
    }

    public float getyAngle() {
        return this.yAngle;
    }

    public void setyAngle(final float yAngle) {
        this.yAngle = yAngle;
    }

    public float getxTranslation() {
        return this.xTranslation;
    }

    public void setxTranslation(final float xTranslation) {
        this.xTranslation = xTranslation;
    }

    public float getyTranslation() {
        return this.yTranslation;
    }

    public void setyTranslation(final float yTranslation) {
        this.yTranslation = yTranslation;
    }

    public void setPoints(final float[][] points) {
        this.points = points;
        // if (0 >= points.length) {
        // this.zoom = INITIAL_ZOOM;
        // }
        // this.zoom = 7f / points.length;
    }

}
