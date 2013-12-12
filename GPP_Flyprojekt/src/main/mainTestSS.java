/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import classes.DatabaseHandler;
import classes.Flight;
import classes.SeatChooser;
import experiments.TMPSeatingCreator;
import experiments.TMPSeatingCreator2;

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
        TMPSeatingCreator tmp = new TMPSeatingCreator();
        TMPSeatingCreator2 tmp2 = new TMPSeatingCreator2();
        tmp.makeSeats();
        tmp2.makeSeats();
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
