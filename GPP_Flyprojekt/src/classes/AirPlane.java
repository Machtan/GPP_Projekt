package classes;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
class AirPlane {

    public String id;
    public String name;
    public AirPlaneLayOut airplaneLayout;

    public AirPlane(String id, String name, AirPlaneLayOut airplaneLayout) {
        this.id = id;
        this.name = name;
        this.airplaneLayout = airplaneLayout;
    }
}