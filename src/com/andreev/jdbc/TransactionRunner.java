package com.andreev.jdbc;

import org.postgresql.shaded.com.ongres.stringprep.StringPrep;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionRunner {

    public static void main(String[] args) throws SQLException {

        long flightId = 8;

        String sql = "DELETE FROM flight WHERE id = " + flightId;
        String sqlTwo = "DELETE FROM ticket WHERE flight_id = " + flightId;
        Connection connection = null;
        Statement statement = null;


        try{
            connection = ConnectionManager.get();
            statement = connection.createStatement();

            connection.setAutoCommit(false);

            statement.addBatch(sqlTwo);
            statement.addBatch(sql);

            var resultRequest = statement.executeBatch();

            connection.commit();

        }
        catch(Exception exc){
            if(connection != null){
                connection.rollback();
            }
            throw exc;
        }
        finally{
            if(connection != null){
                connection.close();
            }
            if(statement != null){
                statement.close();
            }
        }

    }
}
