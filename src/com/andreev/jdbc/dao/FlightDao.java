package com.andreev.jdbc.dao;

import com.andreev.jdbc.ConnectionManager;
import com.andreev.jdbc.CustomDAOException;
import com.andreev.jdbc.entity.Flight;
import com.andreev.jdbc.entity.Ticket;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FlightDao implements DAO<Flight, Long>{

    private static volatile FlightDao INSTANCE = new FlightDao();

    private static final String SQL_SELECT_BY_ID = """
        SELECT id, departure_date, flight_no, departure_airport_code,
            arrival_date, arrival_airport_code, aircraft_id,
            status
        FROM flight
        WHERE id = ?
        """;

    private FlightDao(){}

    public static FlightDao getInstance(){
        synchronized (FlightDao.class){
            return INSTANCE;
        }
    }

    @Override
    public List<Flight> findAll() {
        return null;
    }

    @Override
    public Optional<Flight> findById(Long id) {
        Flight flight = null;
        try(Connection connection = ConnectionManager.get();
                var prepareStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            flight = findById(id, connection).orElse(null);
        }
        catch(SQLException exc){
            throw new CustomDAOException(exc);
        }
        return Optional.ofNullable(flight);
    }

    public Optional<Flight> findById(Long id, Connection connection) {
        Flight flight = null;
        try(var prepareStatement = connection.prepareStatement(SQL_SELECT_BY_ID)){

            prepareStatement.setLong(1, id);
            var result = prepareStatement.executeQuery();
            if(result.next()){
                flight = new Flight(result.getLong("id"),
                        result.getString("flight_no"),
                        result.getTimestamp("departure_date").toLocalDateTime(),
                        result.getString("departure_airport_code"),
                        result.getTimestamp("arrival_date").toLocalDateTime(),
                        result.getString("arrival_airport_code"),
                        result.getInt("aircraft_id"),
                        result.getString("status"));
            }
        }
        catch(SQLException exc){
            throw new CustomDAOException(exc);
        }
        return Optional.ofNullable(flight);
    }

    @Override
    public void updateDB(Flight ticket) {

    }

    @Override
    public Ticket insertInDB(Flight ticket) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
