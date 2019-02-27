package com.aspiralimited.jutils.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDao {

    protected final MysqlDB mysqlDB;

    protected BaseDao(MysqlDB mysqlDB) {
        this.mysqlDB = mysqlDB;
    }

    // TODO move to jutils
    protected Float getFloat(ResultSet rs, String field) throws SQLException {
        Float value = rs.getFloat(field);
        if (rs.wasNull()) return null;
        return value;
    }

    // TODO move to jutils
    protected Integer getInteger(ResultSet rs, String field) throws SQLException {
        Integer value = rs.getInt(field);
        if (rs.wasNull()) return null;
        return value;
    }
}
