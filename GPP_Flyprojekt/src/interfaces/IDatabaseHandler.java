package interfaces;
import classes.DatabaseHandler;
import classes.Reservation;
import java.util.ArrayList;
/**
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */

public interface IDatabaseHandler {
    
    public class ConnectionError extends Exception {
        public Object value;
        
        public <T> ConnectionError(T dummyValue) {
            super("A connection to the database couldn't be established");
            value = dummyValue;
        }
    }
    
    void connect() throws Exception;
    void disconnect();
    
    Reservation[] getReservations(IFlight flight) throws ConnectionError;
    Reservation addReservation(Reservation res) throws ConnectionError; // returns the updated reservation (now with db ID)
    void deleteReservation(Reservation res) throws ConnectionError;
    void updateReservation(Reservation res) throws ConnectionError;
    
    IFlight[] getFlights() throws ConnectionError;    
    IAirport getAirport() throws ConnectionError;
    
}
