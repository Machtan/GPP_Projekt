package classes;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
class Airplane {
    public String id;
    public String name;
    public AirplaneLayout airplaneLayout;

    public Airplane(String id, String name, AirplaneLayout airplaneLayout) {
        this.id = id;
        this.name = name;
        this.airplaneLayout = airplaneLayout;
    }
}