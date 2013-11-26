package classes;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
class Airplane {

    public String id;
    public String name;
    public AirplaneLayOut airplaneLayout;

    public Airplane(String id, String name, AirplaneLayOut airplaneLayout) {
        this.id = id;
        this.name = name;
        this.airplaneLayout = airplaneLayout;
    }
}