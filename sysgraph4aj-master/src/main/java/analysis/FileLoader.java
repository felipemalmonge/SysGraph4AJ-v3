package analysis;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

public class FileLoader {

	private static final RuntimeData data = new RuntimeData();
	private static ArrayList<String> instrumentedCl = new ArrayList<String>();
	private static ClassLoader loader = null;
	private static String packagePath = null;

	private static final IRuntime runtime = new LoggerRuntime();

	public static void add(File f) {
		setNewClassPath(f);
	}

	public static synchronized Class<?> forName(String fully)
			throws ClassNotFoundException {
		ClassLoader cl = getClassLoader();// get all loaded class
		// in system
		/**/
		if (cl instanceof MemoryURLClassLoader) {
			if (!instrumentedCl.contains(fully)) {
				instrumentAndDefine(fully, cl);
			}
			return ((MemoryURLClassLoader) cl).loadClass(
					fully, true);
		}
		return getClassLoader().loadClass(fully);
	}

	public static synchronized ClassLoader getClassLoader() {
		if (loader == null) {
			loader = ClassLoader.getSystemClassLoader();
		}
		return loader;
	}

	public static RuntimeData getData() {
		return data;
	}

	public static String getPackagePath() {
		return packagePath;
	}

	public static IRuntime getRuntime() {
		return runtime;
	}

	public static void instrumentAndDefine(
			String className, ClassLoader cl) {
		final Instrumenter instr = new Instrumenter(runtime);
		final String resource = className.replace(".",
				File.separator) + ".class";
		final InputStream resourceStream = cl
				.getResourceAsStream(resource);
		MemoryURLClassLoader memoryClassLoader = (MemoryURLClassLoader) cl;
		byte[] instrumented = null;

		try {
			instrumented = instr.instrument(resourceStream,
					className);
			System.out.println("[FileLoader]: class \""
					+ className + "\" instrumented.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		memoryClassLoader.addDefinition(className,
				instrumented);
		instrumentedCl.add(className);
	}

	public static synchronized void setNewClassPath(File f) {
		if (f != null) {
			if (!f.isDirectory()) {
				f = f.getParentFile();
			}
			URL[] allLocations = new URL[0];
			try {
				allLocations = new URL[] { f.toURI()
						.toURL() };
				loader = new MemoryURLClassLoader(
						allLocations,
						ClassLoader.getSystemClassLoader());
				if (loader == null) {
					throw new Exception(
							"ClassLoader is null");
				}
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void setPackagePath(String packagePath) {
		FileLoader.packagePath = packagePath;
	}

}