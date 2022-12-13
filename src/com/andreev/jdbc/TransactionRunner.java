package com.andreev.jdbc;

import org.postgresql.shaded.com.ongres.stringprep.StringPrep;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRunner {

    public static void main(String[] args) throws SQLException {

        long flightId = 8;

        String sql = "DELETE FROM flight WHERE id = ?";
        String sqlTwo = "DELETE FROM ticket WHERE flight_id = ?";
        Connection connection = null;
        PreparedStatement statementOne = null;
        PreparedStatement statementTwo = null;

        try{
            connection = ConnectionManager.open();
            statementTwo = connection.prepareStatement(sqlTwo);
            statementOne = connection.prepareStatement(sql);

            connection.setAutoCommit(false);

            statementOne.setFetchSize(50);
            statementTwo.setFetchSize(50);

            statementOne.setLong(1, flightId);
            statementTwo.setLong(1, flightId);

            statementTwo.executeUpdate();
            if(true){
                throw new RuntimeException("Ooops!");
            }
            statementOne.executeUpdate();
            connection.commit();
        }
        catch(Exception exc){
            if(connection != null){
                connection.rollback();
            }
            throw exc;
        }
        finally{
            if(statementOne != null){
                statementOne.close();
            }
            if(statementTwo != null){
                statementTwo.close();
            }
            if(connection != null){
                connection.close();
            }
        }

    }
}
