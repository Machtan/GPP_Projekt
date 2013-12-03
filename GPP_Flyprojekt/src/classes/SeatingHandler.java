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

    public SeatingHandler(IFlight flight)
    {
        setFlight(flight);
    }

    @Override
    public void setFlight(IFlight flight)
    {
        seating = new Seating(flight);
    }

    @Override
    public int getNumberOfFreeSeats()
    {
        return getFreeSeats().size();
    }

    @Override
    public void setChosen(Point seat)
    {
        if(seating.getSeatFree(seat.x,seat.y))
            chosenSeats.add(seat);
    }

    @Override
    public void changeTakenToChosen(ArrayList<Point> seats)
    {
        for(Point seat : seats)
        {
            if(seating.getSeatFree(seat.x,seat.y))
                throw new UnsupportedOperationException("Fail not supported yet.");
            
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
            if(!seating.getSeatFree(iterPoint.x,iterPoint.y))
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
            if(seating.getSeatFree(iterPoint.x,iterPoint.y))
                freeSeats.add(iterPoint);
        }
        return freeSeats;
    }

    @Override
    public ArrayList<Point> getChosenSeats()
    {
        return chosenSeats;
    }

    @Override
    public ArrayList<Point> getSeatsAt(Point mousePosition, int number)
    {
        Iterator<Point> iter = seating.getSeatIterator();
        Point seatPoint = iter.next();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            if(Math.sqrt(Math.pow((mousePosition.x - iterPoint.x),2)+
                    Math.pow((mousePosition.y - iterPoint.y),2)) < 
                    Math.sqrt(Math.pow((mousePosition.x - seatPoint.x),2)+
                    Math.pow((mousePosition.y - seatPoint.y),2)))
                seatPoint = iterPoint;
                
        }
        
        if(!seating.getSeatStatus(seatPoint))
            return null;
        
        return seating.getVacantSeatsAtPoint(seatPoint, number);
    }

    @Override
    public ArrayList<Point> getSeatsPositions(ArrayList<Point> seats)
    {
        ArrayList<Point> seatsPositions = new ArrayList<Point>();
        for(Point seat : seats)
        {
            seatsPositions.add(getSeatPosition(seat));
        }
        return seatsPositions;
    }

    @Override
    public Point getSeatPosition(Point seat)
    {
        return seating.getSeatPosition(seat.x, seat.y);
    }
}
