/*
 * To change this license header, choose License Headers in
 * Project Properties. To change this template file, choose Tools
 * | Templates and open the template in the editor.
 */
package guiFx;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import gui.GUIWindowInterface;
import gui.SysUtils;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.IElement;
import model.SysRoot;

/**
 * @author Diego
 */
public class MainWindowFX extends JPanel implements
		GUIWindowInterface {

	private static MainWindowFX self;

	private static final long serialVersionUID = 1L;

	public static MainWindowFX getInstance() {
		if (self == null) {
			self = new MainWindowFX();
		}
		return self;
	}

	// Getters and setters
	public static MainWindowFX getSelf() {
		return self;
	}

	public static void setSelf(MainWindowFX self) {
		MainWindowFX.self = self;
	}

	private int deltaX;
	private int deltaY;
	private boolean isVisualizationViewerEnabled;
	private String path;

	private SysRoot sysRoot;

	private VisualizationViewer<IElement, Object> visualizationViewer;

	private boolean window = false;

	// Constructor
	private MainWindowFX() {
		this.window = false;
		this.visualizationViewer = null;
		this.isVisualizationViewerEnabled = false;
		this.path = "bin";
		this.sysRoot = new SysRoot();
		this.deltaX = 100;
		this.deltaY = 80;

		this.setBackground(new java.awt.Color(80, 90, 80));
		// this.setPreferredSize(new Dimension(600,300));
	}

	@Override
	public Container getCenter() {
		throw new UnsupportedOperationException(
				"Not supported yet."); // To change body of
										// generated methods,
										// choose Tools |
										// Templates.
	}

	@Override
	public Container getContentPane() {
		throw new UnsupportedOperationException(
				"Not supported yet."); // To change body of
										// generated methods,
										// choose Tools |
										// Templates.
	}

	public int getDeltaX() {
		return this.deltaX;
	}

	public int getDeltaY() {
		return this.deltaY;
	}

	@Override
	public JFrame getFrame() {
		throw new UnsupportedOperationException(
				"Not supported yet."); // To change body of
										// generated methods,
										// choose Tools |
										// Templates.
	}

	public String getPath() {
		return this.path;
	}

	public SysRoot getSysRoot() {
		return this.sysRoot;
	}

	@Override
	public JTextArea getTextArea() {
		throw new UnsupportedOperationException(
				"Not supported yet."); // To change body of
										// generated methods,
										// choose Tools |
										// Templates.
	}

	public VisualizationViewer<IElement, Object> getVisualizationViewer() {
		return this.visualizationViewer;
	}

	public boolean isIsVisualizationViewerEnabled() {
		return this.isVisualizationViewerEnabled;
	}

	public boolean isWindow() {
		return this.window;
	}

	@Override
	public void makeGoodVisual(
			VisualizationViewer<IElement, Object> vv) {
		SysUtils.makeGoodVisual(this.visualizationViewer,
				this);
	}

	@Override
	public void makeMenuBar(
			VisualizationViewer<IElement, Object> vv) {
		throw new UnsupportedOperationException(
				"Not supported yet."); // To change body of
										// generated methods,
										// choose Tools |
										// Templates.
	}

	@Override
	public boolean rightClickEnabled() {
		throw new UnsupportedOperationException(
				"Not supported yet."); // To change body of
										// generated methods,
										// choose Tools |
										// Templates.
	}

	// Interface implementations

	@Override
	public void setCenter(Container c) {
		throw new UnsupportedOperationException(
				"Not supported yet."); // To change body of
										// generated methods,
										// choose Tools |
										// Templates.
	}

	@Override
	public void setCenterPanel(Container pane) {
		throw new UnsupportedOperationException(
				"Not supported yet."); // To change body of
										// generated methods,
										// choose Tools |
										// Templates.
	}

	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}

	public void setDeltaY(int deltaY) {
		this.deltaY = deltaY;
	}

	public void setIsVisualizationViewerEnabled(
			boolean isVisualizationViewerEnabled) {
		this.isVisualizationViewerEnabled = isVisualizationViewerEnabled;
	}

	@Override
	public void setJMenuBar(JMenuBar menuBar) {
		throw new UnsupportedOperationException(
				"Not supported yet."); // To change body of
										// generated methods,
										// choose Tools |
										// Templates.
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSysRoot(SysRoot sysRoot) {
		this.sysRoot = sysRoot;
	}

	public void setVisualizationViewer(
			VisualizationViewer<IElement, Object> visualizationViewer) {
		this.visualizationViewer = visualizationViewer;
		this.add(visualizationViewer);
	}

	public void setWindow(boolean window) {
		this.window = window;
	}
}
