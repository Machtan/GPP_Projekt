package classes;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
public class AirplaneSeat {

    public String id;
    public String airplaneLayoutID;
    public int positionX;
    public int positionY;
    public int rowIndex;
    public int columnIndex;

    public AirplaneSeat(String id, String airplaneLayoutID, int positionX, int positionY, int rowIndex, int columnIndex) {
        this.id = id;
        this.airplaneLayoutID = airplaneLayoutID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }
}
