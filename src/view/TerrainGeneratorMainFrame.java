package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import controller.events.ExportTerrainEvent;
import controller.events.GenerateTerrainEvent;
import edt.EDT;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.gui.EDTSafeFrame;
import hochberger.utilities.gui.input.SelfHighlightningValidatingTextField;
import hochberger.utilities.gui.input.ValidatingTextField;
import hochberger.utilities.gui.input.validator.InputValidator;
import hochberger.utilities.gui.lookandfeel.SetLookAndFeelTo;
import hochberger.utilities.text.Text;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.threading.ThreadRunner;
import net.miginfocom.swing.MigLayout;

public class TerrainGeneratorMainFrame extends EDTSafeFrame {

    private final FPSAnimator animator;
    private TerrainVisualization visualization;
    private JProgressBar progressBar;
    private final BasicSession session;
    private ValidatingTextField dimensionTextField;
    private ValidatingTextField roughnessTextField;

    public TerrainGeneratorMainFrame(final BasicSession session) {
        super(session.getProperties().title() + Text.space() + session.getProperties().version());
        this.session = session;
        this.animator = new FPSAnimator(60);
    }

    @Override
    protected void buildUI() {
        setSize(1000, 1000);
        center();
        disposeOnClose();
        SetLookAndFeelTo.systemLookAndFeel();
        setContentPane(new JPanel(new BorderLayout()));
        getContentPane().setBackground(Color.BLACK);
        final JPanel controllPanel = controllPanel();
        getContentPane().add(controllPanel, BorderLayout.NORTH);
        final GLProfile glp = GLProfile.getDefault();
        final GLCapabilities caps = new GLCapabilities(glp);
        final GLCanvas canvas = new GLCanvas(caps);
        this.visualization = new TerrainVisualization();
        canvas.addGLEventListener(this.visualization);
        getContentPane().add(canvas, BorderLayout.CENTER);
        this.animator.add(canvas);
        this.animator.start();
        canvas.addMouseWheelListener(new ZoomListener());
        final RotationTranslationListener rotationListener = new RotationTranslationListener();
        canvas.addMouseListener(rotationListener);
        canvas.addMouseMotionListener(rotationListener);
        final JPanel progressPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
        this.progressBar = new JProgressBar(0, 100);
        progressPanel.add(this.progressBar, "grow");
        getContentPane().add(progressPanel, BorderLayout.SOUTH);
    }

    private JPanel controllPanel() {
        final JPanel controllPanel = new JPanel();
        controllPanel.setOpaque(false);
        final JPanel generationPanel = generationPanel();
        controllPanel.add(generationPanel);
        final JButton exportButton = new JButton(new DirectI18N("Export...").toString());
        exportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
                fileChooser.showSaveDialog(frame());
                if (null == fileChooser.getSelectedFile()) {
                    return;
                }
                final String path = fileChooser.getSelectedFile().getAbsolutePath();
                TerrainGeneratorMainFrame.this.session.getEventBus().publishFromEDT(new ExportTerrainEvent(path));
            }
        });
        controllPanel.add(exportButton);
        return controllPanel;
    }

    private JPanel generationPanel() {
        final JPanel generationPanel = new JPanel();
        generationPanel.setOpaque(false);
        this.dimensionTextField = new SelfHighlightningValidatingTextField("513", 4);
        this.dimensionTextField.addValidator(new InputValidator<String>() {

            @Override
            public boolean isValid(final String input) {
                try {
                    Integer.parseInt(input);
                } catch (final NumberFormatException e) {
                    return false;
                }
                final int parsedInput = Integer.parseInt(input);
                final int n = parsedInput - 1;
                return 0 == (n & (n - 1));
            }
        });
        final JLabel dimensionLabel = new JLabel(new DirectI18N("Dimension: ").toString());
        dimensionLabel.setForeground(Color.WHITE);
        generationPanel.add(dimensionLabel);
        generationPanel.add(this.dimensionTextField);
        this.roughnessTextField = new SelfHighlightningValidatingTextField("0.2", 4);
        this.roughnessTextField.addValidator(new InputValidator<String>() {

            @Override
            public boolean isValid(final String input) {
                try {
                    Float.parseFloat(input);
                } catch (final NumberFormatException e) {
                    return false;
                }
                final float parsedInput = Float.parseFloat(input);
                return 0 <= parsedInput && 1 >= parsedInput;
            }
        });
        final JLabel roughnessLabel = new JLabel(new DirectI18N("Roughness: ").toString());
        roughnessLabel.setForeground(Color.WHITE);
        generationPanel.add(roughnessLabel);
        generationPanel.add(this.roughnessTextField);
        generationPanel.add(generateTerrainButton());
        return generationPanel;
    }

    private JButton generateTerrainButton() {
        final JButton result = new JButton(new DirectI18N("Generate terrain").toString());
        result.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                ThreadRunner.startThread(new Runnable() {

                    @Override
                    public void run() {
                        if (!(TerrainGeneratorMainFrame.this.dimensionTextField.isValid() && TerrainGeneratorMainFrame.this.roughnessTextField.isValid())) {
                            return;
                        }
                        final int dimension = Integer.parseInt(TerrainGeneratorMainFrame.this.dimensionTextField.getText());
                        final float roughness = Float.parseFloat(TerrainGeneratorMainFrame.this.roughnessTextField.getText());
                        TerrainGeneratorMainFrame.this.session.getEventBus().publish(new GenerateTerrainEvent(dimension, roughness));
                    }
                });
            }
        });
        return result;
    }

    public void setProgress(final int percentage) {
        EDT.performBlocking(new Runnable() {

            @Override
            public void run() {
                TerrainGeneratorMainFrame.this.progressBar.setValue(percentage);
            }
        });
    }

    public class ZoomListener implements MouseWheelListener {

        public ZoomListener() {
            super();
        }

        @Override
        public void mouseWheelMoved(final MouseWheelEvent e) {
            final float stepSize = 0.05f * TerrainGeneratorMainFrame.this.visualization.getZoom();
            final int wheelRotation = e.getWheelRotation();
            final float zoom = TerrainGeneratorMainFrame.this.visualization.getZoom();
            if (stepSize >= zoom && 0 < wheelRotation) {
                return;
            }
            TerrainGeneratorMainFrame.this.visualization.setZoom(zoom - (stepSize * wheelRotation));
        }
    }

    public void setHeightMap(final float[][] heightMap) {
        EDT.performBlocking(new Runnable() {

            @Override
            public void run() {
                TerrainGeneratorMainFrame.this.visualization.setPoints(heightMap);
            }
        });
    }

    public class RotationTranslationListener extends MouseAdapter {
        private int oldX;
        private int oldY;

        @Override
        public void mousePressed(final MouseEvent e) {
            super.mousePressed(e);
            this.oldX = e.getX();
            this.oldY = e.getY();
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            super.mouseDragged(e);
            translation(e);
            rotation(e);
        }

        private void translation(final MouseEvent e) {
            if (!SwingUtilities.isRightMouseButton(e)) {
                return;
            }
            final int newX = e.getX();
            final int newY = e.getY();
            TerrainGeneratorMainFrame.this.visualization.setxTranslation(TerrainGeneratorMainFrame.this.visualization.getxTranslation() + (newX - this.oldX) * 0.3f);
            TerrainGeneratorMainFrame.this.visualization.setyTranslation(TerrainGeneratorMainFrame.this.visualization.getyTranslation() - (newY - this.oldY) * 0.3f);
            this.oldX = newX;
            this.oldY = newY;
        }

        private void rotation(final MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            final int newX = e.getX();
            final int newY = e.getY();
            TerrainGeneratorMainFrame.this.visualization.setxAngle(TerrainGeneratorMainFrame.this.visualization.getxAngle() + (newY - this.oldY) * 0.3f);
            TerrainGeneratorMainFrame.this.visualization.setyAngle(TerrainGeneratorMainFrame.this.visualization.getyAngle() + (newX - this.oldX) * 0.3f);
            this.oldX = newX;
            this.oldY = newY;
        }
    }
}
