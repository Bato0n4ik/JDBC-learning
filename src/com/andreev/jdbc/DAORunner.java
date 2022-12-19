package com.andreev.jdbc;

import com.andreev.jdbc.dao.TicketDao;
import com.andreev.jdbc.dto.TicketFilter;
import com.andreev.jdbc.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DAORunner {
    public static void main(String[] args){
        //Ticket ticket = new Ticket();
        //update(ticket);

        //TicketDao obj = TicketDao.getObject();
        //System.out.println(obj.delete(60L));

        /*Ticket ticket = TicketDao.getObject().findById(21).orElse(null);
        System.out.println("Selected ticket dy id: " + ticket);
        if(ticket != null)
        {
            ticket.setCost(new BigDecimal("129.99"));
            System.out.println(ticket);
        }*/
        TicketFilter filter = new TicketFilter(10, 0, "A1", null);
        TicketDao.getObject().findAll(filter).forEach(System.out::println);

    }

    public static void update(Ticket ticket){

        ticket.setCost(new BigDecimal("1400.65"));
        ticket.setFlightId(3L);
        ticket.setPassengerName("Andrew");
        ticket.setSeatNo("C5");
        ticket.setPassengerNo("569834");
        TicketDao obj = TicketDao.getObject();
        System.out.println(obj.insertInDB(ticket));

    }
}
