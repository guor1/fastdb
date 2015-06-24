package org.fastdb;

import java.util.List;

public interface DBQuery {
	List<DBRow> getResultList();

	DBRow getSingleResult();

	int executeUpdate();
	
	boolean execute();

	DBQuery setParameter(int position, Object value);

	DBQuery setParameter(int position, Object value, int sqlType);
}
