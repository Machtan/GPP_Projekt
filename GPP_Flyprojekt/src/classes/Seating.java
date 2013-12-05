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
    private HashMap<Point, Boolean> isSeatChosen;
    private AirplaneSeat[] seats;

    public Seating(IFlight flight) {
        seats = flight.getPlane().airplaneLayout.airplaneSeats;
        isSeatFree = new HashMap<Point, Boolean>();
        isSeatChosen = new HashMap<Point, Boolean>();
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
            isSeatChosen.put(seatPlacement, false);
        }
       
    }
    
    @Override
    public Point getSeatPosition(Point seat) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(seat)) {
            return seatPositions.get(seat);
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement");
        }
    }
    
    @Override
    public boolean getSeatFree(Point seat) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(seat)) {
            return isSeatFree.get(seat);
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement: " + seat.toString());
        }
    }
    
     @Override
    public boolean getSeatChosen(Point seat) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(seat)) {
            return isSeatChosen.get(seat);
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement");
        }
    }

    @Override
    public Iterator<Point> getSeatIterator() {
        return seatPositions.keySet().iterator();
    }
    
    // --- the old ISeatingHandler ---
    // the next methos use the privios methos to giv data in better ways

    @Override
    public int getNumberOfFreeSeats()
    {
        return getFreeSeats().size();
    }

    @Override
    public void setChosen(Point seat)
    {
        if(getSeatFree(seat))
            isSeatChosen.put(seat,true);
    }
    
    @Override
    public void removeChosen(Point seat)
    {
        if(getSeatChosen(seat))
            isSeatChosen.put(seat,false);
    }

    @Override
    public void changeTakenToChosen(ArrayList<Point> seats)
    {
        for(Point seat : seats)
        {
            if(getSeatFree(seat))
                throw new UnsupportedOperationException("Fail not supported yet.");
            
            isSeatChosen.put(seat,true);
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
            if(!getSeatFree(iterPoint))
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
            if(getSeatFree(iterPoint))
                freeSeats.add(iterPoint);
        }
        return freeSeats;
    }

    @Override
    public ArrayList<Point> getChosenSeats()
    {
        ArrayList<Point> chosenSeats = new ArrayList<Point>();
        Iterator<Point> iter = getSeatIterator();
        while(iter.hasNext())
        {
            Point iterPoint = iter.next();
            if(getSeatChosen(iterPoint))
                chosenSeats.add(iterPoint);
        }
        return chosenSeats;
    }
}
