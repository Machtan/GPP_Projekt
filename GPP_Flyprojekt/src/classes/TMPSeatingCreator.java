/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.util.HashSet;

/**
 *
 * @author Stinus Thomsen
 * @email smot@itu.dk
 * @version 10-12-2013
 */
public class TMPSeatingCreator 
{
    
    public void makeSeats()
    {
        int startXOfset = 31;
        int startYOfset = 135; 
        int seatSize = 12; 
        
        for(int col = 0;col < 6; col++)
        {
            if(col == 3)
            {
                startXOfset += 24;
                startYOfset -= 6;
            }
            for(int row = 0;row < 13; row++)
            {
                  DatabaseHandler.getHandler().addAirplaneSeat(new AirplaneSeat("","1",startXOfset + col * seatSize,startYOfset + row * seatSize,row,col));
            }
        }
        
        startXOfset = 31;
        startYOfset = 312; 
        seatSize = 12;
        int lastRow = 26;
        
        for(int col = 0;col < 6; col++)
        {
            if(col == 3)
            {
                startXOfset += 24;
                lastRow++;
            }
            for(int row = 13;row < lastRow; row++)
            {
                  DatabaseHandler.getHandler().addAirplaneSeat(new AirplaneSeat("","1",startXOfset + col * seatSize,startYOfset + row * seatSize,row,col));
            }
        }
    }
}
