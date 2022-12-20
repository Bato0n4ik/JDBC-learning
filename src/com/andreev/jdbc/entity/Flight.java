package com.andreev.jdbc.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Flight (long id,
                      String flightNo,
                      LocalDateTime departureDate,
                      String departureAirportCode,
                      LocalDateTime arrivalDate,
                      String arrivalAirportCode,
                      Integer aircraftId,
                      String status){

}
