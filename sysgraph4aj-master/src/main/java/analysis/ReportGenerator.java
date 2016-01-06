package analysis;

import java.io.File;
import java.io.IOException;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;

public class ReportGenerator {

	private final File classesDirectory;
	private ExecFileLoader execFileLoader;
	private final File executionDataFile;
	private final File reportDirectory;

	private final File sourceDirectory;

	private final String title;

	public ReportGenerator(final File projectDirectory) {
		this.title = projectDirectory.getName();
		this.executionDataFile = new File(projectDirectory,
				"jacoco.exec");
		this.classesDirectory = new File(projectDirectory,
				"bin");
		this.sourceDirectory = new File(projectDirectory,
				"src");
		this.reportDirectory = new File(projectDirectory,
				"coveragereport");
	}

	private IBundleCoverage analyzeStructure()
			throws IOException {
		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(
				this.execFileLoader.getExecutionDataStore(),
				coverageBuilder);

		analyzer.analyzeAll(this.classesDirectory);

		return coverageBuilder.getBundle(this.title);
	}

	public void create() throws IOException {

		this.loadExecutionData();

		final IBundleCoverage bundleCoverage = this
				.analyzeStructure();

		this.createReport(bundleCoverage);

	}

	private void createReport(
			final IBundleCoverage bundleCoverage)
					throws IOException {

		final HTMLFormatter htmlFormatter = new HTMLFormatter();
		final IReportVisitor visitor = htmlFormatter
				.createVisitor(new FileMultiReportOutput(
						this.reportDirectory));

		visitor.visitInfo(this.execFileLoader
				.getSessionInfoStore().getInfos(),
				this.execFileLoader.getExecutionDataStore()
				.getContents());

		visitor.visitBundle(bundleCoverage,
				new DirectorySourceFileLocator(
						this.sourceDirectory, "utf-8", 4));

		visitor.visitEnd();

	}

	private void loadExecutionData() throws IOException {
		this.execFileLoader = new ExecFileLoader();
		this.execFileLoader.load(this.executionDataFile);
	}

}
