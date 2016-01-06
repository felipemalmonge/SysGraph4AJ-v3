package gui;

import javax.swing.JTextArea;

import edu.uci.ics.jung.visualization.VisualizationViewer;

public interface GUIWindowInterfaceNew {
	public void run();
	public JTextArea getConsole();
	public VisualizationViewer<?, ?> getViewer();
}
