package interfaces;
import classes.Reservation;
import java.util.ArrayList;
/**
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */

public interface IDatabaseHandler {
    void connect();
    void disconnect();
    
    Reservation[] getReservations(IFlight flight);
    Reservation addReservation(Reservation res); // returns the updated reservation (now with db ID)
    void deleteReservation(Reservation res);
    void updateReservation(Reservation res);
    
    IFlight[] getFlights();    
    IAirport getAirport();
    
}
