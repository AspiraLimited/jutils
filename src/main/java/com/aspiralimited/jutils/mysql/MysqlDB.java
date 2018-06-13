package com.aspiralimited.jutils.mysql;

import com.aspiralimited.jutils.AbbLogger;
import com.aspiralimited.jutils.ThrowingConsumer;
import com.aspiralimited.jutils.ConfigLoader;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.*;


public class MysqlDB {
    private final static AbbLogger logger = new AbbLogger();

    private static MysqlDB mysqlDB;

    private final HikariDataSource readPool;
    private final HikariDataSource writePool;

    public MysqlDB(String database) throws IOException {
        this.readPool = initPool("/config/ReadJdbcConfig.json", "read", database);
        this.writePool = initPool("/config/WriteJdbcConfig.json", "write", database);
    }

    public static MysqlDB mysqlDB() {
        return mysqlDB(null);
    }

    public static MysqlDB mysqlDB(String database) {
        if (mysqlDB != null) return mysqlDB;

        synchronized (MysqlDB.class) {
            try {
                mysqlDB = new MysqlDB(database);
            } catch (IOException e) {
                throw new Error(e);
            }
        }

        return mysqlDB;
    }

    public String readStats() {
        return stats(readPool);
    }

    public String writeStats() {
        return stats(writePool);
    }

    private String stats(HikariDataSource pool) {
        return pool.getPoolName() +
                " total:" + pool.getHikariPoolMXBean().getTotalConnections() +
                " idle:" + pool.getHikariPoolMXBean().getIdleConnections() +
                " active:" + pool.getHikariPoolMXBean().getActiveConnections() +
                " threads awaiting:" + pool.getHikariPoolMXBean().getThreadsAwaitingConnection()
                ;
    }

    private HikariDataSource initPool(String fileName, String name) throws IOException {
        return initPool(fileName, name, null);
    }

    private HikariDataSource initPool(String fileName, String name, String database) throws IOException {
        try {
            HikariDataSource pool = new HikariDataSource();

            JdbcConfig jdbcConfig = ConfigLoader.loadConfig(JdbcConfig.class, fileName);

            pool.setPoolName("HikariCPPool[" + name + "]");
            pool.setRegisterMbeans(true);

            pool.setConnectionTimeout(jdbcConfig.connectionTimeout);
            pool.setIdleTimeout(jdbcConfig.idleTimeout);
            pool.setMaxLifetime(jdbcConfig.maxLifetime);
            pool.setDriverClassName(jdbcConfig.driver);
            pool.setJdbcUrl(jdbcConfig.url(database));
            pool.setReadOnly(jdbcConfig.isReadOnly);
            // pool.setLeakDetectionThreshold(15000);
            // pool.setConnectionTestQuery("SELECT 1");
            pool.setMaximumPoolSize(jdbcConfig.maximumPoolSize);
            pool.setValidationTimeout(jdbcConfig.validationTimeout);
            pool.setLeakDetectionThreshold(jdbcConfig.leakDetectionThreshold);

            pool.addDataSourceProperty("user", jdbcConfig.username);
            pool.addDataSourceProperty("password", jdbcConfig.password);
            pool.addDataSourceProperty("characterEncoding", jdbcConfig.characterEncoding);
            pool.addDataSourceProperty("useSSL", jdbcConfig.useSSL);
            pool.addDataSourceProperty("cachePrepStmts", jdbcConfig.cachePrepStmts);
            pool.addDataSourceProperty("prepStmtCacheSize", jdbcConfig.prepStmtCacheSize);
            pool.addDataSourceProperty("prepStmtCacheSqlLimit", jdbcConfig.prepStmtCacheSqlLimit);
            pool.addDataSourceProperty("useCompression", jdbcConfig.useCompression);
            pool.addDataSourceProperty("useUnicode", jdbcConfig.useUnicode);
            pool.addDataSourceProperty("autoReconnect", jdbcConfig.autoReconnect);
            pool.addDataSourceProperty("rewriteBatchedStatements", jdbcConfig.rewriteBatchedStatements);
            pool.addDataSourceProperty("useServerPrepStmts", jdbcConfig.useServerPrepStmts);
            pool.addDataSourceProperty("profileSql", jdbcConfig.profileSql);
            pool.addDataSourceProperty("connectTimeout", jdbcConfig.connectTimeout);

            return pool;
        } catch (IOException e) {
            logger.error("Error by loadConfig " + name + "PoolConnection.", e);
            throw e;
        }
    }

    public void select(String sql, ThrowingConsumer<ResultSet> resultSetConsumer) {
        select(sql, null, resultSetConsumer);
    }

    public void select(String sql, ThrowingConsumer<PreparedStatement> preparedStatementConsumer, ThrowingConsumer<ResultSet> resultSetConsumer) {
        ResultSet rs = null;

        try (Connection conn = readPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (preparedStatementConsumer != null)
                preparedStatementConsumer.accept(ps);

            rs = ps.executeQuery();

            while (rs.next())
                resultSetConsumer.accept(rs);

        } catch (SQLException e) {
            logSQLException(e, sql, null);

        } finally {
            closeConnections(null, null, rs);
        }
    }

    public int update(String sql) {
        return update(sql, x -> {
        });
    }

    public int update(String sql, ThrowingConsumer<PreparedStatement> preparedStatementConsumer) {
        int count = 0;

        try (Connection conn = writePool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (preparedStatementConsumer != null)
                preparedStatementConsumer.accept(ps);

            ps.executeUpdate();
            count = ps.getUpdateCount();

        } catch (SQLException e) {
            logSQLException(e, sql, null);

        }

        return count;
    }

//    public void transaction(ThrowingConsumer<Connection> connectionConsumer) {
//        try (Connection conn = pool.getConnection()) {
//            conn.setAutoCommit(false);
//            connectionConsumer.accept(conn);
//            conn.commit();
//
//        } catch (SQLException e) {
//            logSQLException(e, null, null);
//        }
//    }

    public int insert(String sql, ThrowingConsumer<PreparedStatement> preparedStatementConsumer) {
        int count = 0;

        try (Connection conn = writePool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (preparedStatementConsumer != null)
                preparedStatementConsumer.accept(ps);

            count = ps.executeUpdate();

        } catch (SQLException e) {
            logSQLException(e, sql, null);

        }

        return count;
    }

    public int multiInsert(String sql, ThrowingConsumer<PreparedStatement> preparedStatementConsumer) {
        return multiInsert(sql, preparedStatementConsumer, null);
    }

    //TODO batchSize
    public int multiInsert(String sql, ThrowingConsumer<PreparedStatement> preparedStatementConsumer, ThrowingConsumer<SQLException> exception) {
        int count = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = writePool.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if (preparedStatementConsumer != null)
                preparedStatementConsumer.accept(ps);

            int[] counts = ps.executeBatch();
            for (int l : counts) {
                if (l == Statement.SUCCESS_NO_INFO) {
                    count++;
                } else if (l >= 0) {
                    count = (count + l);
                } else if (l == Statement.EXECUTE_FAILED) {
                    // skip counting
                }
            }
            conn.commit();

        } catch (SQLException e) {
            logSQLException(e, sql, ps);
            if (exception != null) exception.accept(e);

        } finally {
            closeConnections(conn, ps, null);
        }

        return count;
    }

    private void logSQLException(SQLException e, String sql, PreparedStatement ps) {
        // TODO NewRelic
        if (ps == null)
            logger.error("Error by select with sql: '{}'", sql, e);
        else
            logger.error("Error by select with ps: '{}' {}", ps.toString(), e);
    }

    private void closeConnections(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            logger.error("Error by close ResultSet: ", e);
            // TODO NewRelic
        }

        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            // TODO NewRelic
            logger.error("Error by close PreparedStatement: ", e);
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            // TODO NewRelic
            logger.error("Error by close Connection: ", e);
        }
    }
}
