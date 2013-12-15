/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import interfaces.IAirport;
import interfaces.IFlight;
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author patr0805
 */
public class DatabaseHandlerTest {

    public DatabaseHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getHandler method, makes sure that every class gets the same
     * instance of DatabaseHandler.
     */
    @Test
    public void testGetHandler() {
        System.out.println("getHandler");
        DatabaseHandler result = DatabaseHandler.getHandler();
        // TODO review the generated test code and remove the default call to fail.
        if (result == null) {
            fail("Failed to get an instance of DatabaseHandler.");
        } else {
            DatabaseHandler result2 = DatabaseHandler.getHandler();
            assertEquals(result, result2);
        }
    }

    /**
     * Test of getReservations method, of class DatabaseHandler.
     */
    @Test
    public void testGetReservations() throws Exception {
        System.out.println("getReservations");
        try {
            IFlight flight = DatabaseHandler.getHandler().getFlights()[0];
            Reservation[] result = DatabaseHandler.getHandler().getReservations(flight);
            if (result == null) // TODO review the generated test code and remove the default call to fail.
            {
                fail("getReservations failed, returned null.");
            }
        } catch (Exception ex) {
            fail("getReservations failed. " + ex.getMessage());
        }
    }

    /**
     * Test of deleteReservation method, of class DatabaseHandler.
     */
    @Test
    public void testDeleteReservation() throws Exception {
        System.out.println("deleteReservation");
        try {
            DatabaseHandler instance = DatabaseHandler.getHandler();
            ArrayList<Person> additionalPassengers = new ArrayList<Person>() {
            };
            additionalPassengers.add(new Person("Mason Ocupma", "Danish", "26322"));
            ArrayList<Point> seatPoints = new ArrayList<Point>();
            seatPoints.add(new Point(0, 1));
            seatPoints.add(new Point(1, 1));
            Reservation reservation = new Reservation("", new Person("Jason Ocupma", "Danish", "13435"), additionalPassengers, seatPoints, instance.getFlights()[0], "2362466", "35463636", "dgdgr22");
            instance.addReservation(reservation);
            Reservation[] reservationsFetchedFromDatabase = instance.getReservations(reservation.flight);
            if (reservation.reservationID == null ? reservationsFetchedFromDatabase[reservationsFetchedFromDatabase.length - 1].reservationID == null : reservation.reservationID.equals(reservationsFetchedFromDatabase[reservationsFetchedFromDatabase.length - 1].reservationID)) {
                instance.deleteReservation(reservation);
                reservationsFetchedFromDatabase = instance.getReservations(reservation.flight);
                if (reservation.reservationID == null ? reservationsFetchedFromDatabase[reservationsFetchedFromDatabase.length - 1].reservationID == null : reservation.reservationID.equals(reservationsFetchedFromDatabase[reservationsFetchedFromDatabase.length - 1].reservationID)) {

                    fail("DeleteReservation does not workt. Added reservation still exists in database");
                }

            } else {
                fail("Failed to add a reservation for removal test.");

            }
        } catch (Exception ex) {
            fail("A problem accoured while trying to test deletion of reservations.");

        }
    }

    /**
     * Test of addReservation method, of class DatabaseHandler.
     */
    @Test
    public void testAddReservationAndMYSQLInjection() throws Exception {
        System.out.println("addReservation");
        try {
            DatabaseHandler instance = DatabaseHandler.getHandler();
            ArrayList<Person> additionalPassengers = new ArrayList<Person>() {
            };
            additionalPassengers.add(new Person("Mason Ocupma", "Danish", "26322"));
            ArrayList<Point> seatPoints = new ArrayList<Point>();
            seatPoints.add(new Point(0, 1));
            seatPoints.add(new Point(1, 1));
            //Test MYSQL injection
            Reservation reservation = new Reservation("", new Person("Jason Ocupma", "Danish", "13435"), additionalPassengers, seatPoints, DatabaseHandler.getHandler().getFlights()[0], "2362466", "35463636", "');TRUNCATE TABLE  `Reservation`");
            instance.addReservation(reservation);

            if (instance.getReservations(reservation.flight).length == 0) {
                fail("MYSQLinjection is posible with current setup! All reservations are now lost!");
            } else {
                instance.deleteReservation(reservation);
            }

            // TODO review the generated test code and remove the default call to fail.
        } catch (Exception ex) {
            fail("Failed to add an reservation.");
        }
    }

    /**
     * Test of updateReservation method, of class DatabaseHandler.
     */
    @Test
    public void testUpdateReservation() throws Exception {
        System.out.println("updateReservation");
        try {
            DatabaseHandler instance = DatabaseHandler.getHandler();
            Reservation[] reservations = instance.getReservations(instance.getFlights()[0]);
            Reservation reservationToUpdate = reservations[0];
            reservationToUpdate.cardnumber += "2";
            String newValue = reservationToUpdate.cardnumber;
            instance.updateReservation(reservationToUpdate);
            reservations = instance.getReservations(instance.getFlights()[0]);
            Reservation reservationToCheck = reservations[0];

            assertEquals(newValue, reservationToCheck.cardnumber);

            //Return things to normal
            reservationToUpdate.cardnumber = reservationToUpdate.cardnumber.substring(0, reservationToUpdate.cardnumber.length());
            instance.updateReservation(reservationToUpdate);


            // TODO review the generated test code and remove the default call to fail.
        } catch (Exception ex) {
            fail("Failed to add a new seat.");
        }
    }

    /**
     * Test of updateFlightFreeSeats method, of class DatabaseHandler.
     */
    @Test
    public void testUpdateFlightFreeSeats() throws Exception {
        System.out.println("updateFlightFreeSeats");
        try {

            DatabaseHandler instance = DatabaseHandler.getHandler();
            Flight flight = instance.getFlights()[0];
            instance.updateFlightFreeSeats(flight);

            assertEquals((new Seating(flight).getNumberOfFreeSeats()), flight.numberOfFreeSeats);


            // TODO review the generated test code and remove the default call to fail.
        } catch (Exception ex) {
            fail("Failed to test updateFlightFreeSeats.");
        }
    }

    /**
     * Test of getFlights method, of class DatabaseHandler.
     */
    @Test
    public void testGetFlights() throws Exception {
        System.out.println("getFlights");
        try {
            Flight[] result = DatabaseHandler.getHandler().getFlights();
            if (result == null) // TODO review the generated test code and remove the default call to fail.
            {
                fail("getFlights failed, returned null.");
            }
        } catch (Exception ex) {
            fail("getFlights failed. " + ex.getMessage());
        }
    }

    /**
     * Test of getAirport method, of class DatabaseHandler.
     */
    @Test
    public void testGetAirport() throws Exception {
        System.out.println("getAirport");
        try {
            IAirport result = DatabaseHandler.getHandler().getAirport();
            if (result == null) // TODO review the generated test code and remove the default call to fail.
            {
                fail("getAirport failed, returned null.");
            }
        } catch (Exception ex) {
            fail("getAirport failed. " + ex.getMessage());
        }
    }

    /**
     * Test of getPeople method, of class DatabaseHandler.
     */
    @Test
    public void testGetPeople() throws Exception {
        System.out.println("getPeople");
        try {
            Person[] result = DatabaseHandler.getHandler().getPeople();
            if (result == null) // TODO review the generated test code and remove the default call to fail.
            {
                fail("getPeople failed, returned null.");
            }
        } catch (Exception ex) {
            fail("getPeople failed. " + ex.getMessage());
        }
    }

    /**
     * Test of getAirplanes method, of class DatabaseHandler.
     */
    @Test
    public void testGetAirplanes() throws Exception {
        System.out.println("getAirplanes");
        try {
            Airplane[] result = DatabaseHandler.getHandler().getAirplanes();
            if (result == null) // TODO review the generated test code and remove the default call to fail.
            {
                fail("getAirplanes failed, returned null.");
            }
        } catch (Exception ex) {
            fail("getAirplanes failed. " + ex.getMessage());
        }
    }

    /**
     * Test of getAirplaneLayOuts method, of class DatabaseHandler.
     */
    @Test
    public void testGetAirplaneLayOuts() throws Exception {
        System.out.println("getAirplaneLayOuts");
        try {
            AirplaneLayout[] result = DatabaseHandler.getHandler().getAirplaneLayouts();
            if (result == null) // TODO review the generated test code and remove the default call to fail.
            {
                fail("getAirplaneLayOuts failed, returned null.");
            }
        } catch (Exception ex) {
            fail("getAirplaneLayOuts failed. " + ex.getMessage());
        }
    }

    /**
     * Test of getAirplaneSeats method, of class DatabaseHandler.
     */
    @Test
    public void testGetAirplaneSeats() throws Exception {
        System.out.println("getAirplaneSeats");
        try {
            ArrayList<AirplaneSeat> result = DatabaseHandler.getHandler().getAirplaneSeats();
            if (result == null) // TODO review the generated test code and remove the default call to fail.
            {
                fail("getAirplaneSeats failed, returned null.");
            }
        } catch (Exception ex) {
            fail("getAirplaneSeats failed. " + ex.getMessage());
        }
    }
}