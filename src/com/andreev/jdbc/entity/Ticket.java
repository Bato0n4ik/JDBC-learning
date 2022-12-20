package com.andreev.jdbc.entity;

import java.math.BigDecimal;

public class Ticket {
    private long id;
    private String passengerNo;
    private String passengerName;
    private Flight flight;
    private String seatNo;
    private BigDecimal cost;

    public Ticket(long id, String passengerNo, String passengerName, Flight flight, String seatNo, BigDecimal cost) {
        this.id = id;
        this.passengerNo = passengerNo;
        this.passengerName = passengerName;
        this.flight = flight;
        this.seatNo = seatNo;
        this.cost = cost;
    }

    public Ticket(){}

    public void setId(long id) {
        this.id = id;
    }

    public void setPassengerNo(String passengerNo) {
        this.passengerNo = passengerNo;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public void setFlight(Flight flightId) {
        this.flight = flightId;
    }

    public long getId() {
        return id;
    }

    public String getPassengerNo() {
        return passengerNo;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public Flight getFlight() {
        return flight;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", passengerNo='" + passengerNo + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", flight=" + flight +
                ", seatNo='" + seatNo + '\'' +
                ", cost=" + cost +
                '}';
    }
}
