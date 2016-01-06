/*
 * To change this license header, choose License Headers in
 * Project Properties. To change this template file, choose Tools
 * | Templates and open the template in the editor.
 */
package guiFx;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.IElement;
import model.SysPackage;
import model.SysRoot;
import analysis.SysAnalysis;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import gui.SysUtils;

/**
 * @author Diego
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class sg4ajMainWindowController implements
		Initializable {

	@FXML
	private MenuItem btnMenuAbout;
	@FXML
	private MenuItem btnMenuClose;
	@FXML
	private MenuItem btnMenuDrag;
	@FXML
	private MenuItem btnMenuOpen;
	@FXML
	private MenuItem btnMenuPick;
	@FXML
	private MenuItem btnMenuSave;

	@FXML
	private TextArea console;
	@FXML
	private StackPane mainPane;

	private MainWindowFX mainWindow;

	private void analyse() {
		if (this.mainWindow.getPath().isEmpty()) {
			this.console
					.appendText("That is not a valid path\n");
		}
		else {
			this.console.appendText("Beginning analysis\n");
			SysRoot root = SysAnalysis
					.initialModel(this.mainWindow.getPath()); // do
			// the
			// initial
			// model
			this.mainWindow.setSysRoot(root);

			VisualizationViewer<IElement, Object> visualizationViewer = SysUtils
					.createVisualizationViewerBySysRoot(
							root,
							this.mainWindow.getDeltaX(),
							this.mainWindow.getDeltaX());

			this.mainWindow
					.setVisualizationViewer(visualizationViewer);
			visualizationViewer.updateUI();
			this.console.appendText(root.getPackages()
					.toString() + "\n");

			this.mainWindow
					.makeGoodVisual(visualizationViewer);
		}
	}

	private void buildSwingMainWindow() {
		this.mainWindow = MainWindowFX.getInstance();

		DelegateTree delegateTree = new DelegateTree();
		SysRoot sysRoot = new SysRoot();
		sysRoot.add(new SysPackage("null"));
		delegateTree.addVertex(sysRoot);

		this.embedSwingContent(this.mainWindow);
	}

	private void embedSwingContent(final JComponent content) {
		final SwingNode mainSwingPane = new SwingNode();
		this.mainPane.getChildren().add(mainSwingPane);

		SwingUtilities.invokeLater(() -> mainSwingPane
				.setContent(content));
	}

	@FXML
	private void handleBtnMenuAboutAction(ActionEvent event) {
		System.out.println("You clicked me!");
		this.console.setText("Drag");
	}

	@FXML
	private void handleBtnMenuCloseAction(ActionEvent event) {
		System.out.println("You clicked me!");
		this.console.setText("Close");
	}

	@FXML
	private void handleBtnMenuDragAction(ActionEvent event) {
		System.out.println("You clicked me!");
		this.console.setText("Drag");
	}

	@FXML
	private void handleBtnMenuOpenAction(ActionEvent event) {
		System.out.println("Opening...");
		JFileChooser fc = new JFileChooser("."
				+ File.separator + "..");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			this.mainWindow.setPath(fc.getSelectedFile()
					.getAbsolutePath());
			this.console.appendText("You choose: "
					+ this.mainWindow.getPath() + "\n");
			int option = JOptionPane.showConfirmDialog(
					null, "Analyse path: "
							+ this.mainWindow.getPath()
							+ " ?");
			if (option == JOptionPane.OK_OPTION) {
				this.analyse();
			}
		}
	}

	@FXML
	private void handleBtnMenuPickAction(ActionEvent event) {
		System.out.println("You clicked me!");
		this.console.setText("Pick");
	}

	@FXML
	private void handleBtnMenuSaveAction(ActionEvent event) {
		System.out.println("You clicked me!");
		this.console.setText("Save");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		this.buildSwingMainWindow();
	}
}
