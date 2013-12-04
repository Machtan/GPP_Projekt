package interfaces;

import classes.AirplaneSeat;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The ISeating interface
 * This implementation assumes 
 * @author Jakob Lautrup Nysom (jaln@itu.dk) & Stinus Møhl Thomsen (smot@itu.dk)
 * @version 22-Nov-2013
 */
public interface ISeating {
    public Point getSeatPosition(int row, int column);
    public boolean getSeatFree(int row, int column); // Whether a seat is taken    
    public Iterator<Point> getSeatIterator();  // Allows for iterating over all seat 
    // positions
    
    // --- the old ISeatingHandler ---
    
    int getNumberOfFreeSeats();
    
    void setChosen(Point seat); // temporary
    
    void changeTakenToChosen(ArrayList<Point> seats); // Used when a reservation
    // is loaded to change 'taken' seats to the 'chosen' state.
    
    ArrayList<Point> getTakenSeats(); // A list of positions for the taken seats
    ArrayList<Point> getFreeSeats(); // A list of positions for the free seats
    ArrayList<Point> getChosenSeats(); // A list of positions for the chosen seats
}
