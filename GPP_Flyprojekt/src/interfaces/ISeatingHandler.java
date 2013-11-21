package interfaces;

import java.awt.Point;

/**
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public interface ISeatingHandler {
    int getNumberOfFreeSeats();
    Point[] getCloseFreeSeats(Point seat, int amount); // Returns an 
    // auto-calculated guess about where a group of 'amount' people might sit 
    // close to each other
    
    void setTaken(Point seat); // temporary
    void setFree(Point seat); // temporary
    
    Point[] getSeatPositions(); // A list of positions for all the seats
    
    void setSeating(); // Sets the seating for the handler... perhaps this will
    // be better to do in the constructor
    ISeating getSeating(); // Returns the state of the seating
}
