/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import classes.DatabaseHandler;
import classes.Flight;
import classes.Reservation;
import classes.SeatChooser;
import classes.TMPSeatingCreator2;
import interfaces.ISeatChooser;
import javax.swing.JFrame;

/**
 *
 * @author Stinus Thomsen
 * @email smot@itu.dk
 * @version 05-12-2013
 */
public class mainTestSS 
{
    public static void main(String[] args)
    {
        DatabaseHandler database = DatabaseHandler.getHandler();
        database.connect();
      
        Flight fligt;
        fligt = database.getFlights()[0];
        SeatChooser vis = new SeatChooser();
        
        vis.setRequestedSeats(8);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.add(vis);
        frame.pack();
        frame.setVisible(true);
        vis.setFlight(fligt);
    }
}
