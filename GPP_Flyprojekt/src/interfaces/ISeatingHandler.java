package interfaces;

import java.awt.Point;
import java.util.ArrayList;

/**
 * <TODO> The seating handler SHOULD have a constructor taking an 'IFlight'
 * argument, which calls setSeating(flight.getSeating());
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public interface ISeatingHandler {
    public void setFlight(IFlight flight); // Sets the flight to choose a 
    // seating on.
    
    int getNumberOfFreeSeats();
    
    ArrayList<Point> getSeatsAt(Point mousePosition, int number);// find the nerest seats
    
    void setChosen(Point seat); // temporary
    
    void changeTakenToChosen(ArrayList<Point> seats); // Used when a reservation
    // is loaded to change 'taken' seats to the 'chosen' state.
    
    ArrayList<Point> getTakenSeats(); // A list of positions for the taken seats
    ArrayList<Point> getFreeSeats(); // A list of positions for the free seats
    ArrayList<Point> getChosenSeats(); // A list of positions for the chosen seats
    Point getSeatPosition(Point seat);
    ArrayList<Point> getSeatsPositions(ArrayList<Point> seats);
}
