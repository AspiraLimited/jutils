package com.aspiralimited.jutils.mysql;

import com.aspiralimited.jutils.ConfigLoader;
import com.aspiralimited.jutils.ThrowingConsumer;
import com.aspiralimited.jutils.logger.AbbLogger;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.*;

import static java.lang.System.currentTimeMillis;
import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class MysqlDB {
    private final static AbbLogger logger = new AbbLogger();

    private static MysqlDB mysqlDB;

    private final HikariDataSource readPool;
    private final HikariDataSource writePool;

    private boolean debug = false;

    private MysqlDB(String database) throws IOException {
        this(database, "");
    }

    private MysqlDB(String database, String prefixPath) throws IOException {
        this.readPool = initPool((prefixPath == null ? "" : prefixPath) + "/config/ReadJdbcConfig.json", "read", database);
        this.writePool = initPool((prefixPath == null ? "" : prefixPath) + "/config/WriteJdbcConfig.json", "write", database);
    }

    public static MysqlDB mysqlDB() {
        return mysqlDB(null);
    }

    public static MysqlDB mysqlDB(String database) {
        return mysqlDB(database, null);
    }

    public static MysqlDB mysqlDB(String database, String prefixPath) {
        if (mysqlDB != null) return mysqlDB;

        synchronized (MysqlDB.class) {
            try {
                mysqlDB = new MysqlDB(database, prefixPath);
            } catch (IOException e) {
                throw new Error(e);
            }
        }

        return mysqlDB;
    }

    // for integration tests
    public static MysqlDB mysqlDB(JdbcConfig readConfig, JdbcConfig writeConfig, String database) {
        if (mysqlDB != null) return mysqlDB;

        synchronized (MysqlDB.class) {
            try {
                mysqlDB = new MysqlDB(readConfig, writeConfig, database);
            } catch (IOException e) {
                throw new Error(e);
            }
        }

        return mysqlDB;
    }

    private MysqlDB(JdbcConfig readConfig, JdbcConfig writeConfig, String database) throws IOException {
        this.readPool = initPool(readConfig, "read", database);
        this.writePool = initPool(writeConfig, "write", database);
    }

    public boolean debug() {
        return debug;
    }

    public MysqlDB debug(boolean debug) {
        this.debug = debug;
        return this;
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
            JdbcConfig jdbcConfig = ConfigLoader.loadConfig(JdbcConfig.class, fileName);
            return initPool(jdbcConfig, name, database);
        } catch (IOException e) {
            logger.error("Error in loadConfig(fileName=" + fileName + ")", e);
            throw e;
        }
    }

    private HikariDataSource initPool(JdbcConfig jdbcConfig, String name, String database) {
        HikariDataSource pool = new HikariDataSource();


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

        if (jdbcConfig.transactionIsolation != null && !jdbcConfig.transactionIsolation.isEmpty()) {
            pool.addDataSourceProperty("transactionIsolation", jdbcConfig.transactionIsolation);
        }

        return pool;
    }

    public Connection getSelectConnection() throws SQLException {
        return readPool.getConnection();
    }

    public Connection getUpdateConnection() throws SQLException {
        return writePool.getConnection();
    }

    public void select(String sql, ThrowingConsumer<ResultSet> resultSetConsumer) {
        select(sql, null, resultSetConsumer);
    }

    public void select(String sql, ThrowingConsumer<PreparedStatement> psConsumer, ThrowingConsumer<ResultSet> rsConsumer) {
        long start = currentTimeMillis();

        ResultSet rs = null;

        try (Connection conn = readPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (psConsumer != null)
                psConsumer.accept(ps);

            rs = ps.executeQuery();

            while (rs.next())
                rsConsumer.accept(rs);

        } catch (SQLException e) {
            logSQLException(e, sql, null);

        } finally {
            closeConnections(null, null, rs);
            printDebugInfo(start, "SELECT", sql);
        }
    }

    public int update(String sql) {
        return update(sql, x -> {
        });
    }

    public int update(String sql, ThrowingConsumer<PreparedStatement> psConsumer) {
        long start = currentTimeMillis();

        int count = 0;

        try (Connection conn = writePool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (psConsumer != null)
                psConsumer.accept(ps);

            ps.executeUpdate();
            count = ps.getUpdateCount();

        } catch (SQLException e) {
            logSQLException(e, sql, null);

        } finally {
            printDebugInfo(start, "UPDATE", sql);

        }

        return count;
    }

    public int insert(String sql, ThrowingConsumer<PreparedStatement> psConsumer) {
        long start = currentTimeMillis();

        int count = 0;

        try (Connection conn = writePool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (psConsumer != null)
                psConsumer.accept(ps);

            count = ps.executeUpdate();

        } catch (SQLException e) {
            logSQLException(e, sql, null);

        } finally {
            printDebugInfo(start, "INSERT", sql);
        }

        return count;
    }

    // WARN return only the first ID. Do not use for multi inserts
    public int insert(String sql, ThrowingConsumer<PreparedStatement> psConsumer, ThrowingConsumer<ResultSet> rsConsumer) {
        long start = currentTimeMillis();

        int count = 0;

        try (Connection conn = writePool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, RETURN_GENERATED_KEYS)) {

            if (psConsumer != null)
                psConsumer.accept(ps);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                rsConsumer.accept(rs);
                count++;
            }

            while (rs.next())
                count++;

        } catch (SQLException e) {
            logSQLException(e, sql, null);

        } finally {
            printDebugInfo(start, "INSERT", sql);

        }

        return count;
    }

    public int multiInsert(String sql, ThrowingConsumer<PreparedStatement> preparedStatementConsumer) {
        return multiInsert(sql, preparedStatementConsumer, null);
    }

    //TODO batchSize
    public int multiInsert(String sql, ThrowingConsumer<PreparedStatement> psConsumer, ThrowingConsumer<SQLException> exception) {
        long start = currentTimeMillis();

        int count = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = writePool.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql, RETURN_GENERATED_KEYS);

            if (psConsumer != null)
                psConsumer.accept(ps);

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
            printDebugInfo(start, "INSERT", sql);
        }

        return count;
    }

    private void logSQLException(SQLException e, String sql, PreparedStatement ps) {
        if (ps == null)
            logger.error("Error by sql: '{}'", sql, e);
        else
            logger.error("Error by ps: '{}' {}", ps.toString(), e);
    }

    private void closeConnections(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            logger.error("Error by close ResultSet: ", e);
        }

        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            logger.error("Error by close PreparedStatement: ", e);
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            logger.error("Error by close Connection: ", e);
        }
    }

    private void printDebugInfo(long start, String type, String sql) {
        if (!debug()) return;

        long duration = (currentTimeMillis() - start);

        String msg = type + "\t" + duration + "\t" + sql;

        if (duration < 10) {
            logger.debug(msg);

        } else if (duration < 100) {
            logger.info(msg);

        } else {
            logger.warn(msg);

        }
    }
}
