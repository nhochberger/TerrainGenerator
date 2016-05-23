package view;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public class TerrainVisualization implements GLEventListener {

    private final GLU glu;
    private float yAngle;
    private float xAngle;
    private float zoom = 1f;

    public TerrainVisualization() {
        super();
        this.glu = new GLU();
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
        gl.glTranslatef(0f, 0f, -5.0f);

        // Rotate The Cube On X, Y & Z
        gl.glRotatef(this.getxAngle(), 1.0f, 0.0f, 0.0f);
        gl.glRotatef(this.getyAngle(), 0.0f, 1.0f, 0.0f);
        gl.glScaled(this.getZoom(), this.getZoom(), this.getZoom());

        drawCoordinates(gl);

        final GLUT glut = new GLUT();
        final float[] matShininess = { 50.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, FloatBuffer.wrap(matShininess));

        final float[] matAmbient = { 0.2f, 0.2f, 0.2f, 0.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, FloatBuffer.wrap(matAmbient));

        final float[] matDiffuse = { 0.87f, 0.72f, 0.53f, 1.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, FloatBuffer.wrap(matDiffuse));

        final float[] matSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, FloatBuffer.wrap(matSpecular));
        glut.glutSolidTeapot(1.2f);

        gl.glFlush();
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

        final float[] ambientLight = { 0.2f, 0.2f, 0.2f, 0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);

        final float[] diffuseLight = { 0.5f, 0.5f, 0.5f, 0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);

        final float[] specularLight = { 1.0f, 1.0f, 1.0f, 0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);

        final float[] lightPosition = { 100.0f, 100.0f, 100.0f, 0.0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, FloatBuffer.wrap(lightPosition));

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);
    }

    private void drawCoordinates(final GL2 gl) {
        gl.glPushMatrix();
        final float[] matShininess = { 0.0f };
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
        return xAngle;
    }

    public void setxAngle(float xAngle) {
        this.xAngle = xAngle;
    }

    public float getyAngle() {
        return yAngle;
    }

    public void setyAngle(float yAngle) {
        this.yAngle = yAngle;
    }
}
