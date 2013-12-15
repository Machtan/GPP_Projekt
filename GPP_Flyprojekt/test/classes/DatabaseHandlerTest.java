/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import interfaces.IAirport;
import interfaces.IFlight;
import java.util.ArrayList;
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
     * Test of getHandler method, of class DatabaseHandler.
     */
    @Test
    public void testGetHandler() {
        System.out.println("getHandler");
        DatabaseHandler expResult = null;
        DatabaseHandler result = DatabaseHandler.getHandler();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of connect method, of class DatabaseHandler.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        DatabaseHandler instance = null;
        instance.connect();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of disconnect method, of class DatabaseHandler.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        DatabaseHandler instance = null;
        instance.disconnect();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReservations method, of class DatabaseHandler.
     */
    @Test
    public void testGetReservations() throws Exception {
        System.out.println("getReservations");
        IFlight flight = null;
        DatabaseHandler instance = null;
        Reservation[] expResult = null;
        Reservation[] result = instance.getReservations(flight);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addReservation method, of class DatabaseHandler.
     */
    @Test
    public void testAddReservation() throws Exception {
        System.out.println("addReservation");
        Reservation res = null;
        DatabaseHandler instance = null;
        Reservation expResult = null;
        Reservation result = instance.addReservation(res);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteReservation method, of class DatabaseHandler.
     */
    @Test
    public void testDeleteReservation() throws Exception {
        System.out.println("deleteReservation");
        Reservation res = null;
        DatabaseHandler instance = null;
        instance.deleteReservation(res);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAirplaneSeat method, of class DatabaseHandler.
     */
    @Test
    public void testAddAirplaneSeat() throws Exception {
        System.out.println("addAirplaneSeat");
        AirplaneSeat res = null;
        DatabaseHandler instance = null;
        instance.addAirplaneSeat(res);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateReservation method, of class DatabaseHandler.
     */
    @Test
    public void testUpdateReservation() throws Exception {
        System.out.println("updateReservation");
        Reservation res = null;
        DatabaseHandler instance = null;
        instance.updateReservation(res);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateFlightFreeSeats method, of class DatabaseHandler.
     */
    @Test
    public void testUpdateFlightFreeSeats() throws Exception {
        System.out.println("updateFlightFreeSeats");
        IFlight flight = null;
        DatabaseHandler instance = null;
        instance.updateFlightFreeSeats(flight);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFlights method, of class DatabaseHandler.
     */
    @Test
    public void testGetFlights() throws Exception {
        System.out.println("getFlights");
        DatabaseHandler instance = null;
        Flight[] expResult = null;
        Flight[] result = instance.getFlights();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAirport method, of class DatabaseHandler.
     */
    @Test
    public void testGetAirport() throws Exception {
        System.out.println("getAirport");
        DatabaseHandler instance = null;
        IAirport expResult = null;
        IAirport result = instance.getAirport();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPeople method, of class DatabaseHandler.
     */
    @Test
    public void testGetPeople() throws Exception {
        System.out.println("getPeople");
        DatabaseHandler instance = null;
        Person[] expResult = null;
        Person[] result = instance.getPeople();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAirplanes method, of class DatabaseHandler.
     */
    @Test
    public void testGetAirplanes() throws Exception {
        System.out.println("getAirplanes");
        DatabaseHandler instance = null;
        Airplane[] expResult = null;
        Airplane[] result = instance.getAirplanes();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAirplaneLayOuts method, of class DatabaseHandler.
     */
    @Test
    public void testGetAirplaneLayOuts() throws Exception {
        System.out.println("getAirplaneLayOuts");
        DatabaseHandler instance = null;
        AirplaneLayout[] expResult = null;
        AirplaneLayout[] result = instance.getAirplaneLayOuts();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAirplaneSeats method, of class DatabaseHandler.
     */
    @Test
    public void testGetAirplaneSeats() throws Exception {
        System.out.println("getAirplaneSeats");
        DatabaseHandler instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getAirplaneSeats();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isConnected method, of class DatabaseHandler.
     */
    @Test
    public void testIsConnected() {
        System.out.println("isConnected");
        DatabaseHandler instance = null;
        boolean expResult = false;
        boolean result = instance.isConnected();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validateConnect method, of class DatabaseHandler.
     */
    @Test
    public void testValidateConnect() throws Exception {
        System.out.println("validateConnect");
        DatabaseHandler instance = null;
        instance.validateConnect();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}