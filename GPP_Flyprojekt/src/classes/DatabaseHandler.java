package classes;

import interfaces.IAirport;
import interfaces.IDatabaseHandler;
import interfaces.IFlight;
import java.awt.Point;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    static DatabaseHandler handler = null;

    /**
     * Constructor for DatabaseHandler, provide AirportID to filter out entries
     * relevant to the specific airport.
     */
    private DatabaseHandler() {
    }

    public static DatabaseHandler getHandler() {
        if (handler == null) {
            handler = new DatabaseHandler();
            try {
                handler.connect();
            } catch (Exception ex) {
                System.out.println("");
            }
        }
        return handler;
    }

    /**
     * Connect to the database.
     */
    @Override
    public void connect() throws Exception {
        Class.forName(driver).newInstance();
        conn = DriverManager.getConnection(url + dbName + "?autoReconnect=true", userName, password);
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
     *
     * @param flight
     * @return
     * @throws interfaces.IDatabaseHandler.ConnectionError
     */
    @Override
    public Reservation[] getReservations(IFlight flight) throws ConnectionError {
        try {
            validateConnect();
        } catch (Exception ex) {
            //Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No connection could be established");
            throw new ConnectionError(new Reservation[0]);
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
                if (!additionalPassengers.equals("")) {
                    for (String passengerID : additionalPassengers.split(",")) {
                        additionalPassengers_array.add(peopleMapped.get(passengerID));
                    }
                }
                for (String seat : seats.split(";")) {
                    String[] cordinates = seat.split(",");
                    if (cordinates.length > 1) {
                        seatpoints_array.add(new Point(Integer.parseInt(cordinates[0]), Integer.parseInt(cordinates[1])));
                    }
                }
                reservations.add(new Reservation(id, peopleMapped.get(passengerid), additionalPassengers_array, seatpoints_array, flight, tlf, cardNumber, bookingNumber));
            }
        } catch (SQLException ex) {
            System.out.println("No reservations could be loaded for flight '"
                    + flight.getID() + "' from " + flight.getOrigin() + " to "
                    + flight.getDestination() + ".");
            //Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (Reservation[]) reservations.toArray(new Reservation[0]);

    }

    /**
     * Add a new reservation to the database.
     */
    @Override
    public Reservation addReservation(Reservation res) throws ConnectionError {
        try {
            validateConnect();
        } catch (Exception ex) {
            throw new ConnectionError(res);
        }
        if (res != null) {
            try {
                if (res.passenger != null) {
                    String query = "INSERT INTO  `Airport`.`Passenger` (\n"
                            + "`ID` ,\n"
                            + "`Name` ,\n"
                            + "`Nationality` ,\n"
                            + "`CPR` \n"
                            + ")\n"
                            + "VALUES ( ?,?,?,?);";

                    // create the mysql insert preparedstatement
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setNull(1, java.sql.Types.INTEGER);
                    preparedStmt.setString(2, res.passenger.name);
                    preparedStmt.setString(3, res.passenger.nationality);
                    preparedStmt.setString(4, res.passenger.cpr);

                    // execute the preparedstatement
                    preparedStmt.executeUpdate();
                    res.passenger.id = GetAutoIncremented_Value("`Airport`.`Passenger`");


                }
                for (Person person : res.additionalPassengers) {
                    String query = "INSERT INTO  `Airport`.`Passenger` (\n"
                            + "`ID` ,\n"
                            + "`Name` ,\n"
                            + "`Nationality` ,\n"
                            + "`CPR` \n"
                            + ")\n"
                            + "VALUES ( ?,?,?,?);";

                    // create the mysql insert preparedstatement
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setNull(1, java.sql.Types.INTEGER);
                    preparedStmt.setString(2, person.name);
                    preparedStmt.setString(3, person.nationality);
                    preparedStmt.setString(4, person.cpr);

                    // execute the preparedstatement
                    preparedStmt.executeUpdate();

                    person.id = GetAutoIncremented_Value("`Airport`.`Passenger`");

                }
                String result = Utils.formatAndJoinVars(res.additionalPassengers, "%s", ",", "id");
                String result2 = Utils.formatAndJoinVars(res.seats, "%s,%s;", "", "x", "y");

                String query = "INSERT INTO  `Airport`.`Reservation` (\n"
                        + "`ID` ,\n"
                        + "`FlightID` ,\n"
                        + "`PassengerID` ,\n"
                        + "`additionalPassengersID` ,\n"
                        + "`Seats` ,\n"
                        + "`Tlf` ,\n"
                        + "`Cardnumber` ,\n"
                        + "`BookingNumber`\n"
                        + ")\n"
                        + "VALUES (?,?,?,?,?,?,?,?);";

                PreparedStatement preparedStmt = conn.prepareStatement(query);
                if ((res.reservationID != "") && (res.reservationID != null)) {
                    preparedStmt.setInt(1, Integer.parseInt(res.reservationID));
                } else {
                    preparedStmt.setNull(1, java.sql.Types.INTEGER);
                }
                preparedStmt.setString(2, res.flight.getID());
                if ((res.passenger.id != "") && (res.passenger.id != null)) {
                    preparedStmt.setInt(3, Integer.parseInt(res.passenger.id));
                } else {
                    preparedStmt.setNull(3, java.sql.Types.INTEGER);
                }
                preparedStmt.setString(4, result);
                preparedStmt.setString(5, result2);
                preparedStmt.setString(6, res.tlf);
                preparedStmt.setString(7, res.cardnumber);
                preparedStmt.setString(8, res.bookingNumber);
                // execute the preparedstatement
                preparedStmt.executeUpdate();
                res.reservationID = GetAutoIncremented_Value("`Airport`.`Reservation`");

            } catch (SQLException ex) {
                System.out.println("Could not add the requested Reservation");
            }
        }
        updateFlightFreeSeats(res.flight);
        return res;
    }
    /*
     * delete a reservation from the database.
     */

    private String GetAutoIncremented_Value(String path) {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String query = "select max(ID) from " + path;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @Override
    public void deleteReservation(Reservation res) throws ConnectionError {
        try {
            validateConnect();
        } catch (Exception ex) {
            throw new ConnectionError(null);
        }
        if (res != null) {
            Statement stmt;
            try {
                stmt = conn.createStatement();
                String query = "DELETE FROM  `Reservation` WHERE ID = " + res.reservationID;
                stmt.executeUpdate(query);
                if (res.passenger != null) {
                    query = "DELETE FROM  `Passenger` WHERE ID = " + res.passenger.id;
                    stmt.executeUpdate(query);
                }
                for (Person person : res.additionalPassengers) {
                    if (person != null) {
                        query = "DELETE FROM  `Passenger` WHERE ID = " + person.id;
                        stmt.executeUpdate(query);
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Couldn't find the reservation with the ID '"
                        + res.reservationID + "'");
            }
        }
        updateFlightFreeSeats(res.flight);
    }

    /**
     * Add a new seat to the database.
     */
    public void addAirplaneSeat(AirplaneSeat res) {
        try {
            validateConnect();
        } catch (Exception ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if (res != null) {
            try {

                String query = "INSERT INTO  `Airport`.`AirplaneSeats` (\n"
                        + "`ID` ,\n"
                        + "`AirplaneLayoutID` ,\n"
                        + "`PositionX` ,\n"
                        + "`PositionY` ,\n"
                        + "`RowIndex` ,\n"
                        + "`ColumnIndex` \n"
                        + ")\n"
                        + "VALUES ( ?,?,?,?,?,?);";

                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setNull(1, java.sql.Types.INTEGER);
                preparedStmt.setInt(2, Integer.parseInt(res.airplaneLayoutID));
                preparedStmt.setInt(3, res.positionX);
                preparedStmt.setInt(4, res.positionY);
                preparedStmt.setInt(5, res.rowIndex);
                preparedStmt.setInt(6, res.columnIndex);

                // execute the preparedstatement
                preparedStmt.executeUpdate();


            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * update a reservation.
     */
    @Override
    public void updateReservation(Reservation res) throws ConnectionError {
        IFlight[] flights;
        try {
            validateConnect();
            flights = getFlights();
        } catch (Exception ex) {
            throw new ConnectionError(res);
        }

        if (res != null) {
            for (IFlight flight : flights) {
                Reservation[] reservations;
                try {
                    reservations = getReservations(flight);
                } catch (ConnectionError ce) {
                    throw new ConnectionError(res);
                }
                for (Reservation reservation : reservations) {
                    if (reservation.reservationID.equals(res.reservationID) && (res.reservationID != null)) {
                        deleteReservation(reservation);
                        break;
                    }
                }
            }
            System.out.println("Updated reservation with reference: " + res.bookingNumber);
            addReservation(res);
        }
    }

    /**
     * update the numberOfFreeSeats collumn of the flights table.
     */
    public void updateFlightFreeSeats(IFlight flight) {

        int numberOfFreeSeats = (new Seating(flight)).getNumberOfFreeSeats();
        String query = "UPDATE  `Airport`.`Flight` SET  `numberOfFreeSeats` =  ? WHERE  `Flight`.`ID` =" + flight.getID() + ";";

        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt;
        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, numberOfFreeSeats);

            // execute the preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * get flights.
     */
    @Override
    public Flight[] getFlights() throws ConnectionError {
        Airplane[] airPlanes;
        try {
            validateConnect();
            airPlanes = getAirplanes();
        } catch (Exception ex) {
            throw new ConnectionError(new Flight[0]);
        }

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
                Date departureTime = rs.getTimestamp(6);
                Date arrivalTime = rs.getTimestamp(7);
                int numberOfFreeSeats = rs.getInt(8);
                if (airportID.equals(this.AirportID) && airPlanesMapped.containsKey(airplaneID)) {
                    Airplane airPlane = airPlanesMapped.get(airplaneID);
                    Flight newflight = new Flight(id, origin, destination, airportID, airPlane, departureTime, arrivalTime, numberOfFreeSeats);
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
    public IAirport getAirport() throws ConnectionError {
        try {
            validateConnect();
        } catch (Exception ex) {
            throw new ConnectionError(null);
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
    Person[] getPeople() throws ConnectionError {
        try {
            validateConnect();
        } catch (Exception ex) {
            throw new ConnectionError(new Person[0]);
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
                String nationality = rs.getString(3);
                String cpr = rs.getString(4);
                result.add(new Person(id, name, nationality, cpr));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (Person[]) result.toArray(new Person[0]);
    }

    /**
     * get all airplanes.
     */
    Airplane[] getAirplanes() throws ConnectionError {
        AirplaneLayout[] airPlaneLayouts;
        try {
            validateConnect();
            airPlaneLayouts = getAirplaneLayOuts();
        } catch (Exception ex) {
            throw new ConnectionError(new Airplane[0]);
        }

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

    AirplaneLayout[] getAirplaneLayOuts() throws ConnectionError {
        ArrayList<AirplaneSeat> airPlaneSeats;
        try {
            validateConnect();
            airPlaneSeats = getAirplaneSeats();
        } catch (Exception ex) {
            throw new ConnectionError(new AirplaneLayout[0]);
        }

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

    ArrayList<AirplaneSeat> getAirplaneSeats() throws ConnectionError {
        try {
            validateConnect();
        } catch (Exception ex) {
            throw new ConnectionError(new ArrayList<AirplaneSeat>());
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

    /**
     * Whether the databasehandler is currently connected
     *
     * @return Whether the databasehandler is currently connected
     */
    public boolean isConnected() {
        if (conn == null) {
            return false;
        } else {
            try {
                return conn.isValid(60000);
            } catch (Exception ex) {
                return false;
            }
        }
    }

    void validateConnect() throws Exception {
        try {
            if (conn == null) {
                connect();
            }
            if (!conn.isValid(60000)) {
                if (!conn.isClosed()) {
                    disconnect();
                }
                connect();
            }
        } catch (Exception ex) {
            System.out.println("Connection to the MYSQL server could not be validated!");
            //Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Connection to the MYSQL server could not be validated!");
        }

    }
}
