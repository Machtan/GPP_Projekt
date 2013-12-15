package classes;

import interfaces.IFlight;
import interfaces.ISeating;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    
    /**
     * setting the seats for the flight, up in the respektable variabels. 
     * @param flight 
     */
    public Seating(IFlight flight) {
        seats = flight.getPlane().airplaneLayout.airplaneSeats;
        isSeatFree = new HashMap<Point, Boolean>();
        isSeatChosen = new HashMap<Point, Boolean>();
        ArrayList<Point> takenSeats = new ArrayList<Point>();
        
        Reservation[] reservations = flight.getReservations();
        
        for (Reservation res : reservations) {
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
    
    /**
     * a method to get the seats position, form the seats row,collen deskripion.
     * @param seat ther is the row,collen deskripion of the sead.
     * @return the aktual position of the seat.
     * @throws IndexOutOfBoundsException if the seat parameter dose not deskripe a seat in seatPositions.
     */
    @Override
    public Point getSeatPosition(Point seat) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(seat)) {
            return seatPositions.get(seat);
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement: " + seat.toString());
        }
    }
    
    /**
     * a method to get if the seat is free, form the seats row,collen deskripion.
     * @param seat ther is the row,collen deskripion of the sead.
     * @return if the seat is free.
     * @throws IndexOutOfBoundsException if the seat parameter dose not deskripe a seat in seatPositions.
     */
    @Override
    public boolean getSeatFree(Point seat) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(seat)) {
            return isSeatFree.get(seat);
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement: " + seat.toString());
        }
    }
    
    /**
     * a method to get if the seat is choosen, form the seats row,collen deskripion.
     * @param seat ther is the row,collen deskripion of the sead.
     * @return if the seat is choosen.
     * @throws IndexOutOfBoundsException if the seat parameter dose not deskripe a seat in seatPositions.
     */
    @Override
    public boolean getSeatChosen(Point seat) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(seat)) {
            return isSeatChosen.get(seat);
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement: " + seat.toString());
        }
    }
    
    /**
     * a method to test if at seat exist with the given row,collen deskripion.  
     * @param seat the given row,collen deskripion of the seat.
     * @return if ther is a seat with the given row,collen deskripion in seatPositions.
     */
    @Override
    public boolean getSeatExists(Point seat) {
        return seatPositions.keySet().contains(seat);
    }
    
    /**
     * a method to make a iterator over the seats row,collen deskripion in seatPositions without giving access to the variable.
     * @return a clone of the keys in seatPositions.
     */
    @Override
    public Iterator<Point> getSeatIterator() {
        HashSet<Point> keySet = new HashSet<Point>();
        for(Point key : seatPositions.keySet())
            keySet.add((Point) key.clone());
        return keySet.iterator();
    }
    
    // --- the old ISeatingHandler ---
    // the next methos use the privios methos to giv data in better ways
    
    /**
     * a method to get the number of free seats.
     * @return the number of free seats.
     */
    @Override
    public int getNumberOfFreeSeats()
    {
        return getFreeSeats().size();
    }
    
    /**
     * a method to set if at seat is to be choosen.
     * @param seat the row,collen deskripion of the seat to be choosen.
     */
    @Override
    public void setChosen(Point seat)
    {
        if(getSeatFree(seat))
            isSeatChosen.put(seat,true);
    }
    
    /**
     * a method to set if at seat is not to be choosen.
     * @param seat the row,collen deskripion of the seat to be unchoosen.
     */
    @Override
    public void removeChosen(Point seat)
    {
        if(getSeatChosen(seat))
            isSeatChosen.put(seat,false);
    }
    
    /**
     * a method to make a arraylist of seats choosen insteat of not free.
     * @param seats the arraylist of the row,collen deskripion seats to be choosen.
     */
    @Override
    public void changeTakenToChosen(ArrayList<Point> seats)
    {
        for(Point seat : seats)
        {
            if(getSeatFree(seat))
                throw new UnsupportedOperationException("Fail not supported yet.");
            
            isSeatFree.put(seat,true);
            isSeatChosen.put(seat,true);
        }
    }
    
    /**
     * a method to get all not free seats.
     * @return a arraylist of the row,collen deskripion seats ther is not free.
     */
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
    
    /**
     * a method to get all free seats.
     * @return a arraylist of the row,collen deskripion seats ther is free.
     */
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
    
    /**
     * a method to get all choosen seats.
     * @return a arraylist of the row,collen deskripion seats ther is choosen.
     */
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
