package classes;

import interfaces.IFlight;
import interfaces.ISeating;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk) & Stinus Møhl Thomsen (smot@itu.dk)
 */
public class Seating implements ISeating {

    private HashMap<Point, Point> seatPositions; //1 is (row, col) : 2 is (x, y)
    private HashMap<Point, Boolean> isSeatFree;
    private AirplaneSeat[] seats;

    public Seating(IFlight flight) {
        seats = flight.getPlane().airplaneLayout.airplaneSeats;
        ArrayList<Point> takenSeats = new ArrayList<Point>();
        for (Reservation res : flight.getReservations()) {
            takenSeats.addAll(res.seats);
        }
        seatPositions = new HashMap<Point, Point>(); 
        for (AirplaneSeat seat : seats) {
            Point seatPlacement = new Point(seat.rowIndex, seat.columnIndex);
            Point seatPosition = new Point(seat.positionX, seat.positionY);
            seatPositions.put(seatPlacement, seatPosition);
            isSeatFree.put(seatPlacement, !takenSeats.contains(seatPlacement));
        }
       
    }
    
    @Override
    public Point getSeatPosition(int row, int column) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(new Point(row, column))) {
            return seatPositions.get(new Point(row, column));
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement");
        }
    }
    
    @Override
    public boolean getSeatFree(int row, int column) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(new Point(row, column))) {
            return isSeatFree.get(new Point(row, column));
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement");
        }
    }

    @Override
    public Iterator<Point> getSeatIterator() {
        return seatPositions.keySet().iterator();
    }
    
    // --- the old ISeatingHandler ---
    
    private ArrayList<Point> chosenSeats;

    @Override
    public int getNumberOfFreeSeats()
    {
        return getFreeSeats().size();
    }

    @Override
    public void setChosen(Point seat)
    {
        if(getSeatFree(seat.x,seat.y))
            chosenSeats.add(seat);
    }

    @Override
    public void changeTakenToChosen(ArrayList<Point> seats)
    {
        for(Point seat : seats)
        {
            if(getSeatFree(seat.x,seat.y))
                throw new UnsupportedOperationException("Fail not supported yet.");
            
            chosenSeats.add(seat);
        }
    }

    @Override
    public ArrayList<Point> getTakenSeats()
    {
        ArrayList<Point> takenSeats = new ArrayList<Point>();
        Iterator<Point> iter = getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            if(!getSeatFree(iterPoint.x,iterPoint.y))
                takenSeats.add(iterPoint);
        }
        
        return takenSeats;
    }

    @Override
    public ArrayList<Point> getFreeSeats()
    {
        ArrayList<Point> freeSeats = new ArrayList<Point>();
        Iterator<Point> iter = getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            if(getSeatFree(iterPoint.x,iterPoint.y))
                freeSeats.add(iterPoint);
        }
        return freeSeats;
    }

    @Override
    public ArrayList<Point> getChosenSeats()
    {
        return chosenSeats;
    }
}
