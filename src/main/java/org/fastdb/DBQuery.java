package org.fastdb;

import java.util.List;

public interface DBQuery {
	List<DBRow> getResultList();

	DBRow getSingleResult();

	int executeUpdate();

	DBQuery setParameter(int position, Object value);
}
