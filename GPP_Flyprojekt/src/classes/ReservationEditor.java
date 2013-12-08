package classes;

import interfaces.IValidatable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.BorderFactory;
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
    
    AdditionalPassengersPanel pasPanel;
    ReservationInfoPanel resPanel;
    SeatChooser chooser;
    FlightPanel flightPanel;
    
    /**
     * Constructor for the ReservationEditor class
     */
    public ReservationEditor () {
        super();
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
        flightInfoPanel.add(flightPanel, BorderLayout.NORTH);
        
        //chooser = new SeatChooser(); // Make a version for no-flight-chosen?
        //chooser.setPreferredSize(new Dimension(300,500));
        //flightInfoPanel.add(chooser, BorderLayout.SOUTH);
        //flightInfoPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        // Pack the editor window
        this.add(reservationInfoPanel, BorderLayout.WEST);
        this.add(flightInfoPanel, BorderLayout.EAST);
    }
    
    /**
     * 
     * @param res 
     */
    public ReservationEditor(Reservation res) {
        this();
        
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
