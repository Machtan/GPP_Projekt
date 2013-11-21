package classes;

import java.awt.Point;
import java.util.ArrayList;

/**
 * The Reservation class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public class Reservation {
    
    public String reservationID;
    public Person passenger;
    public ArrayList<Person> additionalPassengers;
    public ArrayList<Seat> seats; // Seats in the form (0,1), (0,2) etc.
    public IFlight flight;
    
    /**
     * Creates a new reservation from the given parameters
     * @param reservationID The ID of the reservation
     * @param passenger The main passenger
     * @param additionalPassengers Any additional passengers
     * @param seats The 2d coordinates of the occupied seats for this 
     * reservation
     * @param flight The flight this reservation is on
     */
    public Reservation(String reservationID, Person passenger, 
            ArrayList<Person> additionalPassengers, Point[] seats, 
            IFlight flight) {
        this.reservationID = reservationID;
        this.passenger = passenger;
        this.additionalPassengers = additionalPassengers;
        this.seats = seats;
        this.flight = flight;
    }
    
    /**
     * Overloaded constructor for creating empty reservations
     */
    public Reservation () {
        this("", null, new ArrayList<Person>(), new Point[0], null);
    }
    
    /**
     * Returns the passengers 
     * @return An array of the passengers on this reservation
     */
    public ArrayList<Person> getPassengers() {
        ArrayList<Person> passengers = (ArrayList<Person>)additionalPassengers.clone();
        passengers.add(0, passenger);
        return passengers;
    }
}
