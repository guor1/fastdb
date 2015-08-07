package org.fastdb;

import java.util.HashMap;

/**
 * stand for a single db row result
 * 
 * @author guor
 *
 */
public class DBRow extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4247959745278175790L;

	public String getString(String key) {
		Object object = get(key);
		return object == null ? null : object.toString();
	}

	public int getInt(String key) {
		String s = getString(key);
		return (s == null || s.isEmpty()) ? 0 : Integer.parseInt(s);
	}
}