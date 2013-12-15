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
import interfaces.IDatabaseHandler.ConnectionError;

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
        TMPSeatingCreator tmp = new TMPSeatingCreator();
        TMPSeatingCreator2 tmp2 = new TMPSeatingCreator2();
        try {
            tmp.makeSeats();
            tmp2.makeSeats();
        } catch (Exception ex) {
            return;
        }
        Flight fligt;
        try {
            fligt = database.getFlights()[0];
        } catch (ConnectionError ce) {
            return;
        }
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
