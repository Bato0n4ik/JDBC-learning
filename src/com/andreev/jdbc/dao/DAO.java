package com.andreev.jdbc.dao;

import com.andreev.jdbc.dto.TicketFilter;
import com.andreev.jdbc.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface DAO<T,R> {

    List<T> findAll();

    Optional<T> findById(R id);

    void updateDB(T ticket);

    Ticket insertInDB(T ticket);

    boolean delete(R id);

}
