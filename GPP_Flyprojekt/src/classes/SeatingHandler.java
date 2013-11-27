/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;
import interfaces.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Stinus Thomsen
 * @email smot@itu.dk
 * @version 26-11-2013
 */
public class SeatingHandler implements ISeatingHandler
{
    private ISeating seating;
    private ArrayList<Point> chosenSeats;

    public SeatingHandler(ISeating seating)
    {
        setSeating(seating);
    }

    @Override
    public void setSeating(ISeating seating)
    {
        this.seating = seating;
    }

    @Override
    public int getNumberOfFreeSeats()
    {
        return getFreeSeats().size();
    }

    @Override
    public void setChosen(Point seat)
    {
        if(seating.getSeatStatus(seat))
            chosenSeats.add(seat);
    }

    @Override
    public void setFree(Point seat)
    {
        if(!seating.getSeatStatus(seat))
            seating.setSeatStatus(seat, true);
        
        for(int seatNr = 0 ; seatNr < chosenSeats.size() ; seatNr++)
            if (chosenSeats.get(seatNr).x == seat.x && 
                    chosenSeats.get(seatNr).y == seat.y)
                chosenSeats.remove(seatNr);
    }

    @Override
    public void changeTakenToChosen(ArrayList<Point> seats)
    {
        for(Point seat : seats)
        {
            if(!seating.getSeatStatus(seat))
                seating.setSeatStatus(seat, true);
            else
                throw new UnsupportedOperationException("Fail not supported yet.");
                //throw new SeatNotTakenException(seat);
            
            chosenSeats.add(seat);
        }
    }

    @Override
    public ArrayList<Point> getTakenSeats()
    {
        ArrayList<Point> takenSeats = new ArrayList<Point>();
        Iterator<Point> iter = seating.getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            if(!seating.getSeatStatus(iterPoint))
                takenSeats.add(iterPoint);
        }
        
        return takenSeats;
    }

    @Override
    public ArrayList<Point> getFreeSeats()
    {
        ArrayList<Point> freeSeats = new ArrayList<Point>();
        Iterator<Point> iter = seating.getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            if(seating.getSeatStatus(iterPoint))
                freeSeats.add(iterPoint);
        }
        
        return freeSeats;
    }

    @Override
    public ArrayList<Point> getChosenSeats()
    {
        return chosenSeats;
    }
    
    /*
    public class SeatNotTakenException extends Exception 
    {
        public SeatNotTakenException(Point seat) 
        {
            super("Seat: " + seat.x + ";" + seat.y + " is not taken");
        }
    }
    */
}
