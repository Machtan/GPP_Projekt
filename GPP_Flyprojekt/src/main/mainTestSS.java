/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import classes.DatabaseHandler;
import classes.Flight;
import classes.Reservation;
import classes.SeatChooser;
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
        DatabaseHandler database = new DatabaseHandler();
        database.connect();
        Flight fligt;
        fligt = database.getFlights()[0];
        Reservation res = fligt.getReservations()[0];
        if(res == null)
            return;
        SeatChooser vis = new SeatChooser(fligt);
        JFrame frame = new JFrame();
        frame.add(vis);
        frame.pack();
        frame.setVisible(true);
    }
}
