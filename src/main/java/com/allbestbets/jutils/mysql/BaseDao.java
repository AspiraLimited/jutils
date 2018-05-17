package com.allbestbets.jutils.mysql;

abstract class BaseDao {

    final MysqlDB mysqlDB;

    BaseDao(MysqlDB mysqlDB) {
        this.mysqlDB = mysqlDB;
    }
}
