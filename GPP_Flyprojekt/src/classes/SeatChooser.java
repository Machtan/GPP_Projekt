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
import java.util.HashMap;
import java.util.Iterator;
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
    private ISeating seating;
    int numberOfSeats = 1;
    int seatSize = 12;
    private HashMap<Point, Point> positionToSeat;
    
    
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
    
    @Override
    public void setFlight(IFlight flight)
    {
        this.flight = flight;
        this.seating = new Seating(flight);
        
        Iterator<Point> iter = seating.getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            positionToSeat.put(seating.getSeatPosition(iterPoint), iterPoint);
        }
    }
    
    private void onMouseClicked(MouseEvent e) 
    {
        int i = e.getX()/seatSize, j = e.getY()/seatSize;
	if (indenfor(i, j))
        {
            seating.setChosen(new Point());
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
        //denne her skal tjekke om man har tryket paa en at knaperne/plaserne
        return 0 <= i && i < cols && 0 <= j && j < rows; 
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        
        //paint text for how many free seats ther is
        
        //paint a pikture of the airplane
        
        //Paint all the seats in difrint colors: red = taken, green = free, blue = chosen.
        Iterator<Point> iter = seating.getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            
            g.setColor(Color.red);
            if(seating.getSeatFree(iterPoint))
                g.setColor(Color.green);
            if(seating.getSeatChosen(iterPoint))
                g.setColor(Color.blue);
            
            g.fillRect(seating.getSeatPosition(iterPoint).x*seatSize+1 - seatSize/2, seating.getSeatPosition(iterPoint).y*seatSize+1 - seatSize/2, seatSize-1, seatSize-1);
            g.setColor(Color.black);
            g.drawRect(seating.getSeatPosition(iterPoint).x*seatSize+1 - seatSize/2, seating.getSeatPosition(iterPoint).y*seatSize+1 - seatSize/2, seatSize-1, seatSize-1);
        }
    }

    @Override
    public void setChosen(ArrayList<Point> seats)
    {
        try
        {
            seating.changeTakenToChosen(seats);
        }
        catch (UnsupportedOperationException e)
        {
            System.out.println("here is a error i am no sure how to conter");
        }
    }

    @Override
    public ArrayList<Point> getSeats(int numberOfSeats)
    {
        if(seating.getChosenSeats().size() == numberOfSeats)
            return seating.getChosenSeats();
        else
            throw new UnsupportedOperationException("Fail not supported yet.");
            //throw new NotEnoughSeatsException();
        //return null;
    }

    @Override
    public void setRequestedSeats(int number)
    {
        numberOfSeats = number;
    }
}
