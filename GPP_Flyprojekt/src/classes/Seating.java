package classes;

import interfaces.ISeating;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk) & Stinus Møhl Thomsen (smot@itu.dk)
 */
public class Seating implements ISeating {

    boolean[][] seating;

    public Seating(boolean[][] seating) {
        this.seating = seating;
    }

    @Override
    public boolean getSeatStatus(Point seat) {
        if (seat.x < seating.length && seat.y < seating[seat.y].length) {
            return seating[seat.x][seat.y];
        }

        return false;
    }

    @Override
    public Iterator<Point> getSeatIterator() {
        ArrayList<Point> points = new ArrayList();
        for (int ma = 0; ma < seating.length; ma++) {
            for (int mb = 0; mb < seating[ma].length; mb++) {
                points.add(new Point(ma, mb));
            }
        }
        return points.iterator();
    }

    @Override
    public int getVacantRow() {
        return getVacantRowAfter(0);
    }

    @Override
    public int getVacantRowAfter(int row) {
        for (int y = row; y < seating.length; y++) {
            for (int x = 0; x < seating[y].length; x++) {
                if(seating[x][y])
                    return y;
            }
        }
        
        return -1;
    }

    @Override
    public void setSeatStatus(Point seat, boolean newStatus)
    {
        if (seat.x < seating.length && seat.y < seating[seat.y].length) {
            seating[seat.x][seat.y] = newStatus;
        }
    }
}
