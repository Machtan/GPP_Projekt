/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import interfaces.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Stinus Thomsen
 * @email smot@itu.dk
 * @version 27-11-2013
 */
public class SeatChooser extends JPanel implements ISeatChooser
{
    private IFlight flight;
    private ISeatingHandler seatingHandler;
    int numberOfSeats = 1;
    int seatSize = 12;
    
    
    public SeatChooser(IFlight flight)
    {
        super();
        this.addMouseListener(new MouseListener() 
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                onMouseClicked(e);
            }
            @Override
            public void mousePressed(MouseEvent e){}
            @Override
            public void mouseReleased(MouseEvent e){}
            @Override
            public void mouseEntered(MouseEvent e){}
            @Override
            public void mouseExited(MouseEvent e){}
        });
        setFlight(flight);
    }
    
    private void onMouseClicked(MouseEvent e) 
    {
        int i = e.getX()/seatSize, j = e.getY()/seatSize;
	if (indenfor(i, j))
        {
            seatingHandler.setChosen(new Point());
            repaint();
        }
    }
    
    private void choseSeat(Point seat)
    {
        for(int seatNr = 0 ; seatNr < seatingHandler.getChosenSeats().size() ; seatNr++)
            if (seatingHandler.getChosenSeats().get(seatNr).x == seat.x && 
                    seatingHandler.getChosenSeats().get(seatNr).y == seat.y)
            {
                seatingHandler.setFree(seat);
                return;
            }
        
        if (seatingHandler.getChosenSeats().size() < numberOfSeats)
        {
            seatingHandler.setChosen(seat);
            Point TMPseat = seat;
            int TMPnumberOfSeats = seatingHandler.getChosenSeats().size();
            while(numberOfSeats > seatingHandler.getChosenSeats().size())
            {
                TMPseat.x--;
                seatingHandler.setChosen(TMPseat);
                if(TMPnumberOfSeats == seatingHandler.getChosenSeats().size())
                    break;
                TMPnumberOfSeats = seatingHandler.getChosenSeats().size();
            }
            while(numberOfSeats > seatingHandler.getChosenSeats().size())
            {
                TMPseat.x++;
                seatingHandler.setChosen(TMPseat);
                if(TMPnumberOfSeats == seatingHandler.getChosenSeats().size())
                    break;
                TMPnumberOfSeats = seatingHandler.getChosenSeats().size();
            }
        }
    }
    
    private boolean indenfor(int i, int j) 
    { 
        //denne her skal tjekke om man har tryket på en at knaperne/plaserne
        // return 0 <= i && i < cols && 0 <= j && j < rows; 
        return false;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        
        
        for(Point seat : seatingHandler.getTakenSeats())
        {
            g.setColor(Color.red);
            g.fillRect(seat.x*seatSize+1, seat.y*seatSize+1, seatSize-1, seatSize-1);
            g.setColor(Color.black);
            g.drawRect(seat.x*seatSize+1, seat.y*seatSize+1, seatSize-1, seatSize-1);
        }
        
        for(Point seat : seatingHandler.getFreeSeats())
        {
            g.setColor(Color.green);
            g.fillRect(seat.x*seatSize+1, seat.y*seatSize+1, seatSize-1, seatSize-1);
            g.setColor(Color.black);
            g.drawRect(seat.x*seatSize+1, seat.y*seatSize+1, seatSize-1, seatSize-1);
        }
        
        for(Point seat : seatingHandler.getChosenSeats())
        {
            g.setColor(Color.blue);
            g.fillRect(seat.x*seatSize+1, seat.y*seatSize+1, seatSize-1, seatSize-1);
            g.setColor(Color.black);
            g.drawRect(seat.x*seatSize+1, seat.y*seatSize+1, seatSize-1, seatSize-1);
        }
    }

    @Override
    public void setFlight(IFlight flight)
    {
        this.flight = flight;
        this.seatingHandler = new SeatingHandler(flight.getSeating());
    }

    @Override
    public void setChosen(ArrayList<Point> seats)
    {
        try
        {
            seatingHandler.changeTakenToChosen(seats);
        }
        catch (UnsupportedOperationException e)
        {
            System.out.println("her burte man nok gåre noget ved den");
        }
    }

    @Override
    public ArrayList<Point> getSeats(int numberOfSeats)
    {
        if(seatingHandler.getChosenSeats().size() == numberOfSeats)
            return seatingHandler.getChosenSeats();
        else
            throw new UnsupportedOperationException("Fail not supported yet.");
            //throw new NotEnoughSeatsException();
        //return null;
    }
    
    /*
    public class NotEnoughSeatsException extends Exception 
    {
        public NotEnoughSeatsException() 
        {
            super("Not enough seats chosen!");
        }
    }
    */

    @Override
    public void setRequestedSeats(int number)
    {
        numberOfSeats = number;
    }
}
