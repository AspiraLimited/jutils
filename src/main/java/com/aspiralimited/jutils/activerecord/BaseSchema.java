package com.aspiralimited.jutils.activerecord;


import com.aspiralimited.jutils.AbbLogger;
import com.aspiralimited.jutils.Triple;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public abstract class BaseSchema {
    private static final AbbLogger logger = new AbbLogger();

    public abstract Map<Integer, Triple<String, String, String>> fields();

    public abstract String tableName();

    public <T extends ActiveRecord> T fillFromResultSet(ResultSet rs, T record) {
        try {
            for (Map.Entry<Integer, Triple<String, String, String>> entry : fields().entrySet()) {
                String methodName = entry.getValue().right;
                Method method;

                if (entry.getValue().middle.equals("Long")) {
                    method = record.getClass().getMethod(methodName, Long.class);
                    long val = rs.getLong(entry.getValue().left);

                    if (rs.wasNull()) {
                        Object arg = null;
                        method.invoke(record, arg);
                    } else {
                        method.invoke(record, val);
                    }

                } else if (entry.getValue().middle.equals("Float")) {
                    method = record.getClass().getMethod(methodName, Float.class);
                    float val = rs.getFloat(entry.getValue().left);

                    if (rs.wasNull()) {
                        Object arg = null;
                        method.invoke(record, arg);
                    } else {
                        method.invoke(record, val);
                    }

                } else if (entry.getValue().middle.equals("String")) {
                    method = record.getClass().getMethod(methodName, String.class);
                    method.invoke(record, rs.getString(entry.getValue().left));

                } else if (entry.getValue().middle.equals("boolean")) {
                    method = record.getClass().getMethod(methodName, boolean.class);
                    method.invoke(record, rs.getBoolean(entry.getValue().left));

                } else if (entry.getValue().middle.equals("Date")) {
                    method = record.getClass().getMethod(methodName, Date.class);
                    Timestamp val = rs.getTimestamp(entry.getValue().left);

                    if (val != null) {
                        method.invoke(record, new Date(val.getTime()));
                    }

                } else if (entry.getValue().middle.equals("int")) {
                    method = record.getClass().getMethod(methodName, int.class);
                    method.invoke(record, rs.getInt(entry.getValue().left));

                } else if (entry.getValue().middle.equals("float")) {
                    method = record.getClass().getMethod(methodName, float.class);
                    method.invoke(record, rs.getFloat(entry.getValue().left));

                } else if (entry.getValue().middle.equals("Integer")) {
                    method = record.getClass().getMethod(methodName, Integer.class);
                    int val = rs.getInt(entry.getValue().left);

                    if (rs.wasNull()) {
                        Object arg = null;
                        method.invoke(record, arg);
                    } else {
                        method.invoke(record, val);
                    }

                } else {
                    throw new Exception("Unknow type [" + tableName() + "]: " + entry.getValue().middle);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        return record;
    }
}
