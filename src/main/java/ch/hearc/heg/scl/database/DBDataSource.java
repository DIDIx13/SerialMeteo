package ch.hearc.heg.scl.database;

import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBDataSource {
    private static OracleDataSource ds = null;
    public static Connection getJDBCConnection(){
        try {
            if (ds == null) {
                ds = new OracleDataSource();
                ds.setDriverType("thin");
                ds.setServerName("db.ig.he-arc.ch");
                ds.setPortNumber(1521);
                ds.setDatabaseName("ens"); // sid
                ds.setUser("DARWIN_AMELI");
                ds.setPassword("DARWIN_AMELI");
            }
            return ds.getConnection();//ds.getConnection().setAutoCommit(false)
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }}}