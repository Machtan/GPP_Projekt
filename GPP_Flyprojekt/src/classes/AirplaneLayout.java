package classes;

import java.awt.Point;
import java.util.HashMap;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
public class AirplaneLayout {
    private HashMap<Point, Point> seatPositions; //1 is (col, row) : 2 is (x, y)
    private HashMap<Point, Boolean> isSeatFree;
    
    public String id;
    public String placementImagePath;
    
    public AirplaneSeat[] airplaneSeats;
    
    /*
    BIG FAT NOTICE (by Jakob 28/11)
    The changes I've implemented should probably be moved to another class...
    But they SHOULD go somewhere. We'll discuss it tomorrow
    */
    
    public AirplaneLayout(String id, String placementImagePath, AirplaneSeat[] airplaneSeats) {
        this.id = id;
        this.placementImagePath = placementImagePath;
        this.airplaneSeats  = airplaneSeats;
        
        for (AirplaneSeat seat : airplaneSeats) {
            Point seatPlacement = new Point(seat.columnIndex, seat.rowIndex);
            Point seatPosition = new Point(seat.positionX, seat.positionY);
            seatPositions.put(seatPlacement, seatPosition);
            isSeatFree.put(seatPlacement, seat.taken);
        }
    }
    
    public Point getSeatPosition(int row, int column) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(new Point(row, column))) {
            return seatPositions.get(new Point(row, column));
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement");
        }
    }
    
    public boolean getSeatTaken(int row, int column) throws IndexOutOfBoundsException {
        if (seatPositions.keySet().contains(new Point(row, column))) {
            return isSeatFree.get(new Point(row, column));
        } else {
            throw new IndexOutOfBoundsException("No seat at the given placement");
        }
    }
}
