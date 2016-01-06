package analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.Format;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.ILine;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;

public class CoverageAnalysis {

	private static final Format DECIMAL_FORMAT = new DecimalFormat(
			"0.00");

	/**
	 * Método responsável por determinar a cor de fundo de uma
	 * linha coberta através do valor inteiro do seu status de
	 * cobertura
	 * 
	 * @param status
	 * @return
	 */
	private static String getColor(final int status) {
		switch (status) {
			case ICounter.NOT_COVERED:
				return "red";
			case ICounter.PARTLY_COVERED:
				return "yellow";
			case ICounter.FULLY_COVERED:
				return "green";
		}
		return "white";
	}

	@SuppressWarnings("resource")
	public static String getCoverage(String className,
			File file, String classPath) {
		className = className.replace('.', '/') + ".class";
		String ret = "";
		ExecutionDataStore executionData = new ExecutionDataStore();
		SessionInfoStore sessionInfos = new SessionInfoStore();
		FileLoader.getData().collect(executionData,
				sessionInfos, false);
		FileLoader.getRuntime().shutdown();

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(
				executionData, coverageBuilder);
		InputStream analyzedClass;
		try {
			analyzedClass = FileLoader.getClassLoader()
					.getResourceAsStream(className);
			analyzer.analyzeClass(analyzedClass, className);
		}
		catch (IOException e) {
			e.printStackTrace();
			return ret;
		}
		BufferedWriter bw;
		BufferedReader br;
		try {
			FileInputStream fis = new FileInputStream(
					new File(classPath));
			br = new BufferedReader(new InputStreamReader(
					fis));
			FileWriter fw = new FileWriter(
					file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.append("<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Cobertura</title>\n</head>\n<body>\n");
		}
		catch (IOException ioe) {
			return ret;
		}
		catch (NullPointerException npe) {
			return ret;
		}
		/**
		 * Geração da porcentagem de cobertura da classe,
		 * contendo valores de intruções, linhas e branches
		 */
		for (final IClassCoverage cc : coverageBuilder
				.getClasses()) {
			ret += "Coverage of class " + cc.getName()
					+ ":\n";
			ret += printCounter("instructions",
					cc.getInstructionCounter());
			ret += printCounter("branches",
					cc.getBranchCounter());
			ret += printCounter("lines",
					cc.getLineCounter());
			ret += printCounter("methods",
					cc.getMethodCounter());
			ret += printCounter("complexity",
					cc.getComplexityCounter());
			try {
				bw.append("<p>" + cc.getName() + "</p>\n\n");
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
			/**
			 * Geração do relatório em HTML, cobrindo a classe
			 * com o resultado dos testes JUnit e colorindo cada
			 * linha de acordo com o status de saida da cobertura
			 */
			try {
				String sCurrentLine;
				int i = 1;
				while ((sCurrentLine = br.readLine()) != null) {
					ILine line = cc.getLine(i);
					Integer status = ICounter.EMPTY;
					if ("".equals(sCurrentLine)) {
						sCurrentLine = "<br/>";
					}
					sCurrentLine.replaceAll("^[\t]*",
							"&emsp;");
					if (line != null) {
						status = line.getStatus();
					}
					bw.append("<p style=\"background-color:"
							+ getColor(status)
							+ ";\">"
							+ sCurrentLine + "</p>\n");
					i++;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.append("</body>\n</html>");
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private static String printCounter(final String unit,
			final ICounter counter) {
		double covered = Double.valueOf(counter
				.getCoveredRatio()) * 100.0;
		covered = Double.isNaN(covered) ? 0 : covered;
		Integer total = Integer.valueOf(counter
				.getTotalCount());
		return DECIMAL_FORMAT.format(covered) + "% of "
		+ total + " " + unit + ".\n";
	}

}
