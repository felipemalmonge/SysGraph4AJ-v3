package gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import analysis.CoverageAnalysis;
import analysis.FileLoader;

class JUnitDialog extends JDialog implements
		ActionListener, PropertyChangeListener {
	private final class CancelActionListener implements
			ActionListener {
		JDialog jd;

		public CancelActionListener(JDialog d) {
			this.jd = d;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.jd.setVisible(false);
		}
	}

	private final class ChoosePathActionListener implements
			ActionListener {

		private Boolean classPathValid = false;

		public ChoosePathActionListener() {
			// TODO Auto-generated constructor stub
		}

		public ChoosePathActionListener(
				Boolean classPathValid) {
			this.classPathValid = classPathValid;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser("."
					+ File.separator + "..");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				if (!this.classPathValid) {
					JUnitDialog.this.junitTF.setText(fc
							.getSelectedFile()
							.getAbsolutePath());
				}
				else {
					JUnitDialog.this.classFilePath
							.setText(fc.getSelectedFile()
									.getAbsolutePath());
				}
			}
		}
	}

	private final class RunActionListener implements
			ActionListener {

		JDialog jd;

		public RunActionListener(JDialog j) {
			this.jd = j;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				FileLoader.getRuntime().startup(
						FileLoader.getData());
			}
			catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				JOptionPane
						.showMessageDialog(this.jd,
								"Cannot startup coverage analysis runtime.");
				return;
			}
			JUnitCore junitCore = new JUnitCore();
			FileLoader
					.setPackagePath(JUnitDialog.this.packagePath
							.getText());
			ClassLoader cl = FileLoader.getClassLoader();
			Class<?> test;
			try {
				test = cl
						.loadClass(JUnitDialog.this.junitTF
								.getText());
			}
			catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this.jd,
						"JUnit class not found.");
				return;
			}
			long start = System.currentTimeMillis();
			Result r = junitCore.run(test);
			String str = "";
			if (r.getFailureCount() > 0) {
				int failureCount = 0;
				for (int i = 0; i < r.getFailureCount(); i++) {
					String failureMessage = r.getFailures()
							.get(i).getMessage();
					if ((failureMessage != null)
							&& failureMessage
									.startsWith("expected")) {
						str += "\n" + failureMessage;
					}
					failureCount++;
				}
				str = "Failure count = " + failureCount
						+ str;
			}
			File file = null;
			file = new File("cobertura.html");
			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
			str += "\n"
					+ CoverageAnalysis.getCoverage(
							JUnitDialog.this.classNameTF
									.getText(), file,
							JUnitDialog.this.classFilePath
									.getText());
			long end = System.currentTimeMillis();
			long overhead = end - start;
			JOptionPane.showMessageDialog(this.jd, str
					+ "Time: " + overhead + "ms.");
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JButton cancel;
	/**
	 * FileChooser para selecionar a classe que sofrerá a
	 * cobertura de testes
	 */
	private JButton classFileChooser;
	/**
	 * Texto para o caminho da classe selecionada no FileChooser
	 */
	private JTextField classFilePath;
	private JTextField classNameTF;
	private JTextField classPathTF;
	private JButton junitFileChooser;
	private JTextField junitTF;

	/**
	 * Texto para determinar o pacote da class
	 */
	private JTextField packagePath;

	private JButton run;

	private String typedText = null;

	/** Creates the reusable dialog. */
	public JUnitDialog(Frame aFrame) {
		super(aFrame, true);
		this.setTitle("Importing JUnit Test Cases");

		this.junitFileChooser = this
				.createChooseJUnitButton();
		this.classFileChooser = this.createChooseButton();
		this.run = this.createGoButton();
		this.cancel = this.createCancelButton();

		this.junitTF = new JTextField(50);
		this.classPathTF = new JTextField(50);
		this.classNameTF = new JTextField(50);
		this.packagePath = new JTextField(50);
		this.classFilePath = new JTextField(50);

		// Create an array of the text and components to be
		// displayed.
		String junitMsg = "Choose the JUnit test cases class file and set the additional class paths.";
		String classNameMsg = "Name of the class to get coverage info: ";
		String classPathMsg = "Additional class paths:";
		String packageMsg = "Package of JUnit Test file: ";
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel,
				BoxLayout.Y_AXIS));

		panel.add(new JLabel(junitMsg));
		panel.add(this.junitTF);
		panel.add(this.junitFileChooser);
		panel.add(new JLabel(classPathMsg));
		panel.add(this.classPathTF);
		panel.add(new JLabel(packageMsg));
		panel.add(this.packagePath);
		panel.add(new JLabel(classNameMsg));
		panel.add(this.classNameTF);
		panel.add(new JLabel("Select class"));
		panel.add(this.classFilePath);
		panel.add(this.classFileChooser);

		panel.add(Box.createHorizontalGlue());
		panel.add(this.run);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(this.cancel);

		this.setContentPane(panel);

		// Handle window closing correctly.
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);

		// Ensure the text field always gets the first focus.
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent ce) {
				JUnitDialog.this.junitTF
						.requestFocusInWindow();
			}
		});

		// Register an event handler that puts the text into the
		// option pane.
		this.junitTF.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		this.junitTF.setText(null);
		this.setVisible(false);
	}

	private JButton createCancelButton() {
		JButton btn = new JButton("Cancel");
		btn.addActionListener(new CancelActionListener(this));
		btn.setSize(new Dimension(80, 30));
		btn.setMaximumSize(new Dimension(80, 30));
		return btn;
	}

	private JButton createChooseButton() {
		JButton btn = new JButton("Choose class");
		btn.addActionListener(new ChoosePathActionListener(
				true));
		btn.setSize(new Dimension(80, 30));
		btn.setMaximumSize(new Dimension(80, 30));
		return btn;
	}

	private JButton createChooseJUnitButton() {
		JButton btn = new JButton("Choose JUnit class");
		btn.addActionListener(new ChoosePathActionListener());
		btn.setSize(new Dimension(80, 30));
		btn.setMaximumSize(new Dimension(80, 30));
		return btn;
	}

	private JButton createGoButton() {
		JButton btn = new JButton("Run");
		btn.addActionListener(new RunActionListener(this));
		btn.setSize(new Dimension(80, 30));
		btn.setMaximumSize(new Dimension(80, 30));
		return btn;
	}

	/**
	 * Returns null if the typed string was invalid; otherwise,
	 * returns the string as the user entered it.
	 */
	public String getValidatedText() {
		return this.typedText;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}
}
