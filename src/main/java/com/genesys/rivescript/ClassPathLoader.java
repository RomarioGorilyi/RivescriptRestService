package com.genesys.rivescript;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ClassPathLoader {
	
	public static interface ConfigStreamReader {
		boolean loadStream(String file, InputStream is) throws IOException;
	}

	private ConfigStreamReader loader;
	
	
	public ClassPathLoader(ConfigStreamReader loader) {
		this.loader = loader;
	}
	
	public boolean loadDirectory (String path) throws ZipException, IOException {
		String[] exts = { ".rive", ".rs" };
		return this.loadDirectory(path, exts);
	}

	private boolean loadDirectory(String path, String[] exts) throws ZipException, IOException {
		URLClassLoader  urlClassLoader = (URLClassLoader) this.getClass().getClassLoader();
		URL[] urls = urlClassLoader.getURLs();
		
		for (URL url : urls) {
			File f = new File (URLDecoder.decode(url.getFile()));
			if (f.isDirectory()) {
				scanDirectory (f, f, path, exts);
			} else if (url.getFile().toLowerCase().endsWith(".jar")) {
				scanJar (f, path, exts);
			}
		}
		return true;
	}
	
	
	private void scanJar(File file, String path, String[] exts) throws ZipException, IOException {
		ZipFile zin = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zin.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry  = entries.nextElement();
			String entryName = entry.getName();
			if (entryName.startsWith(path) && fileHasSignificantExt (entryName, exts)) {
				InputStream is = zin.getInputStream(entry);
				loader.loadStream(entryName, is);
				is.close();
			}
			
		}
		
	}

	private void scanDirectory(File baseFile, File file, String path, String[] exts) throws IOException {
		File[] nestedFiles = file.listFiles();
		
		for (File f : nestedFiles) {
			if (f.isDirectory()) {
				scanDirectory (baseFile, f, path, exts);
			} else {
				String requiredPath = baseFile.getAbsolutePath()+File.separatorChar+path;
				if (f.getAbsolutePath().startsWith(requiredPath) && fileHasSignificantExt (f.getName(), exts)) {
					loadFileContent (f);
				}
			}
		}
		
		
	}

	private boolean loadFileContent(File f) throws IOException {
		FileInputStream in  = new FileInputStream(f); 
		boolean result = loader.loadStream(f.getAbsolutePath(), in);
		in.close();
		return result;
	}

	private boolean fileHasSignificantExt(String f, String[] exts) {
		for (String e : exts) {
			if (f.toLowerCase().endsWith(e)) return true;
		}
		return false;
	}

	public static void main(String[] args) throws ZipException, IOException {
		new ClassPathLoader (new ConfigStreamReader () {

			@Override
			public boolean loadStream(String file, InputStream is) throws IOException {
				System.out.println(file);
				return false;
			}
			
		}).loadDirectory("Stas");
		
	}
	

}
