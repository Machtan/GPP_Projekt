package classes;

import interfaces.IAirport;
import interfaces.IDatabaseHandler;
import interfaces.IFlight;
import java.awt.Point;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import classes.Utils.*;

/**
 *
 * DatabaseHandler is the class responsible for interacting with the
 * database.
 * 
 * @author Patrick Evers Bjørkman (pebj@itu.dk)
 */
public class DatabaseHandler implements IDatabaseHandler {
    //AirportID makes sure that the user of this software can only access
    //information regarding his own airport.

    final String AirportID = "1";
    //MYSQL database connection information.
    final String url = "jdbc:mysql://mysql.itu.dk/";
    final String dbName = "Airport";
    final String driver = "com.mysql.jdbc.Driver";
    final String userName = "patr0805";
    final String password = "testuser";
    //Driver that allows us to communicate with the database.
    Connection conn = null;
    //This static variabel allows for all classes to use the same instance
    //of DatabaseHandler by use of the static void named getHandler().
    static DatabaseHandler handler = null;

    /**
     * Constructor for DatabaseHandler.
     */
    private DatabaseHandler() {
    }

    /**
     * getHandler returns the same DatabaseHandler instance amoung all classes
     * that needs it. This allows the software to keep using the same connection
     * to the database.
     */
    public static DatabaseHandler getHandler() {
        if (handler == null) {
            handler = new DatabaseHandler();
            try {
                handler.connect();
            } catch (Exception ex) {
                System.out.println("Could not establish connection to the database, please check internet connection or contact support");
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
        conn = DriverManager.getConnection(url + dbName, userName, password);
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
            System.out.println("Could not close the connection to the database! Please try again.");
        }
    }

    /**
     * Get reservations from a flight.
     *
     *
     * @param flight
     * @return Reservations for a the given flight param.
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
        //Get all passengers
        Person[] people = getPeople();
        //For performance reasons, convert to hashmap with personID as KEY.
        //This way we will be able to get the passenger given that we have
        //the ID of the passenger we want.
        HashMap<String, Person> peopleMapped = new HashMap<String, Person>();
        for (Person item : people) {
            peopleMapped.put(item.id, item);
        }
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        Statement stmt;
        try {
            //Iterate all reservations from the reservation table of the
            //database.
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
                //AdditionalPassengers contains the IDs of the additional passengers
                //seperated by comma. We will now proceed to convert this to
                //Person[] containing these passengers.
                ArrayList<Person> additionalPassengers_array = new ArrayList<Person>();
                ArrayList<Point> seatpoints_array = new ArrayList<Point>();
                if (!additionalPassengers.equals("")) {
                    for (String passengerID : additionalPassengers.split(",")) {
                        additionalPassengers_array.add(peopleMapped.get(passengerID));
                    }
                }
                //seats explains the reserved seats. It consists of X,Y
                //(row, collumn)
                //coordinates seperated by semicolon.
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
     * Adds a new reservation to the database.
     *
     *
     * @param reservation
     * @return The same reservationen, but with a uniqe ID added to it.
     * @throws interfaces.IDatabaseHandler.ConnectionError
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
                //MYSQLtemplate for inserting a row in the Passenger table.
                String query = "INSERT INTO  `Airport`.`Passenger` (\n"
                        + "`ID` ,\n"
                        + "`Name` ,\n"
                        + "`Nationality` ,\n"
                        + "`CPR` \n"
                        + ")\n"
                        + "VALUES ( ?,?,?,?);";
                if (res.passenger != null) {
                    // create the mysql insert preparedstatement
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setNull(1, java.sql.Types.INTEGER);
                    preparedStmt.setString(2, res.passenger.name);
                    preparedStmt.setString(3, res.passenger.nationality);
                    preparedStmt.setString(4, res.passenger.cpr);

                    // execute the preparedstatement
                    preparedStmt.executeUpdate();
                    res.passenger.id = getAutoIncremented_Value("`Airport`.`Passenger`");


                }
                for (Person person : res.additionalPassengers) {
                    // create the mysql insert preparedstatement
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setNull(1, java.sql.Types.INTEGER);
                    preparedStmt.setString(2, person.name);
                    preparedStmt.setString(3, person.nationality);
                    preparedStmt.setString(4, person.cpr);

                    // execute the preparedstatement
                    preparedStmt.executeUpdate();

                    person.id = getAutoIncremented_Value("`Airport`.`Passenger`");

                }
                String result = Utils.formatAndJoinVars(res.additionalPassengers, "%s", ",", "id");
                String result2 = Utils.formatAndJoinVars(res.seats, "%s,%s;", "", "x", "y");
                //MYSQLtemplate for inserting a row in the Reservation table.
                query = "INSERT INTO  `Airport`.`Reservation` (\n"
                        + "`ID` ,\n"
                        + "`FlightID` ,\n"
                        + "`PassengerID` ,\n"
                        + "`AdditionalPassengersID` ,\n"
                        + "`Seats` ,\n"
                        + "`Tlf` ,\n"
                        + "`CardNumber` ,\n"
                        + "`BookingNumber`\n"
                        + ")\n"
                        + "VALUES (?,?,?,?,?,?,?,?);";

                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                //If no ID is present. Set it to null from which the database
                //will autogenerate the ID for us.
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

                //Now that the database has autogenerated an ID. We will have to
                //get this ID and assign it to the reservation param.
                res.reservationID = getAutoIncremented_Value("`Airport`.`Reservation`");

            } catch (SQLException ex) {
                System.out.println("Could not add the requested Reservation");
            }
        }
        //When updating the reservation table, we will have to update
        //the flight with the new correct number of free seats.
        updateFlightFreeSeats(res.flight);
        return res;
    }

    /**
     * Deletes a reservation from the database.
     *
     *
     * @param databasepath to the table
     * @return current autoincrement value as a String
     */
    private String getAutoIncremented_Value(String path) {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            //Autoincrement that autogenerates ID' for the rows we insert in our
            //tables works by increasing a counter by +1 every time we add a 
            //new row and use the current value. Therefore the highest ID
            //will allways be the ID of the last inserted row or autoincrement
            //value.
            String query = "select max(ID) from " + path;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException ex) {
            System.out.println("Could not fetch current autoincrement value from the database. Please contact support!");
        }
        return "";
    }

    /**
     * Deletes a reservation from the database.
     *
     *
     * @param reservation
     * @throws interfaces.IDatabaseHandler.ConnectionError
     */
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
                //Execute a delete statement for the reservation param.
                stmt = conn.createStatement();
                String query = "DELETE FROM  `Reservation` WHERE ID = " + res.reservationID;
                stmt.executeUpdate(query);
                if (res.passenger != null) {
                    //Delete the main passenger that is created this 
                    //reservation.
                    query = "DELETE FROM  `Passenger` WHERE ID = " + res.passenger.id;
                    stmt.executeUpdate(query);
                }
                for (Person person : res.additionalPassengers) {
                    //Deletes other passengers associated with this
                    //reservation.
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
        //When updating the reservation table, we will have to update
        //the flight with the new correct number of free seats.
        updateFlightFreeSeats(res.flight);
    }

    /**
     * Adds a new seat to an airplane to the database.
     *
     * @param AirplaneSeat to be added
     * @throws interfaces.IDatabaseHandler.ConnectionError
     */
    public void addAirplaneSeat(AirplaneSeat res) throws ConnectionError {
        try {
            validateConnect();
        } catch (Exception ex) {
            throw new ConnectionError(null);
        }
        if (res != null) {
            try {

                //MYSQLtemplate for inserting a row in the AirplaneSeats table.
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
                System.out.println("A problem accoured while trying to add a seat to the database! Please contact support!");
            }
        }
    }

    /**
     * Updates a given reservation.
     *
     * @param AirplaneSeat to be added
     * @throws interfaces.IDatabaseHandler.ConnectionError
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
        //It is possible to do an update using MYSQL UPDATE, however due to the
        //fact that we would have to find out which passengers should be updated,
        //removed or added, we ended it making it as remove and readd the
        //reservation.
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

    /*
     * Updates the numberOfFreeSeats collumn of the flights table.
     * @param flight to update
     * @throws interfaces.IDatabaseHandler.ConnectionError
     */
    public void updateFlightFreeSeats(IFlight flight) throws ConnectionError {
        try {
            validateConnect();
        } catch (Exception ex) {
            throw new ConnectionError(flight);
        }
        int numberOfFreeSeats = (new Seating(flight)).getNumberOfFreeSeats();

        //MYSQLtemplate for updating a row in the Flight table.
        String query = "UPDATE  `Airport`.`Flight` SET  `numberOfFreeSeats` =  ? WHERE  `Flight`.`ID` =" + flight.getID() + ";";

        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt;
        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, numberOfFreeSeats);

            // execute the preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("A problem accoured while trying to update the number of free seats in the database! Please report this to support!");
        }

    }

    /**
     * Gets the flights from the database
     *
     * @return Flight[]
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
        //This way we will be able to get the airplane given that we have
        //the ID of the airplane we want.
        HashMap<String, Airplane> airPlanesMapped = new HashMap<String, Airplane>();
        for (Airplane item : airPlanes) {
            airPlanesMapped.put(item.id, item);
        }
        //Get/Iterate all flights
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
                //Is the flight associated with the current airport
                if (airportID.equals(this.AirportID) && airPlanesMapped.containsKey(airplaneID)) {
                    Airplane airPlane = airPlanesMapped.get(airplaneID);
                    Flight newflight = new Flight(id, origin, destination, airportID, airPlane, departureTime, arrivalTime, numberOfFreeSeats);
                    flights.add(newflight);
                }
            }
        } catch (SQLException ex) {
            System.out.println("A problem accoured while trying to fetch flights, please check your internet connection or contact support!");
        }
        return (Flight[]) flights.toArray(new Flight[0]);
    }

    /**
     * Gets an instance of IAirport with general information regarding the
     * airport.
     *
     * @return IAirport
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
            //Execute the MYSQL statement and get/iterate airports.
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
            System.out.println("A problem accoured while trying to fetch airport information, please check your internet connection or contact support!");
        }
        return airport;
    }

    /**
     * Gets all the passengers from the database.
     *
     * @return Person[]
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
            //Get/itterate all passengers
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
            System.out.println("A problem accoured while trying to fetch the passengers, please check your internet connection or contact support!");
        }
        return (Person[]) result.toArray(new Person[0]);
    }

    /**
     * Gets all the airplanes from the database
     *
     * @return Airplane[]
     */
    Airplane[] getAirplanes() throws ConnectionError {
        AirplaneLayout[] airPlaneLayouts;
        try {
            validateConnect();
            airPlaneLayouts = getAirplaneLayouts();
        } catch (Exception ex) {
            throw new ConnectionError(new Airplane[0]);
        }

        //For performance reasons, convert to hashmap with airPlaneLayOutID as KEY.
        //This way we will be able to get the airPlaneLayout given that we have
        //the ID of the airPlaneLayout we want.
        HashMap<String, AirplaneLayout> airPlaneLayoutsMapped = new HashMap<String, AirplaneLayout>();
        for (AirplaneLayout item : airPlaneLayouts) {
            airPlaneLayoutsMapped.put(item.id, item);
        }
        ArrayList<Airplane> result = new ArrayList<Airplane>();
        Statement stmt;
        try {
            //get/iterate all airplanes
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
            System.out.println("A problem accoured while trying to fetch the airplanes, please check your internet connection or contact support!");
        }
        return (Airplane[]) result.toArray(new Airplane[0]);
    }

    /**
     * Gets all the airplanes' layout from the database
     *
     * @return AirplaneLayout[]
     */
    AirplaneLayout[] getAirplaneLayouts() throws ConnectionError {
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
            //get/iterate all airplanelayouts
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
            System.out.println("A problem accoured while trying to fetch the airplanes' layout, please check your internet connection or contact support!");
        }
        return (AirplaneLayout[]) result.toArray(new AirplaneLayout[0]);
    }

    /**
     * Gets all the seats for all airplanes from the database
     *
     * @return ArrayList<AirplaneSeat>
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
            //get/iterate all Airplaneseats
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
            System.out.println("A problem accoured while trying to fetch the seats of the airplane, please check your internet connection or contact support!");
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
                //if the software fails to connect within 60 seconds, return
                //false.
                return conn.isValid(60000);
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Checks whether the databasehandler is currently connected and will
     * reconnect if nessesary.
     *
     * @return Whether the databasehandler is currently connected
     */
    void validateConnect() throws Exception {
        try {
            if (conn == null) {
                connect();
            }
            //if the software fails to connect within 60 seconds, reconnect.
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
