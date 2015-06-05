package org.fastdb;

import java.io.Serializable;
import java.sql.Connection;

/**
 * hold connection and execute sql
 * 
 * @author p
 */
public interface Session {

	Connection connection();

	void flush();

	/**
	 * Get the session factory which created this session.
	 *
	 * @return The session factory.
	 * @see SessionFactory
	 */
	SessionFactory getSessionFactory();

	/**
	 * End the session by releasing the JDBC connection and cleaning up. It is
	 * not strictly necessary to close the session but you must at least
	 * {@link #disconnect()} it.
	 *
	 * @return the connection provided by the application or null.
	 * @throws FastdbException Indicates problems cleaning up.
	 */
	void close() throws FastdbException;

	/**
	 * Cancel the execution of the current query.
	 * <p/>
	 * This is the sole method on session which may be safely called from
	 * another thread.
	 *
	 * @throws FastdbException There was a problem canceling the query
	 */
	void cancelQuery() throws FastdbException;

	/**
	 * Check if the session is still open.
	 *
	 * @return boolean
	 */
	boolean isOpen();

	/**
	 * Check if the session is currently connected.
	 *
	 * @return boolean
	 */
	boolean isConnected();

	/**
	 * Will entities and proxies that are loaded into this session be made
	 * read-only by default?
	 *
	 * To determine the read-only/modifiable setting for a particular entity or
	 * proxy:
	 * 
	 * @see Session#isReadOnly(Object)
	 *
	 * @return true, loaded entities/proxies will be made read-only by default;
	 *         false, loaded entities/proxies will be made modifiable by
	 *         default.
	 */
	boolean isDefaultReadOnly();

	/**
	 * Change the default for entities and proxies loaded into this session from
	 * modifiable to read-only mode, or from modifiable to read-only mode.
	 *
	 * Read-only entities are not dirty-checked and snapshots of persistent
	 * state are not maintained. Read-only entities can be modified, but changes
	 * are not persisted.
	 *
	 * When a proxy is initialized, the loaded entity will have the same
	 * read-only/modifiable setting as the uninitialized proxy has, regardless
	 * of the session's current setting.
	 *
	 * To change the read-only/modifiable setting for a particular entity or
	 * proxy that is already in this session:
	 * 
	 * @see Session#setReadOnly(Object,boolean)
	 *
	 *      To override this session's read-only/modifiable setting for entities
	 *      and proxies loaded by a Query:
	 * @see Query#setReadOnly(boolean)
	 *
	 * @param readOnly true, the default for loaded entities/proxies is
	 *            read-only; false, the default for loaded entities/proxies is
	 *            modifiable
	 */
	void setDefaultReadOnly(boolean readOnly);

	/**
	 * Return the identifier value of the given entity as associated with this
	 * session. An exception is thrown if the given entity instance is transient
	 * or detached in relation to this session.
	 *
	 * @param object a persistent instance
	 * @return the identifier
	 * @throws TransientObjectException if the instance is transient or
	 *             associated with a different session
	 */
	Serializable getIdentifier(Object object);

	/**
	 * Check if this instance is associated with this <tt>Session</tt>.
	 *
	 * @param object an instance of a persistent class
	 * @return true if the given instance is associated with this
	 *         <tt>Session</tt>
	 */
	boolean contains(Object object);

	/**
	 * Remove this instance from the session cache. Changes to the instance will
	 * not be synchronized with the database. This operation cascades to
	 * associated instances if the association is mapped with
	 * <tt>cascade="evict"</tt>.
	 *
	 * @param object The entity to evict
	 *
	 * @throws NullPointerException if the passed object is {@code null}
	 * @throws IllegalArgumentException if the passed object is not defined as
	 *             an entity
	 */
	void evict(Object object);

	<T> T load(Class<T> theClass, Serializable id);

	Object load(String entityName, Serializable id);

	/**
	 * Persist the given transient instance, first assigning a generated
	 * identifier. (Or using the current value of the identifier property if the
	 * <tt>assigned</tt> generator is used.) This operation cascades to
	 * associated instances if the association is mapped with
	 * {@code cascade="save-update"}
	 *
	 * @param object a transient instance of a persistent class
	 *
	 * @return the generated identifier
	 */
	Serializable save(Object object);

	/**
	 * Persist the given transient instance, first assigning a generated
	 * identifier. (Or using the current value of the identifier property if the
	 * <tt>assigned</tt> generator is used.) This operation cascades to
	 * associated instances if the association is mapped with
	 * {@code cascade="save-update"}
	 *
	 * @param entityName The entity name
	 * @param object a transient instance of a persistent class
	 *
	 * @return the generated identifier
	 */
	Serializable save(String entityName, Object object);

	/**
	 * Either {@link #save(Object)} or {@link #update(Object)} the given
	 * instance, depending upon resolution of the unsaved-value checks (see the
	 * manual for discussion of unsaved-value checking).
	 * <p/>
	 * This operation cascades to associated instances if the association is
	 * mapped with {@code cascade="save-update"}
	 *
	 * @param object a transient or detached instance containing new or updated
	 *            state
	 *
	 * @see Session#save(java.lang.Object)
	 * @see Session#update(Object object)
	 */
	void saveOrUpdate(Object object);

	/**
	 * Either {@link #save(String, Object)} or {@link #update(String, Object)}
	 * the given instance, depending upon resolution of the unsaved-value checks
	 * (see the manual for discussion of unsaved-value checking).
	 * <p/>
	 * This operation cascades to associated instances if the association is
	 * mapped with {@code cascade="save-update"}
	 *
	 * @param entityName The entity name
	 * @param object a transient or detached instance containing new or updated
	 *            state
	 *
	 * @see Session#save(String,Object)
	 * @see Session#update(String,Object)
	 */
	void saveOrUpdate(String entityName, Object object);

	/**
	 * Update the persistent instance with the identifier of the given detached
	 * instance. If there is a persistent instance with the same identifier, an
	 * exception is thrown. This operation cascades to associated instances if
	 * the association is mapped with {@code cascade="save-update"}
	 *
	 * @param object a detached instance containing updated state
	 */
	void update(Object object);

	/**
	 * Update the persistent instance with the identifier of the given detached
	 * instance. If there is a persistent instance with the same identifier, an
	 * exception is thrown. This operation cascades to associated instances if
	 * the association is mapped with {@code cascade="save-update"}
	 *
	 * @param entityName The entity name
	 * @param object a detached instance containing updated state
	 */
	void update(String entityName, Object object);

	/**
	 * Copy the state of the given object onto the persistent object with the
	 * same identifier. If there is no persistent instance currently associated
	 * with the session, it will be loaded. Return the persistent instance. If
	 * the given instance is unsaved, save a copy of and return it as a newly
	 * persistent instance. The given instance does not become associated with
	 * the session. This operation cascades to associated instances if the
	 * association is mapped with {@code cascade="merge"}
	 * <p/>
	 * The semantics of this method are defined by JSR-220.
	 *
	 * @param object a detached instance with state to be copied
	 *
	 * @return an updated persistent instance
	 */
	Object merge(Object object);

	/**
	 * Copy the state of the given object onto the persistent object with the
	 * same identifier. If there is no persistent instance currently associated
	 * with the session, it will be loaded. Return the persistent instance. If
	 * the given instance is unsaved, save a copy of and return it as a newly
	 * persistent instance. The given instance does not become associated with
	 * the session. This operation cascades to associated instances if the
	 * association is mapped with {@code cascade="merge"}
	 * <p/>
	 * The semantics of this method are defined by JSR-220.
	 *
	 * @param entityName The entity name
	 * @param object a detached instance with state to be copied
	 *
	 * @return an updated persistent instance
	 */
	Object merge(String entityName, Object object);

	/**
	 * Make a transient instance persistent. This operation cascades to
	 * associated instances if the association is mapped with
	 * {@code cascade="persist"}
	 * <p/>
	 * The semantics of this method are defined by JSR-220.
	 *
	 * @param object a transient instance to be made persistent
	 */
	void persist(Object object);

	/**
	 * Make a transient instance persistent. This operation cascades to
	 * associated instances if the association is mapped with
	 * {@code cascade="persist"}
	 * <p/>
	 * The semantics of this method are defined by JSR-220.
	 *
	 * @param entityName The entity name
	 * @param object a transient instance to be made persistent
	 */
	void persist(String entityName, Object object);

	/**
	 * Remove a persistent instance from the datastore. The argument may be an
	 * instance associated with the receiving <tt>Session</tt> or a transient
	 * instance with an identifier associated with existing persistent state.
	 * This operation cascades to associated instances if the association is
	 * mapped with {@code cascade="delete"}
	 *
	 * @param object the instance to be removed
	 */
	void delete(Object object);

	/**
	 * Remove a persistent instance from the datastore. The <b>object</b>
	 * argument may be an instance associated with the receiving
	 * <tt>Session</tt> or a transient instance with an identifier associated
	 * with existing persistent state. This operation cascades to associated
	 * instances if the association is mapped with {@code cascade="delete"}
	 *
	 * @param entityName The entity name for the instance to be removed.
	 * @param object the instance to be removed
	 */
	void delete(String entityName, Object object);

	/**
	 * Re-read the state of the given instance from the underlying database. It
	 * is inadvisable to use this to implement long-running sessions that span
	 * many business tasks. This method is, however, useful in certain special
	 * circumstances. For example
	 * <ul>
	 * <li>where a database trigger alters the object state upon insert or
	 * update
	 * <li>after executing direct SQL (eg. a mass update) in the same session
	 * <li>after inserting a <tt>Blob</tt> or <tt>Clob</tt>
	 * </ul>
	 *
	 * @param object a persistent or detached instance
	 */
	void refresh(Object object);

	/**
	 * Re-read the state of the given instance from the underlying database. It
	 * is inadvisable to use this to implement long-running sessions that span
	 * many business tasks. This method is, however, useful in certain special
	 * circumstances. For example
	 * <ul>
	 * <li>where a database trigger alters the object state upon insert or
	 * update
	 * <li>after executing direct SQL (eg. a mass update) in the same session
	 * <li>after inserting a <tt>Blob</tt> or <tt>Clob</tt>
	 * </ul>
	 *
	 * @param entityName a persistent class
	 * @param object a persistent or detached instance
	 */
	void refresh(String entityName, Object object);

	/**
	 * Completely clear the session. Evict all loaded instances and cancel all
	 * pending saves, updates and deletions. Do not close open iterators or
	 * instances of <tt>ScrollableResults</tt>.
	 */
	void clear();

	/**
	 * Return the persistent instance of the given entity class with the given
	 * identifier, or null if there is no such persistent instance. (If the
	 * instance is already associated with the session, return that instance.
	 * This method never returns an uninitialized instance.) Obtain the
	 * specified lock mode if the instance exists.
	 *
	 * @param clazz a persistent class
	 * @param id an identifier
	 * @param lockOptions the lock mode
	 *
	 * @return a persistent instance or null
	 */
	<T> T get(Class<T> clazz, Serializable id);

	/**
	 * Return the persistent instance of the given named entity with the given
	 * identifier, or null if there is no such persistent instance. (If the
	 * instance is already associated with the session, return that instance.
	 * This method never returns an uninitialized instance.)
	 *
	 * @param entityName the entity name
	 * @param id an identifier
	 *
	 * @return a persistent instance or null
	 */
	Object get(String entityName, Serializable id);
}
