package classes;

import interfaces.ISeating;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getVacantRowAfter(int row) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
