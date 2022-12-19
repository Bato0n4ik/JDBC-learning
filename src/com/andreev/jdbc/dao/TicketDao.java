package com.andreev.jdbc.dao;

import com.andreev.jdbc.ConnectionManager;
import com.andreev.jdbc.CustomDAOException;
import com.andreev.jdbc.dto.TicketFilter;
import com.andreev.jdbc.entity.Ticket;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao {

    private static volatile TicketDao single = new TicketDao();
    private final String SQL_INSERT = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
             VALUES (?,?,?,?,?);
            """;
    private final String SQL_DELETE = """
            DELETE FROM ticket WHERE id = ?;
            """;

    private final String SQL_UPDATE = """
            UPDATE ticket SET 
            id = ?,
            passenger_no = ?,
            passenger_name = ?,
            flight_id =?,
            seat_no = ?,
            cost = ?
            WHERE id = ?;
            """;

    private final String SQL_SELECT_BY_ID = """
            SELECT * FROM ticket
            WHERE id = ?;
            """;

    private final String SQL_SELECT_ALL = """
            SELECT * FROM ticket
            """;

    private TicketDao(){}

    public static TicketDao getObject(){
        synchronized(single){
            return single;
        }
    }

    public List<Ticket> findAll(TicketFilter filter){
        List<String> whereSQL = new ArrayList<>();
        List<Object> filters = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();


        if(filter.seatNo() != null){
            filters.add("%" + filter.seatNo() + "%");
            whereSQL.add("seat_no LIKE ?");
        }
        if(filter.passengerName() != null){
            filters.add(filter.passengerName());
            whereSQL.add("passenger_name = ?");
        }
        filters.add(filter.limit());
        filters.add(filter.offset());

        String sqlQuery = SQL_SELECT_ALL + whereSQL.stream().collect(Collectors.joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));
        try(var connection = ConnectionManager.get();
            var prepareStatement = connection.prepareStatement(sqlQuery)){
            for(int i = 0; i< filters.size(); i++){
                prepareStatement.setObject(i+1, filters.get(i));
            }
            prepareStatement.executeQuery();
            ResultSet result = prepareStatement.executeQuery();
            while(result.next()){
                tickets.add(getTicket(result));
            }
        }
        catch(SQLException exc){
            throw new CustomDAOException(exc);
            //exc.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> findAll(){
        List<Ticket> ticketList = new ArrayList<>();
        try(var connection = ConnectionManager.get();
        var prepareStatement = connection.prepareStatement(SQL_SELECT_ALL)){

            prepareStatement.executeQuery();
            var result = prepareStatement.getGeneratedKeys();
            while(result.next()){
                ticketList.add(getTicket(result));
            }
        }
        catch(SQLException exc){
            throw new CustomDAOException(exc);
        }

        return ticketList;
    }

    public Optional<Ticket> findById(long id){
        Ticket ticket = null;
        try(Connection connection = ConnectionManager.get();
        var prepareStatement = connection.prepareStatement(SQL_SELECT_BY_ID)){

            prepareStatement.setLong(1, id);
            var result = prepareStatement.executeQuery();
            if(result.next()){
                ticket = getTicket(result);
            }

        }
        catch(SQLException exc){
            throw new CustomDAOException(exc);
        }
        return Optional.ofNullable(ticket);
    }

    public void updateDB(Ticket ticket){
        try(Connection connection = ConnectionManager.get();
        var prepareStatement = connection.prepareStatement(SQL_UPDATE)){

            prepareStatement.setLong(1, ticket.getId());
            prepareStatement.setString(2, ticket.getPassengerNo());
            prepareStatement.setString(3,ticket.getPassengerName());
            prepareStatement.setLong(4, ticket.getFlightId());
            prepareStatement.setString(5, ticket.getSeatNo());
            prepareStatement.setBigDecimal(6, ticket.getCost());

            int result = prepareStatement.executeUpdate();
            System.out.println("Executing condition your SQL-request is: " + (result > 0));
        }
        catch(SQLException exc){
            throw new CustomDAOException(exc);
        }
    }

    public Ticket insertInDB(Ticket ticket) {
        try(Connection connection = ConnectionManager.get();
        var prepareStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)){

            prepareStatement.setString(1, ticket.getPassengerNo());
            prepareStatement.setString(2, ticket.getPassengerName());
            prepareStatement.setLong(3, ticket.getFlightId());
            prepareStatement.setString(4, ticket.getSeatNo());
            prepareStatement.setBigDecimal(5, ticket.getCost());

            prepareStatement.executeUpdate();

            var ticketSet = prepareStatement.getGeneratedKeys();
            if(ticketSet.next()){
                ticket.setId(ticketSet.getLong("id"));
            }
        }
        catch(SQLException exc){
            exc.printStackTrace();
        }
        return ticket;
    }

    public boolean delete(long id){
        try(Connection connection = ConnectionManager.get();
        var prepareStatement = connection.prepareStatement(SQL_DELETE)){

            prepareStatement.setLong(1, id);

            int resultQuery = prepareStatement.executeUpdate();
            return resultQuery > 0;
        }
        catch(SQLException exc){
            throw new CustomDAOException(exc);
        }
    }

    private Ticket getTicket(ResultSet result) throws SQLException {
        return new Ticket(result.getLong("id"),
                result.getString("passenger_no"),
                result.getString("passenger_name"),
                result.getLong("flight_id"),
                result.getString("seat_no"),
                result.getBigDecimal("cost"));
    }
}
