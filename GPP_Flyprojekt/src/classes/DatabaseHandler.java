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
    String url = "jdbc:mysql://mysql.itu.dk/";
    String dbName = "Airport";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "patr0805";
    String password = "testuser";
    Connection conn = null;
    String AirportID = "";
    /**
     * Constructor for DatabaseHandler, provide AirportID to filter out entries
     * relevant to the specific airport.
     */
    public DatabaseHandler(String AirportID) {
        this.AirportID = AirportID;
    }
    
    /**
     * Connect to the database.
     */
    @Override
    public void connect() {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);

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
                reservations.add(new Reservation(id, peopleMapped.get(passengerid), additionalPassengers_array, seatpoints_array, flight, tlf, cardNumber));
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
                            res.cardnumber)
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
                        + ")\n"
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
                    boolean[][] seatinfo = new boolean[airPlane.airplaneLayout.numberOfRows][];
                    for (int row = 0; row < seatinfo.length; row++) {
                        //Find highest for this row
                        AirplaneSeat highestSeat = null;
                        for (AirplaneSeat seat : airPlane.airplaneLayout.airPlaneSeats) {
                            if (seat.rowIndex == row && (highestSeat == null || seat.columnIndex > highestSeat.columnIndex)) {
                                highestSeat = seat;
                            }
                        }
                        if (highestSeat != null) {
                            seatinfo[row] = new boolean[highestSeat.columnIndex + 1];
                        }
                    }
                    Seating seating = new Seating(seatinfo);
                    Flight newflight = new Flight(id, origin, destination, airportID, airPlane, departureTime, arrivalTime, seating);
                    Reservation[] reservations = getReservations(newflight);
                    for (Reservation reservation : reservations) {
                        for (Point point : reservation.seats) {
                            seatinfo[point.x][point.y] = true;
                        }
                    }
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
        AirplaneLayOut[] airPlaneLayouts = getAirplaneLayOuts();
        //For performance reasons, convert to hashmap with airPlaneLayOutID as KEY.
        HashMap<String, AirplaneLayOut> airPlaneLayoutsMapped = new HashMap<String, AirplaneLayOut>();
        for (AirplaneLayOut item : airPlaneLayouts) {
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
    AirplaneLayOut[] getAirplaneLayOuts() {
        ArrayList<AirplaneSeat> airPlaneSeats = getAirplaneSeats();

        ArrayList<AirplaneLayOut> result = new ArrayList<AirplaneLayOut>();
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
                int rows = rs.getInt(3);
                result.add(new AirplaneLayOut(id, placementPhoto, rows, (AirplaneSeat[]) res.toArray(new AirplaneSeat[0])));

            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (AirplaneLayOut[]) result.toArray(new AirplaneLayOut[0]);
    }
/*
 * get airplaneseat setup.
 */
    ArrayList<AirplaneSeat> getAirplaneSeats() {
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
}
