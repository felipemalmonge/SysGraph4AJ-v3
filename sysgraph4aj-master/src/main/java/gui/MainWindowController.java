package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.IElement;
import model.SysElement;
import model.SysPackage;
import model.SysRoot;
import visualization.SysGraphMouse;
import analysis.SysAnalysis;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * Controller to GUI MainWindow
 *
 * @author dmjesus
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class MainWindowController extends JFrame implements
		GUIWindowInterfaceNew {

	/**
	 * Listener responsável por analisar o pacote escolhido.
	 *
	 * @author Robson
	 */
	private final class AnalysePathActionListener implements
			ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// MainWindow.this.path =
			// "C:/Users/Diego/Documents/Eclipse Projects/HelloWorld/bin";
			MainWindowController.this._path = "/home/dmjesus/Softwares/eclipse-kepler-4.3/workspace/HelloWorld/bin";

			// here goes the magic...
			MainWindowController.this.analyse();
		}
	}

	/**
	 * Listener responsável por abrir uma janela onde nesta será
	 * escolhida as pastas e os arquivos a serem analisados.
	 *
	 * @author Robson
	 */
	private final class ChoosePathActionListener implements
			ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser("."
					+ File.separator + "..");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				// just show the selected path into the textArea
				MainWindowController.this._path = fc
						.getSelectedFile()
						.getAbsolutePath();
				MainWindowController.this._console
						.append("You choose: "
								+ MainWindowController.this._path
								+ "\n");
				int cont = JOptionPane
						.showConfirmDialog(
								null,
								"Analyse path: "
										+ MainWindowController.this._path
										+ " ?");
				if (cont == JOptionPane.OK_OPTION) {
					MainWindowController.this.analyse();
				}
			}
		}
	}

	public class JUnitActionListener implements
			ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JUnitDialog j = new JUnitDialog(
					MainWindowController.this);
			j.pack();
			j.setLocationRelativeTo(MainWindowController.this);
			j.setVisible(true);
		}
	}

	private static MainWindowController self;
	/**
	 * This version try centralize the GUI Interface
	 */
	private static final long serialVersionUID = -7799470083870311285L;

	/**
	 * Get the singleton object
	 *
	 * @return The singleton MainWindow object
	 */
	public static MainWindowController getInstance() {

		if (self == null) {
			self = new MainWindowController(
					"SysGraph4AJ v2.1");
		}

		return self;
	}

	private JTextArea _console;
	private int _deltaX;

	private int _deltaY;

	private String _path;

	private SysRoot _sysRoot;

	private VisualizationViewer<IElement, Object> _visualizationViewer;

	/**
	 * Constructs a new window (SingleTon Instance)
	 *
	 * @param name
	 *            - Window title
	 */
	private MainWindowController(String name) {

		this.setTitle(name);

		this._visualizationViewer = null;
		this._sysRoot = new SysRoot();

		this.setDefaultVisualization();
		this.setDefaultStyle();
	}

	/**
	 * Makes an initial and special analysis of given the path
	 */
	public void analyse() {
		if (this._path == null) {
			this._console
					.append("That is not a valid path\n");
		}
		else {
			this._console.append("Beginning analysis\n");
			SysRoot root = SysAnalysis
					.initialModel(this._path); // do the
			// initial model
			this._sysRoot = root;
			this._visualizationViewer = SysUtils
					.createVisualizationViewerBySysRoot(
							root, this._deltaX,
							this._deltaY);
			this._visualizationViewer.updateUI();
			this._console.append(root.getPackages()
					.toString() + "\n");
			// this.makeGoodVisual(_visualizationViewer);
		}
	}

	/**
	 * Configuration of analyze button
	 *
	 * @return The analyze button
	 */
	private JButton createAnalizeButton() {
		JButton btn = new JButton("Analyse");
		btn.addActionListener(new AnalysePathActionListener());
		btn.setSize(new Dimension(80, 30));
		btn.setMaximumSize(new Dimension(80, 30));
		return btn;
	}

	/**
	 * Create the button 'Choose a bin directory'
	 *
	 * @return The button
	 */
	private JButton createChoosePathButton() {
		JButton btn = new JButton("Choose path");
		btn.addActionListener(new ChoosePathActionListener());
		btn.setSize(new Dimension(80, 30));
		btn.setMaximumSize(new Dimension(80, 30));
		return btn;
	}

	@Override
	public JTextArea getConsole() {
		return this._console;
	}

	@Override
	public VisualizationViewer<IElement, Object> getViewer() {
		return this._visualizationViewer;
	}

	private JTextArea makeConsole() {
		JTextArea console = new JTextArea();
		this._console = console;
		console.setFont(new Font("Serif", Font.PLAIN, 16));
		console.setLineWrap(true);
		console.setWrapStyleWord(true);
		console.setEditable(false);

		return console;
	}

	private JMenu makeControlMenu() {
		JMenu menu = new JMenu();
		SysGraphMouse gm = new SysGraphMouse(this,
				this._sysRoot);
		this._visualizationViewer.setGraphMouse(gm);

		JMenuItem jmiDrag = new JMenuItem("Drag Mode");
		JMenuItem jmiPick = new JMenuItem("Pick Mode");

		KeyStroke f2 = KeyStroke.getKeyStroke(
				KeyEvent.VK_F2, 0);
		KeyStroke f3 = KeyStroke.getKeyStroke(
				KeyEvent.VK_F3, 0);
		jmiDrag.setAccelerator(f2);
		jmiDrag.setAccelerator(f3);

		menu.add(jmiDrag);
		menu.add(jmiPick);

		menu.setText("Mouse Mode");

		menu.setMinimumSize(new Dimension(90, 20));
		menu.setPreferredSize(new Dimension(90, 20));

		return menu;
	}

	private JMenu makeJunitMenu() {

		JMenu menu = new JMenu();
		menu.setText("JUnit");
		menu.setIcon(null);
		menu.setPreferredSize(new Dimension(50, 20));

		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("Import");
		menuItem.addActionListener(new JUnitActionListener());
		menu.add(menuItem);

		return menu;
	}

	public JMenuBar makeMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu jUnitMenu = this.makeJunitMenu();
		JMenu controlMenu = this.makeControlMenu();

		menuBar.add(jUnitMenu);
		menuBar.add(controlMenu);

		return menuBar;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	/**
	 * Set default style to MainWindow
	 */
	private void setDefaultStyle() {

		try {
			UIManager
					.setLookAndFeel(UIManager
							.getCrossPlatformLookAndFeelClassName());
		}
		catch (ClassNotFoundException
				| InstantiationException
				| IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setVisible(false);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);

		JPanel leftPanel = new JPanel();
		JScrollPane bottomPanel = new JScrollPane();
		JPanel topPanel = new JPanel();

		JButton btnAnalyze = this.createAnalizeButton();
		leftPanel.add(btnAnalyze);
		leftPanel.setSize(85, 170);
		leftPanel.setVisible(true);
		this.add(leftPanel, BorderLayout.LINE_START);

		JTextArea console = this.makeConsole();
		bottomPanel.add(console);
		bottomPanel.setVisible(true);
		this.add(bottomPanel, BorderLayout.PAGE_END);

		JMenuBar menuBar = this.makeMenuBar();
		this.setJMenuBar(menuBar);

		this._path = "bin";

		this._deltaX = 100;
		this._deltaY = 80;

		// Set true when releasing a distribution
		this.setIcon(false);
	}

	private void setDefaultVisualization() {

		DelegateTree<SysElement, Float> dTree = new DelegateTree<SysElement, Float>();
		TreeLayout<SysElement, Float> tLayout = new TreeLayout<SysElement, Float>(
				dTree);
		AggregateLayout<SysElement, Float> aggLayout = new AggregateLayout<SysElement, Float>(
				tLayout);

		SysRoot sysRoot = new SysRoot();
		SysPackage sysPackage = new SysPackage("null");

		sysRoot.add(sysPackage);
		dTree.addVertex(sysRoot);

		this._visualizationViewer = new VisualizationViewer(
				aggLayout);

	}

	/**
	 * Set the Icon of MainWindow
	 *
	 * @param dist
	 *            - Set the running environment
	 */
	public void setIcon(boolean dist) {

		// set JFrame icon
		String eclipsePath = "./icons/icon_256x256.png";
		String jarPath = "/icon_256x256.png";

		if (dist) {

			// Use this configuration inside eclipse
			ImageIcon img = new ImageIcon(eclipsePath);
			this.setIconImage(img.getImage());

		}
		else {

			// Use to generate jar file
			BufferedImage image = null;
			try {
				image = ImageIO.read(this.getClass()
						.getResource(jarPath));
				this.setIconImage(image);
			}
			catch (IOException e) {
				e.getMessage();
			}
		}

	}

}
