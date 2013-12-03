package classes;

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
    Airplane plane;
    ArrayList<Reservation> reservations;
 
    public Flight(String ID, String Origin, String Destination, String AirportID, 
            Airplane airplane, Date DepartureTime, Date ArrivalTime, 
            Airplane plane, ArrayList<Reservation> reservations) {
        this.id = ID;
        this.origin = Origin;
        this.destination = Destination;
        this.airportID = AirportID;
        this.airplane = airplane;
        this.departureTime = DepartureTime;
        this.arrivalTime = ArrivalTime;
        this.plane = plane;
        this.reservations = reservations;
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
        return plane;
    }

    @Override
    public ArrayList<Reservation> getReservations()
    {
       return reservations;
    }

    
 
   
    
}
