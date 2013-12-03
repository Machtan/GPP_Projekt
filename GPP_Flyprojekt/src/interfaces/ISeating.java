package interfaces;

import classes.AirplaneSeat;
import java.awt.Point;
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
}
