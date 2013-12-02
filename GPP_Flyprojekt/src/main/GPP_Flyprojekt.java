package main;

import classes.Airport;
import classes.MainMenu;
import classes.DatabaseHandler;
import classes.Flight;
import classes.Reservation;

/**
 * This should be the entry point for the application
 * @author jakoblautrupnysom
 */
public class GPP_Flyprojekt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DatabaseHandler db = new DatabaseHandler("1");
        db.connect();
        Airport airport = (Airport) db.getAirport();
        Flight[] flights = db.getFlights();
        Reservation[] reservations = db.getReservations(flights[0]);
        db.disconnect();
        System.out.println("GPP_Flyprojekt has been run!");
        // TODO code application logic here
        MainMenu menu = new MainMenu(db);
        menu.pack();
        menu.setVisible(true);
    }

}
