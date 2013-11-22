package interfaces;
import classes.Reservation;
/**
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */

public interface IDatabaseHandler {
    void connect();
    void disconnect();
    
    Reservation[] getReservations();
    void addReservation(Reservation res);
    void deleteReservation(Reservation res);
    void updateReservation(Reservation res);
    
    IFlight[] getFlights();
    void updateFlightSeating(IFlight);
}
