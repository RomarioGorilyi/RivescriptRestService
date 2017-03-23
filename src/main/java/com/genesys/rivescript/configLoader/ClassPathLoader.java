package com.genesys.rivescript.configLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassPathLoader {

	/**
	 * Loader of RiveScript resource files (config files with the extensions .rs and .rive).
	 */
	private ConfigStreamLoader loader;

	public interface ConfigStreamLoader {

		/**
		 * Loads config RiveScript code from the specified {@code InputStream}.
		 *
		 * @param inputStream the lines of RiveScript source code
		 * @return {@code true} if code is read successfully
		 * @throws IOException in case of an error while reading config code
		 */
		boolean loadStreamCode(InputStream inputStream) throws IOException;
	}

	public ClassPathLoader(ConfigStreamLoader loader) {
		this.loader = loader;
	}

	/**
	 * Loads config RiveScript files from the directory with the specified path.
	 *
	 * @param path path to the directory with config files
	 * @return {@code true} if config files are read successfully
	 * @throws IOException in case of a reading files error
	 */
	public boolean loadDirectory(String path) throws IOException {
		String[] configFileExtensions = {".rive", ".rs"};
		return loadDirectory(path, configFileExtensions);
	}

	/**
	 * Loads config RiveScript files from the directory with the specified path and the specified file extensions.
	 */
	private boolean loadDirectory(String path, String[] fileExtensions) throws IOException {
		URLClassLoader  urlClassLoader = (URLClassLoader) this.getClass().getClassLoader();
		URL[] urls = urlClassLoader.getURLs();
		
		for (URL url : urls) {
			File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
			if (file.isDirectory()) {
				scanDirectory(file, file, path, fileExtensions);
			} else if (url.getFile().toLowerCase().endsWith(".jar")) {
				scanJar(file, path, fileExtensions);
			}
		}
		return true;
	}

	private void scanJar(File file, String path, String[] fileExtensions) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry  = entries.nextElement();
			String entryName = entry.getName();
			if (entryName.startsWith(path) && fileHasSignificantExt(entryName, fileExtensions)) {
				InputStream is = zipFile.getInputStream(entry);
				loader.loadStreamCode(is);
				is.close();
			}
		}
	}

	private void scanDirectory(File baseFile, File currentFile, String path, String[] fileExtensions)
			throws IOException {
		File[] nestedFiles = currentFile.listFiles();

		for (File nestedFile : nestedFiles) {
			if (nestedFile.isDirectory()) {
				scanDirectory(baseFile, nestedFile, path, fileExtensions);
			} else {
				String requiredPath = baseFile.getAbsolutePath() + File.separatorChar + path;
				if (nestedFile.getAbsolutePath().startsWith(requiredPath)
						&& fileHasSignificantExt(nestedFile.getName(), fileExtensions)) {
					loadFileContent(nestedFile);
				}
			}
		}
	}

	private boolean loadFileContent(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		boolean result = loader.loadStreamCode(in);
		in.close();
		return result;
	}

	private boolean fileHasSignificantExt(String f, String[] fileExtensions) {
		for (String extension : fileExtensions) {
			if (f.toLowerCase().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}
}
