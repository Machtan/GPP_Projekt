package classes;

import interfaces.IAirport;
import interfaces.IDatabaseHandler;
import interfaces.IFlight;
import java.awt.Point;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import classes.Utils.*;

/**
 *
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
public class DatabaseHandler implements IDatabaseHandler {
    //MYSQL database connection information.
    final String AirportID = "1";
    final String url = "jdbc:mysql://mysql.itu.dk/";
    final String dbName = "Airport";
    final String driver = "com.mysql.jdbc.Driver";
    final String userName = "patr0805";
    final String password = "testuser";
    Connection conn = null;
    static DatabaseHandler handler = new DatabaseHandler();
    /**
     * Constructor for DatabaseHandler, provide AirportID to filter out entries
     * relevant to the specific airport.
     */
    public DatabaseHandler() {
    this.handler = this;
    }
    
    public static DatabaseHandler getHandler()
    {
        return handler;
    }
    
    /**
     * Connect to the database.
     */
    @Override
    public void connect() {
      
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName+"?autoReconnect=true", userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }      
      
    }
    /**
     * Disconnect from the database.
     */
    @Override
    public void disconnect() {
        try {
        
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get reservations from a flight.
     */
    @Override
    public Reservation[] getReservations(IFlight flight) {
        try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
            
        Person[] people = getPeople();
        //For performance reasons, convert to hashmap with personID as KEY.
        HashMap<String, Person> peopleMapped = new HashMap<String, Person>();
        for (Person item : people) {
            peopleMapped.put(item.id, item);
        }
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM  `Reservation` WHERE flightID = " + flight.getID();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString(1);
                String passengerid = rs.getString(3);
                String additionalPassengers = rs.getString(4);
                String seats = rs.getString(5);
                String tlf = rs.getString(6);
                String cardNumber = rs.getString(7);
                String bookingNumber = rs.getString(8);
                ArrayList<Person> additionalPassengers_array = new ArrayList<Person>();
                ArrayList<Point> seatpoints_array = new ArrayList<Point>();
                for (String pasengerid : additionalPassengers.split(";")) {
                    additionalPassengers_array.add(peopleMapped.get(pasengerid));
                }
                for (String seat : seats.split(";")) {
                    String[] cordinates = seat.split(",");
                    if (cordinates.length > 1) {
                        seatpoints_array.add(new Point(Integer.parseInt(cordinates[0]), Integer.parseInt(cordinates[1])));
                    }
                }
                reservations.add(new Reservation(id, peopleMapped.get(passengerid), additionalPassengers_array, seatpoints_array, flight, tlf, cardNumber,bookingNumber));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (Reservation[]) reservations.toArray(new Reservation[0]);
    }
    
    /**
     * Add a new reservation to the database.
     */
    @Override
    public void addReservation(Reservation res) {
          try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if (res != null) {
            Statement stmt;
            try {
                stmt = conn.createStatement();

                String result = Utils.formatAndJoinVars(res.additionalPassengers, "%s", ",", "id");
                String result2 = Utils.formatAndJoinVars(res.seats, "%s,%s;", "", "x", "y");
               
                String values = "NULL ,  '" + 
                    Utils.joinList(new ArrayList<String> (
                        Arrays.asList(
                            res.flight.getID(),
                            res.passenger.id, 
                            result, 
                            result2, 
                            res.tlf, 
                            res.cardnumber,
                            res.bookingNumber)
                        ), "',  '") 
                    + "'\n";
                
                String query = "INSERT INTO  `Airport`.`Reservation` (\n"
                        + "`ID` ,\n"
                        + "`FlightID` ,\n"
                        + "`Passenger` ,\n"
                        + "`additionalPassengers` ,\n"
                        + "`Seats` ,\n"
                        + "`Tlf` ,\n"
                        + "`Cardnumber`\n"
                        + "`BookingNumber`\n"+
                        ")\n"
                        + "VALUES (\n"
                        + values
                        + ");";
                stmt.executeUpdate(query);

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
/*
 * delete a reservation from the database.
 */
    @Override
    public void deleteReservation(Reservation res) {
          try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (res != null) {
            Statement stmt;
            try {
                stmt = conn.createStatement();
                String query = "DELETE FROM  `Reservation` WHERE ID = " + res.reservationID;
                stmt.executeUpdate(query);

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * update a reservation.
     */
    @Override
    public void updateReservation(Reservation res) {
          try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (res != null) {
            Statement stmt;
            try {
                stmt = conn.createStatement();
                String result = Utils.formatAndJoinVars(res.additionalPassengers, "%s", ",", "id");
                String result2 = Utils.formatAndJoinVars(res.seats, "%s,%s;", "", "x", "y");
                
                String query = "UPDATE `Airport`.`Reservation` SET \n"
                        + "`FlightID` = "+ res.flight.getID() + " ,\n"
                        + "`Passenger`= "+ res.passenger.id +  " ,\n"
                        + "`additionalPassengers` = '"+ result + "',\n"
                        + "`Seats` = '"+ result2 + "',\n"
                        + "`Tlf` = '"+ res.tlf + "',\n"
                        + "`Cardnumber` = '"+ res.cardnumber + "' \n"
                        + " WHERE ID = '" + res.reservationID +"';"
                       ;
                stmt.executeUpdate(query);

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * get flights.
     */
    @Override
    public Flight[] getFlights() {
          try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        Airplane[] airPlanes = getAirplanes();
        //For performance reasons, convert to hashmap with airplaneID as KEY.
        HashMap<String, Airplane> airPlanesMapped = new HashMap<String, Airplane>();
        for (Airplane item : airPlanes) {
            airPlanesMapped.put(item.id, item);
        }
        //Get all flights
        ArrayList<Flight> flights = new ArrayList<Flight>();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM  `Flight` ";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString(1);
                String origin = rs.getString(2);
                String destination = rs.getString(3);
                String airportID = rs.getString(4);
                String airplaneID = rs.getString(5);
                Date departureTime = rs.getDate(6);
                Date arrivalTime = rs.getDate(7);
                if (airportID.equals(this.AirportID) && airPlanesMapped.containsKey(airplaneID)) {
                    Airplane airPlane = airPlanesMapped.get(airplaneID);
                    Flight newflight = new Flight(id, origin, destination, airportID, airPlane, departureTime, arrivalTime);
                    flights.add(newflight);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (Flight[]) flights.toArray(new Flight[0]);
    }

    /**
     * get general airport information.
     */
    @Override
    public IAirport getAirport() {
          try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        Airport airport = null;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM `Airport`";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                String code = rs.getString(3);
                String cand = rs.getString(4);
                String town = rs.getString(5);
                String address = rs.getString(6);
                airport = new Airport(id, name, code, cand, town, address);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return airport;
    }
    /**
     * get all passengers.
     */
    Person[] getPeople() {
     try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        ArrayList<Person> result = new ArrayList<Person>();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM  `Passenger`";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                String nationallity = rs.getString(3);
                String cpr = rs.getString(4);
                result.add(new Person(id, name, nationallity, cpr));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (Person[]) result.toArray(new Person[0]);
    }
    /**
     * get all airplanes.
     */
    Airplane[] getAirplanes() {
             try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        AirplaneLayout[] airPlaneLayouts = getAirplaneLayOuts();
        //For performance reasons, convert to hashmap with airPlaneLayOutID as KEY.
        HashMap<String, AirplaneLayout> airPlaneLayoutsMapped = new HashMap<String, AirplaneLayout>();
        for (AirplaneLayout item : airPlaneLayouts) {
            airPlaneLayoutsMapped.put(item.id, item);
        }
        ArrayList<Airplane> result = new ArrayList<Airplane>();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM  `Airplane`";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                String airPlaneLayOutID = rs.getString(3);
                if (airPlaneLayoutsMapped.containsKey(airPlaneLayOutID)) {
                    result.add(new Airplane(id, name, airPlaneLayoutsMapped.get(airPlaneLayOutID)));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (Airplane[]) result.toArray(new Airplane[0]);
    }
/*
 * get airplane layouts
 */
    AirplaneLayout[] getAirplaneLayOuts() {
             try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        ArrayList<AirplaneSeat> airPlaneSeats = getAirplaneSeats();

        ArrayList<AirplaneLayout> result = new ArrayList<AirplaneLayout>();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM  `AirplaneLayout`";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString(1);
                String placementPhoto = rs.getString(2);
                ArrayList<AirplaneSeat> res = new ArrayList<AirplaneSeat>();
                for (AirplaneSeat item : airPlaneSeats) {
                    if (item.airplaneLayoutID.equals(id)) {
                        res.add(item);
                    }
                }
                result.add(new AirplaneLayout(id, placementPhoto, (AirplaneSeat[]) res.toArray(new AirplaneSeat[0])));

            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (AirplaneLayout[]) result.toArray(new AirplaneLayout[0]);
    }
/*
 * get airplaneseat setup.
 */
    ArrayList<AirplaneSeat> getAirplaneSeats() {
             try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        ArrayList<AirplaneSeat> result = new ArrayList<AirplaneSeat>();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM  `AirplaneSeats`";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString(1);
                String airPlaneLayOutID = rs.getString(2);

                int positionX = rs.getInt(3);
                int positionY = rs.getInt(4);
                int rowIndex = rs.getInt(5);
                int columnIndex = rs.getInt(6);
                result.add(new AirplaneSeat(id, airPlaneLayOutID, positionX, positionY, rowIndex, columnIndex));


            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    void validateConnect() throws Exception
    {
           try {
            if (!conn.isValid(60000))
             {
             if (!conn.isClosed())
             {
             disconnect();
             }
             connect();
             }
           return;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      throw new Exception("Connection to the MYSQL server could not be established!");
    }
}
