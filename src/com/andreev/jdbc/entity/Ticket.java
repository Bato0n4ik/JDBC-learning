package com.andreev.jdbc.entity;

import java.math.BigDecimal;

public class Ticket {
    private long id;
    private String passengerNo;
    private String passengerName;
    private long flightId;
    private String seatNo;
    private BigDecimal cost;

    public Ticket(long id, String passengerNo, String passengerName, long flightId, String seatNo, BigDecimal cost) {
        this.id = id;
        this.passengerNo = passengerNo;
        this.passengerName = passengerName;
        this.flightId = flightId;
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

    public void setFlightId(long flightId) {
        this.flightId = flightId;
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

    public long getFlightId() {
        return flightId;
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
                ", flightId=" + flightId +
                ", seatNo='" + seatNo + '\'' +
                ", cost=" + cost +
                '}';
    }
}
