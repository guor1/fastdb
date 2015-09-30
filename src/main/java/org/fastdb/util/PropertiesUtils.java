package org.fastdb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {

	private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

	/**
	 * Load the file returning the Properties.
	 * 
	 * @param fileName the name of the properties file to load.
	 */
	public static Properties load(String fileName) {
		InputStream is = findInputStream(fileName);
		if (is == null) {
			logger.warn(fileName + " not found");
			return null;
		} else {
			return load(is);
		}
	}

	private static Properties load(InputStream in) {
		Properties props = new Properties();
		try {
			props.load(in);
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return props;
	}

	/**
	 * Find the input stream given the file name.
	 */
	private static InputStream findInputStream(String fileName) {
		if (fileName == null) {
			throw new NullPointerException("fileName is null?");
		}
		try {
			File f = new File(fileName);
			if (f.exists()) {
				logger.info(fileName + " found in file system");
				return new FileInputStream(f);
			} else {
				InputStream in = findInClassPath(fileName);
				if (in != null) {
					logger.info(fileName + " found in classpath");
				}
				return in;
			}

		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static InputStream findInClassPath(String fileName) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
	}
}
