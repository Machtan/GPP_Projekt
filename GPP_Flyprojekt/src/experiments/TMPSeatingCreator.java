/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package experiments;

import classes.AirplaneSeat;
import classes.DatabaseHandler;
import classes.Flight;
import interfaces.IDatabaseHandler;
import interfaces.IDatabaseHandler.ConnectionError;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.GPP_Flyprojekt;

/**
 *
 * @author Stinus Thomsen
 * @email smot@itu.dk
 * @version 10-12-2013
 */
public class TMPSeatingCreator 
{
    
    public void makeSeats() throws ConnectionError
    {
        int startXOfset = 31;
        int startYOfset = 135; 
        int seatSize = 13; 
        
        for(int col = 0;col < 6; col++)
        {
            if(col == 3)
            {
                startXOfset += 19;
                startYOfset -= 5;
            }
            for(int row = 0;row < 13; row++)
            {
                DatabaseHandler.getHandler().addAirplaneSeat(new AirplaneSeat("","1",startXOfset + col * seatSize,startYOfset + row * seatSize,row,col));
            }
        }
        
        startXOfset = 31;
        startYOfset = 316; 
        int lastRow = 31;
        
        for(int col = 0;col < 6; col++)
        {
            if(col == 3)
            {
                startXOfset += 19;
                lastRow++;
            }
            for(int row = 0;row + 13 < lastRow; row++)
            {
                 DatabaseHandler.getHandler().addAirplaneSeat(new AirplaneSeat("","1",startXOfset + col * seatSize,startYOfset + row * seatSize,row + 13,col));
            }
        }
           Flight[] flights;
        try {
            flights = DatabaseHandler.getHandler().getFlights();
            for (Flight flight : flights) {
                DatabaseHandler.getHandler().updateFlightFreeSeats(flight);
            }
        } catch (IDatabaseHandler.ConnectionError ex) {
            Logger.getLogger(GPP_Flyprojekt.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
