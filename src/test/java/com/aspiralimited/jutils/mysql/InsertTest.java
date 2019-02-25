package com.aspiralimited.jutils.mysql;

import org.junit.Test;

import static com.aspiralimited.jutils.mysql.MysqlDB.mysqlDB;
import static java.lang.System.currentTimeMillis;

public class InsertTest {

    @Test
    public void insertWithReturnId() {
        // long time = currentTimeMillis();
        long time = 1551100830363L;

        int c = mysqlDB().insert("INSERT INTO hosts(name,node,server_type) VALUES(?, ?, ?),(?, ?, ?) ON DUPLICATE KEY UPDATE name=CONCAT(VALUES(name), '" + currentTimeMillis() + "')", ps -> {
            ps.setString(1, "host-" + time);
            ps.setString(2, "tmp-node");
            ps.setString(3, "tmp-server-type");

            ps.setString(4, "host-" + time + "aaa");
            ps.setString(5, "tmp-node");
            ps.setString(6, "tmp-server-type");
        }, rs -> {
            System.out.println(rs.getLong(1));
        });
        System.out.println(c);
    }
}
