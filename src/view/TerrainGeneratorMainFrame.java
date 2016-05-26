package view;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.gui.EDTSafeFrame;
import hochberger.utilities.gui.lookandfeel.SetLookAndFeelTo;
import hochberger.utilities.text.i18n.DirectI18N;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import controller.events.GenerateTerrainEvent;
import edt.EDT;

public class TerrainGeneratorMainFrame extends EDTSafeFrame {

	private final FPSAnimator animator;
	private TerrainVisualization visualization;
	private JProgressBar progressBar;
	private final BasicSession session;

	public TerrainGeneratorMainFrame(final BasicSession session) {
		super(session.getProperties().title());
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
		final JPanel controllPanel = new JPanel();
		controllPanel.add(new JButton("Show/hide coordinates"));
		controllPanel.add(generateTerrainButton());
		controllPanel.add(new JButton("Reset zoom"));
		controllPanel.add(new JButton("Reset rotation"));
		controllPanel.setBackground(Color.BLACK);
		getContentPane().add(controllPanel, BorderLayout.NORTH);
		final GLProfile glp = GLProfile.getDefault();
		final GLCapabilities caps = new GLCapabilities(glp);
		final GLCanvas canvas = new GLCanvas(caps);
		this.visualization = new TerrainVisualization();
		canvas.addGLEventListener(this.visualization);
		final JPanel test = new JPanel();
		test.setBackground(Color.BLACK);
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

	private JButton generateTerrainButton() {
		final JButton result = new JButton(new DirectI18N("Generate terrain").toString());
		result.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				TerrainGeneratorMainFrame.this.session.getEventBus().publishFromEDT(new GenerateTerrainEvent());
			}
		});
		return result;
	}

	public void addWindowListener(final WindowListener listener) {
		frame().addWindowListener(listener);
	}

	public void setProgress(final int percentage) {
		EDT.perform(new Runnable() {

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
			int newX = e.getX();
			int newY = e.getY();
			TerrainGeneratorMainFrame.this.visualization.setxTranslation(TerrainGeneratorMainFrame.this.visualization.getxTranslation() + (newX - this.oldX) * 0.3f);
			TerrainGeneratorMainFrame.this.visualization.setyTranslation(TerrainGeneratorMainFrame.this.visualization.getyTranslation() - (newY - this.oldY) * 0.3f);
			this.oldX = newX;
			this.oldY = newY;
		}

		private void rotation(final MouseEvent e) {
			if (!SwingUtilities.isLeftMouseButton(e)) {
				return;
			}
			int newX = e.getX();
			int newY = e.getY();
			TerrainGeneratorMainFrame.this.visualization.setxAngle(TerrainGeneratorMainFrame.this.visualization.getxAngle() + (newY - this.oldY) * 0.3f);
			TerrainGeneratorMainFrame.this.visualization.setyAngle(TerrainGeneratorMainFrame.this.visualization.getyAngle() + (newX - this.oldX) * 0.3f);
			this.oldX = newX;
			this.oldY = newY;
		}
	}
}
