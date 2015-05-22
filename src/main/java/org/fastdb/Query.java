package org.fastdb;

public interface Query<Q> {
	/**
	 * Set the limit / max results for the query results
	 *
	 * @param limit
	 *            max rows
	 * @return the current object
	 */
	Q limit(long limit);

	/**
	 * Set the offset for the query results
	 *
	 * @param offset
	 *            row offset
	 * @return the current object
	 */
	Q offset(long offset);

	/**
	 * Add order expressions
	 *
	 * @param o
	 *            order
	 * @return the current object
	 */
	Q orderBy(String orderByClause);

	/**
	 * Set the given parameter to the given value
	 *
	 * @param <T>
	 * @param param
	 *            param
	 * @param value
	 *            binding
	 * @return the current object
	 */
	Q set(String name, Object value);

	/**
	 * Set the Query to return distinct results
	 *
	 * @return the current object
	 */
	Q distinct();
}
