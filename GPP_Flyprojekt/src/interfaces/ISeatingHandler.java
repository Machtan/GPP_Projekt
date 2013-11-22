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
    void setSeating(ISeating seating); // This should set the seating for the 
    // handler. Call it in the constructor!
    
    int getNumberOfFreeSeats();
    
    Point getSeatAt(Point mousePosition);
    
    void setChosen(Point seat); // temporary
    void setFree(Point seat); // temporary
    
    ArrayList<Point> getTakenSeats(); // A list of positions for the taken seats
    ArrayList<Point> getFreeSeats(); // A list of positions for the free seats
    ArrayList<Point> getChosenSeats(); // A list of positions for the chosen seats
}
