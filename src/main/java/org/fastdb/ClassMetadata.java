package org.fastdb;

import java.io.Serializable;

public interface ClassMetadata {

	/**
	 * The name of the entity
	 */
	public String getEntityName();

	/**
	 * Get the name of the identifier property (or return null)
	 */
	public String getIdentifierPropertyName();

	/**
	 * Get the names of the class' persistent properties
	 */
	public String[] getPropertyNames();

	/**
	 * Does this class support dynamic proxies?
	 */
	public boolean hasProxy();

	/**
	 * Are instances of this class mutable?
	 */
	public boolean isMutable();

	/**
	 * Are instances of this class versioned by a timestamp or version number
	 * column?
	 */
	public boolean isVersioned();

	/**
	 * Get the index of the version property
	 */
	public int getVersionProperty();

	/**
	 * Get the nullability of the class' persistent properties
	 */
	public boolean[] getPropertyNullability();

	/**
	 * Get the "laziness" of the properties of this class
	 */
	public boolean[] getPropertyLaziness();

	/**
	 * Does this class have an identifier property?
	 */
	public boolean hasIdentifierProperty();

	/**
	 * Does this entity declare a natural id?
	 */
	public boolean hasNaturalIdentifier();

	/**
	 * Which properties hold the natural id?
	 */
	public int[] getNaturalIdentifierProperties();

	/**
	 * Does this entity have mapped subclasses?
	 */
	public boolean hasSubclasses();

	/**
	 * Does this entity extend a mapped superclass?
	 */
	public boolean isInherited();

	/**
	 * Return the values of the mapped properties of the object
	 */
	public Object[] getPropertyValuesToInsert(Object entity) throws FastdbException;

	/**
	 * The persistent class, or null
	 */
	public <T> Class<T> getMappedClass();

	/**
	 * Get the value of a particular (named) property
	 */
	public Object getPropertyValue(Object object, String propertyName) throws FastdbException;

	/**
	 * Extract the property values from the given entity.
	 *
	 * @param entity The entity from which to extract the property values.
	 * @return The property values.
	 * @throws FastdbException
	 */
	public Object[] getPropertyValues(Object entity) throws FastdbException;

	/**
	 * Set the value of a particular (named) property
	 */
	public void setPropertyValue(Object object, String propertyName, Object value) throws FastdbException;

	/**
	 * Set the given values to the mapped properties of the given object
	 */
	public void setPropertyValues(Object object, Object[] values) throws FastdbException;

	/**
	 * Get the identifier of an instance (throw an exception if no identifier
	 * property)
	 *
	 * @deprecated Use {@link #getIdentifier(Object,SessionImplementor)} instead
	 */
	public Serializable getIdentifier(Object object) throws FastdbException;

	/**
	 * Does the class implement the <tt>Lifecycle</tt> interface?
	 */
	public boolean implementsLifecycle();

	/**
	 * Get the version number (or timestamp) from the object's version property
	 * (or return null if not versioned)
	 */
	public Object getVersion(Object object) throws FastdbException;
}
