package com.andreev.jdbc;

import org.postgresql.Driver;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;

        /*String sql = """
                     SELECT *
                     FROM flight
                     WHERE flight_no LIKE 'B%';
                    """;*/

        /*String sql2 = """
                CREATE TABLE info (
                id INT SERIAL PRIMARY KEY,
                data VARCHAR(32) NOT NULL
                );
                """;*/

        String sql3 = """
                INSERT INTO info (data) VALUES ('first data');
                """;

        try(Connection connection = ConnectionManager.open();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)){
            System.out.println(connection.getTransactionIsolation());
            //var response = statement.executeQuery(sql);
            //var response = statement.execute(sql2);

            var response = statement.executeUpdate(sql3, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            System.out.println(response);
            if(generatedKeys.next()){
                System.out.println(generatedKeys.getInt("id"));
            }





            /*var response = statement.executeQuery(sql);
            while(response.next()){
                System.out.println(response.getLong("id"));
                System.out.println(response.getString("flight_no"));
                System.out.println(response.getString("departure_date"));
                System.out.println("---------");
                //response.updateString("flight_no", "CV9827");
            }*/

        }
        catch(SQLException exc){
            //exc.printStackTrace();
        }

        long flight_id = 2;
        var listId = getTicketByFlightId(flight_id);
        System.out.println(listId);
    }

    private static List<Long> getTicketByFlightId(Long flightId){
        String sql = """
                SELECT id
                FROM ticket
                WHERE flight_id = %s
                """.formatted(flightId);

        List<Long> list = null;
        try(Connection connection = ConnectionManager.open();
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)){
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                list.add(result.getObject("id", Long.class));
            }
        }
        catch(SQLException exc){
            throw new RuntimeException(exc);
        }
        return list;
    }
}
