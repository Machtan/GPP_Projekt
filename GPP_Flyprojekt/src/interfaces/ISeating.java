package interfaces;

import java.awt.Point;
import java.util.Iterator;

/**
 * The ISeating interface
 * This implementation assumes 
 * @author Jakob Lautrup Nysom (jaln@itu.dk) & Stinus Møhl Thomsen (smot@itu.dk)
 * @version 22-Nov-2013
 */
public interface ISeating {
    boolean getSeatStatus(Point seat); // Whether a seat is taken
    //int getRowWidth(int row); // The number of seats on the row
    void setSeatStatus(Point seat,boolean newStatus);// Change seat status
    
    Iterator<Point> getSeatIterator(); // Allows for iterating over all seat 
    // positions
    
    int getVacantRow(); // Returns the first row with an empty seat
    int getVacantRowAfter(int row); // Returns the first row with an empty seat
    // after the given row
}
