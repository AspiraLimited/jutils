package com.aspiralimited.jutils.mysql;

public abstract class BaseDao {

    protected final MysqlDB mysqlDB;

    protected BaseDao(MysqlDB mysqlDB) {
        this.mysqlDB = mysqlDB;
    }
}
