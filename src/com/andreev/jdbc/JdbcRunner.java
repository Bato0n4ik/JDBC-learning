package com.andreev.jdbc;

import org.postgresql.Driver;
import org.postgresql.core.TypeInfo;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {

       /* long flight_id = 2;
        var listId = getTicketByFlightId(flight_id);
        System.out.println(listId);*/

        /*var start = LocalDateTime.of(2022, 12, 10, 15, 25);
        var begin = LocalDateTime.now();
        System.out.println(getFlightsBBetween(start, begin));*/

        checkMetaData();

    }

    private static void checkMetaData(){
        try(var connection = ConnectionManager.open()){
            var metaData = connection.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            while(catalogs.next()){
                var catalog = catalogs.getString(1);
                //System.out.println(catalog);

                ResultSet schemas = metaData.getSchemas();
                while(schemas.next()){
                    var schema = schemas.getString("TABLE_SCHEM");
                    //System.out.println(schema);
                    if(schema.equals("public"))
                    {
                        var tables = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"});
                        while(tables.next()){
                            var table = tables.getString("TABLE_NAME");
                            System.out.println("Table name: " + table + "\n");
                            var columns = metaData.getColumns(catalog, schema, "%", "%");
                            while(columns.next()){
                                System.out.println("Name: " + columns.getString("COLUMN_NAME") + "\t" +
                                        "Type: " + columns.getString("TYPE_NAME"));
                            }
                        }
                    }
                }
            }
        }
        catch(SQLException exc){
            throw new RuntimeException(exc);
        }
    }

    private static List<Long> getFlightsBBetween(LocalDateTime start, LocalDateTime end){
        String sql = """
                SELECT id
                FROM flight
                WHERE departure_date BETWEEN ? AND ?
                """;

        List<Long> list = new ArrayList<>();
        try(var connection = ConnectionManager.open();
        var statement = connection.prepareStatement(sql)){

            statement.setFetchSize(50);
            statement.setQueryTimeout(10);

            System.out.println(statement);
            statement.setTimestamp(1, Timestamp.valueOf(start));
            System.out.println(statement);
            statement.setTimestamp(2, Timestamp.valueOf(end));
            System.out.println(statement);

            ResultSet result = statement.executeQuery();
            while(result.next()){
                list.add(result.getObject("id", Long.class));
            }
        }
        catch(SQLException exc){
            throw new RuntimeException(exc);
        }

        return list;
    }

    private static List<Long> getTicketByFlightId(long flightId){
        String sql = """
                SELECT id
                FROM ticket
                WHERE flight_id = ?
                """;

        List<Long> list = new ArrayList<>();
        try(Connection connection = ConnectionManager.open();
        var statement = connection.prepareStatement(sql)){
            statement.setLong(1, flightId);
            var result = statement.executeQuery();
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
