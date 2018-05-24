package com.allbestbets.jutils.mysql;

import java.util.Set;

import static java.util.stream.Collectors.joining;

public class JdbcConfig {

    public String driver;
    public String failover;
    public Set<String> hosts;
    public int port;
    public String database;
    public String username;
    public String password;
    public int maximumPoolSize;
    public String characterEncoding;
    public boolean useSSL;
    public boolean useCompression;
    public boolean useUnicode;
    public boolean autoReconnect;
    public boolean rewriteBatchedStatements;
    public int idleTimeout;
    public int maxLifetime;
    public boolean isReadOnly; // use ?
    public boolean profileSql;
    public long leakDetectionThreshold;

    public long connectTimeout;
    public long connectionTimeout;
    public long validationTimeout;

    public boolean cachePrepStmts;
    public boolean useServerPrepStmts;
    public long prepStmtCacheSize;
    public long prepStmtCacheSqlLimit;

    public String url() {
        return url(null);
    }

    public String url(String database) {
        return "jdbc:mariadb:" +
                failover + "//" +
                hosts.stream().map(s -> s + ":" + port).collect(joining(",")) + "/" +
                (database == null ? this.database : database);
    }
}
