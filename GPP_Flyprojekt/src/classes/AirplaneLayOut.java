package classes;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
public class AirplaneLayOut {
      public String id;
    public String placementphoto;
    public AirplaneSeat[] airPlaneSeats;
    public int numberOfRows;

    public AirplaneLayOut(String id, String placementphoto, int numberOfRows, AirplaneSeat[] airPlaneSeats) {
        this.id = id;
        this.placementphoto = placementphoto;
        this.numberOfRows = numberOfRows;
        this.airPlaneSeats = airPlaneSeats;
    }
}
