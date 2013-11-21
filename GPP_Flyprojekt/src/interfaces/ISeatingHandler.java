/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.awt.Point;

/**
 *
 * @author jakoblautrupnysom
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
