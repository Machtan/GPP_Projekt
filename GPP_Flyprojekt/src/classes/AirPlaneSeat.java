package classes;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
class AirPlaneSeat {

    public String id;
    public String airplaneLayoutID;
    public int positionX;
    public int positionY;
    public int rowIndex;
    public int collumnIndex;

    public AirPlaneSeat(String id, String airplaneLayoutID, int positionX, int positionY, int rowIndex, int collumnIndex) {
        this.id = id;
        this.airplaneLayoutID = airplaneLayoutID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.rowIndex = rowIndex;
        this.collumnIndex = collumnIndex;
    }
}
