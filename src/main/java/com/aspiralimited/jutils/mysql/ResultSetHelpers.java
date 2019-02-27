package com.aspiralimited.jutils.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ResultSetHelpers {

    public static Float getFloat(ResultSet rs, String field) throws SQLException {
        Float value = rs.getFloat(field);
        if (rs.wasNull()) return null;
        return value;
    }

    public static Integer getInteger(ResultSet rs, String field) throws SQLException {
        Integer value = rs.getInt(field);
        if (rs.wasNull()) return null;
        return value;
    }

    public static Long getLong(ResultSet rs, String field) throws SQLException {
        Long value = rs.getLong(field);
        if (rs.wasNull()) return null;
        return value;
    }

    public static Double getDouble(ResultSet rs, String field) throws SQLException {
        Double value = rs.getDouble(field);
        if (rs.wasNull()) return null;
        return value;
    }

    public static Date getDate(ResultSet rs, String field) throws SQLException {
        Date value = rs.getTimestamp(field);
        if (rs.wasNull()) return null;
        return value;
    }
}
