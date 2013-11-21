/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.awt.Point;

/**
 * This interface just needs to be able to return an array of points, referring
 * to the grid-positions of the chosen seats
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public interface ISeatChooser {
    
    public Point[] getSeats(int numberOfSeats);
}
