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
		return get(key).toString();
	}

	public int getInt(String key) {
		return Integer.parseInt(getString(key));
	}
}