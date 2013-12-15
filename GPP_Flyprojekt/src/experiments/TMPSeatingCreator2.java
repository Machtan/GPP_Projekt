/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package experiments;

import classes.AirplaneSeat;
import classes.DatabaseHandler;
import classes.Flight;
import interfaces.IDatabaseHandler;

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
public class TMPSeatingCreator2 
{
    
    public void makeSeats()
    {
        int startXOfset = 31;
        int startYOfset = 135; 
        int seatSize = 13; 
        
        for(int col = 0;col < 6; col++)
        {
            if(col == 3)
            {
                startXOfset += 19;
                startYOfset += 4;
            }
            for(int row = 0;row < 23; row++)
            {
                //col 1
                if(col == 0 && row == 0)
                    row++;
                if(col == 0 && row == 9)
                    row++;
                if(col == 0 && row == 22)
                    break;
                //col 2
                if(col == 1 && row == 0)
                    row++;
                //col 3
                if(col == 2 && row == 0)
                    row++;
                //col 4
                if(col == 3 && row == 22)
                    break;
                //col 5
                if(col == 4 && row == 22)
                    break;
                //col 6
                if(col == 5 && row == 9)
                    row++;
                if(col == 5 && row == 22)
                    break;
                
                DatabaseHandler.getHandler().addAirplaneSeat(new AirplaneSeat("","2",startXOfset + col * seatSize,startYOfset + row * (seatSize + 1),row,col));
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
