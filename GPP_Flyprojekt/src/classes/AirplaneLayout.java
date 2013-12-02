package classes;

import java.awt.Point;
import java.util.HashMap;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
public class AirplaneLayout {  
    public String id;
    public String placementImagePath;
    
    public AirplaneSeat[] airplaneSeats;
    
    public AirplaneLayout(String id, String placementImagePath, AirplaneSeat[] airplaneSeats) {
        this.id = id;
        this.placementImagePath = placementImagePath;
        this.airplaneSeats  = airplaneSeats;
    }
}
