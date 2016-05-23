package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import hochberger.utilities.gui.EDTSafeFrame;

public class TerrainGeneratorMainFrame extends EDTSafeFrame {

    private final FPSAnimator animator;
    private TerrainVisualization visualization;

    public TerrainGeneratorMainFrame(final String title) {
        super(title);
        this.animator = new FPSAnimator(60);
    }

    @Override
    protected void buildUI() {
        setSize(1000, 1000);
        center();
        disposeOnClose();
        setContentPane(new JPanel(new BorderLayout()));
        final JPanel controllPanel = new JPanel();
        controllPanel.add(new JButton("Show/hide coordinates"));
        controllPanel.add(new JButton("Regenerate terrain"));
        controllPanel.add(new JButton("Reset zoom"));
        controllPanel.add(new JButton("Rest rotation"));
        controllPanel.setBackground(Color.BLACK);
        getContentPane().add(controllPanel, BorderLayout.NORTH);
        final GLProfile glp = GLProfile.getDefault();
        final GLCapabilities caps = new GLCapabilities(glp);
        final GLCanvas canvas = new GLCanvas(caps);
        this.visualization = new TerrainVisualization();
        canvas.addGLEventListener(this.visualization);
        final JPanel test = new JPanel();
        test.setBackground(Color.black);
        getContentPane().add(canvas, BorderLayout.CENTER);
        this.animator.add(canvas);
        this.animator.start();
        canvas.addMouseWheelListener(new ZoomListener());
        final RotationListener rotationListener = new RotationListener();
        canvas.addMouseListener(rotationListener);
        canvas.addMouseMotionListener(rotationListener);
    }

    public void addWindowListener(final WindowListener listener) {
        frame().addWindowListener(listener);
    }

    public class ZoomListener implements MouseWheelListener {

        public ZoomListener() {
            super();
        }

        @Override
        public void mouseWheelMoved(final MouseWheelEvent e) {
            final float stepSize = 0.05f;
            final int wheelRotation = e.getWheelRotation();
            final float zoom = TerrainGeneratorMainFrame.this.visualization.getZoom();
            if (stepSize >= zoom && 0 < wheelRotation) {
                return;
            }
            TerrainGeneratorMainFrame.this.visualization.setZoom(zoom - (stepSize * wheelRotation));
        }
    }

    public class RotationListener extends MouseAdapter {
        private int x;
        private int y;

        @Override
        public void mousePressed(final MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            super.mousePressed(e);
            this.x = e.getX();
            this.y = e.getY();
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            super.mouseDragged(e);
            TerrainGeneratorMainFrame.this.visualization.setyAngle(((e.getX() - this.x) * 0.5f) % 360);
            TerrainGeneratorMainFrame.this.visualization.setxAngle(((e.getY() - this.y) * 0.5f) % 360);
        }
    }
}
