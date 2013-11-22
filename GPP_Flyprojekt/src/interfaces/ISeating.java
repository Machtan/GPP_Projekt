package interfaces;

import java.awt.Point;
import java.util.Iterator;

/**
 * The ISeating interface <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 22-Nov-2013
 */
public interface ISeating {
    boolean getSeatStatus(Point seat); // Whether a seat is taken
    //int getRowWidth(int row); // The number of seats on the row
    
    Iterator<Point> getSeatIterator(); // Allows for iterating over all seat 
    // positions
    
    int getVacantRow(); // Returns the first row with an empty seat
    int getVacantRowAfter(int row); // Returns the first row with an empty seat
    // after the given row
}
