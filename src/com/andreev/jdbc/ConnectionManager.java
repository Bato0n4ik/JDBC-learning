package com.andreev.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private final static String NAME_KEY = "db.name";
    private final static String PASSWORD_KEY = "db.password";
    private final static String URL_KEY = "db.url";



    private ConnectionManager(){}

    public static Connection open()  {
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
}
