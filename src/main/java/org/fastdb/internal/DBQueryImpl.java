package org.fastdb.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fastdb.DBConfig;
import org.fastdb.DBQuery;
import org.fastdb.DBRow;
import org.fastdb.DBServer;
import org.fastdb.FastdbException;
import org.fastdb.PreparedStatementSetter;
import org.fastdb.RowMapper;
import org.fastdb.SysProperties;
import org.fastdb.Transaction;
import org.fastdb.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBQueryImpl implements DBQuery {

    private static Logger            LOGGER      = LoggerFactory.getLogger(DBQueryImpl.class);

    private final DBServer           dbServer;

    private final String             sql;

    private Map<Integer, TypedValue> indexParams = new HashMap<Integer, TypedValue>();

    public DBQueryImpl(DBServer dbServer, String sql) {
        this.dbServer = dbServer;
        this.sql = sql;
    }

    public List<DBRow> findList() {
        if (SysProperties.debugSql()) {
            LOGGER.info(this.sql);
        }
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

    public DBRow findUnique() {
        List<DBRow> list = findList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public int executeUpdate() {
        Transaction tx = this.dbServer.currentTransaction();
        if (tx != null) {
            Connection conn = tx.getConnection();
            return executeUpdate(conn);
        } else {
            try {
                Connection conn = this.dbServer.getConnection();
                try {
                    return executeUpdate(conn);
                } finally {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new FastdbException(e);
            }
        }
    }

    private int executeUpdate(Connection conn) {
        if (SysProperties.debugSql()) {
            LOGGER.info(this.sql);
        }
        try {
            PreparedStatement pstmt = conn.prepareStatement(this.sql);
            try {
                doPrepare(pstmt);
                return pstmt.executeUpdate();
            } finally {
                pstmt.close();
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

    @Override
    public <T> List<T> executeQuery(PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        if (SysProperties.debugSql()) {
            LOGGER.info(this.sql);
        }
        List<T> result = new ArrayList<T>();
        try {
            Connection conn = this.dbServer.getConnection();
            try {
                PreparedStatement pstmt = conn.prepareStatement(this.sql);
                try {
                    preparedStatementSetter.setValues(pstmt);
                    ResultSet rs = pstmt.executeQuery();
                    try {
                        while (rs.next()) {
                            result.add(rowMapper.mapRow(rs, rs.getRow()));
                        }
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
        return result;
    }

    @Override
    public <T> List<T> findList(Class<T> klass) {
        try {
            return DBUtils.buildResult(this.dbServer, DBConfig.getBeanDescriptor(klass), findList());
        } catch (Exception e) {
            throw new FastdbException(e.getMessage());
        }
    }

    @Override
    public <T> T findUnique(Class<T> klass) {
        try {
            return DBUtils.buildResult(this.dbServer, DBConfig.getBeanDescriptor(klass), findUnique());
        } catch (Exception e) {
            throw new FastdbException(e.getMessage());
        }
    }
}
