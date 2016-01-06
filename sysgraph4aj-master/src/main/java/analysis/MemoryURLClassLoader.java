package analysis;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.util.HashMap;
import java.util.Map;

public class MemoryURLClassLoader extends URLClassLoader {

	@SuppressWarnings("unused")
	private AccessControlContext acc;

	private final Map<String, byte[]> definitions = new HashMap<String, byte[]>();

	public MemoryURLClassLoader(URL[] urls) {
		super(urls);
	}

	public MemoryURLClassLoader(URL[] urls,
			ClassLoader parent) {
		super(urls, parent);
	}

	public void addDefinition(final String name,
			final byte[] bytes) {
		this.definitions.put(name, bytes);
	}

	@Override
	public Class<?> loadClass(final String name,
			final boolean resolve)
					throws ClassNotFoundException {
		final byte[] bytes = this.definitions.get(name);
		if (bytes != null) {
			Class<?> c = this.findLoadedClass(name);
			return (c != null) ? c : this.defineClass(name,
					bytes, 0, bytes.length);
		}
		/**
		 * Caso não contenha um array de bytes da classe já
		 * presente nas definições, inicializará uma variavel
		 * File para carregar o arquivo e utilizará apenas do
		 * nome do arquivo para recupera-lo
		 */
		File file = new File(name);
		String javaName = name;
		try {
			String path[] = file.toURI().toURL().toString()
					.split("/");
			javaName = path[path.length - 1];
			if (javaName.toLowerCase().endsWith(".java")) {
				javaName = javaName.substring(0,
						javaName.length() - 5);
			}
			if (javaName.toLowerCase().endsWith(".class")) {
				javaName = javaName.substring(0,
						javaName.length() - 6);

			}
			if (FileLoader.getPackagePath() != null) {
				javaName = FileLoader.getPackagePath()
						+ "." + javaName;
			}
			FileLoader.setPackagePath(null);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return super.loadClass(javaName, resolve);
	}

}
