package classes;

import interfaces.IFlight;
import interfaces.IValidatable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 * The ReservationEditor class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 27-Nov-2013
 */
public class ReservationEditor extends JFrame {
    
    Reservation reservation;
    AdditionalPassengersPanel pasPanel;
    ReservationInfoPanel resPanel;
    SeatChooser chooser;
    FlightPanel flightPanel;
    JButton addReservationButton;
    
    /**
     * Constructor for the ReservationEditor class
     */
    public ReservationEditor () {
        super();
        reservation = null;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        // Make the panel for the reservation info
        JPanel reservationInfoPanel = new JPanel(new BorderLayout());
        resPanel = new ReservationInfoPanel(400, 300);
        reservationInfoPanel.add(resPanel, "North");
        pasPanel = new AdditionalPassengersPanel(400, 300);
        reservationInfoPanel.add(pasPanel, "South");
        reservationInfoPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        // Make the panel for the flight and seating info
        JPanel flightInfoPanel = new JPanel(new BorderLayout());
        flightPanel = new FlightPanel();
        addReservationButton = new JButton("Gem reservation");
        addReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveReservation();
            }
        });
        
        flightInfoPanel.add(flightPanel, BorderLayout.NORTH);
        flightInfoPanel.add(addReservationButton, BorderLayout.SOUTH);
        
        //chooser = new SeatChooser(); // Make a version for no-flight-chosen?
        //chooser.setPreferredSize(new Dimension(300,500));
        //flightInfoPanel.add(chooser, BorderLayout.SOUTH);
        //flightInfoPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        // Pack the editor window
        this.add(reservationInfoPanel, BorderLayout.WEST);
        this.add(flightInfoPanel, BorderLayout.EAST);
    }
    
    /**
     * This method was somewhat hastily concocted. Please bear with it.
     * @param readOnly Whether the editor shouldn't be able to make changes to
     * reservations
     */
    public void setReadOnly(boolean readOnly) {
        addReservationButton.setVisible(!readOnly);
    }
    
    /**
     * Returns the reservation assembled from the data currently in the editor
     * @return 
     */
    /*private Reservation getReservation() {
        HashMap<IValidatable, String> resData = resPanel.getReservationInfo();
        Person passenger = new Person(
            "DURP, PATRICK!", 
            resData.get(PersonData.NAME), 
            resData.get(PersonData.NATIONALITY), 
            resData.get(PersonData.CPR));
        
        Reservation res = new Reservation(
            "DURP, PATRICK!", 
            passenger, 
            pasPanel.getPersons(), 
            seatChooser.getSeats(1+pasPanel.getPersons.size())), 
            flightPanel.getFlight(), 
            resData.get(ReservationData.PHONENUMBER),
            resData.get(ReservationData.CARDNUMBER),
            resPanel.getBookingReference());
    }*/
    
    /**
     * Saves the reservation currently being edited. Prompts the user if 
     * fields are left invalid or un-validated
     */
    private void saveReservation() {
        /*DatabaseHandler handler = DatabaseHandler.getHandler();
        if (reservation != null) {
            handler.updateReservation(getReservation());
        } else {
            handler.addReservation(getReservation());
        }*/
    }
    
    /**
     * 
     * @param res 
     */
    public ReservationEditor(Reservation res) {
        this();
        reservation = res;
        
        // Add reservation info
        HashMap<IValidatable, String> resInfo = new HashMap<IValidatable, String>();
        resInfo.put(ReservationData.CARDNUMBER, res.cardnumber);
        resInfo.put(ReservationData.PHONENUMBER, res.tlf);
        
        // Add info about the person having reserved
        resInfo.put(PersonData.CPR, res.passenger.cpr);
        resInfo.put(PersonData.NAME, res.passenger.name);
        resInfo.put(PersonData.NATIONALITY, res.passenger.nationality);
        
        // Update the data in the panel :)
        resPanel.setReservationInfo(resInfo);
        resPanel.setBookingReference(res.bookingNumber);
        
        // Add additional passengers
        for (Person per : res.additionalPassengers) {
            HashMap<PersonData, String> person = new HashMap<PersonData, String>();
            person.put(PersonData.CPR, per.cpr);
            person.put(PersonData.NAME, per.name);
            person.put(PersonData.NATIONALITY, per.nationality);
            pasPanel.addPerson(person);
        }
    }
}
