package com.aspiralimited.jutils.mysql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class BaseDao {

    protected final MysqlDB mysqlDB;

    protected BaseDao(MysqlDB mysqlDB) {
        this.mysqlDB = mysqlDB;
    }

    protected void updateTable(String tableName, long id, Map<String, Object> fields) {
        if (fields.isEmpty()) return;

        final String[] sql = {"UPDATE " + tableName + " SET "};
        final int[] i = {0};

        List<String> updates = new ArrayList<>();
        fields.forEach((k, v) -> {
            updates.add(k + " = " + ((v instanceof java.lang.String || v instanceof java.util.Date) ? "?" : v));
        });
        sql[0] += String.join(", ", updates);

        sql[0] += " WHERE id = " + id;

        mysqlDB.update(sql[0],
                ps -> {
                    for (Object v : fields.values()) {
                        if (v instanceof java.lang.String)
                            ps.setString(++i[0], (String) v);

                        else if (v instanceof java.util.Date) {
                            java.sql.Timestamp sqlDate = new java.sql.Timestamp(((Date) v).getTime());
                            ps.setTimestamp(++i[0], sqlDate);

                        }
                    }
                }
        );
    }
}
