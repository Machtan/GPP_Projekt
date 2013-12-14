package classes;

import interfaces.IDatabaseHandler.ConnectionError;
import interfaces.IFlight;
import interfaces.ISeating;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
public class Flight implements IFlight {

    String id;
    String origin;
    String destination;
    String airportID;
    Airplane airplane;
    Date departureTime;
    Date arrivalTime;
 
    public Flight(String ID, String Origin, String Destination, String AirportID, 
            Airplane airplane, Date DepartureTime, Date ArrivalTime) {
        this.id = ID;
        this.origin = Origin;
        this.destination = Destination;
        this.airportID = AirportID;
        this.airplane = airplane;
        this.departureTime = DepartureTime;
        this.arrivalTime = ArrivalTime;
    }

    

    @Override
    public String getOrigin() {
        return origin;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public Date getDepartureTime() {
        return departureTime;
    }

    @Override
    public Date getArrivalTime() {
      return arrivalTime;
    }

    @Override
    public String getID() {
      return id;
    }

    @Override
    public Airplane getPlane()
    {
        return airplane;
    }

    @Override
    public Reservation[] getReservations() {
       try {
            return DatabaseHandler.getHandler().getReservations(this);
       } catch (ConnectionError ce) {
           Utils.showNoConnectionNotice("Reservationerne for afgang "+
                this.id+" fra "+this.origin+" til "+this.destination+
                   " kunne ikke indlæses.");
           return (Reservation[]) ce.value;
       }
    }

    
 
   
    
}
