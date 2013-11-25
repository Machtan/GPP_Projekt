package interfaces;
import java.util.Date;
/**
 * The IFlight interface <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 22-Nov-2013
 */
public interface IFlight {
    String getID();
    String getOrigin();
    String getDestination();
    Date getDepartureTime();
    Date getArrivalTime();
    ISeating getSeating();
}
