package org.fastdb;

public class FastdbException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8573814320981622351L;

	public FastdbException(String message) {
		super(message);
	}

	public FastdbException(Throwable cause) {
		super(cause);
	}

	public FastdbException(String message, Throwable cause) {
		super(message, cause);
	}
}
