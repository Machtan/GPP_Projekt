package classes;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
public class AirPlaneLayOut {
      public String id;
    public String placementphoto;
    public AirPlaneSeat[] airPlaneSeats;
    public int numberOfRows;

    public AirPlaneLayOut(String id, String placementphoto, int numberOfRows, AirPlaneSeat[] airPlaneSeats) {
        this.id = id;
        this.placementphoto = placementphoto;
        this.numberOfRows = numberOfRows;
        this.airPlaneSeats = airPlaneSeats;
    }
}
