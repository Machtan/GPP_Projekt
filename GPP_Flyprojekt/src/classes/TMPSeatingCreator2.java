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
                if(col == 0 && row == 23)
                    break;
                //col 2
                if(col == 1 && row == 0)
                    row++;
                //col 3
                if(col == 2 && row == 0)
                    row++;
                //col 4
                if(col == 3 && row == 23)
                    break;
                //col 5
                if(col == 4 && row == 23)
                    break;
                //col 6
                if(col == 5 && row == 9)
                    row++;
                if(col == 5 && row == 23)
                    break;
                
                DatabaseHandler.getHandler().addAirplaneSeat(new AirplaneSeat("","1",startXOfset + col * seatSize,startYOfset + row * (seatSize + 1),row,col));
            }
        }
    }
}
