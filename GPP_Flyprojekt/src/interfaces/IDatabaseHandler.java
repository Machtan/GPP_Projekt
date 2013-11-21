/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */

public interface IDatabaseHandler {
    void connect();
    void disconnect();
    
    Reservation[] getReservations();
    void addReservation(Reservation);
    void deleteReservation(Reservation);
    void updateReservation(Reservation);
    
    IFlight[] getFlights();
    void updateFlightSeating(IFlight);
}
