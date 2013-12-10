package interfaces;

import java.awt.Point;
import java.util.ArrayList;

/**
 * This interface just needs to be able to return an array of points, referring
 * to the grid-positions of the chosen seats.
 * This class will be the one showing the representation of the seats and lets 
 * the user choose a seating.
 * This will probably be done with a GUI, with an attached MouseListener.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public interface ISeatChooser {
    public void setFlight(IFlight flight); // Sets the flight to choose a 
    // seating on.
    public void setChosen(ArrayList<Point> seats); // Sets the given seats to
    // be chosen in the seating handler
    
    public void setRequestedSeats(int number);
    
    public ArrayList<Point> getSeats(int numberOfSeats) throws NotEnoughSeatsException; // Returns the 
    
    public class NotEnoughSeatsException extends Exception {
        public NotEnoughSeatsException(int chosen, int needed) {
            super("Der er kun valgt "+chosen+" ud af "+needed+" sæder.");
        }
    }
}
