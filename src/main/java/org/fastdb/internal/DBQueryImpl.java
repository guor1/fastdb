package org.fastdb.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fastdb.DBQuery;
import org.fastdb.DBRow;
import org.fastdb.DBServer;
import org.fastdb.FastdbException;
import org.fastdb.util.DBUtils;

public class DBQueryImpl implements DBQuery {

	private final DBServer dbServer;

	private final String sql;

	private Map<Integer, TypedValue> indexParams = new HashMap<Integer, TypedValue>();

	public DBQueryImpl(DBServer dbServer, String sql) {
		this.dbServer = dbServer;
		this.sql = sql;
	}

	public List<DBRow> getResultList() {
		try {
			Connection conn = this.dbServer.getConnection();
			try {
				PreparedStatement pstmt = conn.prepareStatement(this.sql);
				try {
					doPrepare(pstmt);
					ResultSet rs = pstmt.executeQuery();
					try {
						return DBUtils.buildResult(rs);
					} finally {
						rs.close();
					}
				} finally {
					pstmt.close();
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			throw new FastdbException(e);
		}
	}

	private void doPrepare(PreparedStatement pstmt) throws SQLException {
		Iterator<Integer> iterator = indexParams.keySet().iterator();
		while (iterator.hasNext()) {
			Integer index = iterator.next();
			TypedValue typedValue = indexParams.get(index);
			DBUtils.setParameterValue(pstmt, index, typedValue.getSqlType(), typedValue.getValue());
		}
	}

	public DBRow getSingleResult() {
		List<DBRow> list = getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public int executeUpdate() {
		try {
			Connection conn = this.dbServer.getConnection();
			try {
				PreparedStatement pstmt = conn.prepareStatement(this.sql);
				try {
					doPrepare(pstmt);
					return pstmt.executeUpdate();
				} finally {
					pstmt.close();
				}
			} finally {
				conn.close();
			}
		} catch (Exception e) {
			throw new FastdbException(e);
		}
	}

	public DBQueryImpl setParameter(int position, Object value) {
		indexParams.put(position, new TypedValue(Types.OTHER, value));
		return this;
	}

	public DBQueryImpl setParameter(int position, Object value, int sqlType) {
		indexParams.put(position, new TypedValue(sqlType, value));
		return this;
	}

	@Override
	public String toString() {
		return "SqlQuery:[" + sql + "]";
	}
}
