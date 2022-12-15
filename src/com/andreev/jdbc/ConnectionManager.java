package com.andreev.jdbc;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionManager {
    private final static String NAME_KEY = "db.name";
    private final static String PASSWORD_KEY = "db.password";
    private final static String URL_KEY = "db.url";
    private final static String POOL_SIZE_KEY = "db.pool.size";
    private static BlockingQueue<Connection> connectionPool;
    private static List<Connection> copyConnectionPool;

    static {
        //loadDriver();
        initConnectionPool();
    }

    private static void initConnectionPool() {
        int poolSize = Integer.parseInt(PropertiesUtil.get(POOL_SIZE_KEY));
        int size = poolSize > 0 ? poolSize : 10;
        copyConnectionPool = new ArrayList<>(size);
        connectionPool = new ArrayBlockingQueue<>(size);
        for(int i = 0; i < size; i++){
            Connection connection = ConnectionManager.open();
            var proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(),
                    new Class[]{Connection.class}, (proxy, method, args) -> method.getName().equals("close")
                            ? connectionPool.add((Connection) proxy) : method.invoke(connection, args));
            connectionPool.add(proxyConnection);
            copyConnectionPool.add(connection);

        }
    }

    public static Connection get(){
        try{
            return connectionPool.take();
        }
        catch(Exception exc){
            throw new RuntimeException(exc);
        }
    }


    private ConnectionManager(){}

    private static Connection open()  {
        try{
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(NAME_KEY)
                    ,PropertiesUtil.get(PASSWORD_KEY));
        }
        catch(SQLException exc){
            throw new RuntimeException(exc);
        }

    }

    public static void closePool() {
        try{
            for(Connection connection : copyConnectionPool){
                connection.close();
            }
        }
        catch(SQLException exc){
            throw new RuntimeException(exc);
        }
    }

    public static void loadDriver(){
        try{
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
