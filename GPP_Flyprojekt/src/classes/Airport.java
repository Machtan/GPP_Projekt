package classes;

import interfaces.IAirport;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
public class Airport implements IAirport {

    String id;
    String name;
    String code;
    String cand;
    String town;
    String address;
    public Airport(String id, String name, String code, String cand, String town, String address)
    {
        this.id = id;
        this.name = name;
        this.code = code;
        this.cand = cand;
        this.town = town;
        this.address = address;
    }
    @Override
    public String getID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAddress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
